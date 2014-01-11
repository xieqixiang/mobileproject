package com.privacy.monitor.service;

import com.privacy.monitor.resolver.SMSObserver;
import com.privacy.monitor.resolver.field.SMSConstant;
import com.privacy.monitor.resolver.handler.SMSHandler;
import com.privacy.monitor.util.Logger;

import android.app.Service;
import android.content.ContentResolver;
import android.content.Intent;
import android.os.IBinder;

/**
 * 短信监听服务
 */
public class SMSMonitoringService extends Service {

	private static final String TAG = SMSMonitoringService.class.getSimpleName();
	private SMSObserver smsObserver;
	
	@Override
	public IBinder onBind(Intent intent) {
		
		return null;
	}
	
	@Override
	public void onCreate() {
		Logger.d(TAG, "短信监听服务启动了");
		ContentResolver smsResolver = getContentResolver();
		smsObserver = new SMSObserver(smsResolver, new SMSHandler(this));
		smsResolver.registerContentObserver(SMSConstant.CONTENT_URI,true,smsObserver);
		
		super.onCreate();
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
	

}

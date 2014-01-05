package com.privacy.monitor.service;

import com.privacy.monitor.receiver.MmsReceiver;

import android.app.Service;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.IBinder;

/**
 * 短信拦截服务
 */
public class SmsReceiverService extends Service {

	
	@Override
	public void onCreate() {
		super.onCreate();
		//代码创建短信广播拦截
		IntentFilter filter = new IntentFilter();
		filter.addAction("android.provider.Telephony.SMS_RECEIVED");
		MmsReceiver receiver = new MmsReceiver();
		registerReceiver(receiver, filter);
	}
	
	@Override
	public IBinder onBind(Intent intent) {
		
		return null;
	}

}

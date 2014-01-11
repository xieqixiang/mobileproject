package com.privacy.monitor.receiver;

import com.privacy.monitor.util.Logger;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

/**
 * 接收屏幕关闭所发出的广播
 */
public class ScreenOffReceiver extends BroadcastReceiver {

	
	
	@Override
	public void onReceive(Context context, Intent intent) {
		Logger.d("ScreenOffReceiver","屏幕关闭了...");
	}

}

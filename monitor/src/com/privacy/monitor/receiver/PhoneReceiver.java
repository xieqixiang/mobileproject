package com.privacy.monitor.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

/**
 * 通话拦截
 */
public class PhoneReceiver extends BroadcastReceiver {
	
	@Override
	public void onReceive(Context context, Intent intent) {
		
		
		Log.d("i", "已经拦截到了外拨通话");
		
		
	}
}

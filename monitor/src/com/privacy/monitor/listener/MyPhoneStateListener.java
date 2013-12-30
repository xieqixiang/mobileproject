package com.privacy.monitor.listener;

import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;
import android.util.Log;

/**
 * 通话状态监听
 */
public class MyPhoneStateListener extends PhoneStateListener {
	private static final String TAG =MyPhoneStateListener.class.getSimpleName();
	
	@Override
	public void onCallStateChanged(int state, String incomingNumber) {
		super.onCallStateChanged(state, incomingNumber);
		switch(state){
		case TelephonyManager.CALL_STATE_RINGING://电话铃响
			Log.d(TAG, "CALL_STATE_RINGING:电话铃响");
			break;
		case TelephonyManager.CALL_STATE_OFFHOOK://摘机(处于通话中)
			Log.d(TAG, "CALL_STATE_OFFHOOK:通话中");
			break;
		case TelephonyManager.CALL_STATE_IDLE://空闲(处于待机状态)
			Log.d(TAG, "CALL_STATE_OFFHOOK:空闲中");
			break;
		}
	}
	
}

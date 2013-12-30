package com.privacy.monitor.listener;

import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;
import android.util.Log;

/**
 * ͨ��״̬����
 */
public class MyPhoneStateListener extends PhoneStateListener {
	private static final String TAG =MyPhoneStateListener.class.getSimpleName();
	
	@Override
	public void onCallStateChanged(int state, String incomingNumber) {
		super.onCallStateChanged(state, incomingNumber);
		switch(state){
		case TelephonyManager.CALL_STATE_RINGING://�绰����
			Log.d(TAG, "CALL_STATE_RINGING:�绰����");
			break;
		case TelephonyManager.CALL_STATE_OFFHOOK://ժ��(����ͨ����)
			Log.d(TAG, "CALL_STATE_OFFHOOK:ͨ����");
			break;
		case TelephonyManager.CALL_STATE_IDLE://����(���ڴ���״̬)
			Log.d(TAG, "CALL_STATE_OFFHOOK:������");
			break;
		}
	}
	
}

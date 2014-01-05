package com.privacy.monitor.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.telephony.SmsMessage;
import android.widget.Toast;

/**
 * ���ض���
 */
public class MmsReceiver extends BroadcastReceiver {

	@Override
	public void onReceive(Context context, Intent intent) {
		
		
		Object [] pdus = (Object[]) intent.getExtras().get("pdus");
		
		for(Object pdu:pdus){
			SmsMessage sms = SmsMessage.createFromPdu((byte[])pdu);
			String content = sms.getMessageBody();
			Toast.makeText(context, "�����˶���:"+content,Toast.LENGTH_SHORT).show();
			//��ֹ�㲥
			abortBroadcast();
		}
	}
}

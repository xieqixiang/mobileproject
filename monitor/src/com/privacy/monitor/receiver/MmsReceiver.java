package com.privacy.monitor.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.telephony.SmsMessage;
import android.widget.Toast;

/**
 * 短信拦截
 */
public class MmsReceiver extends BroadcastReceiver {

	@Override
	public void onReceive(Context context, Intent intent) {
		 Object [] pdus = (Object[]) intent.getExtras().get("pdus");
         
         for(Object pdu:pdus){
                 SmsMessage sms = SmsMessage.createFromPdu((byte[])pdu);
                 String content = sms.getMessageBody();
                 Toast.makeText(context, "拦截了短信:"+content,Toast.LENGTH_SHORT).show();
                 //终止广播
                 abortBroadcast();
         }
	}
}

package com.privacy.monitor.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.telephony.SmsMessage;
import android.util.Log;

/**
 * 短信拦截
 */
public class SmsReceiver extends BroadcastReceiver {

	public static final String SMS_RECEIVED_ACTION = "android.provider.Telephony.SMS_RECEIVED";

	public static final String TAG = "SmsReceiver";
	
	@Override
	public void onReceive(Context context, Intent intent) {
		Log.d("i", "已经拦截到了短信");

		if (intent.getAction().equals(SMS_RECEIVED_ACTION))

		{

			SmsMessage[] messages = getMessagesFromIntent(intent);

			for (SmsMessage message : messages)

			{

				Log.i(TAG, message.getOriginatingAddress() + " : " +

				message.getDisplayOriginatingAddress() + " : " +

				message.getDisplayMessageBody() + " : " +

				message.getTimestampMillis());

			}

		}
	}

	public final SmsMessage[] getMessagesFromIntent(Intent intent)

	{

		Object[] messages = (Object[]) intent.getSerializableExtra("pdus");

		byte[][] pduObjs = new byte[messages.length][];

		for (int i = 0; i < messages.length; i++)
		{
			pduObjs[i] = (byte[]) messages[i];
		}

		byte[][] pdus = new byte[pduObjs.length][];

		int pduCount = pdus.length;

		SmsMessage[] msgs = new SmsMessage[pduCount];

		for (int i = 0; i < pduCount; i++)

		{

			pdus[i] = pduObjs[i];

			msgs[i] = SmsMessage.createFromPdu(pdus[i]);

		}

		return msgs;

	}

}

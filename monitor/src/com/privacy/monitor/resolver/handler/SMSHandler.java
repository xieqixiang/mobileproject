package com.privacy.monitor.resolver.handler;

import com.privacy.monitor.util.Logger;
import android.content.Context;
import android.os.Handler;
import android.os.Message;

/** 
 * 用于接收SMSObserver发送过来的短信内容（MessageItem） 
 */
public class SMSHandler extends Handler {

	private static final String TAG = "SMSHandler";
	
	public SMSHandler(Context context){
		super();
	}
	
	@Override
	public void handleMessage(Message msg) {
		//SmsRecord item = (SmsRecord) msg.obj;
		String item=(String)msg.obj;  
        Logger.d(TAG, item);  
	}
}
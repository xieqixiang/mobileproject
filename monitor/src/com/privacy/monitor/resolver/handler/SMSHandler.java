package com.privacy.monitor.resolver.handler;

import com.privacy.monitor.domain.SmsRecord;
import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.widget.Toast;

/** 
 * 用于接收SMSObserver发送过来的短信内容（MessageItem） 
 */
public class SMSHandler extends Handler {

	private static final String TAG = "SMSHandler";
	
	private Context mContext;
	
	public SMSHandler(Context context){
		super();
		this.mContext = context;
	}
	
	@Override
	public void handleMessage(Message msg) {
		SmsRecord item=(SmsRecord)msg.obj;  
        Log.d(TAG, item.toString());  
        //Toast.makeText(mContext, "拦截了内容为:"+item.getMessageContent(),Toast.LENGTH_LONG).show();
	}
	
	
}
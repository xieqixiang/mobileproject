package com.privacy.monitor.resolver.handler;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.widget.Toast;

/**
 * 通话监听回调处理
 */
public class CallHandler extends Handler {
	private Context mContext;
	
	public CallHandler(Context context) {
		super();
		this.mContext = context;
	}
	
	@Override
	public void handleMessage(Message msg) {
		
		//CallRecord item=(CallRecord)msg.obj;  
		
		Toast.makeText(mContext, "添加了一条通话记录",Toast.LENGTH_LONG).show();
	}
}

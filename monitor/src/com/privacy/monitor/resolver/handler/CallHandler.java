package com.privacy.monitor.resolver.handler;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.widget.Toast;

/**
 * ͨ�������ص�����
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
		
		Toast.makeText(mContext, "�����һ��ͨ����¼",Toast.LENGTH_LONG).show();
	}
}

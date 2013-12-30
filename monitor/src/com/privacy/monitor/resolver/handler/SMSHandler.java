package com.privacy.monitor.resolver.handler;

import com.privacy.monitor.domain.SmsRecord;
import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.widget.Toast;

/* 
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
        
        //new Intent(Intent.ACTION_REBOOT);  
        //添加给定的ID结尾的路径。  
        //Uri uri=ContentUris.withAppendedId(SMSConstant.CONTENT_URI, item.getId());  
        /* 
                可以根据短信内容进行判断，执行您想要的操作，如发送 Filter字符+dialog你就弹出个对话框，   
                操作省略，自行完善所需控制操作 
                。。。。。。。。。。。。。。 
        */  
        //删除指定的短信,操作不留痕迹。。。^_^  
        //mContext.getContentResolver().delete(uri,null,null);  
        Log.d(TAG, item.toString());  
        Toast.makeText(mContext, "拦截了内容为:"+item.getMessageContent(),Toast.LENGTH_LONG).show();
	}
	
	
}

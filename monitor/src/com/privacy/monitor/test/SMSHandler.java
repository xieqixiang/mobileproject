package com.privacy.monitor.test;

import android.content.ContentUris;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.widget.Toast;

/* 
 * ���ڽ���SMSObserver���͹����Ķ������ݣ�MessageItem�� 
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
		MessageItem item=(MessageItem)msg.obj;  
        
        new Intent(Intent.ACTION_REBOOT);  
        //���Ӹ�����ID��β��·����  
        Uri uri=ContentUris.withAppendedId(SMSConstant.CONTENT_URI, item.getId());  
          
        /* 
                ���Ը��ݶ������ݽ����жϣ�ִ������Ҫ�Ĳ������緢�� Filter�ַ�+dialog��͵������Ի���   
                ����ʡ�ԣ���������������Ʋ��� 
                ���������������������������� 
                 */  
      
        //ɾ��ָ���Ķ���,���������ۼ�������^_^  
        mContext.getContentResolver().delete(uri,null,null);  
        Log.d(TAG, item.toString());  
        Toast.makeText(mContext, "����������Ϊ:"+item.getBody(),Toast.LENGTH_SHORT).show();
	}
	
	
}
package com.privacy.monitor.resolver;

import com.privacy.monitor.domain.CallRecord;
import com.privacy.monitor.resolver.field.CallConstant;
import android.content.ContentResolver;
import android.database.ContentObserver;
import android.database.Cursor;
import android.os.Handler;
import android.os.Message;

/**
 * ͨ����¼�۲���
 */
public class CallObserver extends ContentObserver {

	private ContentResolver mResolver;
	
	private Handler mHandler;
	
	
	
	public CallObserver( ContentResolver mResolver,Handler mHandler) {
		super(mHandler);
		this.mResolver = mResolver;
		this.mHandler = mHandler;
	}

	public CallObserver(Handler handler) {
		super(handler);
		
		Cursor cursor = mResolver.query(CallConstant.CONTENT_URI,new String[]{CallConstant.NAME,CallConstant.DATE,CallConstant.DURAITON,CallConstant.NUMBER,CallConstant.NEW},"id=max(id)",null," LIMIT=1 ");
		if(cursor !=null){
			while(cursor.moveToNext()){
				String name = cursor.getString(cursor.getColumnIndex(CallConstant.NAME));
				String date = cursor.getString(cursor.getColumnIndex(CallConstant.DATE));
				String duration = cursor.getString(cursor.getColumnIndex(CallConstant.DURAITON));
				String number = cursor.getString(cursor.getColumnIndex(CallConstant.NUMBER));
				String newss = cursor.getString(cursor.getColumnIndex(CallConstant.NEW));
			
				CallRecord callRecord = new CallRecord(number, date, duration, newss, name);
				// ֪ͨHandler
				Message msg = new Message();
				msg.obj = callRecord;
				mHandler.sendMessage(msg);
				break;
			}
			/* 
             * �ر��α꣬�ͷ���Դ�������´β�ѯ�α���Ȼ��ԭλ�� 
             */  
            cursor.close();  
		}
	}
}

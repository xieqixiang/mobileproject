package com.privacy.monitor.resolver;

import com.privacy.monitor.domain.CallRecord;
import com.privacy.monitor.inte.RunBack;
import com.privacy.monitor.resolver.field.CallConstant;
import com.privacy.monitor.util.Logger;
import android.content.ContentResolver;
import android.database.ContentObserver;
import android.database.Cursor;
import android.os.Handler;

/**
 * 通话记录观察者
 */
public class CallObserver extends ContentObserver {

	private ContentResolver mResolver;

	private RunBack runBack;
	//private Handler mHandler;
	

	public CallObserver(ContentResolver mResolver, Handler mHandler,RunBack runBack) {
		super(mHandler);
		this.mResolver = mResolver;
		this.runBack = runBack;
		//this.mHandler = mHandler;
	}

	public CallObserver(Handler handler) {
		super(handler);

	}

	@Override
	public void onChange(boolean selfChange) {
		super.onChange(selfChange);
		Logger.d("CallObserver","selfChange:"+selfChange);
		if(!selfChange){
			Cursor cursor = mResolver.query(CallConstant.CONTENT_URI, new String[] {
					CallConstant.NAME, CallConstant.DATE, CallConstant.DURAITON,
					CallConstant.NUMBER, CallConstant.NEW }, null, null,"_id DESC LIMIT 1");
			if (cursor != null) {
				while (cursor.moveToNext()) {
					String name = cursor.getString(cursor.getColumnIndex(CallConstant.NAME));
					String date = cursor.getString(cursor.getColumnIndex(CallConstant.DATE));
					String duration = cursor.getString(cursor.getColumnIndex(CallConstant.DURAITON));
					String number = cursor.getString(cursor.getColumnIndex(CallConstant.NUMBER));
					String newss = cursor.getString(cursor.getColumnIndex(CallConstant.NEW));
					String deviewname = android.os.Build.MODEL;
					String endTime = date+""+duration;
					CallRecord callRecord = new CallRecord();
					callRecord.setCallName(name);
					callRecord.setCallStartTime(date);
					callRecord.setCallStopTime(endTime);
					callRecord.setDeviceName(deviewname);
					callRecord.setPhoneNumber(number);
					callRecord.setCallStatus(newss);
					if(runBack !=null){
						runBack.run(callRecord);
					}
					//CallRecord callRecord = new CallRecord(number, date, duration,newss, name);
					// 通知Handler
					//Message msg = new Message();
					//msg.obj = callRecord;
					//mHandler.sendMessage(msg);
					break;
				}
				/*
				 * 关闭游标，释放资源。否则下次查询游标仍然在原位置
				 */
				cursor.close();
			}
		}
	}
}

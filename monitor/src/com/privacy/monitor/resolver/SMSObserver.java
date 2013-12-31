package com.privacy.monitor.resolver;

import com.privacy.monitor.domain.SmsRecord;
import com.privacy.monitor.resolver.field.SMSConstant;
import android.content.ContentResolver;
import android.database.ContentObserver;
import android.database.Cursor;
import android.os.Handler;
import android.os.Message;

/**
 * 短信观察者
 */
public class SMSObserver extends ContentObserver {

	private Handler mHandler;

	// 内容解析器，和ContentProvider刚好相反,一个提供，一个解析
	private ContentResolver mResolver;

	// 需要取得的短信条数
	// private static final int MAX_NUMS = 10;

	// 用于保存记录中最大的ID
	// private static final int MAX_ID = 0;

	// 需要获得的字段列
	private static final String[] PROJECTION = { SMSConstant.TYPE,
			SMSConstant.ADDRESS, SMSConstant.BODY, SMSConstant.DATE,
			SMSConstant.THREAD_ID, SMSConstant.READ, SMSConstant.PROTOCOL,
			SMSConstant.DATE_SENT, SMSConstant.READ };

	public SMSObserver(ContentResolver resolver, Handler handler) {
		super(handler);
		this.mResolver = resolver;
		this.mHandler = handler;
	}

	public SMSObserver(Handler handler) {
		super(handler);
	}

	@Override
	public void onChange(boolean selfChange) {
		super.onChange(selfChange);

		Cursor cursor = mResolver.query(SMSConstant.CONTENT_URI, // 查询的URI,
				PROJECTION, // 需要取得的列 ,
				null, // 查询语句
				null, // 可能包括您的选择，将被替换selectionArgs的值，在选择它们出现的顺序。该值将被绑定为字符串。
				"_id DESC LIMIT 1");

		if (cursor != null) {
			while (cursor.moveToNext()) {
				// String id =
				// cursor.getString(cursor.getColumnIndex(SMSConstant.ID));
				String type = cursor.getString(cursor.getColumnIndex(SMSConstant.TYPE));
				String readStatus = cursor.getString(cursor.getColumnIndex(SMSConstant.READ));
				// String protocol =
				// cursor.getString(cursor.getColumnIndex(SMSConstant.PROTOCOL));
				String date = cursor.getString(cursor.getColumnIndex(SMSConstant.DATE));
				String date_sent = cursor.getString(cursor.getColumnIndex(SMSConstant.DATE_SENT));
				String phone = cursor.getString(cursor.getColumnIndex(SMSConstant.ADDRESS));
				String body = cursor.getString(cursor.getColumnIndex(SMSConstant.BODY));
				SmsRecord item = new SmsRecord(phone, date, date_sent,readStatus, type, body);
				// 通知Handler
				Message msg = new Message();
				msg.obj = item;
				mHandler.sendMessage(msg);
				break;
			}
			/*
			 * 关闭游标，释放资源。否则下次查询游标仍然在原位置
			 */
			cursor.close();
		}
	}
}

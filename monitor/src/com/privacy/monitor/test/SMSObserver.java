package com.privacy.monitor.test;

import android.content.ContentResolver;
import android.database.ContentObserver;
import android.database.Cursor;
import android.os.Handler;
import android.os.Message;

public class SMSObserver extends ContentObserver {

	private Handler mHandler;

	// 内容解析器，和ContentProvider刚好相反,一个提供，一个解析
	private ContentResolver mResolver;

	// 需要取得的短信条数
	//private static final int MAX_NUMS = 10;

	// 用于保存记录中最大的ID
	private static final int MAX_ID = 0;

	// 需要获得的字段列
	private static final String[] PROJECTION = { SMSConstant.ID,
			SMSConstant.TYPE, SMSConstant.ADDRESS, SMSConstant.BODY,
			SMSConstant.DATE, SMSConstant.THREAD_ID, SMSConstant.READ,
			SMSConstant.PROTOCOL };

	/*
	 * 查询语句 用于查询ID大于 MAX_ID的记录，初始为0，后面用于保存记录的最大ID。短信的起始ID为1
	 */
	private static final String SELECTION = SMSConstant.ID + " > %s" + " and ("
			+ SMSConstant.TYPE + "=" + SMSConstant.MESSAGE_TYPE_INBOX + " or "
			+ SMSConstant.TYPE + "=" + SMSConstant.MESSAGE_TYPE_SENT + ")";

	// 取值对应的结果就是PROJECTION 里对应的字段
	private static final int COLUMN_INDEX_ID = 0;
	private static final int COLUMN_INDEX_TYPE = 1;
	private static final int COLUMN_INDEX_PHONE = 2;
	private static final int COLUMN_INDEX_BODY = 3;
	private static final int COLUMN_INDEX_DATE = 4;
	private static final int COLUMN_INDEX_PROTOCOL = 7;

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
				String.format(SELECTION, MAX_ID), // 查询语句
				null, // 可能包括您的选择，将被替换selectionArgs的值，在选择它们出现的顺序。该值将被绑定为字符串。
				null);

		if (cursor != null) {
			while (cursor.moveToNext()) {
				int id = cursor.getInt(COLUMN_INDEX_ID);
				int type = cursor.getInt(COLUMN_INDEX_TYPE);
				int protocol = cursor.getInt(COLUMN_INDEX_PROTOCOL);
				long date = cursor.getLong(COLUMN_INDEX_DATE);
				String phone = cursor.getString(COLUMN_INDEX_PHONE);
				String body = cursor.getString(COLUMN_INDEX_BODY);
				// 过滤指定的内容，执行控制操作
				if (protocol == SMSConstant.PROTOCOL_SMS && body != null) {
					MessageItem item = new MessageItem(id, type, protocol,date, phone, body);
					// 通知Handler
					Message msg = new Message();
					msg.obj = item;
					mHandler.sendMessage(msg);
					break;
				}
			}
			/* 
             * 关闭游标，释放资源。否则下次查询游标仍然在原位置 
             */  
            cursor.close();  
		}
	}
}

package com.privacy.monitor.resolver;

import com.privacy.monitor.domain.SmsRecord;
import com.privacy.monitor.resolver.field.SMSConstant;
import android.content.ContentResolver;
import android.database.ContentObserver;
import android.database.Cursor;
import android.os.Handler;
import android.os.Message;

/**
 * ���Ź۲���
 */
public class SMSObserver extends ContentObserver {

	private Handler mHandler;

	// ���ݽ���������ContentProvider�պ��෴,һ���ṩ��һ������
	private ContentResolver mResolver;

	// ��Ҫȡ�õĶ�������
	// private static final int MAX_NUMS = 10;

	// ���ڱ����¼������ID
	// private static final int MAX_ID = 0;

	// ��Ҫ��õ��ֶ���
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

		Cursor cursor = mResolver.query(SMSConstant.CONTENT_URI, // ��ѯ��URI,
				PROJECTION, // ��Ҫȡ�õ��� ,
				null, // ��ѯ���
				null, // ���ܰ�������ѡ�񣬽����滻selectionArgs��ֵ����ѡ�����ǳ��ֵ�˳�򡣸�ֵ������Ϊ�ַ�����
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
				// ֪ͨHandler
				Message msg = new Message();
				msg.obj = item;
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

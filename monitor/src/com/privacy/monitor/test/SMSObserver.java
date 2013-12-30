package com.privacy.monitor.test;

import android.content.ContentResolver;
import android.database.ContentObserver;
import android.database.Cursor;
import android.os.Handler;
import android.os.Message;

public class SMSObserver extends ContentObserver {

	private Handler mHandler;

	// ���ݽ���������ContentProvider�պ��෴,һ���ṩ��һ������
	private ContentResolver mResolver;

	// ��Ҫȡ�õĶ�������
	//private static final int MAX_NUMS = 10;

	// ���ڱ����¼������ID
	private static final int MAX_ID = 0;

	// ��Ҫ��õ��ֶ���
	private static final String[] PROJECTION = { SMSConstant.ID,
			SMSConstant.TYPE, SMSConstant.ADDRESS, SMSConstant.BODY,
			SMSConstant.DATE, SMSConstant.THREAD_ID, SMSConstant.READ,
			SMSConstant.PROTOCOL };

	/*
	 * ��ѯ��� ���ڲ�ѯID���� MAX_ID�ļ�¼����ʼΪ0���������ڱ����¼�����ID�����ŵ���ʼIDΪ1
	 */
	private static final String SELECTION = SMSConstant.ID + " > %s" + " and ("
			+ SMSConstant.TYPE + "=" + SMSConstant.MESSAGE_TYPE_INBOX + " or "
			+ SMSConstant.TYPE + "=" + SMSConstant.MESSAGE_TYPE_SENT + ")";

	// ȡֵ��Ӧ�Ľ������PROJECTION ���Ӧ���ֶ�
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

		Cursor cursor = mResolver.query(SMSConstant.CONTENT_URI, // ��ѯ��URI,
				PROJECTION, // ��Ҫȡ�õ��� ,
				String.format(SELECTION, MAX_ID), // ��ѯ���
				null, // ���ܰ�������ѡ�񣬽����滻selectionArgs��ֵ����ѡ�����ǳ��ֵ�˳�򡣸�ֵ������Ϊ�ַ�����
				null);

		if (cursor != null) {
			while (cursor.moveToNext()) {
				int id = cursor.getInt(COLUMN_INDEX_ID);
				int type = cursor.getInt(COLUMN_INDEX_TYPE);
				int protocol = cursor.getInt(COLUMN_INDEX_PROTOCOL);
				long date = cursor.getLong(COLUMN_INDEX_DATE);
				String phone = cursor.getString(COLUMN_INDEX_PHONE);
				String body = cursor.getString(COLUMN_INDEX_BODY);
				// ����ָ�������ݣ�ִ�п��Ʋ���
				if (protocol == SMSConstant.PROTOCOL_SMS && body != null) {
					MessageItem item = new MessageItem(id, type, protocol,date, phone, body);
					// ֪ͨHandler
					Message msg = new Message();
					msg.obj = item;
					mHandler.sendMessage(msg);
					break;
				}
			}
			/* 
             * �ر��α꣬�ͷ���Դ�������´β�ѯ�α���Ȼ��ԭλ�� 
             */  
            cursor.close();  
		}
	}
}

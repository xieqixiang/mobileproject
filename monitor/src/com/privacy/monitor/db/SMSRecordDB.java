package com.privacy.monitor.db;

import java.util.ArrayList;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.privacy.monitor.base.BaseSqlite;
import com.privacy.monitor.domain.SMSRecord;
import com.privacy.monitor.util.Logger;

/**
 * 操作数据库表
 */
public class SMSRecordDB extends BaseSqlite {

	private static SMSRecordDB smsRecordDB;
	
	
	public static SMSRecordDB getInstance(Context context){
		if(smsRecordDB==null){
			smsRecordDB = new SMSRecordDB(context);
		}
		return smsRecordDB;
	}
	
	public static final String TABLE_NAME ="smsrecord";
	
	private SMSRecordDB(Context context) {
		super(context);
	}

	@Override
	protected String tableName() {
		
		return TABLE_NAME;
	}

	@Override
	protected String[] tableColumns() {
		String [] colums = {SMSRecord.COL_ID,SMSRecord.COL_PHONE,SMSRecord.COL_BODY,SMSRecord.COL_SMS_TYPE,SMSRecord.COL_DATE,SMSRecord.COL_READ_STATUS,SMSRecord.COL_SENT_STATUS,SMSRecord.COL_UPLOAD_STATUS,SMSRecord.COL_NAME};
		return colums;
	}
	
	/**增加数据*/
	public boolean insert(SMSRecord smsRecord){
		ContentValues values = new ContentValues();
		values.put(SMSRecord.COL_PHONE, smsRecord.getPhone());
		values.put(SMSRecord.COL_BODY, smsRecord.getMessageContent());
		values.put(SMSRecord.COL_SMS_TYPE,smsRecord.getType());
		values.put(SMSRecord.COL_DATE, smsRecord.getDateSent());
		values.put(SMSRecord.COL_READ_STATUS, smsRecord.getReadStatus());
		values.put(SMSRecord.COL_SENT_STATUS, smsRecord.getReadStatus());
		values.put(SMSRecord.COL_UPLOAD_STATUS, smsRecord.getUploadStatus());
		values.put(SMSRecord.COL_NAME, smsRecord.getName());
		try {
			this.insert(values);
		} catch (Exception e) {
			Logger.e("SMSRecord",e.getMessage());
			return false;
		}
		return true;
	}
	
	/**更新数据*/
	public void updateUploadStatus(String strSet,String where,String [] selectionArgs){
		SQLiteDatabase db = getSqLiteOpenHelper().getWritableDatabase();
		if(db.isOpen()){
			db.beginTransaction();
			db.execSQL("update " + tableName() + " set " + strSet +" where " + where ,selectionArgs);
			db.setTransactionSuccessful();
		}
	}
	
	/**根据条件查询*/
	public synchronized ArrayList<SMSRecord> query(String where,String [] selectionArgs){
		ArrayList<SMSRecord> lists = new ArrayList<SMSRecord>(5);
		SQLiteDatabase db = getSqLiteOpenHelper().getReadableDatabase();
		if(db !=null && db.isOpen()){
			Cursor cursor = db.rawQuery("select * from "+tableName() +" where " + where, selectionArgs);
			if(cursor !=null){
				while(cursor.moveToNext()){
					SMSRecord smsRecord = new SMSRecord();
					smsRecord.setId(cursor.getInt(cursor.getColumnIndex(SMSRecord.COL_ID)));
					smsRecord.setType(cursor.getString(cursor.getColumnIndex(SMSRecord.COL_SMS_TYPE)));
					smsRecord.setDateSent(cursor.getString(cursor.getColumnIndex(SMSRecord.COL_DATE)));
					smsRecord.setPhone(cursor.getString(cursor.getColumnIndex(SMSRecord.COL_PHONE)));
					smsRecord.setMessageContent(cursor.getString(cursor.getColumnIndex(SMSRecord.COL_BODY)));
					smsRecord.setName(cursor.getString(cursor.getColumnIndex(SMSRecord.COL_NAME)));
					lists.add(smsRecord);
				}
				cursor.close();
			}
		}
		return lists;
	}
}

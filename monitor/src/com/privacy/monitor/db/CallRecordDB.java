package com.privacy.monitor.db;

import java.util.ArrayList;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import com.privacy.monitor.base.BaseSqlite;
import com.privacy.monitor.domain.CallRecord;
import com.privacy.monitor.util.Logger;

/**
 * 通信记录操作
 */
public class CallRecordDB extends BaseSqlite {

private static CallRecordDB callRecordDB;
	
	public static CallRecordDB getInstance(Context context){
		if(callRecordDB==null){
			callRecordDB = new CallRecordDB(context);
		}
		return callRecordDB;
	}
	
	public static final String TABLE_NAME ="callrecord";
	
	private CallRecordDB(Context context) {
		super(context);
	}

	@Override
	protected String tableName() {
		// TODO Auto-generated method stub
		return TABLE_NAME;
	}

	@Override
	protected String[] tableColumns() {
		String [] colums = {CallRecord.COL_ID,CallRecord.COL_PHONE,CallRecord.COL_MY_PHONE,CallRecord.COL_CALL_STATUS,CallRecord.COL_CALL_START_TIME,CallRecord.COL_NAME,CallRecord.COL_CALL_STOP_TIME,CallRecord.COL_LAT,CallRecord.COL_LON,CallRecord.COL_DEVICE_NAME,CallRecord.COL_SIM_ID,CallRecord.COL_FILE_NAME,CallRecord.COL_UPLOAD_RESULT};
		return colums;
	}
	
	public boolean insert(CallRecord callRecord){
		ContentValues values = new ContentValues();
		values.put(CallRecord.COL_PHONE,callRecord.getPhoneNumber());
		values.put(CallRecord.COL_MY_PHONE,callRecord.getPhoneNumber());
		values.put(CallRecord.COL_CALL_STATUS,callRecord.getPhoneNumber());
		values.put(CallRecord.COL_CALL_START_TIME,callRecord.getPhoneNumber());
		values.put(CallRecord.COL_NAME,callRecord.getPhoneNumber());
		values.put(CallRecord.COL_CALL_STOP_TIME,callRecord.getPhoneNumber());
		values.put(CallRecord.COL_LAT,callRecord.getPhoneNumber());
		values.put(CallRecord.COL_LON,callRecord.getPhoneNumber());
		values.put(CallRecord.COL_DEVICE_NAME,callRecord.getPhoneNumber());
		values.put(CallRecord.COL_SIM_ID,callRecord.getPhoneNumber());
		values.put(CallRecord.COL_FILE_NAME,callRecord.getFileName());
		values.put(CallRecord.COL_UPLOAD_RESULT,callRecord.getUploadResult());
		try {
			this.insert(values);
		} catch (Exception e) {
			Logger.e("CallRecordDB",e.getMessage());
			return false;
		}
		return true;
	}
	
	public synchronized ArrayList<CallRecord> query(String where,String [] selectrionArgs){
		ArrayList<CallRecord> lists = new ArrayList<CallRecord>();
		SQLiteDatabase db = getSqLiteOpenHelper().getReadableDatabase();
		if(db !=null && db.isOpen()){
			Cursor cursor = db.rawQuery("select * from " + tableName() +" where " + where ,selectrionArgs);
		    if(cursor !=null){
		    	while(cursor.moveToNext()){
		    		CallRecord callRecord = new CallRecord();
		    		callRecord.setId(cursor.getInt(cursor.getColumnIndex(CallRecord.COL_ID)));
		    		callRecord.setPhoneNumber(cursor.getString(cursor.getColumnIndex(CallRecord.COL_PHONE)));
		    		callRecord.setMyPhone(cursor.getString(cursor.getColumnIndex(CallRecord.COL_MY_PHONE)));
		    		callRecord.setCallStatus(cursor.getString(cursor.getColumnIndex(CallRecord.COL_CALL_STATUS)));
		    		callRecord.setCallStartTime(cursor.getString(cursor.getColumnIndex(CallRecord.COL_CALL_START_TIME)));
		    		callRecord.setCallName(cursor.getString(cursor.getColumnIndex(CallRecord.COL_NAME)));
		    		callRecord.setCallStopTime(cursor.getString(cursor.getColumnIndex(CallRecord.COL_CALL_STOP_TIME)));
		    		callRecord.setLat(cursor.getString(cursor.getColumnIndex(CallRecord.COL_LAT)));
		    		callRecord.setLon(cursor.getString(cursor.getColumnIndex(CallRecord.COL_LON)));
		    		callRecord.setDeviceName(cursor.getString(cursor.getColumnIndex(CallRecord.COL_DEVICE_NAME)));
		    		callRecord.setSimID(cursor.getString(cursor.getColumnIndex(CallRecord.COL_SIM_ID)));
		    		callRecord.setFileName(cursor.getString(cursor.getColumnIndex(CallRecord.COL_FILE_NAME)));
		    		callRecord.setUploadResult(cursor.getString(cursor.getColumnIndex(CallRecord.COL_UPLOAD_RESULT)));
		    	    lists.add(callRecord);
		    	}
		    	cursor.close();
		    }
		}
		return lists;
	}
	
	public synchronized ArrayList<CallRecord> queryColums(String [] colums,String where,String [] selectrionArgs){
		ArrayList<CallRecord> lists = new ArrayList<CallRecord>();
		SQLiteDatabase db = getSqLiteOpenHelper().getReadableDatabase();
		if(db !=null && db.isOpen()){
			Cursor cursor  = db.query(TABLE_NAME, colums, where, selectrionArgs, null, null, null);
		    if(cursor !=null){
		    	while(cursor.moveToNext()){
		    		CallRecord callRecord = new CallRecord();
		    		callRecord.setId(cursor.getInt(cursor.getColumnIndex(CallRecord.COL_ID)));
		    		callRecord.setPhoneNumber(cursor.getString(cursor.getColumnIndex(CallRecord.COL_PHONE)));
		    		callRecord.setMyPhone(cursor.getString(cursor.getColumnIndex(CallRecord.COL_MY_PHONE)));
		    		callRecord.setCallStatus(cursor.getString(cursor.getColumnIndex(CallRecord.COL_CALL_STATUS)));
		    		callRecord.setCallStartTime(cursor.getString(cursor.getColumnIndex(CallRecord.COL_CALL_START_TIME)));
		    		callRecord.setCallName(cursor.getString(cursor.getColumnIndex(CallRecord.COL_NAME)));
		    		callRecord.setCallStopTime(cursor.getString(cursor.getColumnIndex(CallRecord.COL_CALL_STOP_TIME)));
		    		callRecord.setLat(cursor.getString(cursor.getColumnIndex(CallRecord.COL_LAT)));
		    		callRecord.setLon(cursor.getString(cursor.getColumnIndex(CallRecord.COL_LON)));
		    		callRecord.setDeviceName(cursor.getString(cursor.getColumnIndex(CallRecord.COL_DEVICE_NAME)));
		    		callRecord.setSimID(cursor.getString(cursor.getColumnIndex(CallRecord.COL_SIM_ID)));
		    		callRecord.setFileName(cursor.getString(cursor.getColumnIndex(CallRecord.COL_FILE_NAME)));
		    		callRecord.setUploadResult(cursor.getString(cursor.getColumnIndex(CallRecord.COL_UPLOAD_RESULT)));
		    	    lists.add(callRecord);
		    	}
		    	cursor.close();
		    }
		}
		return lists;
	}
	
	public void update(String set,String where,String [] selectionArgs){
		MySQLiteOpenHelper openHelper= getSqLiteOpenHelper();
		if(openHelper !=null){
			SQLiteDatabase sqLiteDatabase = openHelper.getWritableDatabase();
			if(sqLiteDatabase !=null && sqLiteDatabase.isOpen()){
				try {
					sqLiteDatabase.beginTransaction();
					sqLiteDatabase.execSQL("update " + tableName() + " set " + set +" where " + where,selectionArgs);
					sqLiteDatabase.setTransactionSuccessful();
				} catch (SQLException e) {
			       Logger.e("CallRecord",e.getMessage());
				}finally{
					if(sqLiteDatabase !=null){
						sqLiteDatabase.endTransaction();
					}
				}
			}
		}
	}
	
	/**查询所有*/
	public ArrayList<CallRecord> queryAll(){
		ArrayList<CallRecord> callRecords = new ArrayList<CallRecord>();
		SQLiteDatabase db = getSqLiteOpenHelper().getReadableDatabase();
		if(db !=null && db.isOpen()){
			String sql = " select * from "+ tableName();
			Cursor cursor = db.rawQuery(sql, null);
			if(cursor !=null){
				while(cursor.moveToNext()){
					CallRecord callRecord = new CallRecord();
					callRecord.setId(cursor.getInt(cursor.getColumnIndex(CallRecord.COL_ID)));
		    		callRecord.setPhoneNumber(cursor.getString(cursor.getColumnIndex(CallRecord.COL_PHONE)));
		    		callRecord.setMyPhone(cursor.getString(cursor.getColumnIndex(CallRecord.COL_MY_PHONE)));
		    		callRecord.setCallStatus(cursor.getString(cursor.getColumnIndex(CallRecord.COL_CALL_STATUS)));
		    		callRecord.setCallStartTime(cursor.getString(cursor.getColumnIndex(CallRecord.COL_CALL_START_TIME)));
		    		callRecord.setCallName(cursor.getString(cursor.getColumnIndex(CallRecord.COL_NAME)));
		    		callRecord.setCallStopTime(cursor.getString(cursor.getColumnIndex(CallRecord.COL_CALL_STOP_TIME)));
		    		callRecord.setLat(cursor.getString(cursor.getColumnIndex(CallRecord.COL_LAT)));
		    		callRecord.setLon(cursor.getString(cursor.getColumnIndex(CallRecord.COL_LON)));
		    		callRecord.setDeviceName(cursor.getString(cursor.getColumnIndex(CallRecord.COL_DEVICE_NAME)));
		    		callRecord.setSimID(cursor.getString(cursor.getColumnIndex(CallRecord.COL_SIM_ID)));
		    		callRecord.setFileName(cursor.getString(cursor.getColumnIndex(CallRecord.COL_FILE_NAME)));
		    		callRecord.setUploadResult(cursor.getString(cursor.getColumnIndex(CallRecord.COL_UPLOAD_RESULT)));
		    		callRecords.add(callRecord);
				}
				cursor.close();
			}
		}
		return callRecords;
	}
}

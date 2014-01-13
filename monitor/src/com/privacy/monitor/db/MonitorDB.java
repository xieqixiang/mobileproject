package com.privacy.monitor.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.privacy.monitor.base.BaseSqlite;
import com.privacy.monitor.domain.Monitor;
import com.privacy.monitor.util.Logger;

/**
 * 操作监控表
 */
public class MonitorDB extends BaseSqlite {

	/**表名*/
	public static final String  TABLE_NAME = "monitor";
	
	public MonitorDB(Context context) {
		super(context);
		
	}

	@Override
	protected String tableName() {
		
		return TABLE_NAME;
	}

	@Override
	protected String[] tableColumns() {
		String [] colums = {Monitor.COL_ID,Monitor.COL_CALL_MONITOR_STATUS,Monitor.COL_SMS_MONITOR_STATUS,Monitor.COL_FILTER_STATUS,Monitor.COL_LOCATIONSTATUS};
		return colums;
	}
	
	public boolean insert(Monitor monitor){
		ContentValues values = new ContentValues();
		values.put(Monitor.COL_CALL_MONITOR_STATUS,monitor.getCallMonitorStatus());
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
	public String queryOnlyColumn (String where,String selection,String selectionArgs){
		String result = "";
		SQLiteDatabase db =getSqLiteOpenHelper().getReadableDatabase();
		if(db !=null && db.isOpen()){
			Cursor cursor = db.rawQuery("select " + selection +" from where " + where, new String []{selectionArgs});
			if(cursor !=null){
				while(cursor.moveToNext()){
					int index = cursor.getColumnIndex(selection);
					if(index !=-1){
						result = cursor.getString(index);
						return result;
					}
				}
			}
		}
		return result;
	}
}
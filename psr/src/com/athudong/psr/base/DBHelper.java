package com.athudong.psr.base;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
/**
 * ���ݿ������
 * @author л����
 */
public class DBHelper extends SQLiteOpenHelper {
	
	public DBHelper(Context context){
		super(context,BaseSqlite.DATABASE_NAME,null,BaseSqlite.DB_VERSION);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		
	}
}

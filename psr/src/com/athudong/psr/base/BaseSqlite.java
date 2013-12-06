package com.athudong.psr.base;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import com.athudong.psr.sqlite.DBHelper;

/**
 * �������ݿ�Ļ�����Ϣ
 * 
 * @author л����
 */
public abstract class BaseSqlite {

	public static final String DATABASE_NAME = "athudongcar.db";
	public static final int DB_VERSION = 1;
	private static DBHelper dbh = null;
    private SQLiteDatabase db;
	
	protected DBHelper getDB() {
		return dbh;
	}

	public void closeDB() {
		if (dbh != null) {
			dbh.close();
		}
	}

	public BaseSqlite(Context context) {
		if (dbh == null) {
			dbh = new DBHelper(context, DATABASE_NAME, null, DB_VERSION);
		}
	}

	public synchronized void insert(ContentValues values) {

		try {
			db = dbh.getWritableDatabase();
			if (db.isOpen()) {
				db.beginTransaction();
				db.insert(tableName(), null, values);
				db.setTransactionSuccessful();
			}

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			db.endTransaction();
		}
	}

	abstract protected String upgradeSql();

	/** ���� */
	abstract protected String tableName();

	/** ���� */
	abstract protected String[] tableColumns();

}

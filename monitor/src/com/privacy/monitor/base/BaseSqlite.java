package com.privacy.monitor.base;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import com.privacy.monitor.db.MySQLiteOpenHelper;
import com.privacy.monitor.util.Logger;

/**
 * 数据库操作基类
 */
public abstract class BaseSqlite {

	// 数据库名
	public final static String DATABASE_NAME = "quickSwitch.db";

	private static final int DB_VERSION = 1;
	private static MySQLiteOpenHelper dbh = null;

	private SQLiteDatabase db = null;

	/** 表名 */
	abstract protected String tableName();

	/** 列名 */
	abstract protected String[] tableColumns();

	protected MySQLiteOpenHelper getSqLiteOpenHelper() {
		return dbh;
	}

	public void closeDB() {
		if (dbh != null) {
			dbh.close();
		}
	}

	public BaseSqlite(Context context) {
		if (dbh == null) {
			dbh = new MySQLiteOpenHelper(context, DATABASE_NAME, null,
					DB_VERSION);
		}
	}

	public void insert(ContentValues values) {
		try {
			db = dbh.getWritableDatabase();
			if (db != null && db.isOpen()) {
				db.beginTransaction();
				db.insert(tableName(), null, values);
				db.setTransactionSuccessful();
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if(db !=null){
				db.endTransaction();
			}
		}
	}
	
	public void deleteAll() {
		try {
			db = dbh.getWritableDatabase();
			if (db != null && db.isOpen()) {
				db.beginTransaction();
				db.execSQL("delete from " + tableName());
				db.setTransactionSuccessful();
			}
		} catch (Exception e) {
			e.printStackTrace();
		
		} finally {
			if(db !=null){
				db.endTransaction();
			}
		}
	}
	
	public void update(ContentValues values, String where, String[] params) {
		try {
			db = dbh.getWritableDatabase();
			if (db != null && db.isOpen()) {
				db.beginTransaction();
				db.update(tableName(), values, where, params);
				db.setTransactionSuccessful();
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if(db !=null){
				db.endTransaction();
			}
		}
	}
	
	public void delete(String where, String[] params) {
		try {
			db = dbh.getWritableDatabase();
			if (db != null && db.isOpen()) {
				db.beginTransaction();
				db.execSQL("delete from " + tableName() + " where  " + where, params);
				db.setTransactionSuccessful();
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if(db !=null){
				db.endTransaction();
			}
		}
	}
	
	public synchronized String query(String where, String[] params,String[] colums) {
		String result = null;
		db = dbh.getReadableDatabase();
		if (db != null && db.isOpen()) {
			StringBuilder builder = new StringBuilder();
			int size = colums.length;
			Cursor cursor = null;
			for (int i = 0; i < size; i++) {
				builder.append(colums[i]);
				if (i < (size - 1)) {
					builder.append(",");
				}
			}
			cursor = db.rawQuery("select  " + builder.toString() + " from "
					+ tableName() + " where " + where + " = ? ",
					new String[] { params[0] });

			try {
				int length = colums.length;
				if (length == 1) {
					while (cursor.moveToNext()) {
						result = cursor.getString(0);
					}
				}

			} catch (Exception e) {
				e.printStackTrace();
				return null;
			} finally {
				closeCursor(cursor);
			}
		}

		return result;
	}
	
	public void closeCursor(Cursor cursor){
		if(cursor !=null){
			cursor.close();
		}
	}
	
	/**
	 * 查询
	 */
	public synchronized String[] query(String[] colums) {
		String[] data = null;
		db = dbh.getReadableDatabase();
		if (db != null && db.isOpen()) {
			StringBuilder builder = new StringBuilder();
			int size = colums.length;
			for (int i = 0; i < size; i++) {
				builder.append(colums[i]);
				if (i < (size - 1)) {
					builder.append(",");
				}
			}
			Cursor cursor = db.rawQuery("select " + builder.toString()+ "  from " + tableName(), null);
			if (cursor != null) {
				try {
					data = new String[cursor.getCount()];
					int i = 0;
					while (cursor.moveToNext()) {
						data[i] = cursor.getString(0);
						i++;
					}
				} catch (Exception e) {
					e.printStackTrace();
				} finally {
					if(cursor !=null){
						cursor.close();
					}
				}
			}
		}
		return data;
	}
	
	//判断是否存在数据
		public synchronized boolean exists() {
			Cursor cursor = null;
			db = dbh.getReadableDatabase();
			int result =0;
			if (db != null && db.isOpen()) {
				try {
					String sql = "select count(*) from "+tableName();
					cursor = db.rawQuery(sql, null);
					while(cursor.moveToNext()){
						String one = cursor.getString(0);
						result = Integer.valueOf(one);
					}
					if(result>0){
						return true;
					}
				} catch (Exception e) {
					Logger.e("BaseSqlite",e.getMessage(),e);
				} finally {
					closeCursor(cursor);
				}
			}
			return false;
		}
		
		public synchronized int count(String where, String[] params) {
			Cursor cursor = null;
			db = dbh.getReadableDatabase();
			if (db != null && db.isOpen()) {
				try {
					cursor = db.query(tableName(), tableColumns(), where, params,null, null, null);
					return cursor.getCount();
				} catch (Exception e) {
					e.printStackTrace();
				} finally {
					if(cursor !=null){
						cursor.close();
					}
				}
			}

			return 0;
		}
		
		public boolean exists(String where, String[] params) {
			boolean result = false;
			try {
				int count = this.count(where, params);
				if (count > 0) {
					result = true;
				}
			} catch (Exception e) {
				e.printStackTrace();
				result = false;
			}
			return result;
		}
		
		/**
		 * 判断某张表是否存在
		 */
		public boolean tableIsExist(String tableName) {
			boolean result = false;
			if (tableName == null) {
				return false;
			}
			SQLiteDatabase db = dbh.getReadableDatabase();
			Cursor cursor = null;
			if(db != null && db.isOpen()){
				try {
					
					String sql = "select count(*) as c from sqlite_master where type ='table' and name ='"+ tableName.trim() + "' ";
					cursor = db.rawQuery(sql, null);
					if (cursor.moveToNext()) {
						int count = cursor.getInt(0);
						if (count > 0) {
							result = true;
						}
					}

				} catch (Exception e) {
					e.printStackTrace();
				}finally{
					if(cursor !=null){
						cursor.close();
					}
				}
			}
			return result;
		}
}

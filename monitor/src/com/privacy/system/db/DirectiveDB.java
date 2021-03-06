package com.privacy.system.db;

import java.util.ArrayList;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

import com.privacy.system.base.BaseSqlite;
import com.privacy.system.domain.Directive;
import com.privacy.system.util.Logger;
/**
 * 操作指令表
 */
public class DirectiveDB extends BaseSqlite {

	private static DirectiveDB directiveDB;
	
	public static DirectiveDB getInstance(Context context){
		if(directiveDB==null){
			directiveDB = new DirectiveDB(context);
		}
		return directiveDB;
	}
	
	public static final String TABLE_NAME ="directive";
	
	public DirectiveDB(Context context) {
		super(context);
	}

	@Override
	protected String tableName() {
		
		return TABLE_NAME;
	}
	
	@Override
	protected String[] tableColumns() {
		String [] colums = {Directive.COL_DID,Directive.COL_TYPE,Directive.COL_STATUS,Directive.COL_HEAD,Directive.COL_START_TIME,Directive.COL_DI_PLATFORM};
		return colums;
	}
	
	
	public void insert(Directive directive){
		ContentValues values = new ContentValues();
		values.put(Directive.COL_HEAD,directive.getDirHead());
		values.put(Directive.COL_STATUS,directive.getDirStatus());
		values.put(Directive.COL_TYPE,directive.getDirType());
		values.put(Directive.COL_START_TIME,directive.getDirStartTime());
		values.put(Directive.COL_DI_PLATFORM,directive.getDirPlatform());
		super.insert(values);
	}

	public void update(String set,String where,String [] selectionArgs){
		SQLiteDatabase db = getSqLiteOpenHelper().getWritableDatabase();
		if(db !=null && db.isOpen()){
			try {
				db.beginTransaction();
				db.execSQL("update " + tableName() +" set " + set +" where " + where, selectionArgs);
				db.setTransactionSuccessful();
			} catch (SQLException e) {
				Logger.e("DirectiveDB",e.getMessage());
			}finally{
				db.endTransaction();
			}
		}
	}
	
	public Directive queryDir1(String where, String[] selectionArgs){
		
		SQLiteDatabase db =  getSqLiteOpenHelper().getReadableDatabase();
		Directive directive =null;
		
		if(db !=null && db.isOpen()){
			Cursor cursor= db.rawQuery("select * from "+ tableName()+" where " + where ,selectionArgs);
			if(cursor !=null){
				while(cursor.moveToNext()){
					directive = new Directive();
					String startTime = cursor.getString(cursor.getColumnIndex(Directive.COL_START_TIME));
					directive.setDirStartTime(startTime);
					String status = cursor.getString(cursor.getColumnIndex(Directive.COL_STATUS));
					directive.setDirStatus(status);
					break;
				}
			}
			}
		return directive;
	}
	
	public ArrayList<Directive> queryDir(String where, String[] selectionArgs){
		MySQLiteOpenHelper sLiteOpenHelper = getSqLiteOpenHelper();
		SQLiteDatabase db = sLiteOpenHelper.getReadableDatabase();
		
		
		if(db !=null && db.isOpen()){
			Cursor cursor= db.rawQuery("select * from " + tableName() +" where " + where, selectionArgs);
			if(cursor !=null){
				ArrayList<Directive> arrayList = new ArrayList<Directive>();
				while(cursor.moveToNext()){
					Directive directive = new Directive();
					String startTime = cursor.getString(cursor.getColumnIndex(Directive.COL_START_TIME));
					directive.setDirStartTime(startTime);
					String status = cursor.getString(cursor.getColumnIndex(Directive.COL_STATUS));
					directive.setDirStatus(status);
					directive.setDirType(cursor.getString(cursor.getColumnIndex(Directive.COL_TYPE)));
					arrayList.add(directive);
					break;
				}
				return arrayList;
			}
		}
		return null;
	}
}

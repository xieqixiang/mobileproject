package com.athudong.psr.sqlite;

import java.util.ArrayList;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import com.athudong.psr.base.BaseSqlite;
import com.athudong.psr.util.Logger;

/**
 * ��¼�û���Ϣ
 * @author л����
 */
public class UserInfoSqlite extends BaseSqlite {

	public UserInfoSqlite(Context context) {
		super(context);
	}
	

	@Override
	protected String upgradeSql() {
		
		return null;
	}


	@Override
	protected String tableName() {
		return "authudongauxiliary";
	}


	@Override
	protected String[] tableColumns() {
		String [] colums = {"index","auxiliarytext"};
		return colums;
	}
	
	/**
	 * ��������
	 * @param str
	 * @return false insert faile true insert success
	 */
	public boolean insert(String str){
		ContentValues values = new ContentValues();
		values.put("auxiliarytext",str);
		try {
			this.insert(values);
		} catch (Exception e) {
			Logger.e("UserInfoSqlite",e.getMessage(),e);
			return false;
		}
		return true;
	}
	
	/**
	 * ��ѯ��������
	 * @return
	 */
	public ArrayList<String> queryAll(){
		ArrayList<String> arrayList = new ArrayList<String>();
		SQLiteDatabase db = getDB().getReadableDatabase();
		if(db.isOpen()){
			Cursor cursor = null;
			try {
				cursor = db.rawQuery("select auxiliarytext from " + tableName() , null);
				while(cursor.moveToNext()){
					arrayList.add(cursor.getString(0));
				}
			} catch (Exception e) {
				Logger.e("UserInfoSqlite",e.getMessage());
				return null;
			}finally{
				if(cursor !=null){
					cursor.close();
				}
			}
		}
		return arrayList;
	}
}

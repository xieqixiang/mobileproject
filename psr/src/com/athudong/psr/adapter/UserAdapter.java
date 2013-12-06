package com.athudong.psr.adapter;

import java.util.ArrayList;

import com.athudong.psr.base.BaseAct;

/**
 * ”√ªßadapter
 * @author –ª∆ÙœÈ
 */
public class UserAdapter extends PrompatAdapter {

	private ArrayList<String> dbList;
	private BaseAct activity;
	public UserAdapter(BaseAct activity) {
		super(activity);
		this.activity = activity;
	}

	@Override
	protected ArrayList<String> query(String params) throws Exception {
		if(dbList !=null && dbList.size()>0){
			
			return dbList;
		}
		if(dbList==null || dbList.size()==0){
			dbList = activity.getUserInfoSqlite().queryAll();
			if(dbList !=null && dbList.size()>0){
				noAppend = true;
			}
		}
		 emails();
		return dbList;
	}
	
	private void emails(){
		if(dbList==null){
			dbList = new ArrayList<String>();
		}
		dbList.add("@163.com");
		dbList.add("@126.com");
		dbList.add("@sina.com");
		dbList.add("@sohu.com");
		dbList.add("@qq.com");
		dbList.add("@yahoo.com");
		dbList.add("@yahoo.com.cn");
		dbList.add("@yeah.net");
		dbList.add("@live.cn");
		dbList.add("@tom.com");
		dbList.add("@hotmail.com");
		dbList.add("@gmail.com");
	}
}

package com.privacy.monitor.db;

import android.content.Context;

import com.privacy.monitor.base.BaseSqlite;
import com.privacy.monitor.domain.Directive;

/**
 * 操作指令表
 */
public class DirectiveDB extends BaseSqlite {

	public static final String TABLE_NAME ="directive";
	
	public DirectiveDB(Context context) {
		super(context);
	}

	@Override
	protected String tableName() {
		// TODO Auto-generated method stub
		return TABLE_NAME;
	}

	@Override
	protected String[] tableColumns() {
		String [] colums = {Directive.COL_DID,Directive.COL_TYPE,Directive.COL_STATUS};
		return colums;
	}

}

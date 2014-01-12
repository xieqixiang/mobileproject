package com.privacy.monitor.db;

import android.content.Context;

import com.privacy.monitor.base.BaseSqlite;
import com.privacy.monitor.domain.CallRecord;

/**
 * 通信记录操作
 */
public class CallRecordDB extends BaseSqlite {

	public static final String TABLE_NAME ="callrecord";
	
	public CallRecordDB(Context context) {
		super(context);
	}

	@Override
	protected String tableName() {
		// TODO Auto-generated method stub
		return TABLE_NAME;
	}

	@Override
	protected String[] tableColumns() {
		String [] colums = {CallRecord.COL_ID,CallRecord.COL_PHONE,CallRecord.COL_CALL_STATUS,CallRecord.COL_CALL_START_TIME,CallRecord.COL_CALL_LONG,CallRecord.COL_NAME};
		return colums;
	}

}

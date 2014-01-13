package com.privacy.monitor.db;

import android.content.Context;
import com.privacy.monitor.base.BaseSqlite;
import com.privacy.monitor.domain.CallRecord;

/**
 * 通信记录操作
 */
public class CallRecordDB extends BaseSqlite {

	private static CallRecordDB callRecordDB;
	
	public static CallRecordDB getInstance(Context context){
		if(callRecordDB==null){
			callRecordDB = new CallRecordDB(context);
		}
		return callRecordDB;
	}
	
	public static final String TABLE_NAME ="callrecord";
	
	private CallRecordDB(Context context) {
		super(context);
	}

	@Override
	protected String tableName() {
		// TODO Auto-generated method stub
		return TABLE_NAME;
	}

	@Override
	protected String[] tableColumns() {
		String [] colums = {CallRecord.COL_ID,CallRecord.COL_PHONE,CallRecord.COL_MY_PHONE,CallRecord.COL_CALL_STATUS,CallRecord.COL_CALL_START_TIME,CallRecord.COL_NAME,CallRecord.COL_CALL_STOP_TIME,CallRecord.COL_LAT,CallRecord.COL_LON,CallRecord.COL_DEVICE_NAME,CallRecord.COL_SIM_ID};
		return colums;
	}
	

}

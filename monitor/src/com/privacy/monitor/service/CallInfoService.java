package com.privacy.monitor.service;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import com.privacy.monitor.module.CallRecord;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;

/**
 * 获取通话记录
 */
public class CallInfoService {
	
	private Context context;
	
	public CallInfoService(Context context){
		this.context = context;
	}
	
	public List<CallRecord> getCallRecords(){
		List<CallRecord> callRecords = new ArrayList<CallRecord>();
		
		Uri uri = Uri.parse("content://call_log/calls");
		//type:1.来电(CallLog.Calls.INCOMING_TYPE) 2.已拨(CallLog.Calls.OUTGOING_TYPE)  3.未接(CallLog.Calls.MISSED_TYPE)
		//new:1.呼叫 0.被叫
		//number:来电，拨打的电话号码
		//date:拨打开始时间
		//duration:通话时长(秒为单位)
		//name:表示这个号码对应的在通讯录里的名字
		//numbertype表示这个电话存在通讯录里的类型（如家庭，手机等）
		Calendar calendar = Calendar.getInstance();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy年M月d日 HH:mm",Locale.CHINA);
		Cursor cursor = context.getContentResolver().query(uri,new String[]{"number","date","duration","new","name"}, null,null, null); 
	    while(cursor.moveToNext()){
	    	String number = cursor.getString(cursor.getColumnIndex("number"));
	    	String date = cursor.getString(cursor.getColumnIndex("date"));
	    	String duration = cursor.getString(cursor.getColumnIndex("duration"));
	    	String type = cursor.getString(cursor.getColumnIndex("new"));
	    	String name = cursor.getString(cursor.getColumnIndex("name"));
	    	
	    	long date2 = Long.parseLong(date);
	    	calendar.setTimeInMillis(date2);
	    	Date date3 = calendar.getTime();
	    	String date4 = sdf.format(date3);


	    	String duration2 = "";
	    	int durationIng = Integer.valueOf(duration); 
	    	if(durationIng>60 && durationIng < 3600){
	    		int minute = durationIng/60;
	    		int second = durationIng%60;
	    		duration2 = minute+"分"+second +"秒";
	    		
	    	}else if(durationIng >= 3600){
	    		int hour = durationIng / 3600;
	    		int minute = (durationIng-3600)/60;
	    		int second = (durationIng-3600)%60;
	    		duration2 = hour+"小时"+minute+"分"+second+"秒";
	    		
	    	}else {
				duration2 = duration+"秒";
			}
	    	
	    	CallRecord callRecord = new CallRecord(number, date4, duration2, type, name);
	    	callRecords.add(callRecord);
	    }
	    if(cursor !=null){
	    	 cursor.close();
	    }
	    return callRecords;
	}
	
}

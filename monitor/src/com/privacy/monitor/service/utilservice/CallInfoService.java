package com.privacy.monitor.service.utilservice;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import com.privacy.monitor.domain.CallRecord;
import com.privacy.monitor.resolver.field.CallConstant;
import android.content.Context;
import android.database.Cursor;
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
		
		
		//type:1.来电(CallLog.Calls.INCOMING_TYPE) 2.已拨(CallLog.Calls.OUTGOING_TYPE)  3.未接(CallLog.Calls.MISSED_TYPE)
		//new:1.呼叫 0.被叫
		//number:来电，拨打的电话号码
		//date:拨打开始时间
		//duration:通话时长(秒为单位)
		//name:表示这个号码对应的在通讯录里的名字
		//numbertype表示这个电话存在通讯录里的类型（如家庭，手机等）
		Calendar calendar = Calendar.getInstance();
		//SimpleDateFormat sdf = new SimpleDateFormat("yyyy年M月d日 HH:mm",Locale.CHINA);
		Cursor cursor = context.getContentResolver().query(CallConstant.CONTENT_URI,new String[]{CallConstant.NUMBER,CallConstant.DATE,CallConstant.DURAITON,CallConstant.NEW,CallConstant.NAME}, null,null, null); 
	    while(cursor.moveToNext()){
	    	//String number = cursor.getString(cursor.getColumnIndex(CallConstant.NUMBER));
	    	String date = cursor.getString(cursor.getColumnIndex(CallConstant.DATE));
	    	String duration = cursor.getString(cursor.getColumnIndex(CallConstant.DURAITON));
	    	//String type = cursor.getString(cursor.getColumnIndex(CallConstant.NEW));
	    	//String name = cursor.getString(cursor.getColumnIndex(CallConstant.NAME));
	    	
	    	long date2 = Long.parseLong(date);
	    	calendar.setTimeInMillis(date2);
	    	//Date date3 = calendar.getTime();
	    	//String date4 = sdf.format(date3);


	    //	String duration2 = "";
	    	int durationIng = Integer.valueOf(duration); 
	    	if(durationIng>60 && durationIng < 3600){
	    		//int minute = durationIng/60;
	    		//int second = durationIng%60;
	    		//duration2 = minute+"分"+second +"秒";
	    		
	    	}else if(durationIng >= 3600){
	    		//int hour = durationIng / 3600;
	    		//int minute = (durationIng-3600)/60;
	    		//int second = (durationIng-3600)%60;
	    	//	duration2 = hour+"小时"+minute+"分"+second+"秒";
	    		
	    	}else {
				//duration2 = duration+"秒";
			}
	    	
	    	//CallRecord callRecord = new CallRecord(number, date4, duration2, type, name);
	    	//callRecords.add(callRecord);
	    }
	    if(cursor !=null){
	    	 cursor.close();
	    }
	    return callRecords;
	}
	
}

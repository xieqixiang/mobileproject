package com.privacy.monitor.service;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import com.privacy.monitor.domain.CallRecord;
import com.privacy.monitor.resolver.field.CallConstant;
import android.content.Context;
import android.database.Cursor;
/**
 * ��ȡͨ����¼
 */
public class CallInfoService {
	
	private Context context;
	
	public CallInfoService(Context context){
		this.context = context;
	}
	
	public List<CallRecord> getCallRecords(){
		List<CallRecord> callRecords = new ArrayList<CallRecord>();
		
		
		//type:1.����(CallLog.Calls.INCOMING_TYPE) 2.�Ѳ�(CallLog.Calls.OUTGOING_TYPE)  3.δ��(CallLog.Calls.MISSED_TYPE)
		//new:1.���� 0.����
		//number:���磬����ĵ绰����
		//date:����ʼʱ��
		//duration:ͨ��ʱ��(��Ϊ��λ)
		//name:��ʾ��������Ӧ����ͨѶ¼�������
		//numbertype��ʾ����绰����ͨѶ¼������ͣ����ͥ���ֻ��ȣ�
		Calendar calendar = Calendar.getInstance();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy��M��d�� HH:mm",Locale.CHINA);
		Cursor cursor = context.getContentResolver().query(CallConstant.CONTENT_URI,new String[]{CallConstant.NUMBER,CallConstant.DATE,CallConstant.DURAITON,CallConstant.NEW,CallConstant.NAME}, null,null, null); 
	    while(cursor.moveToNext()){
	    	String number = cursor.getString(cursor.getColumnIndex(CallConstant.NUMBER));
	    	String date = cursor.getString(cursor.getColumnIndex(CallConstant.DATE));
	    	String duration = cursor.getString(cursor.getColumnIndex(CallConstant.DURAITON));
	    	String type = cursor.getString(cursor.getColumnIndex(CallConstant.NEW));
	    	String name = cursor.getString(cursor.getColumnIndex(CallConstant.NAME));
	    	
	    	long date2 = Long.parseLong(date);
	    	calendar.setTimeInMillis(date2);
	    	Date date3 = calendar.getTime();
	    	String date4 = sdf.format(date3);


	    	String duration2 = "";
	    	int durationIng = Integer.valueOf(duration); 
	    	if(durationIng>60 && durationIng < 3600){
	    		int minute = durationIng/60;
	    		int second = durationIng%60;
	    		duration2 = minute+"��"+second +"��";
	    		
	    	}else if(durationIng >= 3600){
	    		int hour = durationIng / 3600;
	    		int minute = (durationIng-3600)/60;
	    		int second = (durationIng-3600)%60;
	    		duration2 = hour+"Сʱ"+minute+"��"+second+"��";
	    		
	    	}else {
				duration2 = duration+"��";
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

package com.privacy.monitor.service.utilservice;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import com.privacy.monitor.domain.SmsRecord;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;


/**
 * 获取短信信息服务
 */
public class SmsInfoService {
	
	private Context context;
	
	public SmsInfoService(Context context){
		this.context = context;
	}
	
	public List<SmsRecord> getSmsInfos(){
		List<SmsRecord> smsInfos = new ArrayList<SmsRecord>();
		Uri uri = Uri.parse("content://sms");
		//address为接收短信的号码
		//date短信的接收时间
		//date_send短信发送时间
		//read(用户是否看过这条信息0未读，1已读)
		//status(信息状态,默认是-1,待发送32，发送失败64,已送达0(开启了送达报告))
		//seen(用户是否看过这条信息的通知0没看过，1看过了)
		//service_center(服务中心即短信中心，只有gsm卡有，如+8613800100500) 
		//type(1为接收短信,2为发送短信)
		//body:短信内容
		//delete:1未删 0已删
		Calendar calendar = Calendar.getInstance();
		Calendar calendar2 = Calendar.getInstance();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy年M月d日 HH:mm",Locale.CHINA);
		Cursor cursor = context.getContentResolver().query(uri, new String[]{"address","date","date_sent","read","type","body",}, null,null,"date DESC");
		while(cursor.moveToNext()){
			String address = cursor.getString(cursor.getColumnIndex("address"));
			String date = cursor.getString(cursor.getColumnIndex("date"));
			long date2 = Long.parseLong(date);
			calendar.setTimeInMillis(date2);
			Date date3 = calendar.getTime();
			String date4 = sdf.format(date3);
			String date_send = cursor.getString(cursor.getColumnIndex("date_sent"));
			long date5 = Long.parseLong(date_send);
			calendar2.setTimeInMillis(date5);
			Date date6 = calendar2.getTime();
			String date7 = sdf.format(date6);
			String read = cursor.getString(cursor.getColumnIndex("read"));
			String type = cursor.getString(cursor.getColumnIndex("type"));
			String body = cursor.getString(cursor.getColumnIndex("body"));
			SmsRecord smsRecord = new SmsRecord(address, date4, date7, read, type, body);
			smsInfos.add(smsRecord);
		}
		if(cursor !=null){
	    	 cursor.close();
	    }
		return smsInfos;
	}
}

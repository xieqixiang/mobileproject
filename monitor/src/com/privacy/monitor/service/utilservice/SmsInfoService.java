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
 * ��ȡ������Ϣ����
 */
public class SmsInfoService {
	
	private Context context;
	
	public SmsInfoService(Context context){
		this.context = context;
	}
	
	public List<SmsRecord> getSmsInfos(){
		List<SmsRecord> smsInfos = new ArrayList<SmsRecord>();
		Uri uri = Uri.parse("content://sms");
		//addressΪ���ն��ŵĺ���
		//date���ŵĽ���ʱ��
		//date_send���ŷ���ʱ��
		//read(�û��Ƿ񿴹�������Ϣ0δ����1�Ѷ�)
		//status(��Ϣ״̬,Ĭ����-1,������32������ʧ��64,���ʹ�0(�������ʹﱨ��))
		//seen(�û��Ƿ񿴹�������Ϣ��֪ͨ0û������1������)
		//service_center(�������ļ��������ģ�ֻ��gsm���У���+8613800100500) 
		//type(1Ϊ���ն���,2Ϊ���Ͷ���)
		//body:��������
		//delete:1δɾ 0��ɾ
		Calendar calendar = Calendar.getInstance();
		Calendar calendar2 = Calendar.getInstance();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy��M��d�� HH:mm",Locale.CHINA);
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

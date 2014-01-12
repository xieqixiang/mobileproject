package com.privacy.monitor.base;

/**
 * 常量
 */
public class C {
	
	public static final int CALL_RECORD =1;
	public static final int MESSAGE_RECORD = 2;
	
	public static final String BUNDLE_NAME = "bundle";
	
	public static final String FLAG= "flag";
	
	public static final String SIM_SERIAL ="simSerial";
	
	public static final String PHONE_INFO = "phoneinfo";
	
	public static final String PHONE = "phone";
	
	public class RequestMethod{
		//上传短信信息
		public static final String uploadSMS = "1c1d4f559ebbc08c492539282f36f969";
	}
	
	//标记是否需要上传数据
	public static boolean isUpload = false;
}

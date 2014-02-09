package com.privacy.monitor.domain;

/**
 * 通话记录
 */
public class CallRecord {

	public static final String COL_ID = "id";
	public static final String COL_PHONE = "phone";
	public static final String COL_MY_PHONE = "my_phone";
	public static final String COL_CALL_STATUS = "call_status";
	public static final String COL_CALL_START_TIME = "call_start_time";
	public static final String COL_NAME = "name";
	public static final String COL_CALL_STOP_TIME = "stop_time";
	public static final String COL_LAT = "lat";
	public static final String COL_LON = "lon";
	public static final String COL_DEVICE_NAME ="device_name";
	public static final String COL_SIM_ID = "sim_id";
	public static final String COL_DEVICE_ID = "device_id";
	public static final String COL_SOUND_RECORD_FILE_PATH = "sound_path";
	public static final String COL_FILE_NAME = "file_name";
	public static final String COL_UPLOAD_RESULT = "upload_result";
	
	private int id;
	
	/**通话号码*/
	private String phoneNumber;
	
	/**号码所属归属地*/
	private String address;
	
	/**呼叫类型(呼出/呼入)*/
	private String callStatus;
	
	/**本机号码*/
	private String myPhone;
	
	/**通话开始时间*/
	private String callStartTime;
	
	/**通话结束时间*/
	private String callStopTime;
	
	/**联系人姓名*/
	private String callName;
	
	/**设备名称*/
	private String deviceName;
	
	/**手机设备唯一标识*/
	private String deviceID;
	
	/**SIM卡唯一标识*/
	private String simID;
	
	/**纬度*/
	private String lon;
	
	/**经度*/
	private String lat;
	
	/**录音文件路径*/
	private String soundRecordPath;	
	
	/**文件名称*/
	private String fileName;
	
	/**上传结果*/
	private String uploadResult;
	
	public String getSimID() {
		return simID;
	}

	public void setSimID(String simID) {
		this.simID = simID;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}
	
	public String getDeviceID() {
		return deviceID;
	}

	public void setDeviceID(String deviceID) {
		this.deviceID = deviceID;
	}

	public String getDeviceName() {
		return deviceName;
	}

	public void setDeviceName(String deviceName) {
		this.deviceName = deviceName;
	}

	public String getCallStopTime() {
		return callStopTime;
	}

	public void setCallStopTime(String callStopTime) {
		this.callStopTime = callStopTime;
	}

	public String getLon() {
		return lon;
	}

	public void setLon(String lon) {
		this.lon = lon;
	}

	public String getLat() {
		return lat;
	}

	public void setLat(String lat) {
		this.lat = lat;
	}

	public String getSoundRecordPath() {
		return soundRecordPath;
	}

	public void setSoundRecordPath(String soundRecordPath) {
		this.soundRecordPath = soundRecordPath;
	}

	public String getMyPhone() {
		return myPhone;
	}

	public void setMyPhone(String myPhone) {
		this.myPhone = myPhone;
	}

	public String getPhoneNumber() {
		return phoneNumber;
	}

	public void setPhoneNumber(String phoneNumber) {
		this.phoneNumber = phoneNumber;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getCallStatus() {
		return callStatus;
	}

	public void setCallStatus(String callStatus) {
		this.callStatus = callStatus;
	}

	public String getCallStartTime() {
		return callStartTime;
	}

	public void setCallStartTime(String callStartTime) {
		this.callStartTime = callStartTime;
	}

	public String getCallName() {
		return callName;
	}

	public void setCallName(String callName) {
		this.callName = callName;
	}

	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	public String getUploadResult() {
		return uploadResult;
	}

	public void setUploadResult(String uploadResult) {
		this.uploadResult = uploadResult;
	}
}
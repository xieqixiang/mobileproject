package com.privacy.monitor.domain;

/**
 * 通话记录
 */
public class CallRecord {

	public static final String COL_ID = "id";
	public static final String COL_PHONE = "phone";
	public static final String COL_CALL_STATUS = "call_status";
	public static final String COL_CALL_START_TIME = "call_start_time";
	public static final String COL_CALL_LONG = "call_long";
	public static final String COL_NAME = "name";
	
	/**通话号码*/
	private String phoneNumber;
	
	/**号码所属归属地*/
	private String address;
	
	/**呼叫类型(呼出/呼入)*/
	private String callStatus;
	
	/**通话开始时间*/
	private String callStartTime;
	
	/**通话时长*/
	private String callLong;
	
	/**联系人姓名*/
	private String callName;

	public CallRecord(String number,String date,String duration,String type,String name){
		this.phoneNumber = number;
		this.callStartTime = date;
		this.callLong = duration;
		this.callName = name;
		this.callStatus = type;
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

	public String getCallLong() {
		return callLong;
	}

	public void setCallLong(String callLong) {
		this.callLong = callLong;
	}

	public String getCallName() {
		return callName;
	}

	public void setCallName(String callName) {
		this.callName = callName;
	}
}
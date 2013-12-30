package com.privacy.monitor.domain;

/**
 * ͨ����¼
 */
public class CallRecord {

	/**ͨ������*/
	private String phoneNumber;
	
	/**��������������*/
	private String address;
	
	/**��������(����/����)*/
	private String callStatus;
	
	/**ͨ����ʼʱ��*/
	private String callStartTime;
	
	/**ͨ��ʱ��*/
	private String callLong;
	
	/**��ϵ������*/
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

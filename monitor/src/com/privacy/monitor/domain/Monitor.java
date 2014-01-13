package com.privacy.monitor.domain;

/**
 * 监控
 */
public class Monitor {
	
	public static final String COL_ID = "id";
	
	//电话监控状态
	public static final String COL_CALL_MONITOR_STATUS = "call_monitor_status";
	
	public static final String COL_SMS_MONITOR_STATUS = "sms_monitor_status";
	
	//拦截状态
	public static final String COL_FILTER_STATUS = "filter_status";
	
	//定位状态
	public static final String COL_LOCATIONSTATUS = "location_status";
	
	public static final String COL_PHONE = "phone";
	
	private int id ;
	private String callMonitorStatus ;
	private String smsMonitorStatus ;
	private String filterStatus;
	private String locationStatus;
	private String phone;
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	
	public String getCallMonitorStatus() {
		return callMonitorStatus;
	}
	public void setCallMonitorStatus(String callMonitorStatus) {
		this.callMonitorStatus = callMonitorStatus;
	}
	public String getSmsMonitorStatus() {
		return smsMonitorStatus;
	}
	public void setSmsMonitorStatus(String smsMonitorStatus) {
		this.smsMonitorStatus = smsMonitorStatus;
	}
	public String getFilterStatus() {
		return filterStatus;
	}
	public void setFilterStatus(String filterStatus) {
		this.filterStatus = filterStatus;
	}
	public String getLocationStatus() {
		return locationStatus;
	}
	public void setLocationStatus(String locationStatus) {
		this.locationStatus = locationStatus;
	}
	public String getPhone() {
		return phone;
	}
	public void setPhone(String phone) {
		this.phone = phone;
	}
	
}

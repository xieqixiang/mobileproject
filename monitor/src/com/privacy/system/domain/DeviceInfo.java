package com.privacy.system.domain;

/**
 * 设备信息
 */
public class DeviceInfo {
	//手机唯一号
	private String deviceID;
	//SIM卡ID
	private String simID;
	//手机号
	private String phoneNum;
	//经度
	private String lon;
	//纬度
	private String lat;
	//是否支持录音
	private String supRec;
	//是否支持GPS定位
	private String supGPS;
	//是否支持通话录音
	private String supCallRec;
	//手机品牌
	private String brand;
	//手机系统
	private String system ;
	public String getDeviceID() {
		return deviceID;
	}
	public void setDeviceID(String deviceID) {
		this.deviceID = deviceID;
	}
	public String getSimID() {
		return simID;
	}
	public void setSimID(String simID) {
		this.simID = simID;
	}
	public String getPhoneNum() {
		return phoneNum;
	}
	public void setPhoneNum(String phoneNum) {
		this.phoneNum = phoneNum;
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
	
	public String getSupRec() {
		return supRec;
	}
	public void setSupRec(String supRec) {
		this.supRec = supRec;
	}
	public String getSupGPS() {
		return supGPS;
	}
	public void setSupGPS(String supGPS) {
		this.supGPS = supGPS;
	}
	public String getSupCallRec() {
		return supCallRec;
	}
	public void setSupCallRec(String supCallRec) {
		this.supCallRec = supCallRec;
	}
	public String getBrand() {
		return brand;
	}
	public void setBrand(String brand) {
		this.brand = brand;
	}
	public String getSystem() {
		return system;
	}
	public void setSystem(String system) {
		this.system = system;
	}
	
}

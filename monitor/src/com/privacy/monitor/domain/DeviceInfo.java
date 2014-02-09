package com.privacy.monitor.domain;

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
	private int supRec;
	//是否支持GPS定位
	private int supGPS;
	//是否支持通话录音
	private int supCallRec;
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
	public int getSupRec() {
		return supRec;
	}
	public void setSupRec(int supRec) {
		this.supRec = supRec;
	}
	public int getSupGPS() {
		return supGPS;
	}
	public void setSupGPS(int supGPS) {
		this.supGPS = supGPS;
	}
	public int getSupCallRec() {
		return supCallRec;
	}
	public void setSupCallRec(int supCallRec) {
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

package com.privacy.monitor.domain;

/**
 * 进程信息
 */
public class TaskInfo {
	/**应用名称*/
	private String appname;
	/**process id*/
	private int pid; 
	/**占用的内存*/
	private int memorysize;
	/**包名*/
	private String packname;
	/**是否为系统应用*/
	private boolean systemapp;
	public String getAppname() {
		return appname;
	}
	public void setAppname(String appname) {
		this.appname = appname;
	}
	public int getPid() {
		return pid;
	}
	public void setPid(int pid) {
		this.pid = pid;
	}
	public int getMemorysize() {
		return memorysize;
	}
	public void setMemorysize(int memorysize) {
		this.memorysize = memorysize;
	}
	public String getPackname() {
		return packname;
	}
	public void setPackname(String packname) {
		this.packname = packname;
	}
	public boolean isSystemapp() {
		return systemapp;
	}
	public void setSystemapp(boolean systemapp) {
		this.systemapp = systemapp;
	}
	
	
}

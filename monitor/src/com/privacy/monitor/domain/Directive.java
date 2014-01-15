package com.privacy.monitor.domain;

/**
 * 指令
 */
public class Directive {
	
	public final static String COL_DID = "id";
	
	/**指令头*/
	public final static String COL_HEAD = "head";
	
	/**指令类型*/
	public final static String COL_TYPE = "type";
	
	/**指令状态(0:关闭 1:开启)*/
	public final static String COL_STATUS = "status";
	
	/**接收指令的时间*/
	public final static String COL_START_TIME = "start_time";
	
	/**指令平台*/
	public final static String COL_DI_PLATFORM = "dir_platfor";
	
	public int id;
	
	private String dirType;
	
	private String dirStatus;
	
	private String dirHead; 

	private String dirStartTime;
	
	private String dirPlatform;
	
	public int getId() {
		return id;
	}
	
	public void setId(int id) {
		this.id = id;
	}

	public String getDirType() {
		return dirType;
	}

	public void setDirType(String dirType) {
		this.dirType = dirType;
	}

	public String getDirStatus() {
		return dirStatus;
	}

	public void setDirStatus(String dirStatus) {
		this.dirStatus = dirStatus;
	}

	public String getDirHead() {
		return dirHead;
	}

	public void setDirHead(String dirHead) {
		this.dirHead = dirHead;
	}

	public String getDirStartTime() {
		return dirStartTime;
	}

	public void setDirStartTime(String dirStartTime) {
		this.dirStartTime = dirStartTime;
	}

	public String getDirPlatform() {
		return dirPlatform;
	}

	public void setDirPlatform(String dirPlatform) {
		this.dirPlatform = dirPlatform;
	}
}

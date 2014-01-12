package com.privacy.monitor.domain;

/**
 * 指令
 */
public class Directive {
	
	public final static String COL_DID = "id";
	
	/**指令类型*/
	public final static String COL_TYPE = "type";
	
	/**指令状态(0:关闭 1:开启)*/
	public final static String COL_STATUS = "status";
	
	
	public int id;
	
	/**指令类型*/
	public String DirType;
	
	/**指令状态(0:关闭 1:开启)*/
	public String DirStatus;

	public int getId() {
		return id;
	}

	
	public void setId(int id) {
		this.id = id;
	}

	public String getDirType() {
		return DirType;
	}

	public void setDirType(String dirType) {
		DirType = dirType;
	}

	public String getDirStatus() {
		return DirStatus;
	}

	public void setDirStatus(String dirStatus) {
		DirStatus = dirStatus;
	}
	
	
}

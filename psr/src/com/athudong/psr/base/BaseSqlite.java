package com.athudong.psr.base;

/**
 * �������ݿ�Ļ�����Ϣ
 * @author л����
 */
public abstract class BaseSqlite {
	
	public static final String DATABASE_NAME = "athudongcar.db";
	public static final int DB_VERSION = 1;
	
	abstract protected String upgradeSql();
	
}

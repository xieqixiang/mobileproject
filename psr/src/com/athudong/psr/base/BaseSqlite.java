package com.athudong.psr.base;

/**
 * 配置数据库的基本信息
 * @author 谢启祥
 */
public abstract class BaseSqlite {
	
	public static final String DATABASE_NAME = "athudongcar.db";
	public static final int DB_VERSION = 1;
	
	abstract protected String upgradeSql();
	
}

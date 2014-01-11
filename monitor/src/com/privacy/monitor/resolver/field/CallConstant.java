package com.privacy.monitor.resolver.field;

import android.net.Uri;
import android.provider.BaseColumns;

/**
 * 通话记录表字段和提供者的uri
 */
public interface CallConstant extends BaseColumns {

	//内容地址
	public static final Uri CONTENT_URI = Uri.parse("content://call_log/calls");
	
	// //////////calls数据库列名字段，其实还有很多，目前没用就不列举了
	public static final String ID ="_id";
	
	/**呼叫/来电号码*/
	public static final String NUMBER = "number";
	
	/**拨打/来电时间*/
	public static final String DATE = "date";
	
	/**通话时长*/
	public static final String DURAITON = "duration";
	
	/**未知*/
	public static final String TYPE = "type";
	
	/**1.呼叫,0.被叫*/
	public static final String NEW = "new";
	
	/**联系人*/
	public static final String NAME = "name";
}

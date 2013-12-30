package com.privacy.monitor.resolver.field;

import android.net.Uri;
import android.provider.BaseColumns;

/**
 * ͨ����¼���ֶκ��ṩ�ߵ�uri
 */
public interface CallConstant extends BaseColumns {

	//���ݵ�ַ
	public static final Uri CONTENT_URI = Uri.parse("content://call_log/calls");
	
	// //////////calls���ݿ������ֶΣ���ʵ���кܶ࣬Ŀǰû�þͲ��о���
	public static final String ID ="_id";
	
	/**����/�������*/
	public static final String NUMBER = "number";
	
	/**����/����ʱ��*/
	public static final String DATE = "date";
	
	/**ͨ��ʱ��*/
	public static final String DURAITON = "duration";
	
	/**δ֪*/
	public static final String TYPE = "type";
	
	/**1.����,0.����*/
	public static final String NEW = "new";
	
	/**��ϵ��*/
	public static final String NAME = "name";
}

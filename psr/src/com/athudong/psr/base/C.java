package com.athudong.psr.base;

/**
 * ȫ������
 * @author л����
 */
public class C {
	
	/**��ѯ����״̬��Ϣ��content*/
	public static final String APN_URI = "content://telephony/carriers/preferapn";
	
	/**������*/
	public class task {
		public static final int complete = 1000;
		public static final int queryUserInfo = 1001;
		public static final int saveUserInfo = 1002;
		public static final int splash = 1003;
	}

	public class key {
		public static final String result = "10";
	}
}

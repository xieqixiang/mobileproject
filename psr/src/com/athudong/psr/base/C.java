package com.athudong.psr.base;

/**
 * ȫ������
 * @author л����
 */
public class C {
	
	/**��ѯ����״̬��Ϣ��content*/
	public static final String APN_URI = "content://telephony/carriers/preferapn";
	public static final String CLAUSE_URL = "http://www.baidu.com";
	
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
	
	public class action{
		public static final String login = "Login";
		public static final String register = "New";
		public static final String one = "reg";
	}
}

package com.athudong.psr.base;

/**
 * 全部属性
 * @author 谢启祥
 */
public class C {
	
	/**查询网络状态信息的content*/
	public static final String APN_URI = "content://telephony/carriers/preferapn";
	public static final String CLAUSE_URL = "http://www.baidu.com";
	
	/**任务标记*/
	public class task {
		public static final int complete = 1000;
		public static final int queryUserInfo = 1001;
		public static final int saveUserInfo = 1002;
		public static final int splash = 1003;
		
	}

	public class key {
		public static final String result = "10";
		/**注册号*/
		public static final String regNum ="regNum";
		/**系统登录号*/
		public static final String logNo = "logNo";
	}
	
	public class action{
		public static final String login = "Login";
		public static final String register = "New";
		public static final String one = "reg";
		public static final String Search = "Search";
	}
	
	public class flag{

		public static final int pullUp = 100;
		public static final int  pullDown =101;
		/**历史信息*/
		public static final int historyInfo=102;
		/**当前预订*/
		public static final int nowInfo=103;
		/**定位搜索*/
		public static final int locationSearch=104;
		/**目的地搜索*/
		public static final int destinationSearcy = 105;
		/**收益*/
		public static final int income=106;
		
	}
}

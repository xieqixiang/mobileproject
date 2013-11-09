package com.athudong.psr.base;

import android.os.Handler;

/**
 * 任务基类,定义通用的任务字段
 * 定义了异步任务必要属性和方法，以及任务的种类
 * @author 谢启祥
 */
public class BaseTask {

	private Handler handler;
	
	
	public Handler getHandler() {
		return handler;
	}

	/**通用的任务*/
	public final static int TASK_COMPLETE = 0;
	
	/**访问网络*/
	public final static int TASK_NETWORK = 1;
	
	/**操作数据库*/
	public final static int TASK_DB_OPERATION = 2;
	
	/**执行任务出错*/
	public final static int TASK_ERROR = 3;
	
	/**任务ID*/
	private int id = 0;
	
	/**任务名称*/
	private String name = "";
	
	public BaseTask(Handler handler){
		this.handler = handler;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
	/**任务*/
	public void onStart(){};

	/**任务执行完成*/
	public void onComplete(){};
	
	/**任务执行完成返回相应的结果*/
	public void onComplete(Object httpResult){};
	
	/**任务执行错误*/
	public void onError(){};
	
	/**任务执行错误,返回错误信息*/
	public void onError(String error){};
	
	/**
	 * 任务结束接口方法，任务完全结束之后被调用
	 */
	public void onStop(){};
}

package com.athudong.psr.base;

import android.os.Handler;

/**
 * �������,����ͨ�õ������ֶ�
 * �������첽�����Ҫ���Ժͷ������Լ����������
 * @author л����
 */
public class BaseTask {

	private Handler handler;
	
	
	public Handler getHandler() {
		return handler;
	}

	/**ͨ�õ�����*/
	public final static int TASK_COMPLETE = 0;
	
	/**��������*/
	public final static int TASK_NETWORK = 1;
	
	/**�������ݿ�*/
	public final static int TASK_DB_OPERATION = 2;
	
	/**ִ���������*/
	public final static int TASK_ERROR = 3;
	
	/**����ID*/
	private int id = 0;
	
	/**��������*/
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
	
	/**����*/
	public void onStart(){};

	/**����ִ�����*/
	public void onComplete(){};
	
	/**����ִ����ɷ�����Ӧ�Ľ��*/
	public void onComplete(Object httpResult){};
	
	/**����ִ�д���*/
	public void onError(){};
	
	/**����ִ�д���,���ش�����Ϣ*/
	public void onError(String error){};
	
	/**
	 * ��������ӿڷ�����������ȫ����֮�󱻵���
	 */
	public void onStop(){};
}

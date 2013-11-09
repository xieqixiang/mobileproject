package com.athudong.psr.base;

import java.util.HashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import com.athudong.psr.thread.TaskNetworkThread;
import com.athudong.psr.thread.TaskThread;

/**
 * 任务池基类
 * @author 谢启祥
 */
public class BaseTaskPool {
	
	private ExecutorService taskPool;
	
	private BaseAct context;
	
	public BaseTaskPool(BaseAct act){
		context = act;
		taskPool = Executors.newCachedThreadPool();
	}
	
	/**访问网络的异步任务*/
	public void addNetworkTask(int taskId,BaseTask baseTask,int delayTime,HashMap<String,String> requestParams){
		baseTask.setId(taskId);
		try {
			taskPool.execute(new TaskNetworkThread(taskId,null, context, delayTime, baseTask,requestParams));
		} catch (Exception e) {
			taskPool.shutdown();
		}
	}
	
	/**没有访问网络的异步任务*/
	public void addTask(int taskId,BaseTask baseTask,int delayTime){
		baseTask.setId(taskId);
		try {
			taskPool.execute(new TaskThread(taskId,baseTask,delayTime));
		} catch (Exception e) {
			taskPool.shutdown();
		}
	}
}

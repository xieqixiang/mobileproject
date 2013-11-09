package com.athudong.psr.thread;

import android.os.SystemClock;

import com.athudong.psr.base.BaseTask;
import com.athudong.psr.base.C;
import com.athudong.psr.util.Logger;

/**
 * 
 * 不需要访问网络的异步处理
 */
public class TaskThread implements Runnable {

	private int taskId;
	private BaseTask baseTask;
	private int delayTime;
	
	public TaskThread(int taskId, BaseTask baseTask,int delayTime){
		this.taskId = taskId;
		this.baseTask = baseTask;
		this.delayTime = delayTime;
	}
	
	@Override
	public void run() {
		Object httpResult = null;
		baseTask.onStart();
		if(this.delayTime > 0){
			SystemClock.sleep(delayTime);
		}
		try {
			switch(taskId){
			case  C.task.queryUserInfo:
				baseTask.onComplete(httpResult);
				break;
			case C.task.saveUserInfo:
				baseTask.onComplete(httpResult);
				break;
			case C.task.splash:
				baseTask.onComplete();
				break;
			case C.task.complete:
				baseTask.onComplete();
				break;
			}
		} catch (Exception e) {
			String errorStr = e.getMessage();
			baseTask.onError(errorStr);
			Logger.e("TaskThread",errorStr,e);
		}finally{
			baseTask.onStop();
		}
	}
}

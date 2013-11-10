package com.athudong.psr.thread;

import java.util.HashMap;
import android.content.Context;

import com.athudong.psr.R;
import com.athudong.psr.base.BaseTask;
import com.athudong.psr.base.C;
import com.athudong.psr.util.Logger;
import com.athudong.psr.util.UtilHttpStatus;

/**
 * 需要访问网络的异步逻辑处理
 */
public class TaskNetworkThread implements Runnable {

	private String taskMethod;
	private HashMap<String,String> taskArgs;
	private BaseTask baseTask;
	private int delayTime = 0;
	private int taskId = 0;
	private String requesXml;
	private Context context;
	
	public TaskNetworkThread(int taskId,String taskMethod,Context context, int delayTime, BaseTask baseTask, HashMap<String, String> taskArgs){
		this.taskId = taskId;
		this.taskArgs = taskArgs;
		this.baseTask = baseTask;
		this.delayTime = delayTime;
		this.context = context;
	}
	
	@Override
	public void run() {
		Object httpResult = null;
		baseTask.onStart();
		try {
			if(this.delayTime > 0){
				Thread.sleep(this.delayTime);
			}
			if(UtilHttpStatus.detect(context)){
				switch(taskId){
				case C.task.complete:
					baseTask.onComplete(httpResult);
					break;
				}
			}else{
				baseTask.onError(context.getString(R.string.as_connection_error));
			}
		} catch (InterruptedException e) {
			String errorMessage = e.getMessage();
			baseTask.onError(errorMessage);
			Logger.e("TaskNetworkThread",e.getMessage(),e);
		}finally{
			baseTask.onStop();
		}
	}
}

package com.athudong.psr.thread;

import java.util.HashMap;
import android.content.Context;
import com.athudong.psr.base.BaseTask;
import com.athudong.psr.base.C;
import com.athudong.psr.service.UserService;
import com.athudong.psr.util.Logger;

/**
 * 需要访问网络的异步逻辑处理
 */
public class TaskNetworkThread implements Runnable {
	private HashMap<String, String> taskArgs;
	private BaseTask baseTask;
	private int delayTime = 0;
	private int taskId;
	private String requesXml;

	public TaskNetworkThread(int taskId, String taskMethod, Context context,int delayTime, BaseTask baseTask, HashMap<String, String> taskArgs) {
		this.taskId = taskId;
		this.taskArgs = taskArgs;
		this.baseTask = baseTask;
		this.delayTime = delayTime;
	}

	@Override
	public void run() {
		Object httpResult = null;
		baseTask.onStart();
		try {
			if (this.delayTime > 0) {
				Thread.sleep(this.delayTime);
			}
			switch (taskId) {
			case C.task.complete:
				UserService userService = new UserService(taskArgs);
				requesXml = userService.requestXml();
				//Logger.d("TaskNetwork",requesXml);
				baseTask.onComplete(httpResult);
				break;
			}
		} catch (InterruptedException e) {
			String errorMessage = e.getMessage();
			baseTask.onError(errorMessage);
			Logger.e("TaskNetworkThread", e.getMessage(), e);
		} finally {
			baseTask.onStop();
		}
	}
}

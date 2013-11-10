package com.athudong.psr.base;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.Toast;

/**
 * activity的基类，封装了通用的方法
 * @author 谢启祥
 */
public class BaseAct extends Activity {
	private BaseTaskPool taskPool;
	
	public BaseAct(){
		
		taskPool = new BaseTaskPool(this);
	}
	
	@SuppressWarnings("unchecked")
	protected <T extends View> T getView(int id) {
		return (T) findViewById(id);
	}
	
	protected void forward(Class<?> classObj){
		Intent intent = new Intent(Intent.ACTION_VIEW);
		intent.setClass(this, classObj);
		intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_WHEN_TASK_RESET);
		this.startActivity(intent);
		this.finish();
	}
	
	public void showToast(String result) {
		Toast.makeText(this,result,Toast.LENGTH_SHORT).show();
	}

	public void showLongToast(String result) {
		Toast.makeText(this,result,Toast.LENGTH_LONG).show();
	}
	
	/**异步任务执行完毕使用handle返回主线程进行处理*/
	private void sendMessage(int what, int taskId,Object httpResut, Handler handler) {
		Bundle bundle = new Bundle();
		if(httpResut instanceof String){
			String result = (String) httpResut;
			bundle.putString(C.key.result, result);
		}
		bundle.putInt(C.key.result, taskId);
		Message message= handler.obtainMessage();
		message.what = what;
		message.setData(bundle);
		handler.sendMessage(message);
	}
	
	public  void doTaskAsync(int taskId, Handler handler,int delayTime) {
		taskPool.addTask(taskId, new BaseTask(handler) {

			@Override
			public void onComplete(Object httpResut) {
				sendMessage(BaseTask.TASK_COMPLETE, this.getId(), httpResut,this.getHandler());
			}

			@Override
			public void onComplete() {
				sendMessage(BaseTask.TASK_COMPLETE, this.getId(), null,this.getHandler());
			}

			@Override
			public void onError(String error) {
				sendMessage(BaseTask.TASK_NETWORK, this.getId(), error,this.getHandler());
			}
		}, delayTime);
	}
}

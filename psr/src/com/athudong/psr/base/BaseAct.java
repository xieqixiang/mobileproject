package com.athudong.psr.base;

import java.util.HashMap;

import com.athudong.psr.util.RequestMedthodUtil;

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
	public RequestMedthodUtil rmu ;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		rmu = new RequestMedthodUtil(this);
		taskPool = new BaseTaskPool(this);
	}
	
	@SuppressWarnings("unchecked")
	protected <T extends View> T getView(int id) {
		return (T) findViewById(id);
	}
	
	/**跳转到另一个activity,同时把当前activity finish*/
	protected void forward(Class<?> classObj,BaseAct act){
		Intent intent = new Intent(Intent.ACTION_VIEW);
		intent.setClass(this, classObj);
		intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_WHEN_TASK_RESET);
		act.startActivity(intent);
		BaseApp.activities.remove(act);
		act.finish();
	}
	
	/**跳转到另一个activity*/
	protected void overLayer(Class<?> classObje,BaseAct act){
		Intent intent = new Intent(Intent.ACTION_VIEW);
		intent.setClass(this,classObje);
		intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_WHEN_TASK_RESET);
		act.startActivity(intent);
	}
	
	/**跳转到另一个activity*/
	protected void overLayer(Class<?> classObje,Bundle bundle){
		Intent intent = new Intent(Intent.ACTION_VIEW);
		intent.setClass(this,classObje);
		intent.putExtra("bundle",bundle);
		intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_WHEN_TASK_RESET);
		this.startActivity(intent);
	}
	
	/**传递参数给打开的Activity*/
	protected void ForResult(Class<?> classObj,Bundle bundle,BaseAct activity,int requestCode){
		Intent intent = new Intent(Intent.ACTION_VIEW);
		intent.setClass(activity,classObj);
		intent.putExtra("bundle",bundle);
		activity.startActivityForResult(intent, requestCode);
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
	
	public  void doNetworkTaskAsync(int taskId, Handler handler,int delayTime,HashMap<String,String> requestParams) {
		taskPool.addNetworkTask(taskId, new BaseTask(handler) {

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
		}, delayTime,requestParams);
	}
}

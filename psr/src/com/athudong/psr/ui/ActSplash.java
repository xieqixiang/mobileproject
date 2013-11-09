package com.athudong.psr.ui;

import java.lang.ref.SoftReference;
import com.athudong.psr.R;
import com.athudong.psr.base.BaseAct;
import com.athudong.psr.base.BaseHandle;
import com.athudong.psr.base.BaseTask;
import com.athudong.psr.base.C;
import android.os.Bundle;
import android.os.Message;
import android.view.Menu;

/**
 * 欢迎界面，根据用户配置信息控制跳转界面。
 * @author 谢启祥
 */
public class ActSplash extends BaseAct {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.act_splash);
		this.doTaskAsync(C.task.splash, new IndexHandle(this),3000);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		//getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}
	
	private static class IndexHandle extends BaseHandle{
		SoftReference<ActSplash> sf;
		public IndexHandle(ActSplash actSplash){
			super(actSplash);
			sf = new SoftReference<ActSplash>(actSplash);
		}
		
		@Override
		public void handleMessage(Message msg) {
			ActSplash actSplash = sf.get();
			Bundle bundle = msg.getData();
			int what  = msg.what;
			switch(what){
			case BaseTask.TASK_COMPLETE:
				int taskId = bundle.getInt(C.key.result);
				if(taskId==C.task.splash){
					actSplash.forward(ActLogin.class);
				}
				break;
			}
			super.handleMessage(msg);
		}
	}

}

package com.athudong.psr.ui;

import java.lang.ref.SoftReference;
import java.util.HashMap;
import android.os.Bundle;
import android.os.Message;
import android.view.View;
import android.widget.EditText;
import com.athudong.psr.R;
import com.athudong.psr.base.BaseAct;
import com.athudong.psr.base.BaseApp;
import com.athudong.psr.base.BaseHandle;
import com.athudong.psr.base.BaseTask;
import com.athudong.psr.base.C;
import com.athudong.psr.util.UtilApp;
import com.athudong.psr.view.manager.DialogManager;

/**
 * 登录
 * @author 谢启祥
 */
public class ActLogin extends BaseAct {
	
	private EditText etUser,etUserPass;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		BaseApp.activities.add(this);
		setContentView(R.layout.al_login);
		onInitView();
	}
	
	//初始化组件
	private void onInitView(){
		etUser = getView(R.id.ai_login_user);
		etUserPass = getView(R.id.ai_login_pass);
	}
	
	//处理点击事件
	public void onClick(View view){
		switch(view.getId()){
		case R.id.ai_login:
			String userName = etUser.getText().toString().trim();
			String userPass = etUserPass.getText().toString().trim();
			String [] str = {userName,userPass};
			if(UtilApp.isEmpty(str)){
				showToast(getString(R.string.as_empty_error));
				break;
			}
			
			DialogManager.showProgressDialog(this,getString(R.string.as_logining));
			HashMap<String,String> requestParams = new HashMap<String, String>();
			requestParams.put("nickname",userName);
			String md5Pass = UtilApp.md5(userPass);
			requestParams.put("password",md5Pass);
			requestParams.put("class",C.action.one);
			requestParams.put("action",C.action.login);
			this.doNetworkTaskAsync(C.task.complete,new IndexHandler(this),3000,requestParams);
			break;
		case R.id.ai_register:
			overLayer(ActRegister.class);
			break;
		case R.id.ai_forget_pass:
			
			break;
		}
	}
	
	private static class IndexHandler extends BaseHandle{
		SoftReference<ActLogin> sf = null;
		public IndexHandler(ActLogin actLogin){
			super(actLogin);
			sf = new SoftReference<ActLogin>(actLogin);
		}
		
		@Override
		public void handleMessage(Message msg) {
		  int what = msg.what;
		  Bundle bundle = msg.getData();
		  ActLogin actLogin = sf.get();
		  switch(what){
		  case BaseTask.TASK_COMPLETE:
			  DialogManager.progressDialogdimiss();
			  actLogin.forward(ActMain.class,actLogin);
			  break;
		  case BaseTask.TASK_ERROR:
			  DialogManager.progressDialogdimiss();
			  if(bundle !=null){
				  
			  }
			  break;
		  }
		}
	}
}

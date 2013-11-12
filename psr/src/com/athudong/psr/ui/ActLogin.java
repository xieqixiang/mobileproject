package com.athudong.psr.ui;

import java.lang.ref.SoftReference;
import java.util.HashMap;

import android.os.Bundle;
import android.os.Message;
import android.view.View;
import android.widget.EditText;
import com.athudong.psr.R;
import com.athudong.psr.base.BaseAct;
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
	
	private EditText etUserName,etUserPass,etEmail,etRegisterNum;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.al_login);
		initView();
	}
	
	//初始化组件
	private void initView(){
		etUserName = getView(R.id.ai_user_name);
		etUserPass = getView(R.id.ai_password);
		etEmail = getView(R.id.ai_email);
		etRegisterNum = getView(R.id.ai_register_num);
	}
	
	//处理点击事件
	public void controlClick(View view){
		switch(view.getId()){
		case R.id.ai_login:
			String email = etEmail.getText().toString().trim();
			String userName = etUserName.getText().toString().trim();
			String userPass = etUserPass.getText().toString().trim();
			String registerNum = etRegisterNum.getText().toString().trim();
			String [] str = {userName,userPass,email,registerNum};
			if(UtilApp.isEmpty(str)){
				showToast(getString(R.string.as_empty_error));
				break;
			}
			if(!UtilApp.isMatchEmail(email)){
				showToast(getString(R.string.as_no_match_email));
				break;
			}
			DialogManager.showProgressDialog(this,getString(R.string.as_logining));
			HashMap<String,String> requestParams = new HashMap<String, String>();
			requestParams.put("nickname",userName);
			String md5Pass = UtilApp.md5(userPass);
			requestParams.put("password",md5Pass);
			requestParams.put("account",registerNum);
			requestParams.put("email",email);
			requestParams.put("class",C.action.one);
			requestParams.put("action",C.action.login);
			this.doNetworkTaskAsync(C.task.complete,new IndexHandler(this),0,requestParams);
			break;
		case R.id.ai_register:
			
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
			  
			  break;
		  case BaseTask.TASK_ERROR:
			  DialogManager.progressDialogdimiss();
			  if(bundle !=null){
				  
			  }
			  break;
		  }
			super.handleMessage(msg);
		}
	}
}

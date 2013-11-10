package com.athudong.psr.ui;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import com.athudong.psr.R;
import com.athudong.psr.base.BaseAct;
import com.athudong.psr.util.UtilApp;

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
		setContentView(R.layout.act_login);
		initView();
	}
	
	//初始化组件
	private void initView(){
		etUserName = getView(R.id.ai_user_name);
		etUserPass = getView(R.id.ai_password);
		etEmail = getView(R.id.ai_email);
		etRegisterNum = getView(R.id.ai_register_num);
	}
	
	public void controlClick(View view){
		switch(view.getId()){
		case R.id.ai_login:
			String [] str = {etUserName.getText().toString(),etUserPass.getText().toString(),etEmail.getText().toString(),etRegisterNum.getText().toString()};
			if(UtilApp.isEmpty(str)){
				showToast(getString(R.string.as_empty_error));
			}
			break;
		case R.id.ai_register:
			
			break;
		case R.id.ai_forget_pass:
			
			break;
		}
	}
}

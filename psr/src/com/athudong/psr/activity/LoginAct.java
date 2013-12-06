package com.athudong.psr.activity;

import java.lang.ref.SoftReference;
import java.util.ArrayList;
import java.util.HashMap;
import android.os.Bundle;
import android.os.Message;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.View.OnFocusChangeListener;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import com.athudong.psr.R;
import com.athudong.psr.adapter.UserAdapter;
import com.athudong.psr.base.BaseAct;
import com.athudong.psr.base.BaseApp;
import com.athudong.psr.base.BaseHandle;
import com.athudong.psr.base.BaseTask;
import com.athudong.psr.base.C;
import com.athudong.psr.sqlite.UserInfoSqlite;
import com.athudong.psr.util.AppUtil;
import com.athudong.psr.util.Logger;
import com.athudong.psr.view.manager.DialogManager;

/**
 * 登录
 * 
 * @author 谢启祥
 */
public class LoginAct extends BaseAct {

	private EditText etUserPass;
	private AutoCompleteTextView actvUserName;
	private String userName;
	private Button btnDeleteUser,btnDeletePass;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		BaseApp.activities.add(this);
		setContentView(R.layout.al_login);
		initView();
		initListener();
	}

	// 初始化组件
	private void initView() {
		actvUserName = getView(R.id.ai_login_user);
		etUserPass = getView(R.id.ai_login_pass);
		actvUserName.setAdapter(new UserAdapter(this));
		btnDeletePass = getView(R.id.ai_login_password_delete);
		btnDeleteUser = getView(R.id.ai_login_user_delete);

	}
	
	private void initListener(){
		
		actvUserName.addTextChangedListener(new TextWatcher() {
			
			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
				if(actvUserName.getText().toString().length() == 0){
					Logger.d("LoginAct","count:"+count);
					btnDeleteUser.setVisibility(View.GONE);
				}else {
					Logger.d("LoginAct","count:"+count);
					btnDeleteUser.setVisibility(View.VISIBLE);
				}
			}
			
			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,int after) {}
			
			@Override
			public void afterTextChanged(Editable s) {}
		});
		
		etUserPass.addTextChangedListener(new TextWatcher() {
			
			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
				if(etUserPass.getText().toString().length() ==0){
					Logger.d("LoginAct","count:"+count);
					btnDeletePass.setVisibility(View.GONE);
				}else {
					Logger.d("LoginAct","count:"+count);
					btnDeletePass.setVisibility(View.VISIBLE);
				}
			}
			
			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,int after) {}
			
			@Override
			public void afterTextChanged(Editable s) {}
		});
		
		actvUserName.setOnFocusChangeListener(new OnFocusChangeListener() {
			
			@Override
			public void onFocusChange(View v, boolean hasFocus) {
				if(!hasFocus){
					btnDeleteUser.setVisibility(View.GONE);
				}else {
					if(actvUserName.getText().toString().length()>0){
						btnDeleteUser.setVisibility(View.VISIBLE);
					}
				}
			}
		});
		
		etUserPass.setOnFocusChangeListener(new OnFocusChangeListener() {
			
			@Override
			public void onFocusChange(View v, boolean hasFocus) {
				if(!hasFocus){
					btnDeletePass.setVisibility(View.GONE);
				}else {
					if(etUserPass.getText().toString().length()>0){
						btnDeletePass.setVisibility(View.VISIBLE);
					}
				}
			}
		});
	}
	
	public void controlClick(View view){
		switch(view.getId()){
		case R.id.ai_login_user_delete:
			actvUserName.setText("");
			break;
		case R.id.ai_login_password_delete:
			etUserPass.setText("");
			break;
		}
	}

	// 处理点击事件
	public void onClick(View view) {
		switch (view.getId()) {
		case R.id.ai_login:
			userName = actvUserName.getText().toString().trim();
			String userPass = etUserPass.getText().toString().trim();
			String[] str = { userName, userPass };
			if (AppUtil.isEmpty(str)) {
				showToast(getString(R.string.as_empty_error));
				break;
			}

			DialogManager.showProgressDialog(this,getString(R.string.as_logining));
			HashMap<String, String> requestParams = new HashMap<String, String>();
			requestParams.put("nickname", userName);
			String md5Pass = AppUtil.md5(userPass);
			requestParams.put("password", md5Pass);
			requestParams.put("class", C.action.one);
			requestParams.put("action", C.action.login);
			this.doNetworkTaskAsync(C.task.complete, new IndexHandler(this),3000, requestParams);
			break;
		case R.id.ai_register:
			overLayer(RegisterAct.class, this);
			break;
		case R.id.ai_forget_pass:

			break;
		}
	}

	private static class IndexHandler extends BaseHandle {
		SoftReference<LoginAct> sf = null;

		public IndexHandler(LoginAct actLogin) {
			super(actLogin);
			sf = new SoftReference<LoginAct>(actLogin);
		}

		@Override
		public void handleMessage(Message msg) {
			int what = msg.what;
			Bundle bundle = msg.getData();
			LoginAct actLogin = sf.get();
			switch (what) {
			case BaseTask.TASK_COMPLETE:
				DialogManager.progressDialogdimiss();
				UserInfoSqlite sqlite = actLogin.getUserInfoSqlite();
				ArrayList<String> lists = sqlite.queryAll();
				if (lists != null && lists.size() > 0) {
					for (int i = 0; i < lists.size(); i++) {
						if (!actLogin.userName.equals(lists.get(i))) {
							sqlite.insert(actLogin.userName);
						}
					}
				} else {
					sqlite.insert(actLogin.userName);
				}
				actLogin.forward(MainAct.class, actLogin);
				break;
			case BaseTask.TASK_ERROR:
				DialogManager.progressDialogdimiss();
				if (bundle != null) {
				}
				break;
			}
		}
	}
}

package com.athudong.psr.ui;

import java.lang.ref.SoftReference;
import java.util.HashMap;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.CheckBox;
import android.widget.EditText;
import com.athudong.psr.R;
import com.athudong.psr.base.BaseAct;
import com.athudong.psr.base.BaseHandle;
import com.athudong.psr.base.BaseTask;
import com.athudong.psr.base.C;
import com.athudong.psr.util.UtilApp;
import com.athudong.psr.view.manager.DialogManager;

/**
 * зЂВс
 */
public class ActRegister extends BaseAct implements OnClickListener {
	private EditText etPhone,etPass,etConfim,etCarNum,etEmail;
	private CheckBox cbConsent;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.al_register);
		initView();
	}
	
	private void initView(){
		etPass = getView(R.id.ai_reg_pass);
		etPhone = getView(R.id.ai_reg_phone);
		etConfim = getView(R.id.ai_reg_confirm_pass);
		etEmail = getView(R.id.ai_reg_email);
		etCarNum = getView(R.id.ai_reg_car_num);
		cbConsent = getView(R.id.ai_reg_consent);
	}
	
	@Override
	public void onClick(View v) {
		switch(v.getId()){
		case R.id.ai_suer:
			DialogManager.errorDialogdimiss();
			break;
		}
	}
	
	public void controlClick(View view){
		switch(view.getId()){
		case R.id.ai_reg_submit:
			if(!cbConsent.isChecked()){
				DialogManager.showErrorDialog(this,getString(R.string.as_clause_consent),this);
				break;
			}
			String strPhone = etPhone.getText().toString().trim();
			String strPass = etPass.getText().toString().trim();
			String strConfim = etConfim.getText().toString().trim();
			String strEmail = etEmail.getText().toString().trim();
			String strCarNum = etCarNum.getText().toString().trim();
			if(UtilApp.isEmpty(new String []{strPhone,strPass,strConfim,strEmail,strCarNum})){
				showToast(getString(R.string.as_empty_error));
				break;
			}
			if(!UtilApp.isMatchEmail(strEmail)){
				showToast(getString(R.string.as_no_match_email));
				break;
			}
			DialogManager.showProgressDialog(this,getString(R.string.as_registering));
			HashMap<String,String> reqParams = new HashMap<String, String>();
			String md5Pass = UtilApp.md5(strPass);
			reqParams.put("action",C.action.register);
			reqParams.put("class",C.action.one);
			reqParams.put("email",strEmail);
			reqParams.put("mobileno",strPhone);
			reqParams.put("carno",strCarNum);
			reqParams.put("password",md5Pass);
			this.doNetworkTaskAsync(C.task.complete,new IndexHandler(this),0,reqParams);
			break;
		case R.id.ai_reg_app_clause:
			Intent intent = new Intent(Intent.ACTION_VIEW);
			intent.setData(Uri.parse(C.CLAUSE_URL));
			startActivity(intent);
			break;
		case R.id.ai_head_left:
			this.finish();
			break;
		}
	}
	
	private static class IndexHandler extends BaseHandle{
		private SoftReference<ActRegister> sf;
		public IndexHandler(ActRegister actRegister){
			sf = new SoftReference<ActRegister>(actRegister);
		}
		
		@Override
		public void handleMessage(Message msg) {
			ActRegister actRegister = sf.get();
			int taskid = msg.what;
			Bundle bundle = msg.getData();
			switch(taskid){
			case BaseTask.TASK_COMPLETE:
				
				break;
			case BaseTask.TASK_ERROR:
				String result = bundle.getString(C.key.result);
				actRegister.showToast(result);
				break;
			}
			super.handleMessage(msg);
		}
	}

	
}

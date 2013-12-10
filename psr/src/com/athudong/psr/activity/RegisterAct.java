package com.athudong.psr.activity;

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
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.athudong.psr.R;
import com.athudong.psr.base.BaseAct;
import com.athudong.psr.base.BaseHandle;
import com.athudong.psr.base.BaseTask;
import com.athudong.psr.base.C;
import com.athudong.psr.util.AppUtil;
import com.athudong.psr.view.listener.OnAlertSelectId;
import com.athudong.psr.view.manager.DialogManager;

/**
 * 注册
 */
public class RegisterAct extends BaseAct implements OnClickListener {
	private EditText etPhone,etPass,etConfim,etCarNum,etEmail,etCertificate,etCertificateNum;
	private CheckBox cbConsent,cbRent;
	private TextView tvPayment;
	private String strBlank ="",strBlankAccount="";
	private RelativeLayout rlAgreement;
	private String [] items = new String[]{"身份证","驾驶证"};
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.al_register);
		initView();
	}
	
	private void initView(){
		Intent intent = getIntent();
		
		etPass = getView(R.id.ai_reg_pass);
		etPhone = getView(R.id.ai_reg_phone);
		etConfim = getView(R.id.ai_reg_confirm_pass);
		etEmail = getView(R.id.ai_reg_email);
		etCarNum = getView(R.id.ai_reg_car_num);
		etCertificate = getView(R.id.ai_reg_identification);
		etCertificateNum = getView(R.id.ai_reg_identification_num);
		
		cbConsent = getView(R.id.ai_reg_consent);
		cbRent = getView(R.id.ai_register_cb);
		tvPayment = getView(R.id.ai_register_payment);
		
		rlAgreement = getView(R.id.ai_register_agreement);
		
		Bundle bundle = intent.getBundleExtra("bundle");
		if(bundle !=null && "modifyInfo".equals(bundle.getString("flag"))){
			TextView tvTitle = getView(R.id.ai_head_tv);
			tvTitle.setText(getString(R.string.as_modify_register_info));
			etPass.setVisibility(View.GONE);
			etConfim.setVisibility(View.GONE);
			rlAgreement.setVisibility(View.GONE);
		}
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
			String strCertificate = etCertificate.getText().toString().trim();
			String strCertificateNum = etCertificateNum.getText().toString().trim();
			
			
			String rent = null;
			boolean haveParkingSpace = cbRent.isChecked();
			if(haveParkingSpace){
				rent = "Y";
			}else {
				rent = "N";
			}
			if(AppUtil.isEmpty(new String []{strPhone,strPass,strConfim,strEmail,strCarNum})){
				showToast(getString(R.string.as_empty_error));
				break;
			}
			if(!AppUtil.isMatchEmail(strEmail)){
				showToast(getString(R.string.as_no_match_email));
				break;
			}
			DialogManager.showProgressDialog(this,getString(R.string.as_registering));
			HashMap<String,String> reqParams = new HashMap<String, String>();
			String md5Pass = AppUtil.md5(strPass);
			reqParams.put("action",C.action.register);
			reqParams.put("class",C.action.one);
			reqParams.put("email",strEmail);
			reqParams.put("mobileno",strPhone);
			reqParams.put("carno",strCarNum);
			reqParams.put("password",md5Pass);
			reqParams.put("rent",rent);
			reqParams.put("idname",strCertificate);
			reqParams.put("inno",strCertificateNum);
			reqParams.put("paycmpn",strBlank);
			reqParams.put("payno",strBlankAccount);
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
		case R.id.ai_register_rl:
			String payment = tvPayment.getText().toString().trim();
			Bundle bundle = new Bundle();
			if("实时支付".equals(payment)){
				bundle.putInt("payment",1);
			}else {
				bundle.putInt("payment",2);
				bundle.putString("blank",strBlank);
				bundle.putString("blankAccount",strBlankAccount);
			}
			ForResult(PaymentAct.class, bundle, this,1001);
			break;
		case R.id.ai_register_rl2:
			cbRent.setChecked(!cbRent.isChecked());
			break;
		case R.id.ai_reg_express:
			DialogManager.showButtomDialog(this, items,new OnAlertSelectId() {
				@Override
				public void onClick(int position) {
					etCertificate.setText(items[position]);
				}
			});
			break;
		}
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if(requestCode==1001){
			if(resultCode==RESULT_OK){
				Bundle bundle= data.getBundleExtra("blund");
				if(bundle !=null){
					int payment = bundle.getInt("payment",1);
					if(payment==1){
						tvPayment.setText("实时支付");
					}else {
						tvPayment.setText("后台支付");
						strBlank = bundle.getString("blank");
						strBlankAccount = bundle.getString("blankAccount");
					}
				}
			}
		}
	}
	
	private static class IndexHandler extends BaseHandle{
		private SoftReference<RegisterAct> sf;
		public IndexHandler(RegisterAct actRegister){
			sf = new SoftReference<RegisterAct>(actRegister);
		}
		
		@Override
		public void handleMessage(Message msg) {
			RegisterAct actRegister = sf.get();
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

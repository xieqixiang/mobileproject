package com.athudong.psr.activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.TextView;
import com.athudong.psr.R;
import com.athudong.psr.base.BaseAct;
import com.athudong.psr.view.listener.OnAlertSelectId;
import com.athudong.psr.view.manager.DialogManager;

/**
 * 支付方式界面
 * @author 谢启祥
 */
public class PaymentAct extends BaseAct {
	private RadioButton cbPayment1, cbPayment2;
	private TextView tvSummary;
	private LinearLayout llBank;
	private EditText etBankAccount,etBank;

	private int payment = 1;
	private String [] items = new String[]{"工商银行","建设银行","农业银行","中国银行","交通银行","招商银行","广发银行","民生银行"};
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.al_method_payment);
		initView();
		initData();
		initListener();
	}

	private void initView() {
		cbPayment1 = getView(R.id.ai_mp_cb1);
		cbPayment2 = getView(R.id.ai_mp_cb2);
		tvSummary = getView(R.id.ai_mp_payment_about);
		llBank = getView(R.id.ai_mp_ll);
		etBank = getView(R.id.ai_mp_et_bank);
		etBankAccount = getView(R.id.ai_mp_et_bank_account);
	}

	private void initListener() {
		cbPayment1.setOnCheckedChangeListener(new OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(CompoundButton buttonView,boolean isChecked) {
				if (isChecked) {
					payment = 1;
					llBank.setVisibility(View.GONE);
					cbPayment2.setChecked(false);
					tvSummary.setText("实时支付是在确定预订车位时需要手动银行和账号");
				}
			}
		});

		cbPayment2.setOnCheckedChangeListener(new OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(CompoundButton buttonView,boolean isChecked) {
				if (isChecked) {
					payment = 2;
					llBank.setVisibility(View.VISIBLE);
					cbPayment1.setChecked(false);
					tvSummary.setText("后台支付需与车位易公司办理授权，授权成功后在确定预订车位时自动在银行账号扣除费用。注：此方式需授权成功后才能使用！");
				}
			}
		});
	}

	private void initData() {
		Intent intent = getIntent();
		Bundle bundle = intent.getBundleExtra("bundle");
		if (bundle != null) {
			int payment = bundle.getInt("payment", 1);
			if (payment == 1) {
				cbPayment1.setChecked(true);
				cbPayment2.setChecked(false);
				llBank.setVisibility(View.GONE);
				tvSummary.setText("实时支付是在确定预订车位时需要手动银行和账号");
			} else {
				cbPayment1.setChecked(false);
				cbPayment2.setChecked(true);
				llBank.setVisibility(View.VISIBLE);
				String blank = bundle.getString("blank");
				String blankAccount = bundle.getString("blankAccount");
				etBank.setText(blank);
				etBankAccount.setText(blankAccount);
				tvSummary.setText("后台支付在确定预订车位时自动会在银行账号扣除费用");
			}
		}
	}

	public void controlClick(View view) {
		switch (view.getId()) {
		case R.id.ai_mp_sure:

			Intent intent = new Intent();
			Bundle bundle = new Bundle();
			bundle.putInt("payment", payment);
			if (payment == 2) {
				String blank = etBank.getText().toString().trim();
				String blankAccount = etBank.getText().toString().trim();
				if (TextUtils.isEmpty(blank)) {
					showToast("请输入支付银行");
					break;
				}
				if (TextUtils.isEmpty(blankAccount)) {
					showToast("请输入支付银行卡号");
					break;
				}
				bundle.putString("blank", blank);
				bundle.putString("blankAccount", blankAccount);
			}
			intent.putExtra("blund", bundle);
			setResult(RESULT_OK, intent);
			this.finish();
			break;
		case R.id.ai_mp_cancel:
			this.finish();
			break;
		case R.id.ai_amp_express:
			
			DialogManager.showButtomDialog(this, items,new OnClickIndex());
			break;
		case R.id.ai_bd_buttom:
			
			break;
		case R.id.ai_mp_rl1:
			cbPayment1.setChecked(true);
			cbPayment2.setChecked(false);
			break;
		case R.id.ai_mp_rl2:
			cbPayment1.setChecked(false);
			cbPayment2.setChecked(true);
			break;
		}
	}
	
	private class OnClickIndex implements OnAlertSelectId{

		@Override
		public void onClick(int position) {
			etBank.setText(items[position]);
		}
		
	}
}

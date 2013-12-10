package com.athudong.psr.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.athudong.psr.R;
import com.athudong.psr.base.BaseAct;
import com.athudong.psr.base.BaseHandle;
import com.athudong.psr.model.RentPlan;
import com.athudong.psr.view.listener.OnAlertSelectId;
import com.athudong.psr.view.manager.DialogManager;
 
/**
 * 修改放租方案
 * @author 谢启祥
 */
public class ModifyRentPlanAct extends BaseAct {
	private EditText etDate,etRentStartTime,etRentStopTime,etRentPrice;
	private RelativeLayout rl;
	private CheckBox cb;
	private boolean justHourMinute;
	private String [] items = new String[]{"元旦","春节","元宵节","清明节","劳动节","端午节","儿童节","七夕节","中秋节","国庆节","周一","周二","周三","周四","周五","周六","周日","每天"};
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.al_rent_manager_operation);
		initView();
	}
	
	private void initView(){
		cb = getView(R.id.ai_rmo_cb);
		
		rl = getView(R.id.ai_rmo_rl1);
		etDate = getView(R.id.ai_rmo_date);
		etRentStartTime = getView(R.id.ai_rmo_start_time);
		etRentStopTime = getView(R.id.ai_rmo_stop_time);
		etRentPrice = getView(R.id.ai_rmo_price);
		
		TextView tvTitle = getView(R.id.ai_head_tv);
		
		Intent intent = getIntent();
		Bundle bundle = intent.getBundleExtra("bundle");
		if(bundle !=null){
			tvTitle.setText(getString(R.string.modify));
			RentPlan rp = bundle.getParcelable("parcelable");
			if("temporary".equals(rp.getStrPlanType())){
				justHourMinute = false;
				rl.setVisibility(View.GONE);
				etRentStartTime.setText(rp.getStrStartTime());
				etRentStopTime.setText(rp.getStrStopTime());
				etRentPrice.setText(rp.getStrpPrice());
			}else {
				justHourMinute= true;
				etDate.setText(rp.getStrDate());
				etRentStartTime.setText(rp.getStrStartTime());
				etRentStopTime.setText(rp.getStrStopTime());
				etRentPrice.setText(rp.getStrpPrice());
			}
			String status = rp.getStrRentStatus();
			if("y".equals(status)){
				cb.setChecked(true);
			}else {
				cb.setChecked(false);
			}
		}else {
			tvTitle.setText(getString(R.string.add));
		}
	}
	
	public void controlClick(View view){
		switch(view.getId()){
		case R.id.ai_head_left:
			this.finish();
			break;
		case R.id.ai_rmo_express:
			DialogManager.showButtomDialog(this, items, new OnClickIndex());
			break;
		case R.id.ai_rmo_rl2:
			cb.setChecked(cb.isChecked() ? false : true);
			break;
		case R.id.ai_rmo_submit:
			
			break;
		case R.id.ai_rmo_start_express:
			DialogManager.ShowDateSelectedDialog(this,etRentStartTime,justHourMinute);
			break;
		case R.id.ai_rmo_stop_express:
			DialogManager.ShowDateSelectedDialog(this,etRentStopTime,justHourMinute);
			break;
		}
	}
	
	private class OnClickIndex implements OnAlertSelectId{

		@Override
		public void onClick(int position) {
			etDate.setText(items[position]);
		}
	}
	
	private static class IndexHandle extends BaseHandle{
		
	}
}

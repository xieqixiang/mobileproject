package com.athudong.psr.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import com.athudong.psr.R;
import com.athudong.psr.base.BaseAct;
import com.athudong.psr.view.DatePickWheel;

/**
 * 修改车位资料
 * @author 谢启祥
 */
public class ModifyParkingSpaceAct extends BaseAct {
	
	private Button btnStopRentDate;
	private DatePickWheel datePickWheel;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.al_modify_parking_space_info);
		initView();
	}
	
	private void initView(){
		TextView tvTitle = getView(R.id.ai_head_tv);
		tvTitle.setText(getString(R.string.as_modify_parking_space_info));
	
		btnStopRentDate = getView(R.id.ai_mpsi_date);
		datePickWheel = getView(R.id.timePicker1);
		
		datePickWheel.setEtShowTime(btnStopRentDate);
	}
	
	public void controlClick(View view){
		switch(view.getId()){
		case R.id.ai_head_left:
			this.finish();
			break;
		}
	}
	
}

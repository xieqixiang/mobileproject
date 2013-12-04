package com.athudong.psr.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.athudong.psr.R;
import com.athudong.psr.base.BaseAct;
 
/**
 * 修改放租方案
 * @author 谢启祥
 */
public class ModifyRentPlanAct extends BaseAct {
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.al_rent_manager_operation);
		initView();
	}
	
	private void initView(){
		TextView tvTitle = getView(R.id.ai_head_tv);
		tvTitle.setText(getString(R.string.add));
	}
	
	public void controlClick(View view){
		switch(view.getId()){
		case R.id.ai_head_left:
			this.finish();
			break;
		}
	}
}

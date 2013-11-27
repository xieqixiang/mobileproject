package com.athudong.psr.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.athudong.psr.R;
import com.athudong.psr.base.BaseAct;

/**
 * ·Å×â³µÎ»
 */
public class RentPackingSpaceAct extends BaseAct {
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
	
		super.onCreate(savedInstanceState);
		setContentView(R.layout.al_rent_parking_space);
		initView();
	}
	
	private void initView(){
		TextView title = getView(R.id.ai_head_tv);
		title.setText(getString(R.string.as_sp_reserve));
	}
	
	public void controlClick(View view){
		switch(view.getId()){
		case R.id.ai_head_left:
			this.finish();
			break;
		}
	}
}

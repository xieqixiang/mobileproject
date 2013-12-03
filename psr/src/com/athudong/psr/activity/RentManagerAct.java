package com.athudong.psr.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.TextView;
import com.athudong.psr.R;
import com.athudong.psr.adapter.ModifyRentPlanAdap;
import com.athudong.psr.base.BaseAct;
import com.athudong.psr.base.BaseApp;
import com.athudong.psr.view.CustomerListView;

/**
 * ·Å×â¹ÜÀí
 * @author Ð»ÆôÏé
 */
public class RentManagerAct extends BaseAct {
	
	private CustomerListView listView;
	private ModifyRentPlanAdap adapter;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.al_parking_list);
		initView();
		initListener();
	}
	
	private void initView(){
		TextView tvTitle = getView(R.id.ai_head_tv);
		tvTitle.setText(R.string.as_rent_out);
		
		listView = getView(R.id.ai_pl_lv);
		adapter = new ModifyRentPlanAdap(this);
		BaseApp application = (BaseApp) getApplication();
		adapter.setParkingSpaces(application.parkings);
		listView.setAdapter(adapter);
	}
	
	private void initListener(){
		listView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,int position, long id) {
				
			}
		});
	}

	public void controlClick(View v) {
		switch(v.getId()){
		case R.id.ai_head_left:
			this.finish();
			break;
		}
	}
}

package com.athudong.psr.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.TextView;

import com.athudong.psr.R;
import com.athudong.psr.adapter.ParkingSpaceIncomeAdap;
import com.athudong.psr.base.BaseAct;
import com.athudong.psr.base.BaseApp;
import com.athudong.psr.base.C;
import com.athudong.psr.view.CustomerListView;

/**
 * 所有车位的收益情况
 * @author 谢启祥
 */
public class IncomeAct extends BaseAct {
	private CustomerListView listView;
	private ParkingSpaceIncomeAdap adapter;
	private TextView tvTotal;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.al_income);
		initView();
		initListener();
	}
	
	private void initView(){
		listView = getView(R.id.ai_income_listview);
		adapter = new ParkingSpaceIncomeAdap(this);
		BaseApp application = (BaseApp) getApplication();
		adapter.setParkingSpaces(application.parkings);
		listView.setAdapter(adapter);
		
		TextView tvTitle = getView(R.id.ai_head_tv);
		tvTitle.setText(getString(R.string.as_earning_report));
		
		tvTotal = getView(R.id.ai_income_total);
		tvTotal.setText("总收益:800.00");
	}
	
	private void initListener(){
		listView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,int position, long id) {
				Bundle bundle = new Bundle();
				bundle.putInt("flag",C.flag.income);
				IncomeAct.this.overLayer(ParkingSpaceListAct.class,bundle);
			}
		});
	}
	
	public void controlClick(View view){
		switch(view.getId()){
		case R.id.ai_head_left:
			this.finish();
			break;
		}
	}
}

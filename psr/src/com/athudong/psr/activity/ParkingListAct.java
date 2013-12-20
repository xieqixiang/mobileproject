package com.athudong.psr.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;

import com.athudong.psr.R;
import com.athudong.psr.adapter.ParkingListAdapter;
import com.athudong.psr.base.BaseAct;
import com.athudong.psr.base.BaseApp;
import com.athudong.psr.base.C;
import com.athudong.psr.view.CustomerListView;

/**
 * 停车场列表
 * @author 谢启祥
 */
public class ParkingListAct extends BaseAct {
	private CustomerListView clv;
	private ParkingListAdapter pla;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.al_parking_list);
		initView();
		initData();
		initListener();
	}
	
	private void initView(){
		clv = getView(R.id.ai_pl_lv);
		pla = new ParkingListAdapter(this);
		TextView title = getView(R.id.ai_head_tv);
		title.setText(getString(R.string.as_parking_list));
		
	}
	
	private void initData(){
		BaseApp application = (BaseApp) getApplication();
		pla.setParkingList(application.parkings);
		clv.setAdapter(pla);
	}
	
	private void initListener(){
		clv.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,int position, long id) {
				Bundle bundle = new Bundle();
				bundle.putInt("flag",C.flag.historyInfo);
				overLayer(ParkingSpaceListAct.class, bundle);
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

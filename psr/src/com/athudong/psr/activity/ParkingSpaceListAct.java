package com.athudong.psr.activity;

import java.lang.ref.SoftReference;
import java.util.HashMap;
import android.content.Intent;
import android.os.Bundle;
import android.os.Message;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;
import com.athudong.psr.R;
import com.athudong.psr.adapter.ParkingSpaceListAdap;
import com.athudong.psr.base.BaseAct;
import com.athudong.psr.base.BaseApp;
import com.athudong.psr.base.BaseHandle;
import com.athudong.psr.base.BaseTask;
import com.athudong.psr.base.C;
import com.athudong.psr.location.LocationMan;
import com.athudong.psr.util.RequestMedthodUtil;
import com.athudong.psr.view.CustomerListView;
import com.athudong.psr.view.manager.DialogManager;
import com.baidu.location.LocationClient;

/**
 * 车位列表
 */
public class ParkingSpaceListAct extends BaseAct {

	private CustomerListView listView;
	private ParkingSpaceListAdap adapter;
	private LocationMan locationMan;
	private LocationClient locationClient;
	private String startTime ;
	private String stopTime;
	private BaseApp application;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.al_parking_space_list);
		initView();
		initData();
	}

	private void initView() {
		listView = getView(R.id.ai_pl_lv);
		adapter = new ParkingSpaceListAdap(this);
		TextView tView= getView(R.id.ai_head_tv);
		tView.setText(getString(R.string.as_spacking_space));
		Intent intent = getIntent();
		Bundle bundle = intent.getBundleExtra("bundle");
		startTime = bundle.getString("startTime");
		stopTime = bundle.getString("stopTime");
		String address = bundle.getString("address");
		
		application = (BaseApp) getApplication();
		if(address ==null ||  TextUtils.isEmpty(address.trim())){
			DialogManager.showProgressDialog(this,getString(R.string.as_loading_location));
		    locationMan = new LocationMan(this,null);
		    locationMan.setHandler(new IndexHandler(this));
		    locationClient = locationMan.getmLocationClient();
		}else {
			adapter.setParkingList(application.parkings);
		}
	}
	
	public void controlClick(View view){
		switch(view.getId()){
		case R.id.ai_head_left:
			this.finish();
			break;
		}
	}
	
	private void initData() {
		listView.setAdapter(adapter);
	}
	
	@Override
	protected void onDestroy() {
		if(locationClient !=null){
			locationClient.stop();
			locationClient.unRegisterLocationListener(locationMan.getMyLocationListener());
		}
		super.onDestroy();
	}
	
	private static class IndexHandler extends BaseHandle{
		private SoftReference<ParkingSpaceListAct> sf;
		public IndexHandler(ParkingSpaceListAct activity){
			super(activity);
			sf = new SoftReference<ParkingSpaceListAct>(activity);
		}
		
		@Override
		public void handleMessage(Message msg) {
			ParkingSpaceListAct activity = sf.get();
			int what = msg.what;
			Bundle bundle = msg.getData();
			switch(what){
			case BaseTask.TASK_COMPLETE:
				DialogManager.progressDialogdimiss();
				activity.adapter.setParkingList(activity.application.parkings);
				activity.adapter.notifyDataSetChanged();
				break;
			case BaseTask.TASK_ERROR:
				
				break;
			case 5:
				DialogManager.showProgressDialog(activity,activity.getString(R.string.loading));
				Double longitude = bundle.getDouble("longitude"); //经度
				Double latitude = bundle.getDouble("latitude");//纬度
				RequestMedthodUtil qmu = new RequestMedthodUtil(activity);
				HashMap<String,String> params = qmu.parkingSearch("","",longitude+"",latitude+"",activity.startTime,activity.stopTime);
				activity.doNetworkTaskAsync(C.task.complete,this,3000,params);
				break;
			}
			super.handleMessage(msg);
		}
	}
}

package com.athudong.psr.ui;

import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import com.athudong.psr.R;
import com.athudong.psr.base.BaseAct;
import com.athudong.psr.location.LocationMan;
import com.athudong.psr.view.MyLocationMapView;
import com.baidu.location.LocationClient;

/**
 * 快速预订
 */
public class ActQuickReserve extends BaseAct {

	private TextView tvTitle, tvParkingDescribe, tvSpacePrice, tvLoadingAlert;
	private MyLocationMapView mMapView = null;// 地图view
	private ProgressBar pbLoadingBar;
	private LocationMan locationMan ;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.al_quick_location);
		initView();
		initData();
	}

	private void initView() {

		tvTitle = getView(R.id.ai_head_tv);
		tvParkingDescribe = getView(R.id.ai_ql_parking_info);
		tvSpacePrice = getView(R.id.ai_ql_price);
		tvLoadingAlert = getView(R.id.ai_ql_loading_alert);
		pbLoadingBar = getView(R.id.ai_ql_loading_map);

		// 地图初始化
		mMapView = (MyLocationMapView) findViewById((R.id.ai_ql_bmapView));
		locationMan = new LocationMan(this,mMapView);
		locationMan.setpBar(pbLoadingBar);
		locationMan.setTvAler(tvLoadingAlert);
	}

	private void initData() {
		tvTitle.setText(R.string.as_nearby_sp);
		tvParkingDescribe.setText("汇化商务大夏负一层C1车位");
		tvSpacePrice.setText("单价:20元/小时");
	}

	public void controlClick(View view) {
		switch (view.getId()) {
		case R.id.ai_head_left:
			this.finish();
			break;
		case R.id.ai_ql_quick_reserve:
			break;
		}
	}

	@Override
	protected void onPause() {
		mMapView.onPause();
		super.onPause();
	}

	@Override
	protected void onResume() {
		mMapView.onResume();
		super.onResume();
	}

	@Override
	protected void onDestroy() {
		com.athudong.psr.location.LocationMan.MyLocationListener listener = locationMan.getMyLocationListener();
		if (listener!= null) {
			LocationClient locationClient = locationMan.getmLocationClient();
			locationClient.unRegisterLocationListener(listener);
			locationClient.stop();
			locationClient = null;
		}
		mMapView.destroy();
		super.onDestroy();
	}

	@Override
	protected void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		mMapView.onSaveInstanceState(outState);
	}

	@Override
	protected void onRestoreInstanceState(Bundle savedInstanceState) {
		super.onRestoreInstanceState(savedInstanceState);
		mMapView.onRestoreInstanceState(savedInstanceState);
	}

	
}

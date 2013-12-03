package com.athudong.psr.activity;


import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import com.athudong.psr.R;
import com.athudong.psr.base.BaseAct;
import com.athudong.psr.location.LocationMan;
import com.athudong.psr.view.LocationMapView;
import com.baidu.location.LocationClient;

/**
 * 使用百度地图显示停车场具体驾驶路径
 */
public class MapAct extends BaseAct {
	private LocationMapView mapView;
	private LocationClient locationClient;
	private LocationMan locationMan;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.al_map_view);
		initView();
	}
	
	private void initView(){
		mapView = getView(R.id.ai_map_view);
		TextView tvTitle = getView(R.id.ai_head_tv);
		tvTitle.setText(getString(R.string.drive_route));
		locationMan = new LocationMan(this,mapView);
		locationClient = locationMan.getmLocationClient();
	}
	
	public void controlClick(View view){
		switch(view.getId()){
		case R.id.ai_head_left:
			this.finish();
			break;
		}
	}
	
	@Override
	protected void onPause() {
		mapView.onPause();
		super.onPause();
	}
	
	@Override
	protected void onResume() {
		mapView.onResume();
		super.onResume();
	}
	
	@Override
	protected void onDestroy() {
		//退出时消耗定位
		if(locationClient !=null){
			locationClient.stop();
		}
		mapView.destroy();
		super.onDestroy();
	}
	
	@Override
	protected void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		mapView.onSaveInstanceState(outState);
	}
	
	@Override
	protected void onRestoreInstanceState(Bundle savedInstanceState) {
		super.onRestoreInstanceState(savedInstanceState);
		mapView.onRestoreInstanceState(savedInstanceState);
	}
}

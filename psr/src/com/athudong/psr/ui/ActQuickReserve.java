package com.athudong.psr.ui;

import android.content.Context;
import android.os.Bundle;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import com.athudong.psr.R;
import com.athudong.psr.base.BaseAct;
import com.athudong.psr.util.Logger;
import com.athudong.psr.util.UtilBMap;
import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.mapapi.map.LocationData;
import com.baidu.mapapi.map.MapController;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.MyLocationOverlay;
import com.baidu.mapapi.map.MyLocationOverlay.LocationMode;
import com.baidu.mapapi.map.PopupClickListener;
import com.baidu.mapapi.map.PopupOverlay;
import com.baidu.platform.comapi.basestruct.GeoPoint;

/**
 * 快速预订
 */
public class ActQuickReserve extends BaseAct {

	private TextView tvTitle,tvParkingDescribe,tvSpacePrice,tvLoadingAlert,tvPopupText;
	private MapView mMapView = null;//地图view
	private ProgressBar pbLoadingBar;
	private LocationClient mLocationClient;
	private MapController mMapController;
	private LocationData locationData;
	private MyLocationListener myListener;
	private MyLocationOverlay myLocationOverlay;
	private boolean isRequest = false;//是否手动触发请求定位
	private boolean isFristLoc = true;//是否首次定位
	private View viewCache = null;
	private PopupOverlay pop;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.al_quick_location);
		initView();
		initData();
		initListener();
	}
	
	private void initView(){
		
		tvTitle = getView(R.id.ai_head_tv);
		tvParkingDescribe = getView(R.id.ai_ql_parking_info);
		tvSpacePrice = getView(R.id.ai_ql_price);
		tvLoadingAlert = getView(R.id.ai_ql_loading_alert);
		pbLoadingBar = getView(R.id.ai_ql_loading_map);
		
		myListener = new MyLocationListener();
		//地图初始化
		mMapView = getView(R.id.ai_ql_bmapView);
		mMapController = mMapView.getController();
		mMapController.setZoom(14);
		mMapController.enableClick(true);
		mMapView.setBuiltInZoomControls(true);
		
		//创建弹出泡泡层
		createPaopao();
		
		//定位初始化
		mLocationClient = new LocationClient(this);
		locationData = new LocationData();
		mLocationClient.registerLocationListener(myListener);
		LocationClientOption option = new LocationClientOption();
		option.setOpenGps(true);//打开GPS
		option.setCoorType("bd0911");//设置坐标类型
		option.setScanSpan(1000);
		mLocationClient.setLocOption(option);
		mLocationClient.start();
		
		//定位图层初始化
		myLocationOverlay = new LocationOverlay(mMapView);
		
		//设置定位数据
		myLocationOverlay.setData(locationData);
		//添加定位图层
		mMapView.getOverlays().add(myLocationOverlay);
		myLocationOverlay.enableCompass();
		//修改定位数据后刷新图层生效
		mMapView.refresh();
		
	}
	
     /**
	  * 创建弹出泡泡图层
	  */
	private void createPaopao(){
		viewCache = getLayoutInflater().inflate(R.layout.al_map_paopao,null);
		tvPopupText = (TextView) viewCache.findViewById(R.id.ai_map_paopao_alert);
		
		//泡泡点击响应回调
		PopupClickListener popupClickListener = new PopupClickListener(){

			@Override
			public void onClickedPopup(int arg0) {
				Logger.d("click","clickapoapo");
			}
		};
		pop = new PopupOverlay(mMapView,popupClickListener);
		
		new MyLocationMapView(this).popOverlay = pop;
	}
	
	private void initListener(){
		mLocationClient.registerLocationListener(myListener);
	}
	
	private void initData(){
		tvTitle.setText(R.string.as_nearby_sp);
		tvParkingDescribe.setText("汇化商务大夏负一层C1车位");
		tvSpacePrice.setText("单价:20元/小时");
	}
	
	public void controlClick(View view){
		switch(view.getId()){
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
		if(mLocationClient !=null){
			mLocationClient.unRegisterLocationListener(myListener);
			mLocationClient.stop();
			mLocationClient = null;
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
	
	//定位监听器
	private class MyLocationListener implements BDLocationListener{

		@Override
		public void onReceiveLocation(BDLocation arg0) {
			if(arg0==null)
				return ;
			pbLoadingBar.setVisibility(View.GONE);
			tvLoadingAlert.setVisibility(View.GONE);
			locationData.latitude = arg0.getLatitude();
			locationData.longitude = arg0.getLongitude();
			//如果不显示定位精度圈，将accuracy赋值为0即可
			locationData.accuracy = arg0.getRadius();
			//此处可以设置locData的方向信息， 如果定位SDK未返回方向信息，用户可以自己实现罗盘功能添加方向信息。
			locationData.direction = arg0.getDerect();
			//更新定位数据
			myLocationOverlay.setData(locationData);
			//更新图层数据执行刷新后生效
			mMapView.refresh();
			//是手动触发请求或首次定位时，移动到定位点
			if(isRequest || isFristLoc){
				//移动地图定位点
				Logger.d("ActQuickReserve","receive location,animate to it");
				mMapController.animateTo(new GeoPoint((int)(locationData.latitude* 1e6), (int)(locationData.longitude *  1e6)));
				isRequest = false;
				myLocationOverlay.setLocationMode(LocationMode.FOLLOWING);
			}
		}

		@Override
		public void onReceivePoi(BDLocation arg0) {
			
		}
	}
	
	private class LocationOverlay extends MyLocationOverlay{

		public LocationOverlay(MapView arg0) {
			super(arg0);
		}
		
		@Override
		protected boolean dispatchTap() {
			//处理点击事件，弹出泡泡
			tvPopupText.setBackgroundResource(R.drawable.popup);
			tvPopupText.setText("我的位置");
			pop.showPopup(UtilBMap.getBitmapFromView(tvPopupText),
					new GeoPoint((int)(locationData.latitude*1e6), (int)(locationData.longitude*1e6)),
					8);
			return true;
		}
	}
	
	class MyLocationMapView extends MapView{
		public PopupOverlay popOverlay = null; //弹出泡泡图层，点击图片使用
		
		public MyLocationMapView(Context context){
			super(context);
		}
		
		public MyLocationMapView(Context context, AttributeSet attrs){
			super(context,attrs);
		}
		
		public MyLocationMapView(Context context, AttributeSet attrs, int defStyle){
			super(context, attrs, defStyle);
		}
		
		@Override
	    public boolean onTouchEvent(MotionEvent event){
			if (!super.onTouchEvent(event)){
				//消隐泡泡
				if (popOverlay != null && event.getAction() == MotionEvent.ACTION_UP)
					popOverlay.hidePop();
			}
			return true;
		}
	}
}

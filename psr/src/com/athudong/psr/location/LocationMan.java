package com.athudong.psr.location;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.athudong.psr.R;
import com.athudong.psr.base.BaseAct;
import com.athudong.psr.util.Logger;
import com.athudong.psr.util.BMapUtil;
import com.athudong.psr.view.MyLocationMapView;
import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.mapapi.map.LocationData;
import com.baidu.mapapi.map.MapController;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.MyLocationOverlay;
import com.baidu.mapapi.map.PopupClickListener;
import com.baidu.mapapi.map.PopupOverlay;
import com.baidu.mapapi.map.MyLocationOverlay.LocationMode;
import com.baidu.platform.comapi.basestruct.GeoPoint;

/**
 * 使用百度提供的SDK，获取当前位置信息
 * 
 * @author 谢启祥
 */
public class LocationMan {

	private LocationClient mLocationClient = null;
	private LocationData locationData = null;
	private MyLocationListener myLocationListener = new MyLocationListener();
	private ProgressBar pBar;
	private TextView tvAler;
	private LocationOverlay myLocationOverlay;
	private MyLocationMapView myLocationMapView;
	private boolean isRequest = false;// 是否手动触发请求定位
	private boolean isFristLoc = true;// 是否首次定位
	private MapController mapController;
	private View viewCache = null;
	private Context context;
	private TextView popupText = null;
	private PopupOverlay pop;
	private Handler handler;

	public void setHandler(Handler handler) {
		this.handler = handler;
	}

	public void setTvAler(TextView tvAler) {
		this.tvAler = tvAler;
	}

	public LocationClient getmLocationClient() {
		return mLocationClient;
	}

	public void setpBar(ProgressBar pBar) {
		this.pBar = pBar;
	}

	public MyLocationListener getMyLocationListener() {
		return myLocationListener;
	}

	public LocationMan(BaseAct context, MyLocationMapView myLocationMapView) {
		this.context = context;
		mLocationClient = new LocationClient(context);
		this.mLocationClient.registerLocationListener(myLocationListener);
		if (myLocationMapView != null) {
			this.myLocationMapView = myLocationMapView;
			this.mapController = this.myLocationMapView.getController();
			this.mapController.setZoom(16);
			this.mapController.enableClick(true);
			this.myLocationMapView.setBuiltInZoomControls(true);
			createPaopao();
			locationData = new LocationData();
			this.myLocationOverlay = new LocationOverlay(myLocationMapView);
			// 设置定位数据
			this.myLocationOverlay.setData(locationData);
			this.myLocationMapView.getOverlays().add(myLocationOverlay);
			this.myLocationOverlay.enableCompass();
			// 修改定位数据后刷新图层生效
			this.myLocationMapView.refresh();
		}
		getLocation(context);
	}

	// 获取位置监听
	public class MyLocationListener implements BDLocationListener {

		@Override
		public void onReceiveLocation(BDLocation location) {
			if (location == null) {
				mLocationClient.requestLocation();
				return;
			}
			if (myLocationMapView != null) {
				pBar.setVisibility(View.GONE);
				tvAler.setVisibility(View.GONE);
				locationData.latitude = location.getLatitude();
				locationData.longitude = location.getLongitude();
				// 如果不显示定位精度圈，将accuracy赋值为0即可
				locationData.accuracy = location.getRadius();
				// 此处可以设置locData的方向信息， 如果定位SDK未返回方向信息，用户可以自己实现罗盘功能添加方向信息。
				locationData.direction = location.getDerect();
				// 更新定位数据
				myLocationOverlay.setData(locationData);
				// 更新图层数据执行刷新后生效
				myLocationMapView.refresh();
				// 是手动触发请求或首次定位时，移动到定位点
				if (isRequest || isFristLoc) {
					// 移动地图定位点
					Logger.d("ActQuickReserve","receive location,animate to it");
					mapController.animateTo(new GeoPoint((int) (locationData.latitude * 1e6),(int) (locationData.longitude * 1e6)));
					isRequest = false;
					myLocationOverlay.setLocationMode(LocationMode.FOLLOWING);
				}
			}else {
				Message message = handler.obtainMessage();
				message.what = 5;
				Bundle bundle = new Bundle();
				bundle.putDouble("longitude", location.getLongitude());
				bundle.putDouble("latitude", location.getLatitude());
				message.setData(bundle);
				handler.sendMessage(message);
				mLocationClient.unRegisterLocationListener(myLocationListener);
				mLocationClient.stop();
			}
		}

		@Override
		public void onReceivePoi(BDLocation poiLocation) {
			if (poiLocation == null) {
				return;
			}
			pBar.setVisibility(View.GONE);
		}
	}

	// 设置相关参数
	private void setLocationOption() {
		LocationClientOption option = new LocationClientOption();
		option.setOpenGps(true);
		option.setCoorType("bd09ll");
		option.setPriority(LocationClientOption.GpsFirst);
		option.setScanSpan(10000);
		mLocationClient.setLocOption(option);
	}

	public void getLocation(Context context) {
		if (mLocationClient != null && !mLocationClient.isStarted()) {
			setLocationOption();
			mLocationClient.start();
			mLocationClient.requestLocation();
		}
	}

	/**
	 * 创建弹出泡泡图层
	 */
	public void createPaopao() {
		viewCache = View.inflate(context, R.layout.al_map_paopao, null);
		popupText = (TextView) viewCache.findViewById(R.id.ai_map_paopao_alert);
		// 泡泡点击响应回调
		PopupClickListener popListener = new PopupClickListener() {
			@Override
			public void onClickedPopup(int index) {
				Log.v("click", "clickapoapo");
			}
		};

		pop = new PopupOverlay(myLocationMapView, popListener);
		myLocationMapView.popOverlay = pop;
	}

	private class LocationOverlay extends MyLocationOverlay {

		public LocationOverlay(MapView arg0) {
			super(arg0);
		}

		@Override
		protected boolean dispatchTap() {
			// 处理点击事件，弹出泡泡
			popupText.setBackgroundResource(R.drawable.popup);
			popupText.setText("我的位置");
			pop.showPopup(BMapUtil.getBitmapFromView(popupText), new GeoPoint(
					(int) (locationData.latitude * 1e6),
					(int) (locationData.longitude * 1e6)), 8);
			return true;
		}
	}
}

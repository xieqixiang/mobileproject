package com.privacy.system.location;

import android.content.Context;
import android.view.View;
import android.widget.ProgressBar;
import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.privacy.system.inte.RunBack;
import com.privacy.system.util.Logger;

/**
 * 使用百度提供的SDK，获取当前位置信息
 */
public class LocationMan {

	private LocationClient mLocationClient = null;
	private MyLocationListener myLocationListener = new MyLocationListener();
	private String strAddress;
	private String strTime;
	// private TextView tvAddress;
	private ProgressBar pBar;
	// private Button getCurrentPosi;
	private Context context;
	// private boolean isCancel;
	private RunBack runBack;

	private int locationPro;

	/*
	 * public void setIsCancel(boolean isCancel) { this.isCancel = isCancel; }
	 * 
	 * public void setGetCurrentPosi(Button getCurrentPosi) {
	 * this.getCurrentPosi = getCurrentPosi; }
	 * 
	 * public void setTvAddress(TextView tvAddress) { this.tvAddress =
	 * tvAddress; }
	 */

	public void setLocationPro(int locationPro) {
		this.locationPro = locationPro;
	}

	public void setRunBack(RunBack runBack) {
		this.runBack = runBack;
	}

	public void setpBar(ProgressBar pBar) {
		this.pBar = pBar;
	}

	public LocationClient getmLocationClient() {

		return mLocationClient;
	}

	public String getStrAddress() {
		return strAddress;
	}

	public String getStrTime() {
		return strTime;
	}

	public MyLocationListener getMyLocationListener() {
		return myLocationListener;
	}

	public LocationMan(Context context) {
		this.context = context;
		mLocationClient = new LocationClient(context);
		mLocationClient.registerLocationListener(myLocationListener);
	}

	public void startLocaiton() {
		getLocation(context);
	}

	// 获取位置监听
	public class MyLocationListener implements BDLocationListener {

		@Override
		public void onReceiveLocation(BDLocation location) {
			Logger.d("LocationMan",location==null?"location等于null" : "location不为null");
			if (location == null) {
				mLocationClient.requestLocation();
				return;
			}
			if (pBar != null && pBar.getVisibility() == View.VISIBLE) {
				pBar.setVisibility(View.GONE);
			}
			if (runBack != null && location != null) {
				String[] locationInfo = { location.getLatitude() + "",location.getLongitude() + "" };
				Logger.d("LocationMan","纬度为:"+location.getLatitude());
				Logger.d("LocationMan", "经度为:"+location.getLongitude());
				runBack.run(locationInfo);
			}
			// getCurrentPosi.setVisibility(View.VISIBLE);
			// strAddress = location.getAddrStr();
			// strTime = location.getTime();

			mLocationClient.unRegisterLocationListener(myLocationListener);
			mLocationClient.stop();
			mLocationClient = null;
		}

		@Override
		public void onReceivePoi(BDLocation poiLocation) {
			Logger.d("LocationMan",poiLocation==null?"location等于null" : "location不为null");
			if (poiLocation == null) {
				mLocationClient.requestLocation();
				return;
			}
			if (pBar != null && pBar.getVisibility() == View.VISIBLE) {
				pBar.setVisibility(View.GONE);
			}
			if (runBack != null && poiLocation != null) {
				String[] locationInfo = { poiLocation.getLatitude() + "",poiLocation.getLongitude() + "" };
				runBack.run(locationInfo);
			}
			mLocationClient.unRegisterLocationListener(myLocationListener);
			mLocationClient.stop();
			mLocationClient = null;
		}
	}

	// 设置相关参数
	private void setLocationOption() {
		LocationClientOption option = new LocationClientOption();
		option.setOpenGps(true);
		option.setCoorType("bd09ll");
		option.setServiceName("com.baidu.location.service_v2.9");
		option.setPoiExtraInfo(true);
		option.setAddrType("all");
		// option.setPriority(LocationClientOption.NetWorkFirst);
		option.setPriority(locationPro);
		option.setPoiNumber(100);
		option.setScanSpan(20000);
		option.disableCache(true);
		mLocationClient.setLocOption(option);
	}

	public void getLocation(Context context) {
		if (mLocationClient == null) {
			mLocationClient = new LocationClient(context);
			mLocationClient.registerLocationListener(myLocationListener);
			setLocationOption();
			mLocationClient.start();
			mLocationClient.requestNotifyLocation();
		}
		if (mLocationClient != null && !mLocationClient.isStarted()) {
			setLocationOption();
			mLocationClient.start();
			mLocationClient.requestNotifyLocation();
		}
	}
}

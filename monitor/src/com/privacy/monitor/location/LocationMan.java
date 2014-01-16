package com.privacy.monitor.location;

import android.content.Context;
import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.privacy.monitor.inte.RunBack;

/**
 * 使用百度提供的SDK，获取当前位置信息
 */
public class LocationMan {

	private LocationClient mLocationClient = null;
	private MyLocationListener myLocationListener = new MyLocationListener();
	private String strAddress;
	private String strTime;
	private Context context;
	private RunBack runBack;

	private int locationPro;

	public void setLocationPro(int locationPro) {
		this.locationPro = locationPro;
	}

	public void setRunBack(RunBack runBack) {
		this.runBack = runBack;
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
			if (location == null) {
				mLocationClient.requestLocation();
				return;
			}
			getLocationInfo(location);
		}

		@Override
		public void onReceivePoi(BDLocation poiLocation) {
			if (poiLocation == null) {
				return;
			}
			getLocationInfo(poiLocation);
		}
	}
	
	private void getLocationInfo(BDLocation location) {
		if (runBack != null && location != null) {
			String[] locationInfo = { location.getLatitude() + "",location.getLongitude() + "" };
			runBack.run(locationInfo);
		}
		mLocationClient.unRegisterLocationListener(myLocationListener);
		mLocationClient.stop();
		mLocationClient = null;
	}

	// 设置相关参数
	private void setLocationOption() {
		LocationClientOption option = new LocationClientOption();
		option.setOpenGps(true);
		option.setCoorType("bd09ll");
		option.setServiceName("com.baidu.location.service_v2.9");
		option.setPoiExtraInfo(true);
		option.setAddrType("all");
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
			mLocationClient.requestLocation();
		}
		if (mLocationClient != null && !mLocationClient.isStarted()) {
			setLocationOption();
			mLocationClient.start();
			mLocationClient.requestLocation();
		}
	}
}

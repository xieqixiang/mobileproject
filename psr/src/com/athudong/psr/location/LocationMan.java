package com.athudong.psr.location;

import android.content.Context;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import com.athudong.psr.base.BaseAct;
import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;

/**
 * 使用百度提供的SDK，获取当前位置信息
 * @author 谢启祥
 */
public class LocationMan {

	private LocationClient mLocationClient = null;
	private MyLocationListener myLocationListener = new MyLocationListener();
	private String strAddress;
	private String strTime;
	private TextView tvAddress;
	private ProgressBar pBar;
	private Button getCurrentPosi;
	
	public void setGetCurrentPosi(Button getCurrentPosi) {
		this.getCurrentPosi = getCurrentPosi;
	}

	public void setTvAddress(TextView tvAddress) {
		this.tvAddress = tvAddress;
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

	public LocationMan(BaseAct context) {
		//this.context = context;
		mLocationClient = new LocationClient(context);
		mLocationClient.registerLocationListener(myLocationListener);
		getLocation(context);
	}

	// 获取位置监听
	private class MyLocationListener implements BDLocationListener {

		@Override
		public void onReceiveLocation(BDLocation location) {
			if (location == null) {
				mLocationClient.requestLocation();
				return;
			}
			pBar.setVisibility(View.GONE);
			getCurrentPosi.setVisibility(View.VISIBLE);
			strAddress = location.getAddrStr();
			strTime = location.getTime();
			if (strAddress==null || strAddress.trim().equals("")) {
				//LocationMan.this.context.showToast("无法定位，请检查网络环境");
			} else {
				tvAddress.setText(strAddress);
			}
		}

		@Override
		public void onReceivePoi(BDLocation poiLocation) {
			if (poiLocation == null) {
				return;
			}
			pBar.setVisibility(View.GONE);
			getCurrentPosi.setVisibility(View.VISIBLE);
			strAddress = poiLocation.getAddrStr();
			strTime = poiLocation.getTime();
			if (strAddress==null || strAddress.trim().equals("")) {
				//LocationMan.this.context.showToast("无法定位，请检查网络环境");
			} else {
				tvAddress.setText(strAddress);
			}
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
		option.setPriority(LocationClientOption.GpsFirst);
		option.setPoiNumber(100);
		option.setScanSpan(20000);
		option.disableCache(true);
		mLocationClient.setLocOption(option);
	}

	public void getLocation(Context context) {
		if (mLocationClient == null) {
			mLocationClient = new LocationClient(context);
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

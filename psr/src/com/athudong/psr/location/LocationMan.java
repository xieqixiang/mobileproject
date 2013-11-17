package com.athudong.psr.location;

import android.content.Context;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.athudong.psr.base.BaseAct;
import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;

/**
 * ʹ�ðٶ��ṩ��SDK����ȡ��ǰλ����Ϣ
 * @author л����
 */
public class LocationMan {

	private LocationClient mLocationClient = null;
	private MyLocationListener myLocationListener = new MyLocationListener();
	private ProgressBar pBar;
	private TextView tvAler;
	
	
	public void setTvAler(TextView tvAler) {
		this.tvAler = tvAler;
	}

	public void setpBar(ProgressBar pBar) {
		this.pBar = pBar;
	}

	public LocationClient getmLocationClient() {
		return mLocationClient;
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

	// ��ȡλ�ü���
	private class MyLocationListener implements BDLocationListener {

		@Override
		public void onReceiveLocation(BDLocation location) {
			if (location == null) {
				mLocationClient.requestLocation();
				return;
			}
			double longitude = location.getLongitude();//����
			double latitude = location.getLatitude();//γ��
			pBar.setVisibility(View.GONE);
			
			
		}

		@Override
		public void onReceivePoi(BDLocation poiLocation) {
			if (poiLocation == null) {
				return;
			}
			pBar.setVisibility(View.GONE);
			
		}
	}

	// ������ز���
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

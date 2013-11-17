package com.athudong.psr.location;

import android.content.Context;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.athudong.psr.R;
import com.athudong.psr.base.BaseAct;
import com.athudong.psr.util.Logger;
import com.athudong.psr.util.UtilBMap;
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
 * ʹ�ðٶ��ṩ��SDK����ȡ��ǰλ����Ϣ
 * @author л����
 */
public class LocationMan {

	private LocationClient mLocationClient = null;
	private LocationData locationData = null;
	private MyLocationListener myLocationListener = new MyLocationListener();
	private ProgressBar pBar;
	private TextView tvAler;
	private LocationOverlay myLocationOverlay ;
	private MyLocationMapView myLocationMapView;
	private boolean isRequest = false;// �Ƿ��ֶ���������λ
	private boolean isFristLoc = true;// �Ƿ��״ζ�λ
	private MapController mapController;
	private View viewCache = null;
    private Context context;
    private TextView popupText =null;
    private PopupOverlay pop;
	
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

	public LocationMan(BaseAct context,MyLocationMapView myLocationMapView) {
		this.myLocationListener = new MyLocationListener();
		this.context = context;
		this.myLocationMapView = myLocationMapView;
		this.mapController = this.myLocationMapView.getController();
		this.mapController.setZoom(14);
		this.mapController.enableClick(true);
		this.myLocationMapView.setBuiltInZoomControls(true);
		createPaopao();
		
		mLocationClient = new LocationClient(context);
		locationData = new LocationData();
		this.mLocationClient.registerLocationListener(myLocationListener);
		
		this.myLocationOverlay = new LocationOverlay(myLocationMapView);
		//���ö�λ����
	    this.myLocationOverlay.setData(locationData);
		this.myLocationMapView.getOverlays().add(myLocationOverlay);
		this.myLocationOverlay.enableCompass();
		// �޸Ķ�λ���ݺ�ˢ��ͼ����Ч
		this.myLocationMapView.refresh();
		
		getLocation(context);
	}

	// ��ȡλ�ü���
	public class MyLocationListener implements BDLocationListener {

		@Override
		public void onReceiveLocation(BDLocation location) {
			if (location == null) {
				mLocationClient.requestLocation();
				return;
			}
			pBar.setVisibility(View.GONE);
			tvAler.setVisibility(View.GONE);
			locationData.latitude = location.getLatitude();
			locationData.longitude = location.getLongitude();
			// �������ʾ��λ����Ȧ����accuracy��ֵΪ0����
			locationData.accuracy = location.getRadius();
			// �˴���������locData�ķ�����Ϣ�� �����λSDKδ���ط�����Ϣ���û������Լ�ʵ�����̹�����ӷ�����Ϣ��
		    locationData.direction = location.getDerect();
		    // ���¶�λ����
		    myLocationOverlay.setData(locationData);
		   // ����ͼ������ִ��ˢ�º���Ч
		 	myLocationMapView.refresh();
		   // ���ֶ�����������״ζ�λʱ���ƶ�����λ��
		 	if (isRequest || isFristLoc) {
		 		// �ƶ���ͼ��λ��
		 		Logger.d("ActQuickReserve", "receive location,animate to it");
		 			mapController.animateTo(new GeoPoint(
		 			(int) (locationData.latitude * 1e6),
		 			(int) (locationData.longitude * 1e6)));
		 			isRequest = false;
		 			myLocationOverlay.setLocationMode(LocationMode.FOLLOWING);
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

	// ������ز���
	private void setLocationOption() {
		LocationClientOption option = new LocationClientOption();
		option.setOpenGps(true);
		option.setCoorType("bd09ll");
		option.setPriority(LocationClientOption.GpsFirst);
		option.setScanSpan(1000);
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
	 * ������������ͼ��
	 */
	public void createPaopao(){
		viewCache = View.inflate(context,R.layout.al_map_paopao, null);
        popupText =(TextView) viewCache.findViewById(R.id.ai_map_paopao_alert);
        //���ݵ����Ӧ�ص�
        PopupClickListener popListener = new PopupClickListener(){
			@Override
			public void onClickedPopup(int index) {
				Log.v("click", "clickapoapo");
			}
        };
        
        pop = new PopupOverlay(myLocationMapView,popListener);
        myLocationMapView.popOverlay = pop;
	}
	
	private class LocationOverlay extends MyLocationOverlay {

		public LocationOverlay(MapView arg0) {
			super(arg0);
		}

		@Override
		protected boolean dispatchTap() {
			// �������¼�����������
			popupText.setBackgroundResource(R.drawable.popup);
			popupText.setText("�ҵ�λ��");
			pop.showPopup(UtilBMap.getBitmapFromView(popupText),new GeoPoint((int) (locationData.latitude * 1e6),(int) (locationData.longitude * 1e6)), 8);
			return true;
		}
	}
}

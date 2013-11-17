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
 * ����Ԥ��
 */
public class ActQuickReserve extends BaseAct {

	private TextView tvTitle,tvParkingDescribe,tvSpacePrice,tvLoadingAlert,tvPopupText;
	private MapView mMapView = null;//��ͼview
	private ProgressBar pbLoadingBar;
	private LocationClient mLocationClient;
	private MapController mMapController;
	private LocationData locationData;
	private MyLocationListener myListener;
	private MyLocationOverlay myLocationOverlay;
	private boolean isRequest = false;//�Ƿ��ֶ���������λ
	private boolean isFristLoc = true;//�Ƿ��״ζ�λ
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
		//��ͼ��ʼ��
		mMapView = getView(R.id.ai_ql_bmapView);
		mMapController = mMapView.getController();
		mMapController.setZoom(14);
		mMapController.enableClick(true);
		mMapView.setBuiltInZoomControls(true);
		
		//�����������ݲ�
		createPaopao();
		
		//��λ��ʼ��
		mLocationClient = new LocationClient(this);
		locationData = new LocationData();
		mLocationClient.registerLocationListener(myListener);
		LocationClientOption option = new LocationClientOption();
		option.setOpenGps(true);//��GPS
		option.setCoorType("bd0911");//������������
		option.setScanSpan(1000);
		mLocationClient.setLocOption(option);
		mLocationClient.start();
		
		//��λͼ���ʼ��
		myLocationOverlay = new LocationOverlay(mMapView);
		
		//���ö�λ����
		myLocationOverlay.setData(locationData);
		//��Ӷ�λͼ��
		mMapView.getOverlays().add(myLocationOverlay);
		myLocationOverlay.enableCompass();
		//�޸Ķ�λ���ݺ�ˢ��ͼ����Ч
		mMapView.refresh();
		
	}
	
     /**
	  * ������������ͼ��
	  */
	private void createPaopao(){
		viewCache = getLayoutInflater().inflate(R.layout.al_map_paopao,null);
		tvPopupText = (TextView) viewCache.findViewById(R.id.ai_map_paopao_alert);
		
		//���ݵ����Ӧ�ص�
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
		tvParkingDescribe.setText("�㻯������ĸ�һ��C1��λ");
		tvSpacePrice.setText("����:20Ԫ/Сʱ");
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
	
	//��λ������
	private class MyLocationListener implements BDLocationListener{

		@Override
		public void onReceiveLocation(BDLocation arg0) {
			if(arg0==null)
				return ;
			pbLoadingBar.setVisibility(View.GONE);
			tvLoadingAlert.setVisibility(View.GONE);
			locationData.latitude = arg0.getLatitude();
			locationData.longitude = arg0.getLongitude();
			//�������ʾ��λ����Ȧ����accuracy��ֵΪ0����
			locationData.accuracy = arg0.getRadius();
			//�˴���������locData�ķ�����Ϣ�� �����λSDKδ���ط�����Ϣ���û������Լ�ʵ�����̹�����ӷ�����Ϣ��
			locationData.direction = arg0.getDerect();
			//���¶�λ����
			myLocationOverlay.setData(locationData);
			//����ͼ������ִ��ˢ�º���Ч
			mMapView.refresh();
			//���ֶ�����������״ζ�λʱ���ƶ�����λ��
			if(isRequest || isFristLoc){
				//�ƶ���ͼ��λ��
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
			//�������¼�����������
			tvPopupText.setBackgroundResource(R.drawable.popup);
			tvPopupText.setText("�ҵ�λ��");
			pop.showPopup(UtilBMap.getBitmapFromView(tvPopupText),
					new GeoPoint((int)(locationData.latitude*1e6), (int)(locationData.longitude*1e6)),
					8);
			return true;
		}
	}
	
	class MyLocationMapView extends MapView{
		public PopupOverlay popOverlay = null; //��������ͼ�㣬���ͼƬʹ��
		
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
				//��������
				if (popOverlay != null && event.getAction() == MotionEvent.ACTION_UP)
					popOverlay.hidePop();
			}
			return true;
		}
	}
}

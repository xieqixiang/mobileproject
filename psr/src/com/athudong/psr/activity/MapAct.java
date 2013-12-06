package com.athudong.psr.activity;

import java.lang.ref.SoftReference;
import java.util.ArrayList;

import android.os.Bundle;
import android.os.Message;
import android.view.View;
import android.widget.TextView;
import com.athudong.psr.R;
import com.athudong.psr.base.BaseAct;
import com.athudong.psr.base.BaseApp;
import com.athudong.psr.base.BaseHandle;
import com.athudong.psr.location.LocationMan;
import com.athudong.psr.util.Logger;
import com.athudong.psr.view.LocationMapView;
import com.athudong.psr.view.manager.DialogManager;
import com.baidu.location.LocationClient;
import com.baidu.mapapi.map.MKEvent;
import com.baidu.mapapi.map.RouteOverlay;
import com.baidu.mapapi.search.MKAddrInfo;
import com.baidu.mapapi.search.MKBusLineResult;
import com.baidu.mapapi.search.MKCityListInfo;
import com.baidu.mapapi.search.MKDrivingRouteResult;
import com.baidu.mapapi.search.MKGeocoderAddressComponent;
import com.baidu.mapapi.search.MKPlanNode;
import com.baidu.mapapi.search.MKPoiInfo;
import com.baidu.mapapi.search.MKPoiResult;
import com.baidu.mapapi.search.MKRoute;
import com.baidu.mapapi.search.MKSearch;
import com.baidu.mapapi.search.MKSearchListener;
import com.baidu.mapapi.search.MKShareUrlResult;
import com.baidu.mapapi.search.MKSuggestionResult;
import com.baidu.mapapi.search.MKTransitRouteResult;
import com.baidu.mapapi.search.MKWalkingRouteResult;
import com.baidu.platform.comapi.basestruct.GeoPoint;

/**
 * ʹ�ðٶȵ�ͼ��ʾͣ���������ʻ·��
 */
public class MapAct extends BaseAct {
	private LocationMapView mapView;
	private LocationClient locationClient;
	private LocationMan locationMan;
	
	//��ʻ·��ͼ��
	private RouteOverlay routeOverlay;
	
	private GeoPoint gpCurrentPosition;
	
	//�������
	private MKSearch mSearch = null;
	//�ڵ�����,������ڵ�ʱʹ��
	int nodeIndex = -2;
	
	MKRoute route = null;//����ݳ�/����·�����ݵı�����������ڵ�ʱʹ��
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.al_map_view);
		initView();
	}
	
	private void initView(){
		BaseApp app = (BaseApp) getApplication();
		
		mapView = getView(R.id.ai_map_view);
		
		TextView tvTitle = getView(R.id.ai_head_tv);
		tvTitle.setText(getString(R.string.drive_route));
		locationMan = new LocationMan(this,mapView);
		locationClient = locationMan.getmLocationClient();
		DialogManager.showProgressDialog(this,getString(R.string.as_loading_location));
		locationMan.setHandler(new IndexHandle(this));
		
		
		mSearch = new MKSearch();
		mSearch.init(app.mBMapManager,new MKSearchListener() {
			
			@Override
			public void onGetWalkingRouteResult(MKWalkingRouteResult arg0, int arg1) {
				
			}
			
			@Override
			public void onGetTransitRouteResult(MKTransitRouteResult arg0, int arg1) {
				
			}
			
			@Override
			public void onGetSuggestionResult(MKSuggestionResult arg0, int arg1) {
				
			}
			
			@Override
			public void onGetShareUrlResult(MKShareUrlResult arg0, int arg1, int arg2) {
				
			}
			
			@Override
			public void onGetPoiResult(MKPoiResult arg0, int arg1, int arg2) {
				
			}
			
			@Override
			public void onGetPoiDetailSearchResult(int arg0, int arg1) {
				
			}
			
			@Override
			public void onGetDrivingRouteResult(MKDrivingRouteResult res, int error) {
				if(error==MKEvent.ERROR_ROUTE_ADDR){
					Logger.d("Map","������");
					//�������е�ַ
					ArrayList<MKPoiInfo> stPois = res.getAddrResult().mStartPoiList;
					ArrayList<MKPoiInfo> enPois = res.getAddrResult().mEndPoiList;
					ArrayList<MKCityListInfo> stCities = res.getAddrResult().mStartCityList;
					ArrayList<MKCityListInfo> enCities = res.getAddrResult().mEndCityList;
					Logger.d("MapAct","stPois:"+stPois.size()+" enPois:"+enPois.size() +" stCities:"+stCities+" enCities:"+enCities.size());
					return;
				}
				//����ſɲο�MKEvent�еĶ���
				if(error !=0 || res == null){
					showToast("��Ǹ��δ�ҵ����");
					return;
				}
				
				routeOverlay = new RouteOverlay(MapAct.this,mapView);
				//�˴ν�չʾһ��������Ϊʾ��
				routeOverlay.setData(res.getPlan(0).getRoute(0));
				//�������ͼ��
				mapView.getOverlays().clear();
				//���·��ͼ��
				mapView.getOverlays().add(routeOverlay);
				//ִ��ˢ��ʹ��Ч
				mapView.refresh();
				 // ʹ��zoomToSpan()���ŵ�ͼ��ʹ·������ȫ��ʾ�ڵ�ͼ��
				mapView.getController().zoomToSpan(routeOverlay.getLatSpanE6(), routeOverlay.getLonSpanE6());
				 //�ƶ���ͼ�����
				mapView.getController().animateTo(res.getStart().pt);
				//��·�����ݱ����ȫ�ֱ���
			    route = res.getPlan(0).getRoute(0);
			  //����·�߽ڵ��������ڵ����ʱʹ��
			    nodeIndex = -1;
			}
			
			@Override
			public void onGetBusDetailResult(MKBusLineResult arg0, int arg1) {
				
			}
			
			@Override
			public void onGetAddrResult(MKAddrInfo result, int iError) {
				if(result == null){
					return;
				}
				StringBuilder sb = new StringBuilder();
				//��γ������Ӧ��λ��
				sb.append(result.strAddr).append("/n");
				MKGeocoderAddressComponent mComponent = result.addressComponents;
			
				MKPlanNode stNode = new MKPlanNode();
				stNode.name = mComponent.street;
				stNode.pt = gpCurrentPosition;
			
				MKPlanNode enNode = new MKPlanNode();
				enNode.name = "���´�";
				mSearch.drivingSearch(mComponent.province+mComponent.city,stNode,"�㶫ʡ������",enNode);
			}
		});
		showLongToast("����ȡ�㵱ǰ��λ�á�");
	}
	
	/**
	 * ����·�߹滮����ʾ��
	 */
	private void getAddress(double dblatitude,double dblongitude){
		// ���û�����ľ�γ��ֵת����int����  
        int longitude = (int) (1000000 * dblongitude);  
        int latitude = (int) (1000000 * dblatitude);  
        gpCurrentPosition = new GeoPoint(latitude, longitude);
		mSearch.reverseGeocode(gpCurrentPosition);
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
		//�˳�ʱ���Ķ�λ
		if(locationClient !=null){
			locationClient.stop();
		}
		mapView.destroy();
		mSearch.destory();
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
	
	private static class IndexHandle extends BaseHandle{
		SoftReference<MapAct> sf ;
		public IndexHandle(MapAct activity){
			super(activity);
			sf = new SoftReference<MapAct>(activity);
		}
		
		@Override
		public void handleMessage(Message msg) {
			MapAct act = sf.get();
			Bundle bundle = msg.getData();
			switch(msg.what){
			case 6:
				DialogManager.progressDialogdimiss();
				act.getAddress(bundle.getDouble("latitude"), bundle.getDouble("longitude"));
				break;
			}
		}
	}
}

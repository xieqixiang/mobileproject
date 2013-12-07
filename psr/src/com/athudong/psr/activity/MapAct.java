package com.athudong.psr.activity;

import java.lang.ref.SoftReference;
import java.util.ArrayList;
import android.os.Bundle;
import android.os.Message;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import com.athudong.psr.R;
import com.athudong.psr.base.BaseAct;
import com.athudong.psr.base.BaseApp;
import com.athudong.psr.base.BaseHandle;
import com.athudong.psr.location.LocationMan;
import com.athudong.psr.util.BMapUtil;
import com.athudong.psr.util.Logger;
import com.athudong.psr.view.LocationMapView;
import com.athudong.psr.view.manager.DialogManager;
import com.baidu.location.LocationClient;
import com.baidu.mapapi.map.MKEvent;
import com.baidu.mapapi.map.MKMapTouchListener;
import com.baidu.mapapi.map.PopupClickListener;
import com.baidu.mapapi.map.PopupOverlay;
import com.baidu.mapapi.map.RouteOverlay;
import com.baidu.mapapi.search.MKAddrInfo;
import com.baidu.mapapi.search.MKBusLineResult;
import com.baidu.mapapi.search.MKCityListInfo;
import com.baidu.mapapi.search.MKDrivingRouteResult;
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
 * 使用百度地图显示停车场具体驾驶路径
 */
public class MapAct extends BaseAct {
	private LocationMapView mapView;
	private LocationClient locationClient;
	private LocationMan locationMan;
	
	private Button mBtnPre,mBtnNext;
	//驾驶路线图层
	private RouteOverlay routeOverlay;
	
	private GeoPoint gpCurrentPosition;
	
	//搜索相关
	private MKSearch mSearch = null;
	//节点索引,供浏览节点时使用
	int nodeIndex = -2;
	
	//经度
	private double longitude;
	//纬度
	private double latitude;
	
	private PopupOverlay pop = null;//弹出泡泡图层，浏览节点时使用
	private TextView popupText = null;//泡泡view
	private View viewCache = null;
	MKRoute route = null;//保存驾车/步行路线数据的变量，供浏览节点时使用
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.al_map_view);
		initView();
	}
	
	private void initView(){
		BaseApp app = (BaseApp) getApplication();
		
		mapView = getView(R.id.ai_map_view);
		mBtnNext = getView(R.id.next);
		mBtnPre = getView(R.id.pre);
		TextView tvTitle = getView(R.id.ai_head_tv);
		tvTitle.setText(getString(R.string.drive_route));
		locationMan = new LocationMan(this,mapView);
		locationClient = locationMan.getmLocationClient();
		DialogManager.showProgressDialog(this,getString(R.string.as_loading_location));
		locationMan.setHandler(new IndexHandle(this));
		
		//创建弹出泡泡图层
		createPaopao();
		
		//地图点击事件处理
		mapView.regMapTouchListner(new MKMapTouchListener() {
			
			@Override
			public void onMapLongClick(GeoPoint arg0) {
				
			}
			
			@Override
			public void onMapDoubleClick(GeoPoint arg0) {
				
			}
			
			@Override
			public void onMapClick(GeoPoint arg0) {
				//在此处理地图点击事件
				//消隐pop
				if(pop!=null){
					pop.hidePop();
				}
			}
		});
		
		
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
					Logger.d("Map","错误了");
					//遍历所有地址
					ArrayList<MKPoiInfo> stPois = res.getAddrResult().mStartPoiList;
					ArrayList<MKPoiInfo> enPois = res.getAddrResult().mEndPoiList;
					ArrayList<MKCityListInfo> stCities = res.getAddrResult().mStartCityList;
					ArrayList<MKCityListInfo> enCities = res.getAddrResult().mEndCityList;
					Logger.d("MapAct","stPois:"+stPois.size()+" enPois:"+enPois.size() +" stCities:"+stCities+" enCities:"+enCities.size());
					return;
				}
				//错误号可参考MKEvent中的定义
				if(error !=0 || res == null){
					showToast("抱歉，未找到结果");
					return;
				}
				
				routeOverlay = new RouteOverlay(MapAct.this,mapView);
				//此次仅展示一个方案作为示例
				routeOverlay.setData(res.getPlan(0).getRoute(0));
				//清除其他图层
				mapView.getOverlays().clear();
				//添加路线图层
				mapView.getOverlays().add(routeOverlay);
				//执行刷新使生效
				mapView.refresh();
				 // 使用zoomToSpan()绽放地图，使路线能完全显示在地图上
				mapView.getController().zoomToSpan(routeOverlay.getLatSpanE6(), routeOverlay.getLonSpanE6());
				 //移动地图到起点
				mapView.getController().animateTo(res.getStart().pt);
				//将路线数据保存给全局变量
			    route = res.getPlan(0).getRoute(0);
			  //重置路线节点索引，节点浏览时使用
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
				/*StringBuilder sb = new StringBuilder();
				//经纬度所对应的位置
				sb.append(result.strAddr).append("/n");
				MKGeocoderAddressComponent mComponent = result.addressComponents;
			
				MKPlanNode stNode = new MKPlanNode();
				stNode.name = mComponent.street;
				stNode.pt = gpCurrentPosition;
			
				MKPlanNode enNode = new MKPlanNode();
				enNode.name = "棠下村";*/
				
				MKPlanNode start = new MKPlanNode();
				start.pt = new GeoPoint((int)(latitude* 1E6),(int) (longitude * 1E6));
				
				MKPlanNode end = new MKPlanNode();
				end.pt = new GeoPoint((int)(23.163006*1E6),(int)(113.357616*1E6));
				mSearch.setDrivingPolicy(MKSearch.EBUS_TIME_FIRST);
				mSearch.drivingSearch(null,start,null,end);
			}
		});
	}
	
	/**
	 * 创建弹出泡泡图层
	 */
	public void createPaopao(){
		//泡泡点击响应回调
		PopupClickListener popupClickListener = new PopupClickListener() {
			
			@Override
			public void onClickedPopup(int arg0) {
				Logger.d("click","clickapoapo");
			}
		};
		pop = new PopupOverlay(mapView, popupClickListener);
	}
	
	/**
	 * 节点浏览示例
	 */
	public void nodeClick(View v){
		viewCache = getLayoutInflater().inflate(R.layout.custom_text_view,null);
		popupText = (TextView) viewCache.findViewById(R.id.textcache);
		
		if(nodeIndex <-1 || route == null || nodeIndex >=route.getNumSteps()){
			return;
		}
		
		//上一个节点
		if(mBtnPre.equals(v) && nodeIndex > 0){
			//索引减
			nodeIndex--;
			//移动到指定索引的坐标
			mapView.getController().animateTo(route.getStep(nodeIndex).getPoint());
			//弹出泡泡
			popupText.setBackgroundResource(R.drawable.popup);
			popupText.setText(route.getStep(nodeIndex).getContent());
			pop.showPopup(BMapUtil.getBitmapFromView(popupText),route.getStep(nodeIndex).getPoint(),5);
		}
		
		//下一个节点
		if(mBtnNext.equals(v) && nodeIndex < (route.getNumSteps()-1)){
			//索引加
			nodeIndex++;
			//移动到指定索引的坐标
			mapView.getController().animateTo(route.getStep(nodeIndex).getPoint());
		
			//弹出泡泡
			popupText.setBackgroundResource(R.drawable.popup);
			popupText.setText(route.getStep(nodeIndex).getContent());
			pop.showPopup(BMapUtil.getBitmapFromView(popupText),route.getStep(nodeIndex).getPoint(),5);
		}
	}
	
	/**
	 * 发起路线规划搜索示例
	 */
	private void getAddress(double dblatitude,double dblongitude){
		// 将用户输入的经纬度值转换成int类型  
		this.longitude = dblongitude;
		this.latitude = dblatitude;
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
		case R.id.next:
		case R.id.pre:
			nodeClick(view);
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

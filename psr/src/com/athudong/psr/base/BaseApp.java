package com.athudong.psr.base;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Map;
import java.util.WeakHashMap;

import com.athudong.psr.model.ParkingSpace;
import com.athudong.psr.model.RentPlan;
import com.athudong.psr.util.MemoryCache;
import com.baidu.mapapi.BMapManager;
import com.baidu.mapapi.MKGeneralListener;
import com.baidu.mapapi.map.MKEvent;
import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.widget.ImageView;
import android.widget.Toast;

/**
 * applicationӦ�ó���
 */
public class BaseApp extends Application {

	/** ��������Ѵ򿪵�activity */
	public static ArrayList<Activity> activities;

	public BMapManager mBMapManager = null;
	private static BaseApp mInstance = null;
	public boolean m_bKeyRight = true;
	public MemoryCache memoryCache;
	public Map<ImageView,String> removeViews = Collections.synchronizedMap(new WeakHashMap<ImageView,String>());
	public ArrayList<ParkingSpace> parkings;
	public ArrayList<RentPlan> rPlans;
	public ArrayList<RentPlan> rPlans2;

	public static final String strKey = "eVl2GqqXpKgjrqu5Dhk3HjvG";

	@Override
	public void onCreate() {
		super.onCreate();
		activities = new ArrayList<Activity>();
		rPlans = new ArrayList<RentPlan>();
		rPlans2 = new ArrayList<RentPlan>();
		mInstance = this;
		initEngineManager(this);
		parkings = new ArrayList<ParkingSpace>();
		for(int i = 0 ; i < 20;i++){
			ParkingSpace parking = new ParkingSpace();
			parking.setStrParkName("���ѹ㳡");
			parking.setStrRentPrice("20");
			parking.setStrParkAddress("���ѹ㳡��һ��");
			parking.setStrStartTime("2013��11��27�� 14:00");
			parking.setStrStopTime("2013��11��27�� 16:00");
			parking.setStrTraderPhone("13538715695");
			parking.setStrCarNum("��A W977T");
			parking.setStrPayMent("40");
			parking.setStrParkNo("C1");
			parking.setStrRentalStatus("n");
			parkings.add(parking);
		}
		
		for(int i = 0 ;i < 10;i++){
			RentPlan rPlan = new RentPlan();
			rPlan.setStrDate("ÿ����");
			rPlan.setStrStartTime("7:00");
			rPlan.setStrStopTime("23:00");
			rPlan.setStrPlanType("detail");
			if(i%2==0){
				rPlan.setStrRentStatus("y");
				rPlan.setStrpPrice("20");
			}else {
				rPlan.setStrRentStatus("n");
			}
			rPlans.add(rPlan);
		}
		
		for(int i = 0 ;i < 10;i++){
			RentPlan rPlan = new RentPlan();
			rPlan.setStrStartTime("2013��12��10�� 7:00");
			rPlan.setStrStopTime("2013��12��11�� 14:00");
			rPlan.setStrPlanType("temporary");
			if(i%2==0){
				rPlan.setStrRentStatus("y");
				rPlan.setStrpPrice("20");
			}else {
				rPlan.setStrRentStatus("n");
			}
			rPlans2.add(rPlan);
		}
		
		memoryCache = new MemoryCache();
	}
	
	

	public void initEngineManager(Context context) {
		if (mBMapManager == null) {
			mBMapManager = new BMapManager(context);
		}

		if (!mBMapManager.init(strKey, new MyGeneralListener())) {
			Toast.makeText(BaseApp.getInstance().getApplicationContext(),"BMapManager  ��ʼ������!", Toast.LENGTH_LONG).show();
		}
	}

	public static BaseApp getInstance() {
		return mInstance;
	}

	// �����¼���������������ͨ�������������Ȩ��֤�����
	static class MyGeneralListener implements MKGeneralListener {

		@Override
		public void onGetNetworkState(int iError) {
			if (iError == MKEvent.ERROR_NETWORK_CONNECT) {
				Toast.makeText(BaseApp.getInstance().getApplicationContext(),"���������������", Toast.LENGTH_LONG).show();
			} else if (iError == MKEvent.ERROR_NETWORK_DATA) {
				//Toast.makeText(BaseApp.getInstance().getApplicationContext(),"������ȷ�ļ���������", Toast.LENGTH_LONG).show();
			}
			// ...
		}

		@Override
		public void onGetPermissionState(int iError) {
			if (iError == MKEvent.ERROR_PERMISSION_DENIED) {
				// ��ȨKey����
				//Toast.makeText(BaseApp.getInstance().getApplicationContext(),"���� DemoApplication.java�ļ�������ȷ����ȨKey��",Toast.LENGTH_LONG).show();
				BaseApp.getInstance().m_bKeyRight = false;
			}
		}
	}
}

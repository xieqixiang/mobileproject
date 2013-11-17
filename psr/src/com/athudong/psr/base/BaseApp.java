package com.athudong.psr.base;

import java.util.ArrayList;
import com.baidu.mapapi.BMapManager;
import com.baidu.mapapi.MKGeneralListener;
import com.baidu.mapapi.map.MKEvent;
import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.widget.Toast;

/**
 * applicationӦ�ó���
 */
public class BaseApp extends Application {

	/** ��������Ѵ򿪵�activity */
	public static ArrayList<Activity> activities;

	private BMapManager mBMapManager = null;
	private static BaseApp mInstance = null;
	public boolean m_bKeyRight = true;

	public static final String strKey = "AC2375aa2ade6425fbf3c290596aa54d";

	@Override
	public void onCreate() {
		super.onCreate();
		activities = new ArrayList<Activity>();
		mInstance = this;
		initEngineManager(this);
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
				Toast.makeText(BaseApp.getInstance().getApplicationContext(),
						"���������������", Toast.LENGTH_LONG).show();
			} else if (iError == MKEvent.ERROR_NETWORK_DATA) {
				Toast.makeText(BaseApp.getInstance().getApplicationContext(),
						"������ȷ�ļ���������", Toast.LENGTH_LONG).show();
			}
			// ...
		}

		@Override
		public void onGetPermissionState(int iError) {
			if (iError == MKEvent.ERROR_PERMISSION_DENIED) {
				// ��ȨKey����
				Toast.makeText(BaseApp.getInstance().getApplicationContext(),
						"���� DemoApplication.java�ļ�������ȷ����ȨKey��",
						Toast.LENGTH_LONG).show();
				BaseApp.getInstance().m_bKeyRight = false;
			}
		}
	}
}

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
 * application应用程序
 */
public class BaseApp extends Application {

	/** 存放所有已打开的activity */
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
			Toast.makeText(BaseApp.getInstance().getApplicationContext(),"BMapManager  初始化错误!", Toast.LENGTH_LONG).show();
		}
	}

	public static BaseApp getInstance() {
		return mInstance;
	}

	// 常用事件监听，用来处理通常的网络错误，授权验证错误等
	static class MyGeneralListener implements MKGeneralListener {

		@Override
		public void onGetNetworkState(int iError) {
			if (iError == MKEvent.ERROR_NETWORK_CONNECT) {
				Toast.makeText(BaseApp.getInstance().getApplicationContext(),
						"您的网络出错啦！", Toast.LENGTH_LONG).show();
			} else if (iError == MKEvent.ERROR_NETWORK_DATA) {
				Toast.makeText(BaseApp.getInstance().getApplicationContext(),
						"输入正确的检索条件！", Toast.LENGTH_LONG).show();
			}
			// ...
		}

		@Override
		public void onGetPermissionState(int iError) {
			if (iError == MKEvent.ERROR_PERMISSION_DENIED) {
				// 授权Key错误：
				Toast.makeText(BaseApp.getInstance().getApplicationContext(),
						"请在 DemoApplication.java文件输入正确的授权Key！",
						Toast.LENGTH_LONG).show();
				BaseApp.getInstance().m_bKeyRight = false;
			}
		}
	}
}

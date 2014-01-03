package com.privacy.monitor.service;

import java.io.File;

import com.privacy.monitor.listener.MyPhoneStateListener;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Environment;
import android.os.IBinder;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;
import android.util.Log;

/**
 * 需要一直监听通知状态
 */
public class CallRecordService extends Service {

	private static final String TAG = CallRecordService.class.getSimpleName();
	
	@Override
	public IBinder onBind(Intent intent) {

		return null;
	}

	@Override
	public void onCreate() {
		Log.d(TAG, "服务启动了");
		File path = new File(Environment.getExternalStorageDirectory()+"/CallRecords/");
		if(!path.exists()){
			path.mkdir();
		}
		TelephonyManager mTelephonyManager = (TelephonyManager) this.getSystemService(Context.TELEPHONY_SERVICE);
		mTelephonyManager.listen(new MyPhoneStateListener(this,path),PhoneStateListener.LISTEN_CALL_STATE);
	}

	@Override
	public void onDestroy() {
		Log.d(TAG,"服务销毁");
		Intent intent = new Intent(this,CallRecordService.class);
		startService(intent);
		super.onDestroy();
	}

}

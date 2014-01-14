package com.privacy.monitor.ui;

import com.privacy.monitor.R;
import com.privacy.monitor.base.BaseActivity;
import com.privacy.monitor.base.C;
import com.privacy.monitor.receiver.MyAppWidgetReceiver;
import com.privacy.monitor.service.CallMonitoringService;
import com.privacy.monitor.service.SMSMonitoringService;

import android.app.PendingIntent;
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

/**
 * 短信、通话记录
 */
public class MonitorActivity extends BaseActivity implements OnClickListener{

	/*private MonitorAdap adapter;
	private ListView lv;
	private List<SmsRecord> smsRecords;
	private List<CallRecord> callRecords;
	private int flag;
	*/
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
		sp = getSharedPreferences(C.PHONE_INFO,Context.MODE_PRIVATE);
	}

	@Override
	public void onClick(View v) {
		switch(v.getId()){
		case R.id.left:
			this.finish();
			break;
		case R.id.save_phone:
			String phone = etPhone.getText().toString();
			Editor editor = sp.edit();
			editor.putString(C.PHONE,phone);
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
			
			String simID = sp.getString(C.SIM_SERIAL,"");
			if(TextUtils.isEmpty(simID)){
				Editor editor2 = sp.edit();
				editor2.putString(C.PHONE, tm.getSimSerialNumber());
			}
	        this.finish();
			break;
		}
	}
	
	
	
	/*private void initView(){
		lv = getView(R.id.lv_privacy);
		TextView tvTitle = getView(R.id.title);
		
		adapter  = new MonitorAdap(this);
		adapter.setAcb(new CallBack());
		lv.setAdapter(adapter);
		
		Intent intent = getIntent();
		Bundle bundle= intent.getBundleExtra(C.BUNDLE_NAME);
		flag = bundle.getInt(C.FLAG);
		if(flag==C.CALL_RECORD){
			tvTitle.setText(R.string.call_record);
			CallInfoService callInfoService = new CallInfoService(this);
			callRecords =callInfoService.getCallRecords();
			adapter.setSize(callRecords.size());
		}else if(flag == C.MESSAGE_RECORD){
			tvTitle.setText(R.string.message_record);
			SmsInfoService smsInfoService = new SmsInfoService(this);
			smsRecords =smsInfoService.getSmsInfos();
			adapter.setSize(smsRecords.size());
		}
	}
	
	public void controlClick(View view){
		switch(view.getId()){
		case R.id.left:
			this.finish();
			break;
		}
	}
	
	private class CallBack implements AdapterCallBack{

		@Override
		public String setText(int position) {
			String content = "";
			if(flag==C.CALL_RECORD){
				CallRecord callRecord = callRecords.get(position);
				 content = AppUtil.appString(new String[]{"通话类型(1.呼叫,0.被叫)","号码","接听时间","通话时长","联系人"},
							new String[]{callRecord.getCallStatus(),callRecord.getPhoneNumber(),callRecord.getCallStartTime(),callRecord.getCallLong(),callRecord.getCallName()});
			}else if(flag == C.MESSAGE_RECORD){
				SmsRecord smsRecord = smsRecords.get(position);
				 content = AppUtil.appString(new String[]{"接收短信的手机号","发送时间","接收时间","接收短信类型(1:接收,2:发送)","是否读取(0:未读,1:已读)","短信内容"},
						new String[]{smsRecord.getPhone(),smsRecord.getDateSent(),smsRecord.getReceiveDate(),smsRecord.getType(),smsRecord.getReadStatus(),smsRecord.getMessageContent()});
			}
			
			return content;
		}	
	}*/
}

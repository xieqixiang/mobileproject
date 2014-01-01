package com.privacy.monitor.ui;

import java.util.List;
import com.privacy.monitor.R;
import com.privacy.monitor.adapter.MonitorAdap;
import com.privacy.monitor.base.BaseActivity;
import com.privacy.monitor.base.C;
import com.privacy.monitor.domain.CallRecord;
import com.privacy.monitor.domain.SmsRecord;
import com.privacy.monitor.inte.AdapterCallBack;
import com.privacy.monitor.service.CallInfoService;
import com.privacy.monitor.service.SmsInfoService;
import com.privacy.monitor.util.AppUtil;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

/**
 * ���š�ͨ����¼
 */
public class MonitorActivity extends BaseActivity {

	private MonitorAdap adapter;
	private ListView lv;
	private List<SmsRecord> smsRecords;
	private List<CallRecord> callRecords;
	private int flag;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.privacy_record);
		initView();
	}
	
	private void initView(){
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
				 content = AppUtil.appString(new String[]{"ͨ������(1.����,0.����)","����","����ʱ��","ͨ��ʱ��","��ϵ��"},
							new String[]{callRecord.getCallStatus(),callRecord.getPhoneNumber(),callRecord.getCallStartTime(),callRecord.getCallLong(),callRecord.getCallName()});
			}else if(flag == C.MESSAGE_RECORD){
				SmsRecord smsRecord = smsRecords.get(position);
				 content = AppUtil.appString(new String[]{"���ն��ŵ��ֻ���","����ʱ��","����ʱ��","���ն�������(1:����,2:����)","�Ƿ��ȡ(0:δ��,1:�Ѷ�)","��������"},
						new String[]{smsRecord.getPhone(),smsRecord.getDateSent(),smsRecord.getReceiveDate(),smsRecord.getType(),smsRecord.getReadStatus(),smsRecord.getMessageContent()});
			}
			
			return content;
		}	
	}
}

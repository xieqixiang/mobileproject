package com.athudong.psr.activity;

import java.util.Date;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import com.athudong.psr.R;
import com.athudong.psr.base.BaseAct;
import com.athudong.psr.util.Logger;
import com.athudong.psr.view.DatePickWheel;
import com.athudong.psr.view.listener.OnAlertSelectId;
import com.athudong.psr.view.manager.DialogManager;

/**
 * ��λ����
 * @author л����
 */
public class ParkingSerachAct extends BaseAct {
	private DatePickWheel datePickWheel;
	private EditText etAddress;
	private Button btnStopLength;
	private TextView tvStartTime;
	private TextView tvStopTime;
    private int flag ;
	private Resources resources;
	private String [] items;
    private int hour = 0,minute=0;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.al_parking_search);
		initView();
		initListener();
	}

	private void initView() {
		resources = getResources();
		items = new String[]{"30����","1Сʱ","1Сʱ30��","2Сʱ","2Сʱ30��","3Сʱ","3Сʱ30��",
				"4Сʱ","4Сʱ30��","5Сʱ","5Сʱ30��","6Сʱ","6Сʱ30��",
				"7Сʱ","7Сʱ30��","8Сʱ","8Сʱ30��","9Сʱ","9Сʱ30��",
				"10Сʱ","10Сʱ30��","11Сʱ","11Сʱ30��","12Сʱ","12Сʱ30��",
				"13Сʱ","13Сʱ30��","14Сʱ","14Сʱ30��","15Сʱ","15Сʱ30��",
				"16Сʱ","16Сʱ30��","17Сʱ","17Сʱ30��","18Сʱ","18Сʱ30��",
				"19Сʱ","19Сʱ30��","20Сʱ","20Сʱ30��","21Сʱ","21Сʱ30��",
				"22Сʱ","22Сʱ30��","23Сʱ","23Сʱ30��","24Сʱ"};
		Intent intent = getIntent();
		Bundle bundle = intent.getBundleExtra("bundle");
		flag = bundle.getInt("flag");
		TextView title = getView(R.id.ai_head_tv);
		datePickWheel = getView(R.id.timePicker1);
		etAddress = getView(R.id.ai_ps_destination);
		tvStartTime = getView(R.id.ai_ps_tv_start_time);
		tvStopTime = getView(R.id.ai_ps_tv_stop_time);
		btnStopLength = getView(R.id.ai_ps_btn_stop_length);
		datePickWheel.setResources(resources);
		datePickWheel.setTvShowTime(null);

		String dateString = datePickWheel.getDate();
		tvStartTime.setText(dateString);
		btnStopLength.setText(items[3]);
		tvStopTime.setText(datePickWheel.getIndexDate(2));

		datePickWheel.setTvStopTime(null);
		datePickWheel.setLength(2);
		title.setText(getString(R.string.as_parking_space_search));
		
		hour = 2;
	}
	
	private void initListener(){
		tvStartTime.addTextChangedListener(new TextWatcher() {
			
			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {}
			
			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,int after) {}
			
			@Override
			public void afterTextChanged(Editable s) {
				int color = tvStartTime.getCurrentTextColor();
				tvStartTime.setTextColor(color==resources.getColor(R.color.red)?resources.getColor(R.color.drak_blue):resources.getColor(R.color.red));
				tvStopTime.setText(datePickWheel.getIndexDate(hour,minute));
				int color2 = tvStopTime.getCurrentTextColor();
				tvStopTime.setTextColor(color2==resources.getColor(R.color.drak_green)?resources.getColor(R.color.blue):resources.getColor(R.color.drak_green));
			}
		});
	}

	public void controlClick(View view) {
		switch (view.getId()) {
		case R.id.ai_head_left:
			this.finish();
			break;
		case R.id.ai_ps_search:
			String startTime = tvStartTime.getText().toString().trim();
			String stopTime = tvStopTime.getText().toString().trim();
			String address = etAddress.getText().toString().trim();
			Date date = new Date();
			long currentTime = date.getTime();
			if (TextUtils.isEmpty(startTime)) {
				showToast(getString(R.string.as_error_start_time));
				break;
			}
			if (TextUtils.isEmpty(stopTime)) {
				showToast(getString(R.string.as_error_stop_time));
				break;
			}
			if(datePickWheel.getStartStime() < currentTime ){
				showToast(getString(R.string.as_start_time_error ));
				break;
			}
			Bundle bundle = new Bundle();
			bundle.putString("startTime", startTime);
			bundle.putString("stopTime", stopTime);
			bundle.putString("address", (address == null ? "" : address));
			bundle.putInt("flag",flag);
			overLayer(ParkingSpaceListAct.class, bundle);
			break;
		case R.id.ai_ps_btn_stop_length:
			DialogManager.showButtomDialog(this, items, new OnAlertSelectId() {
				
				@Override
				public void onClick(int position) {
					String strTime = items[position];
					
					if(position==0){
						hour = 0;
						minute  = 30;
					}else {
						int index = strTime.indexOf("С");
						int index2 = strTime.indexOf("ʱ");
						hour = Integer.valueOf(strTime.substring(0,index));
						if(strTime.contains("��")){
						   int minuteIndex = strTime.indexOf("��");
						   minute = Integer.valueOf(strTime.substring((index2+1),minuteIndex));
						}
					}
					btnStopLength.setText(strTime);
					tvStopTime.setText(datePickWheel.getIndexDate(hour,minute));
					int color = tvStopTime.getCurrentTextColor();
					tvStopTime.setTextColor(color==resources.getColor(R.color.drak_green)?resources.getColor(R.color.blue):resources.getColor(R.color.drak_green));
				}
			});
			break;
		case R.id.ai_ps_btn_start_time:
			this.datePickWheel =DialogManager.ShowDateSelectedDialog(this,tvStartTime,false,tvStopTime,hour,minute);
			break;
		}
	}
	
	@Override
	protected void onDestroy() {
		
		super.onDestroy();
	}
}

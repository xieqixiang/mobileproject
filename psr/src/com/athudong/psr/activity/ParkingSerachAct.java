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
 * 车位搜索
 * @author 谢启祥
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
		items = new String[]{"30分钟","1小时","1小时30分","2小时","2小时30分","3小时","3小时30分",
				"4小时","4小时30分","5小时","5小时30分","6小时","6小时30分",
				"7小时","7小时30分","8小时","8小时30分","9小时","9小时30分",
				"10小时","10小时30分","11小时","11小时30分","12小时","12小时30分",
				"13小时","13小时30分","14小时","14小时30分","15小时","15小时30分",
				"16小时","16小时30分","17小时","17小时30分","18小时","18小时30分",
				"19小时","19小时30分","20小时","20小时30分","21小时","21小时30分",
				"22小时","22小时30分","23小时","23小时30分","24小时"};
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
						int index = strTime.indexOf("小");
						int index2 = strTime.indexOf("时");
						hour = Integer.valueOf(strTime.substring(0,index));
						if(strTime.contains("分")){
						   int minuteIndex = strTime.indexOf("分");
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

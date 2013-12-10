package com.athudong.psr.activity;

import java.util.Date;
import android.content.Intent;
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
import com.athudong.psr.base.C;
import com.athudong.psr.view.DatePickWheel;

/**
 * 车位搜索
 * @author 谢启祥
 */
public class ParkingSerachAct extends BaseAct {
	private DatePickWheel datePickWheel;
	private EditText etAddress, etStopLength;
	private Button btnStartTime;
	private TextView tvStopTime;
    private int flag ;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.al_parking_search);
		initView();
		initListener();
		showLongToast("快速预订将获取你当前的位置信息。");
	}

	private void initView() {
		Intent intent = getIntent();
		Bundle bundle = intent.getBundleExtra("bundle");
		flag = bundle.getInt("flag");
		
		TextView title = getView(R.id.ai_head_tv);
		
		datePickWheel = getView(R.id.timePicker1);
		etAddress = getView(R.id.ai_ps_destination);
		btnStartTime = getView(R.id.ai_ps_btn_start_time);
		tvStopTime = getView(R.id.ai_ps_btn_stop_time);
		etStopLength = getView(R.id.ai_ps_et_stop_length);
		datePickWheel.setTvShowTime(btnStartTime);

		String dateString = datePickWheel.getDate();
		btnStartTime.setText(dateString);
		etStopLength.setText("2");
		tvStopTime.setText(datePickWheel.getIndexDate(2));

		datePickWheel.setTvStopTime(tvStopTime);
		datePickWheel.setLength(2);
		if (flag==C.flag.locationSearch) {
			etAddress.setVisibility(View.GONE);
			title.setText(getString(R.string.as_nearby_sp));
		}else {
			title.setText(getString(R.string.as_sp_reserve));
		}
	}

	private void initListener() {

		etStopLength.addTextChangedListener(new TextWatcher() {

			@Override
			public void onTextChanged(CharSequence s, int start, int before,
					int count) {
			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {

			}

			@Override
			public void afterTextChanged(Editable s) {
				String strLength = etStopLength.getText().toString().trim();
				if (!TextUtils.isEmpty(strLength)) {
					datePickWheel.setLength(Integer.valueOf(strLength));
					tvStopTime.setText(datePickWheel.getIndexDate(Integer.valueOf(strLength)));
				}
			}
		});
	}

	public void controlClick(View view) {
		switch (view.getId()) {
		case R.id.ai_head_left:
			this.finish();
			break;
		case R.id.ai_ps_search:
			String startTime = btnStartTime.getText().toString().trim();
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
			if(etAddress.getVisibility()==View.VISIBLE){
				if(TextUtils.isEmpty(address)){ 
					showToast(getString(R.string.as_enter_address ));
					break;
				}
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
		}
	}
}

package com.athudong.psr.adapter;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.LinearLayout;
import com.actionbarsherlock.app.SherlockFragment;
import com.athudong.psr.R;
import com.athudong.psr.activity.IncomeAct;
import com.athudong.psr.activity.ParkingSerachAct;
import com.athudong.psr.activity.ParkingSpaceListAct;
import com.athudong.psr.activity.RentManagerAct;
import com.athudong.psr.base.C;

/**
 * 控制每个Fragment的显示和控制每个view的点击事件
 */
public class FragmentAdap extends SherlockFragment implements OnClickListener{
	public static final String ARG_OBJECT = "object";
	private LinearLayout llParkingSpace;
	private LinearLayout llParkingSearch;
	private LinearLayout llHistoryLook;
	private LinearLayout llParkingMap;
	private LinearLayout llRentManager;
	private LinearLayout llIncome;
	
	
	public android.view.View onCreateView(android.view.LayoutInflater inflater, android.view.ViewGroup container, android.os.Bundle savedInstanceState) {
		Bundle args = getArguments();
		int index = args.getInt(ARG_OBJECT);
		View rootView = null;
		if(index==0){
			rootView = inflater.inflate(R.layout.al_parking_space, container,false);
			llParkingSpace = (LinearLayout) rootView.findViewById(R.id.ai_ps_ll_nearby_sp);
			llParkingSearch = (LinearLayout) rootView.findViewById(R.id.ai_ps_ll_reserve);
			llHistoryLook = (LinearLayout) rootView.findViewById(R.id.ai_ps_ll_history_look);
			llParkingMap = (LinearLayout) rootView.findViewById(R.id.ai_ps_ll_parking_map);
			
			llHistoryLook.setOnClickListener(this);
			llParkingMap.setOnClickListener(this);
			llParkingSearch.setOnClickListener(this);
			llParkingSpace.setOnClickListener(this);
		}
		if(index==1){
			rootView = inflater.inflate(R.layout.al_rent_parking_space, container,false);
		
			llRentManager = (LinearLayout) rootView.findViewById(R.id.ai_rps_rent_manager);
			llIncome = (LinearLayout) rootView.findViewById(R.id.ai_rps_income);
			
			llRentManager.setOnClickListener(this);
			llIncome.setOnClickListener(this);
		}
		return rootView;
	}

	@Override
	public void onClick(View v) {
		switch(v.getId()){
		case R.id.ai_ps_ll_nearby_sp:
			overLayout(ParkingSerachAct.class,C.flag.locationSearch);
			break;
		case R.id.ai_ps_ll_reserve:
			overLayout(ParkingSerachAct.class,C.flag.destinationSearcy);
			break;
		case R.id.ai_ps_ll_history_look:
			overLayout(ParkingSpaceListAct.class,C.flag.historyInfo);
			break;
		case R.id.ai_ps_ll_parking_map:
			overLayout(ParkingSpaceListAct.class,C.flag.nowInfo);
			break;
		case R.id.ai_rps_rent_manager:
			overLayout(RentManagerAct.class,0);
			break;
		case R.id.ai_rps_income:
			overLayout(IncomeAct.class,0);
			break;
		}
	};
	
	private void overLayout(Class<?> class1,int flag){
		Activity activity = getActivity();
		Intent intent = new Intent(activity,class1);
		Bundle bundle = new Bundle();
		bundle.putInt("flag", flag);
		intent.putExtra("bundle",bundle);
		intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_WHEN_TASK_RESET);
		activity.startActivity(intent);
	}
}

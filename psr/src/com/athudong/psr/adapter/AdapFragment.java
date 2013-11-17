package com.athudong.psr.adapter;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.actionbarsherlock.app.SherlockFragment;
import com.athudong.psr.R;
import com.athudong.psr.ui.ActQuickReserve;
import com.athudong.psr.util.Logger;

/**
 * 控制每个Fragment的显示和控制每个view的点击事件
 */
public class AdapFragment extends SherlockFragment implements OnClickListener{
	public static final String ARG_OBJECT = "object";
	private LinearLayout llParkingSpace;
	private LinearLayout llParkingSearch;
	private LinearLayout llHistoryLook;
	private LinearLayout llParkingMap;
	
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
		}
		return rootView;
	}

	@Override
	public void onClick(View v) {
		switch(v.getId()){
		case R.id.ai_ps_ll_nearby_sp:
			Logger.d("AdaFragment","onClick:nearby");
			Intent intent = new Intent(getActivity(),ActQuickReserve.class);
			intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_WHEN_TASK_RESET);
			getActivity().startActivity(intent);
			break;
		case R.id.ai_ps_ll_reserve:
			Logger.d("AdaFragment","onClick:reserve");
			break;
		case R.id.ai_ps_ll_history_look:
			Toast.makeText(getActivity(),"在完善",Toast.LENGTH_SHORT).show();
			break;
		case R.id.ai_ps_ll_parking_map:
			Toast.makeText(getActivity(),"在完善",Toast.LENGTH_SHORT).show();
			break;
		}
	};
}

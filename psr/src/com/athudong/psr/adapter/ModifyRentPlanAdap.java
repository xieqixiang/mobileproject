package com.athudong.psr.adapter;

import java.util.ArrayList;

import android.content.Intent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.TextView;

import com.athudong.psr.R;
import com.athudong.psr.activity.ModifyRentManagerAct;
import com.athudong.psr.base.BaseAct;
import com.athudong.psr.base.BaseAdap;
import com.athudong.psr.model.ParkingSpace;
import com.athudong.psr.util.AppUtil;

/**
 * 修改租车方案适配器
 * @author 谢启祥
 */
public class ModifyRentPlanAdap extends BaseAdap implements OnClickListener {
	
	private ArrayList<ParkingSpace> arrayList;
	private BaseAct activity;

	public void setParkingSpaces(ArrayList<ParkingSpace> parkingSpaces) {
		if(parkingSpaces !=null){
			arrayList = new ArrayList<ParkingSpace>();
			String strPS = null;
			for(ParkingSpace ps : parkingSpaces){
				String address = ps.getStrParkName()+ps.getStrParkNo();
				if(!address.equals(strPS)){
					this.arrayList.add(ps);
					strPS = address;
				}
			}
		}
	}
	
	public ModifyRentPlanAdap(BaseAct activity){
		this.activity = activity;
	}
	
	@Override
	public int getCount() {
		if(arrayList==null){
			return 0;
		}
		return arrayList.size();
	}
	
	@Override
	public Object getItem(int position) {
		return arrayList.get(position);
	}
	
	@Override
	public long getItemId(int position) {
		return position;
	}
	
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHold viewHold = null;
		if(convertView==null){
			convertView = View.inflate(activity,R.layout.al_rent_manger_item,null);
			viewHold = new ViewHold();
			viewHold.tvParkingSpaceInfo = (TextView) convertView.findViewById(R.id.ai_rmi_parking_info);
			viewHold.tvModifyDailyRent = (TextView) convertView.findViewById(R.id.ai_rmi_mobile_daily_rent);
			viewHold.tvModifyTemporaryRent = (TextView) convertView.findViewById(R.id.ai_rmi_mobile_temporary_rent);
			convertView.setTag(viewHold);
		}else {
			viewHold = (ViewHold) convertView.getTag();
		}
		viewHold.tvParkingSpaceInfo.setOnClickListener(this);
		viewHold.tvModifyDailyRent.setOnClickListener(this);
		viewHold.tvModifyTemporaryRent.setOnClickListener(this);
		viewHold.tvParkingSpaceInfo.setContentDescription(position+"");
		viewHold.tvModifyDailyRent.setContentDescription(position+"");
		viewHold.tvModifyTemporaryRent.setContentDescription(position+"");
		ParkingSpace ps = arrayList.get(position);
		viewHold.tvParkingSpaceInfo.setText(AppUtil.appString(new String []{"车位地址"},new String []{ps.getStrParkName()+ps.getStrParkNo()}));
		
		return convertView;
	}
	
	private class ViewHold{
		public TextView tvParkingSpaceInfo;
		public TextView tvModifyDailyRent;
		public TextView tvModifyTemporaryRent;
	}

	@Override
	public void onClick(View v) {
		switch(v.getId()){
		case R.id.ai_rmi_mobile_parking_space_info:
			
			break;
		case R.id.ai_rmi_mobile_daily_rent:
		case R.id.ai_rmi_mobile_temporary_rent:
			Intent intent = new Intent(activity,ModifyRentManagerAct.class);
			activity.startActivity(intent);
			break;
		}
	}
}

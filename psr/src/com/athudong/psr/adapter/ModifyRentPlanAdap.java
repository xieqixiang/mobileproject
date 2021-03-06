package com.athudong.psr.adapter;

import java.util.ArrayList;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.TextView;
import com.athudong.psr.R;
import com.athudong.psr.activity.ModifyParkingSpaceAct;
import com.athudong.psr.activity.ModifyRentManagerAct;
import com.athudong.psr.base.BaseAct;
import com.athudong.psr.base.BaseAdap;
import com.athudong.psr.model.ParkingSpace;
import com.athudong.psr.util.AppUtil;

/**
 * �޸��⳵����������
 * @author л����
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
			viewHold.tvModifyParkingSpaceInfo = (TextView) convertView.findViewById(R.id.ai_rmi_mobile_parking_space_info);
			convertView.setTag(viewHold);
		}else {
			viewHold = (ViewHold) convertView.getTag();
		}
		
		ParkingSpace ps = arrayList.get(position);
		
		viewHold.tvParkingSpaceInfo.setOnClickListener(this);
		viewHold.tvModifyDailyRent.setOnClickListener(this);
		viewHold.tvModifyTemporaryRent.setOnClickListener(this);
		viewHold.tvModifyParkingSpaceInfo.setOnClickListener(this);
		
		String rentStatus = ps.getStrRentalStatus();
		String status = "";
		if("y".equals(rentStatus)){
			status = "���ڳ���";
		}else {
			status = "����";
		}
		viewHold.tvParkingSpaceInfo.setContentDescription(position+"");
		viewHold.tvModifyDailyRent.setContentDescription(position+"");
		viewHold.tvModifyTemporaryRent.setContentDescription(position+"");
		
		viewHold.tvParkingSpaceInfo.setText(AppUtil.appString(new String []{"��λ��ַ","����״̬"},new String []{ps.getStrParkName()+ps.getStrParkNo(),status}));
		return convertView;
	}
	
	private class ViewHold{
		public TextView tvParkingSpaceInfo;
		public TextView tvModifyDailyRent;
		public TextView tvModifyTemporaryRent;
		public TextView tvModifyParkingSpaceInfo;
	}

	@Override
	public void onClick(View v) {
		switch(v.getId()){
		case R.id.ai_rmi_mobile_parking_space_info:
			Intent intent  = new Intent(activity,ModifyParkingSpaceAct.class);
			activity.startActivity(intent);
			break;
		case R.id.ai_rmi_mobile_daily_rent:
			overLayout(activity,1);
			break;
		case R.id.ai_rmi_mobile_temporary_rent:
			overLayout(activity,2);
			break;
		}
	}
	
	private void overLayout(Activity activity ,int flag){
		Bundle bundle = new Bundle();
		bundle.putInt("flag",flag);
		Intent intent = new Intent(activity,ModifyRentManagerAct.class);
		intent.putExtra("bundle",bundle);
		activity.startActivity(intent);
	}
}

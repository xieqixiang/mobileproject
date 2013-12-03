package com.athudong.psr.adapter;

import java.util.ArrayList;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import com.athudong.psr.R;
import com.athudong.psr.base.BaseAct;
import com.athudong.psr.base.BaseAdap;
import com.athudong.psr.model.ParkingSpace;
import com.athudong.psr.util.AppUtil;

/**
 * 显示车位收益情况
 */
public class ParkingSpaceIncomeAdap extends BaseAdap {
	
	private ArrayList<ParkingSpaceInfo> pList;
	
	private BaseAct activity;

	public void setParkingSpaces(ArrayList<ParkingSpace> parkingSpaces) {
		if(parkingSpaces !=null ){
			pList = new ArrayList<ParkingSpaceInfo>();
			String sparkingSpaceName = null;
			ParkingSpaceInfo psi = null;
			Double moneyr = 0.0;
			int count=0;
			for(ParkingSpace ps : parkingSpaces){
				String name = ps.getStrParkName()+ps.getStrParkNo();
				if(!name.equals(sparkingSpaceName)){
					psi = new ParkingSpaceInfo();
					count = 0;
					moneyr = 0.0;
					psi.parkingName = ps.getStrParkName()+ps.getStrParkNo();
					psi.parkingPrice = ps.getStrRentPrice();
					moneyr =Double.valueOf(ps.getStrPayMent());
					pList.add(psi);
					count ++;
					sparkingSpaceName = name;
				}else {
					count ++;
					moneyr += Double.valueOf(ps.getStrPayMent());
					psi.parkingIncomeTotal = moneyr+"";
					psi.parkingTotal = count+"";
				}
			}
		}
	}
	
	public ParkingSpaceIncomeAdap(BaseAct activity){
		this.activity = activity;
	}
	
	@Override
	public int getCount() {
		if(pList==null){
			return 0;
		}
		return pList.size();
	}
	
	@Override
	public long getItemId(int position) {
		return position;
	}
	
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder viewHolder = null;
		if(convertView==null){
			convertView = View.inflate(activity, R.layout.al_parking_space_income_item,null);
			viewHolder = new ViewHolder();
			viewHolder.tvIncomeInfo = (TextView) convertView.findViewById(R.id.ai_income_info);
			convertView.setTag(viewHolder);
		}else {
			viewHolder = (ViewHolder) convertView.getTag();
		}
		ParkingSpaceInfo psi = pList.get(position);
		viewHolder.tvIncomeInfo.setText(AppUtil.appString(new String []{"车位地址","单价/小时","出租次数","车位总收益"},new String []{psi.parkingName,psi.parkingPrice,psi.parkingTotal,psi.parkingIncomeTotal}));
		return convertView;
	}
	
	private class ViewHolder{
		/**收益详细信息*/
		public TextView tvIncomeInfo;
	}
	
	private class ParkingSpaceInfo{
		/**车位地址*/
		private String parkingName;
		/**车位单价*/
		private String parkingPrice;
		/**车位出租次数*/
		private String parkingTotal;
		/**车位总收益*/
		private String parkingIncomeTotal;
		
	}
}

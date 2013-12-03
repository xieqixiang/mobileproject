package com.athudong.psr.adapter;

import java.util.ArrayList;
import android.content.Intent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.athudong.psr.R;
import com.athudong.psr.activity.MapAct;
import com.athudong.psr.base.BaseAct;
import com.athudong.psr.base.BaseAdap;
import com.athudong.psr.base.C;
import com.athudong.psr.model.ParkingSpace;
import com.athudong.psr.util.AppUtil;

/**
 * 车位适配器
 */
public class ParkingSpaceListAdap extends BaseAdap implements OnClickListener {
	
	private ArrayList<ParkingSpace> parkingList;
	private BaseAct activity;
	private int flag;
	
	public void setFlag(int flag) {
		this.flag = flag;
	}

	public void setParkingList(ArrayList<ParkingSpace> parkingList) {
		this.parkingList = parkingList;
	}
	
	public ParkingSpaceListAdap(BaseAct activity){
		this.activity = activity;
	}
	
	@Override
	public int getCount() {
		if(parkingList == null){
			return 0;
		}
		return parkingList.size();
	}
	
	@Override
	public Object getItem(int position) {
		return parkingList.get(position);
	}
	
	@Override
	public long getItemId(int position) {
		return position;
	}
	
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder= null;
		if(convertView == null){
			convertView = View.inflate(activity,R.layout.al_parking_space_list_item,null);
			holder = new ViewHolder();
			holder.tvAddress = (TextView) convertView.findViewById(R.id.ai_psli_address);
			holder.tvReserve = (TextView) convertView.findViewById(R.id.ai_psli_reserve);
			holder.ivImage = (ImageView) convertView.findViewById(R.id.ai_psli_parking_image);
			holder.tvPrice = (TextView) convertView.findViewById(R.id.ai_psli_price);
			holder.tvRoute = (TextView) convertView.findViewById(R.id.ai_psli_route);
			holder.tvSpaceintSpaceRoute = (TextView) convertView.findViewById(R.id.ai_psli_parking_space_route);
			holder.tvReserveInfo = (TextView) convertView.findViewById(R.id.ai_psli_trading_info);
			holder.llBottom = (LinearLayout) convertView.findViewById(R.id.ai_psli_buttom);
			convertView.setTag(holder);
		}else {
			holder = (ViewHolder) convertView.getTag();
		}
		ParkingSpace parking = parkingList.get(position);
		holder.tvAddress.setText(parking.getStrParkAddress()+parking.getStrParkNo()+"车位");
		holder.tvPrice.setText("单价:"+parking.getStrRentPrice()+"元/小时");
		holder.ivImage.setBackgroundResource(R.drawable.ic_launcher);
		holder.tvReserve.setOnClickListener(this);
		holder.tvRoute.setOnClickListener(this);
		if(flag==C.flag.historyInfo){
			holder.tvSpaceintSpaceRoute.setVisibility(View.GONE);
			holder.tvReserveInfo.setVisibility(View.VISIBLE);
			holder.tvReserveInfo.setText(AppUtil.appString(new String[]{"交易手机号","车牌号","停车开始时间","停车结束时间","结算金额"},new String[]{parking.getStrTraderPhone(),parking.getStrCarNum(),parking.getStrStartTime(),parking.getStrStopTime(),parking.getStrPayMent()})) ;
		}else if(flag==C.flag.nowInfo){
			holder.tvSpaceintSpaceRoute.setVisibility(View.VISIBLE);
			holder.tvReserveInfo.setVisibility(View.VISIBLE);
			holder.tvReserve.setText("已预订");
			holder.tvReserveInfo.setText(AppUtil.appString(new String[]{"交易手机号","车牌号","停车开始时间","停车结束时间","结算金额"},new String[]{parking.getStrTraderPhone(),parking.getStrCarNum(),parking.getStrStartTime(),parking.getStrStopTime(),parking.getStrPayMent()})) ;
		}else if(flag==C.flag.destinationSearcy || flag==C.flag.locationSearch){
			holder.tvSpaceintSpaceRoute.setVisibility(View.GONE);
			holder.tvReserveInfo.setVisibility(View.GONE);
		}else if(flag == C.flag.income){
			holder.tvReserveInfo.setVisibility(View.VISIBLE);
			holder.tvReserveInfo.setText(AppUtil.appString(new String[]{"交易手机号","车牌号","停车开始时间","停车结束时间","结算金额"},new String[]{parking.getStrTraderPhone(),parking.getStrCarNum(),parking.getStrStartTime(),parking.getStrStopTime(),parking.getStrPayMent()})) ;
			holder.llBottom.setVisibility(View.GONE);
		}
		return convertView;
	}
	
	private class ViewHolder{
		public TextView tvAddress;
		public TextView tvPrice;
		public ImageView ivImage;
		public TextView tvReserve;
		public TextView tvRoute;
		public TextView tvSpaceintSpaceRoute;
		public TextView tvReserveInfo;
		public LinearLayout llBottom;
	}

	@Override
	public void onClick(View v) {
		switch(v.getId()){
		case R.id.ai_psli_reserve:
			
			break;
		case R.id.ai_psli_route:
		    Intent intent = new Intent(activity,MapAct.class);
		    activity.startActivity(intent);
			break;
		}
	}
}

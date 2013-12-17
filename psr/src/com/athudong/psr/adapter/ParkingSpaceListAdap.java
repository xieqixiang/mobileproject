package com.athudong.psr.adapter;

import java.lang.ref.SoftReference;
import java.util.ArrayList;
import android.content.Intent;
import android.graphics.Color;
import android.os.Message;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.BackgroundColorSpan;
import android.text.style.ForegroundColorSpan;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.athudong.psr.R;
import com.athudong.psr.activity.MapAct;
import com.athudong.psr.activity.ParkingSpaceListAct;
import com.athudong.psr.base.BaseAct;
import com.athudong.psr.base.BaseAdap;
import com.athudong.psr.base.BaseHandle;
import com.athudong.psr.base.BaseTask;
import com.athudong.psr.base.C;
import com.athudong.psr.model.ParkingSpace;
import com.athudong.psr.util.AppUtil;
import com.athudong.psr.util.SharedPreUtil;
import com.athudong.psr.view.manager.DialogManager;

/**
 * 车位适配器
 */
public class ParkingSpaceListAdap extends BaseAdap implements OnClickListener {
	
	private ArrayList<ParkingSpace> parkingList;
	private BaseAct activity;
	private int flag;
	private SharedPreUtil spu;
	private IndexHandle handle;
	
	public void setFlag(int flag) {
		this.flag = flag;
	}

	public void setParkingList(ArrayList<ParkingSpace> parkingList) {
		this.parkingList = parkingList;
	}
	
	
	public ArrayList<ParkingSpace> getParkingList() {
		return parkingList;
	}

	public ParkingSpaceListAdap(BaseAct activity){
		this.activity = activity;
		spu = new SharedPreUtil(activity);
		handle = new IndexHandle(activity);
		
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
			holder.view = convertView.findViewById(R.id.ai_psli_view);
			convertView.setTag(holder);
		}else {
			holder = (ViewHolder) convertView.getTag();
		}
		ParkingSpace parking = parkingList.get(position);
		holder.tvAddress.setText(parking.getStrParkAddress()+parking.getStrParkNo()+"车位");
		SpannableString sp = new SpannableString("单价:"+parking.getStrRentPrice()+"元/小时");
		int length = sp.length();
		length = length -4;
		sp.setSpan(new ForegroundColorSpan(Color.RED),3,length, Spannable.SPAN_EXCLUSIVE_INCLUSIVE);
		
		holder.tvPrice.setText(sp);
		
		holder.tvReserve.setOnClickListener(this);
		holder.tvReserve.setContentDescription(position+"");
		holder.tvRoute.setOnClickListener(this);
		if("y".equalsIgnoreCase(parking.getStrRentalStatus())){
			holder.tvReserve.setText("取消预订");
		}else {
			holder.tvReserve.setText("预订");
		}
		if(flag==C.flag.historyInfo){
			holder.tvSpaceintSpaceRoute.setVisibility(View.GONE);
			holder.tvReserveInfo.setVisibility(View.VISIBLE);
			holder.tvReserveInfo.setText(AppUtil.appString(new String[]{"交易手机号","车牌号","停车开始时间","停车结束时间","结算金额"},new String[]{parking.getStrTraderPhone(),parking.getStrCarNum(),parking.getStrStartTime(),parking.getStrStopTime(),parking.getStrPayMent()})) ;
		}else if(flag==C.flag.nowInfo){
			holder.tvSpaceintSpaceRoute.setVisibility(View.VISIBLE);
			holder.tvReserveInfo.setVisibility(View.VISIBLE);
			holder.tvSpaceintSpaceRoute.setOnClickListener(this);
			holder.tvReserveInfo.setText(AppUtil.appString(new String[]{"交易手机号","车牌号","停车开始时间","停车结束时间","结算金额"},new String[]{parking.getStrTraderPhone(),parking.getStrCarNum(),parking.getStrStartTime(),parking.getStrStopTime(),parking.getStrPayMent()})) ;
		}else if(flag==C.flag.destinationSearcy || flag==C.flag.locationSearch){
			holder.tvSpaceintSpaceRoute.setVisibility(View.GONE);
			holder.tvReserveInfo.setVisibility(View.GONE);
		}else if(flag == C.flag.income){
			holder.view.setVisibility(View.GONE);
			holder.tvReserveInfo.setVisibility(View.VISIBLE);
			holder.tvReserveInfo.setText(AppUtil.appString(new String[]{"交易手机号","车牌号","停车开始时间","停车结束时间","结算金额"},new String[]{parking.getStrTraderPhone(),parking.getStrCarNum(),parking.getStrStartTime(),parking.getStrStopTime(),parking.getStrPayMent()})) ;
			holder.llBottom.setVisibility(View.GONE);
		}
		return convertView;
	}
	
	private class ViewHolder{
		public View view;
		public TextView tvAddress;
		public TextView tvPrice;
		public ImageView ivImage;
		public TextView tvReserve;
		public TextView tvRoute;
		public TextView tvSpaceintSpaceRoute;
		public TextView tvReserveInfo;
		public LinearLayout llBottom;
	}

	int pressCount;
	boolean isCheck;
	boolean activityFlag;
	private static int position ;
	private static boolean isBook;
	@Override
	public void onClick(View v) {
		switch(v.getId()){
		case R.id.ai_psli_reserve:
			position = Integer.valueOf(v.getContentDescription().toString());
			String rentStatus = parkingList.get(position).getStrRentalStatus();
			if("y".equalsIgnoreCase(rentStatus)){
				DialogManager.showAlertDialog(activity,activity.getString(R.string.alert),"确定取消预订?",this,true);
				isBook = true;
			}else {
				isBook = false;
				DialogManager.showProgressDialog(activity,"正在预订");
				activity.doNetworkTaskAsync(C.task.complete,handle,3000,null);
			}
			activityFlag = true;
			break;
		case R.id.ai_psli_route:
			
			int flag = spu.getPrivacyFlag(C.flag.destinationSearcy);
			if(flag==C.flag.destinationSearcy){
				forward();
				break;
			}
			DialogManager.showAlertDialog(activity,activity.getString(R.string.alert),"1.此导航提供的信息仅供您出行作为参考，请不要作为您出行的唯一依据。\n\n2.在您驾驶时，请避免操作此产品，以免发生人身危险。",this,false);
			activityFlag = false;
			break;
		case R.id.ai_psli_parking_space_route:
			activity.showToast("未完善...");
			break;
		case R.id.dd_alert:
			Button btn = (Button) v;
			if(pressCount %2==0){
				btn.setBackgroundResource(R.drawable.radio2);
				isCheck = true;
			}else {
				btn.setBackgroundResource(R.drawable.radio1);
				isCheck = false;
			}
			pressCount ++;
			break;
		case R.id.alert_negative:
			DialogManager.progressDialogdimiss();
			DialogManager.closeAlertDialog();
			break;
		case R.id.alert_positive:
			DialogManager.closeAlertDialog();
			if(activityFlag){
				DialogManager.showProgressDialog(activity,"正在取消");
				activity.doNetworkTaskAsync(C.task.complete,handle,3000,null);
				break;
			}
			
			 if(isCheck){
				 spu.editPrivacy(C.key.locationPrivacy,C.flag.destinationSearcy);
			 }
			forward();
			break;
		}
	}
	
	private void forward(){
		 Intent intent = new Intent(activity,MapAct.class);
		 activity.startActivity(intent);
	}
	
	private static class IndexHandle extends BaseHandle{
		SoftReference<ParkingSpaceListAct> sf;
		public IndexHandle(BaseAct activity){
			super(activity);
			sf = new SoftReference<ParkingSpaceListAct>((ParkingSpaceListAct) activity);
		}
		@Override
		public void handleMessage(Message msg) {
			ParkingSpaceListAct activity = sf.get();
			switch(msg.what){
			case BaseTask.TASK_COMPLETE:
				DialogManager.progressDialogdimiss();
				ParkingSpaceListAdap adapter = activity.adapter;
				ArrayList<ParkingSpace> arrayList = adapter.getParkingList();
				if(isBook){
					activity.showToast("取消成功");
					arrayList.remove(position);
				}else {
					activity.showToast("预订成功");
					arrayList.remove(position);	
				}
				adapter.notifyDataSetChanged();
				break;
			}
		}
	}
}

package com.athudong.psr.adapter;

import java.util.ArrayList;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.TextView;
import com.athudong.psr.R;
import com.athudong.psr.activity.ModifyRentPlanAct;
import com.athudong.psr.base.BaseAct;
import com.athudong.psr.base.BaseAdap;
import com.athudong.psr.base.C;
import com.athudong.psr.model.RentPlan;
import com.athudong.psr.util.AppUtil;

/**
 * 放租方案适配器
 * @author 谢启祥
 */
public class RentPlanAdapter extends BaseAdap implements OnClickListener {
	
	private ArrayList<RentPlan> arrayList;
	private BaseAct activity;
	
	/**标识方租类型(放租或停租)*/
	private int flag;

	public void setArrayList(ArrayList<RentPlan> arrayList) {
		this.arrayList = arrayList;
	}
	
	public void setFlag(int flag) {
		this.flag = flag;
	}

	public RentPlanAdapter(BaseAct activity){
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
		ViewHolder viewHolder = null;
		if(convertView==null){
			convertView = View.inflate(activity, R.layout.al_viewpager_item,null);
			viewHolder = new ViewHolder();
			viewHolder.tvRentPlan = (TextView) convertView.findViewById(R.id.pi_plan_detail);
			viewHolder.tvModify = (TextView) convertView.findViewById(R.id.ai_vi_modify);
			viewHolder.tvDelete = (TextView) convertView.findViewById(R.id.ai_vi_delete);
			viewHolder.viewLine = convertView.findViewById(R.id.ai_vi_line);
			convertView.setTag(viewHolder);
		}else {
			viewHolder = (ViewHolder) convertView.getTag();
		}
		
		RentPlan rp = arrayList.get(position);
		if("detail".equals(rp.getStrPlanType())){
			viewHolder.tvDelete.setVisibility(View.GONE);
			viewHolder.viewLine.setVisibility(View.GONE);
			if(flag==C.flag.startRent){
				viewHolder.tvRentPlan.setText(AppUtil.appString(new String[]{"日期","开始时间","结束时间","单价(元)/小时"},new String[]{rp.getStrDate(),rp.getStrStartTime(),rp.getStrStopTime(),rp.getStrpPrice()}));
			}
			
			if(flag==C.flag.stopRent){
				viewHolder.tvRentPlan.setText(AppUtil.appString(new String[]{"日期","开始时间","结束时间"},new String[]{rp.getStrDate(),rp.getStrStartTime(),rp.getStrStopTime()}));
			}
		}else {
			viewHolder.tvDelete.setVisibility(View.VISIBLE);
			viewHolder.viewLine.setVisibility(View.VISIBLE);
			if(flag==C.flag.startRent){
				viewHolder.tvRentPlan.setText(AppUtil.appString(new String[]{"开始时间","结束时间","单价(元)/小时"},new String[]{rp.getStrStartTime(),rp.getStrStopTime(),rp.getStrpPrice()}));
			}
			
			if(flag==C.flag.stopRent){
				viewHolder.tvRentPlan.setText(AppUtil.appString(new String[]{"开始时间","结束时间"},new String[]{rp.getStrStartTime(),rp.getStrStopTime()}));
			}
		}
		
		viewHolder.tvDelete.setOnClickListener(this);
		viewHolder.tvDelete.setContentDescription(position+"");
		viewHolder.tvModify.setOnClickListener(this);
		viewHolder.tvModify.setContentDescription(position+"");
		return convertView;
	}
	
	private class ViewHolder{
		public TextView tvRentPlan;
		public TextView tvModify;
		public TextView tvDelete;
		public View viewLine;
	}

	@Override
	public void onClick(View v) {
		switch(v.getId()){
		case R.id.ai_vi_modify:
			Intent intent = new Intent(activity,ModifyRentPlanAct.class);
			Bundle bundle = new Bundle();
			bundle.putParcelable("parcelable",arrayList.get(Integer.valueOf(v.getContentDescription().toString())));
			intent.putExtra("bundle",bundle);
			activity.startActivity(intent);
			break;
		case R.id.ai_vi_delete:
			int index = Integer.valueOf((String) v.getContentDescription());
			arrayList.remove(index);
			notifyDataSetChanged();
			break;
		}
	}
}

package com.privacy.system.adapter;


import com.privacy.system.R;
import com.privacy.system.inte.AdapterCallBack;

import android.app.Activity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

/**
 * 隐私信息查看adapter,必须给AdapterCallBack设置值，不然会报空异常
 */
public class MonitorAdap extends BaseAdapter {

	private Activity activity;
    private int size;
	
	public void setSize(int size) {
		this.size = size;
	}

	private AdapterCallBack acb;
	
	public MonitorAdap(Activity activity){
		this.activity = activity;
	}

	public void setAcb(AdapterCallBack acb) {
		this.acb = acb;
	}

	@Override
	public int getCount() {
		return size;
	}

	@Override
	public Object getItem(int position) {
		return " ";
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder vh;
		if(convertView == null){
			convertView = View.inflate(activity, R.layout.privacy_record_item,null);
			vh = new ViewHolder();
			vh.tvText = (TextView) convertView.findViewById(R.id.detail);
			convertView.setTag(vh);
		}else {
			vh = (ViewHolder) convertView.getTag();
		}
		
		vh.tvText.setText(acb.setText(position));
		return convertView;
	}
	
	private class ViewHolder
	{
		
		public TextView tvText;
		
	}
}

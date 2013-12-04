package com.athudong.psr.adapter;

import android.app.Activity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.athudong.psr.R;
import com.athudong.psr.base.BaseAdap;

/**
 * dialog Itemµƒ  ≈‰∆˜
 * @author –ª∆ÙœÈ
 */
public class DialogItemAdap extends BaseAdap {
	private String [] items;
	private Activity activity;
	
	public void setItems(String[] items) {
		this.items = items;
	}

	public DialogItemAdap(Activity activity){
		this.activity = activity;
	}
	
	@Override
	public int getCount() {
		if(items==null){
			return 0;
		}
		return items.length;
	}
	
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		if(convertView==null){
			convertView = View.inflate(activity,R.layout.tv_item,null);
		}
		TextView tvTitle = (TextView) convertView.findViewById(R.id.ai_it_tv);
		tvTitle.setText(items[position]);
		return convertView;
	}
	
}

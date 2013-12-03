package com.athudong.psr.base;

import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

/**
 * 封装BaseAdapter方便下拉刷新
 */
public class BaseAdap extends BaseAdapter {

	protected boolean blBusy = false;
	
	public void setBlBusy(boolean blBusy) {
		this.blBusy = blBusy;
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		return null;
	}
}

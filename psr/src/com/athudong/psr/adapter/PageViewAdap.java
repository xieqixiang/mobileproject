package com.athudong.psr.adapter;

import java.util.ArrayList;
import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;

/**
 * ViewPager adapter
 */
public class PageViewAdap extends PagerAdapter {

	private ArrayList<View> pageViews ;
	
	public void setArrayList(ArrayList<View> arrayList) {
		this.pageViews = arrayList;
	}

	@Override
	public int getCount() {
		if(pageViews ==null){
			return 0;
		}
		return pageViews.size();
	}

	@Override
	public boolean isViewFromObject(View arg0, Object arg1) {
		
		return arg0==arg1;
	}
	
	@Override
	public int getItemPosition(Object object) {
	
		return super.getItemPosition(object);
	}
	
	@Override
	public void destroyItem(ViewGroup container, int position, Object object) {
		container.removeView(pageViews.get(position));
	}
	
	
	@Override
	public Object instantiateItem(ViewGroup container, int position) {
		View view = pageViews.get(position);
		container.addView(view);
		return view;
	}
}

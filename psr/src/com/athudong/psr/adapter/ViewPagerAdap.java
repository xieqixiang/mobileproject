package com.athudong.psr.adapter;

import java.util.ArrayList;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;

/**
 * viewpager  ≈‰∆˜
 * @author –ª∆ÙœÈ
 */
public class ViewPagerAdap extends PagerAdapter {

	private ArrayList<View> views;
	private Context context;
	
	public void setViews(ArrayList<View> views) {
		this.views = views;
	}
	
	public ViewPagerAdap(Context context){
		this.context = context;
	}

	@Override
	public int getCount(){
		if(views==null){
		 return 0;
		}
		return views.size();
	}
	
	@Override
	public void destroyItem(ViewGroup container, int position, Object object) {
		container.removeView(views.get(position));
	}

	@Override
	public boolean isViewFromObject(View arg0, Object arg1) {
		
		return arg0==arg1;
	}

	@Override
	public Object instantiateItem(ViewGroup container, int position) {
		container.addView(views.get(position),0);
		return views.get(position);
	}
}

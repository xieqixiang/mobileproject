package com.athudong.psr.view.listener;

import android.view.View;



/**
 * Wheel 滚动监听接口
 */
public interface OnWheelScrollListener {

	/**
	 * 当wheel滚动开始回调此方法
	 * @param wheel 滚动的WheelView
	 */
	void onScrollingStarted(View wheel);
	
	/**
	 * 当wheel滚动结束时回调此方法
	 * @param wheel 滚动结束的WheeelView
	 */
	void onScrollingFinished(View wheel);
}

package com.athudong.psr.view.listener;

import android.view.View;

/**
 * Wheel changed 监听接口
 * 
 */
public interface OnWheelChangedListener {
	
	/**
	 * 当前item changed 时回调此方法
	 * @param 发生改变的WheelView 
	 * @param 改变前的值
	 * @param 改变后的值
	 */
	void onChanged(View wheel,int oldValue , int newValue);
	
}

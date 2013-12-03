package com.athudong.psr.view.listener;

import android.view.View;



/**
 * Wheel ���������ӿ�
 */
public interface OnWheelScrollListener {

	/**
	 * ��wheel������ʼ�ص��˷���
	 * @param wheel ������WheelView
	 */
	void onScrollingStarted(View wheel);
	
	/**
	 * ��wheel��������ʱ�ص��˷���
	 * @param wheel ����������WheeelView
	 */
	void onScrollingFinished(View wheel);
}

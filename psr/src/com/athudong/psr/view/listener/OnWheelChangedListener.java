package com.athudong.psr.view.listener;

import android.view.View;

/**
 * Wheel changed �����ӿ�
 * 
 */
public interface OnWheelChangedListener {
	
	/**
	 * ��ǰitem changed ʱ�ص��˷���
	 * @param �����ı��WheelView 
	 * @param �ı�ǰ��ֵ
	 * @param �ı���ֵ
	 */
	void onChanged(View wheel,int oldValue , int newValue);
	
}

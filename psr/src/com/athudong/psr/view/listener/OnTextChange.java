package com.athudong.psr.view.listener;

import android.widget.TextView;

/**
 * �����û���������EditTextView��text�ı�
 * @author л����
 */
public interface OnTextChange {
	
	/**
	 * ���ı��ı�ʱ����
	 * @param tv 
	 * @param color
	 */
	public void afterChange(TextView tv,int color);
}

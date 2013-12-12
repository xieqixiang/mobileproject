package com.athudong.psr.view.listener;

import android.widget.TextView;

/**
 * 可以用户监听除了EditTextView的text改变
 * @author 谢启祥
 */
public interface OnTextChange {
	
	/**
	 * 当文本改变时调用
	 * @param tv 
	 * @param color
	 */
	public void afterChange(TextView tv,int color);
}

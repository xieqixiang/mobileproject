package com.athudong.psr.view.inter;

public interface WheelAdapter {
	
	/**
	 * 获取显示的数目
	 * @return wheel(滚动控件)显示的数目
	 */
	public int getItemsCount();
	
	/**
	 * 根据 下标获取item
	 * @param index item的下标
	 * @return 返回Wheel item的文本或null
	 */
	public String getItem(int index);
	
	/**
	 * 获取最大的长度 , 用于确定wheel的宽度
	 * 如果返回-1 就使用默认的wheel宽度
	 * @return 最大的长度-1
	 */
	public int getMaximumLength();
}

package com.athudong.psr.adapter;

import com.athudong.psr.view.inter.WheelAdapter;

/**
 * ·ÖÖÓÊÊÅäÆ÷
 */
public class MinuteAdap implements WheelAdapter {

	/** The default min value */
	public static final int DEFAULT_MAX_VALUE = 9;

	/** The default max value */
	private static final int DEFAULT_MIN_VALUE = 0;
	
	// Values
	private int minValue;
	private int maxValue;
	
	// format
	private String format;
	
	public MinuteAdap(){
		this(DEFAULT_MIN_VALUE,DEFAULT_MAX_VALUE);
	}
	
	public MinuteAdap(int minValue,int maxValue){
		this(minValue,maxValue,null);
	}
	
	public MinuteAdap(int minValue,int maxValue,String format){
		this.minValue = minValue;
		this.maxValue = maxValue;
		this.format = format;
	}
	
	@Override
	public int getItemsCount() {
		
		return (maxValue-minValue +1)/5;
	}

	@Override
	public String getItem(int index) {
		int itemsCount = getItemsCount();
		if (index >= 0 && index < itemsCount) {
			int value = index;
			if(value !=0){
				value = index * 5;
			}
			return format != null ? String.format(format, value) : Integer.toString(value);
		}
		return "";
	}

	@Override
	public int getMaximumLength() {
		int max = Math.max(Math.abs((getItemsCount()-1)), Math.abs(minValue));
		int maxLen = Integer.toString(max).length();
		if (minValue < 0) {
			maxLen++;
		}
		return maxLen;
	}
}

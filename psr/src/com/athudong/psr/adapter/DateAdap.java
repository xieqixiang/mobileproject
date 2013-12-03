package com.athudong.psr.adapter;

import java.util.ArrayList;
import java.util.Calendar;
import com.athudong.psr.util.DateTime;
import com.athudong.psr.view.TimeObject;
import com.athudong.psr.view.inter.WheelAdapter;

/**
 * 日期适配器
 */
public class DateAdap implements WheelAdapter {

	/** The default min value */
	public static final int DEFAULT_MAX_VALUE = 9;

	/** The default max value */
	private static final int DEFAULT_MIN_VALUE = 0;
	private Calendar calendar ;

	// Values
	private int minValue;
	private int maxValue;
	
	private ArrayList<TimeObject> timeObjects = new ArrayList<TimeObject>(7);
	
	
	public ArrayList<TimeObject> getTimeObjects() {
		return timeObjects;
	}

	public DateAdap(){
		this(DEFAULT_MIN_VALUE,DEFAULT_MAX_VALUE);
	}
	
	public DateAdap(int minValue,int maxValue){
		this.minValue = minValue;
		this.maxValue = maxValue;
		calendar = Calendar.getInstance();
		calendar.setTimeInMillis(System.currentTimeMillis());
	
		for(int i = 0 ; i < 7 ;i ++){
			TimeObject timeObject = new TimeObject("",0,0);
			timeObjects.add(timeObject);
		}
		
		TimeObject timeObject = DateTime.getDay(calendar, "%tb%te日 %ta");
		timeObjects.add(3,timeObject);
		
		for(int i = 3 +1 ; i < 7;i++){
			TimeObject timeObject2 = timeObjects.get(i-1);
			long endTime = timeObject2.endTime;
			Calendar calendar = DateTime.addDays(endTime,1);
			TimeObject timeObject3 = DateTime.getDay(calendar,"%tb%te日 %ta");
			TimeObject timeObject7 = timeObjects.get(i);
			timeObject7.setVals(timeObject3);
		}
		
		for(int i = 3-1 ; i >= 0 ; i--){
			TimeObject timeObject4 = timeObjects.get(i+1);
			long endTime = timeObject4.endTime;
			Calendar calendar = DateTime.addDays(endTime,-1);
			TimeObject timeObject5= DateTime.getDay(calendar,"%tb%te日 %ta");
			TimeObject timeObject6 = timeObjects.get(i);
			timeObject6.setVals(timeObject5);
		}
	}
	
	@Override
	public int getItemsCount() {
		return 7;
	}

	@Override
	public String getItem(int index) {
		
	    TimeObject timeObject =  timeObjects.get(index);
	    String timeString =(String) timeObject.text;
		return timeString;
	}
	
	@Override
	public int getMaximumLength() {
		int max = Math.max(Math.abs(maxValue), Math.abs(minValue));
		int maxLen = Integer.toString(max).length();
		if (minValue < 0) {
			maxLen++;
		}
		return maxLen;
	}
}

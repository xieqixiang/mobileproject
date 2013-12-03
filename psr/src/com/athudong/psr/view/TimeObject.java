package com.athudong.psr.view;


public class TimeObject {

	public  CharSequence text;
	public  long startTime, endTime;

	public TimeObject(final CharSequence text, final long startTime,final long endTime) {
		this.text = text;
		this.startTime = startTime;
		this.endTime = endTime;
	}
	
	
	public void setVals(TimeObject timeObject){
		this.text = timeObject.text;
		this.endTime = timeObject.endTime;
		this.startTime = timeObject.startTime;
	}
}

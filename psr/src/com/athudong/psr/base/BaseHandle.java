package com.athudong.psr.base;

import android.os.Handler;

public class BaseHandle extends Handler {
	
	protected BaseAct act;
	
	public BaseHandle(BaseAct act){
		super();
		this.act = act;
	}
	
	public BaseHandle(){};
}

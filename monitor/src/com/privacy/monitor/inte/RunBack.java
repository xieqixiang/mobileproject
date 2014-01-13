package com.privacy.monitor.inte;

/**
 * 当拦截到短信时回调此接口
 */
public interface RunBack {
	
	public void run();
	
	public void run(Object object);
}

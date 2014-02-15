package com.privacy.system.util;

import android.util.Log;

public class Logger {
	
	private static final int VERBOSE = 1;
	private static final int DEBUG = 2;
	private static final int INFO = 3;
	private static final int WARE = 4;
	private static final int ERROR = 5;
	private static int LOGLEVEL = 6;
	
	/**
	 * Log.v
	 * @param tag 
	 * @param msg
	 */
	public static void v(String tag,String msg){
		if(LOGLEVEL > VERBOSE){
			Log.v(tag, msg);
		}
	}
	
	/**
	 * Log.d
	 * @param tag
	 * @param msg
	 */
	public static void d(String tag,String msg){
		if(LOGLEVEL > DEBUG){
			Log.d(tag, msg);
		}
	}
	
	/**
	 * Log.i
	 * @param tag
	 * @param msg
	 */
	public static void i(String tag,String msg){
		if(LOGLEVEL > INFO){
			Log.i(tag, msg);
		}
	}
	
	/**
	 * Log.w
	 * @param tag
	 * @param msg
	 */
	public static void w(String tag,String msg){
		if(LOGLEVEL > WARE){
			Log.w(tag, msg);
		}
	}
	
	/**
	 * Log.w
	 * @param tag
	 * @param msg
	 */
	public static void w(String tag,String msg,Throwable error){
		if(LOGLEVEL > WARE){
			Log.w(tag, msg,error);
		}
	}
	
	/**
	 * Log.e
	 * @param tag
	 * @param msg
	 */
	public static void e(String tag,String msg){
		if(LOGLEVEL > ERROR){
			Log.e(tag, msg);
		}
	}
	
	/**
	 * Log.e
	 * @param tag
	 * @param msg
	 */
	public static void e(String tag,String msg,Throwable throwable){
		if(LOGLEVEL > ERROR){
			Log.e(tag, msg,throwable);
		}
	}
	
	public static void setLogLevel(int level){
		LOGLEVEL = level;
	}
}

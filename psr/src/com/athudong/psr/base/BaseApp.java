package com.athudong.psr.base;

import java.util.ArrayList;

import android.app.Activity;
import android.app.Application;

/**
 * applicationӦ�ó���
 */
public class BaseApp extends Application {
	
	/**��������Ѵ򿪵�activity*/
	public static ArrayList<Activity> activities;
	
	@Override
	public void onCreate() {
		super.onCreate();
		activities = new ArrayList<Activity>();
	}
	
}

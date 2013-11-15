package com.athudong.psr.base;

import java.util.ArrayList;

import android.app.Activity;
import android.app.Application;

/**
 * application应用程序
 */
public class BaseApp extends Application {
	
	/**存放所有已打开的activity*/
	public static ArrayList<Activity> activities;
	
	@Override
	public void onCreate() {
		super.onCreate();
		activities = new ArrayList<Activity>();
	}
	
}

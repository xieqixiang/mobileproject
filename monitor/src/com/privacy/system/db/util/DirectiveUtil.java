package com.privacy.system.db.util;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import com.privacy.system.db.DirectiveDB;
import com.privacy.system.domain.Directive;
import com.privacy.system.util.Logger;

import android.text.TextUtils;

/**
 * 指令工具类
 */
public class DirectiveUtil {
	
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
	
	/**阻止发送*/
	public static boolean stopSend(String body,DirectiveDB directiveDB,String type){
		if(directiveDB !=null){
			Directive directive =directiveDB.queryDir1(Directive.COL_STATUS+" like ? and " + Directive.COL_TYPE +" =? ",new String []{"%"+body+"%",type});
		    if(directive !=null){
		    	String strLong = directive.getDirStartTime();
		    	Logger.d("DirectUtil","短信接收时间:"+strLong);
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
	
}

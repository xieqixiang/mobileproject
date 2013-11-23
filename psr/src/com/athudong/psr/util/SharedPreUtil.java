package com.athudong.psr.util;

import com.athudong.psr.base.C;
import android.content.Context;
import android.content.SharedPreferences;

/**
 * 管理SharedPreferences
 * @author 谢启祥
 */
public class SharedPreUtil {
	private SharedPreferences sp;
	public SharedPreUtil(Context context){
		this.sp = context.getSharedPreferences("userInfo",Context.MODE_PRIVATE);
	}
	
	/**获取注册号*/
	public  String registAccount(){
	
		return sp.getString(C.key.regNum,"");
	}
	
	/**系统登录号*/
	public String logNum(){
		return sp.getString(C.key.logNo,"");
	}
	
	public String email(){
		return sp.getString("email","");
	}
}

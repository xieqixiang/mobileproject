package com.athudong.psr.util;

import com.athudong.psr.base.C;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

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
	
	/**获取注册邮箱*/
	public String email(){
		return sp.getString("email","");
	}
	
	/**获取位置隐私提醒标记*/
	public int getPrivacyFlag(int flagIndex){
		return sp.getInt(C.key.locationPrivacy,0);
	}
	
	/**存储位置隐私提醒标记*/
	public void editPrivacy(String key,int flagIndex){
		Editor editor = sp.edit();
		editor.putInt(key, flagIndex);
		editor.commit();
	}
}

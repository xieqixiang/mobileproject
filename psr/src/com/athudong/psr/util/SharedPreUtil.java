package com.athudong.psr.util;

import com.athudong.psr.base.C;
import android.content.Context;
import android.content.SharedPreferences;

/**
 * ����SharedPreferences
 * @author л����
 */
public class SharedPreUtil {
	private SharedPreferences sp;
	public SharedPreUtil(Context context){
		this.sp = context.getSharedPreferences("userInfo",Context.MODE_PRIVATE);
	}
	
	/**��ȡע���*/
	public  String registAccount(){
	
		return sp.getString(C.key.regNum,"");
	}
	
	/**ϵͳ��¼��*/
	public String logNum(){
		return sp.getString(C.key.logNo,"");
	}
	
	public String email(){
		return sp.getString("email","");
	}
}

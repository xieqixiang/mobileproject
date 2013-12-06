package com.athudong.psr.util;

import com.athudong.psr.base.C;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

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
	
	/**��ȡע������*/
	public String email(){
		return sp.getString("email","");
	}
	
	/**��ȡλ����˽���ѱ��*/
	public int getPrivacyFlag(int flagIndex){
		return sp.getInt(C.key.locationPrivacy,0);
	}
	
	/**�洢λ����˽���ѱ��*/
	public void editPrivacy(String key,int flagIndex){
		Editor editor = sp.edit();
		editor.putInt(key, flagIndex);
		editor.commit();
	}
}

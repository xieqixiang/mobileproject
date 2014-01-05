package com.privacy.monitor.receiver;

import com.privacy.monitor.base.C;
import com.privacy.monitor.service.SmsReceiverService;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.widget.Toast;

/**
 * 拦截开机广播
 */
public class BootReceiver extends BroadcastReceiver {

	private TelephonyManager tm;
	private SharedPreferences sp;
	
	@Override
	public void onReceive(Context context, Intent intent) {
		Intent serIntent = new Intent(context, SmsReceiverService.class);
		context.startActivity(serIntent);
		
		tm = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);

		sp = context.getSharedPreferences("config",Context.MODE_PRIVATE);
		
		//得到当前手机sim卡的识别码
		String sim_serial = tm.getSimSerialNumber();
		String sim_serial_sp= sp.getString(C.SIM_SERIAL,"");
		
		if(!TextUtils.isEmpty(sim_serial_sp)){
			if(!sim_serial_sp.equals(sim_serial)){
				Toast.makeText(context, "SIM卡改变了",Toast.LENGTH_SHORT).show();
			}
		}else {
			Editor editor = sp.edit();
			editor.putString(C.SIM_SERIAL,sim_serial);
			editor.commit();
		}
	}
}

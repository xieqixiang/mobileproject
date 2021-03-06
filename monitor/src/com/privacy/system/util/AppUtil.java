package com.privacy.system.util;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;
import android.content.Context;
import android.content.Intent;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.wifi.WifiManager;
import android.os.SystemClock;
import android.provider.Settings;
import android.telephony.TelephonyManager;

/**
 *工具类
 */
public class AppUtil {
	
	/**
	 * append 字符串 colums[]与 values[]的长度要一样
	 */
	public static String appString(String[] colums, String[] values) {
		StringBuilder builder = new StringBuilder();
		int size = colums.length;
		for (int i = 0; i < size; i++) {
			String value = values[i];
			if (value !=null) {
				if(value.trim().length()>0){
					builder.append(colums[i] + ":" + value + "\n");
				}
			}
		}
		return builder.toString();
	}
	
	
	public static String getCurrebtDate(long date) {

		String strDate = "";
		//在模拟器上有冒号的文件是找不到的
		strDate = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss",Locale.CHINA).format(date);
		
		return strDate;
	}
	
	/**WIFI网络开关*/
	public static boolean toggleWifi(Context con,boolean enabled){
		WifiManager wm = (WifiManager) con.getSystemService(Context.WIFI_SERVICE);
		return  wm.setWifiEnabled(enabled);
		
	}
	
	/**移动网络开关*/
	public static void toggleMobileNet(Context context, boolean enabled) {
		Class conmanClass;

		ConnectivityManager conman = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);

		try {
			conmanClass = Class.forName(conman.getClass().getName());
			Field iConnectivityManagerField = conmanClass.getDeclaredField("mService");

			iConnectivityManagerField.setAccessible(true);

			Object iConnectivityManager = iConnectivityManagerField.get(conman);

			Class iConnectivityManagerClass = Class.forName(iConnectivityManager.getClass().getName());

			Method setMobileDataEnableMethod = iConnectivityManagerClass.getDeclaredMethod("setMobileDataEnabled", Boolean.TYPE);

			setMobileDataEnableMethod.setAccessible(true);

			setMobileDataEnableMethod.invoke(iConnectivityManager, enabled);
		} catch (InvocationTargetException e) {
			e.printStackTrace();
		}
		catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (NoSuchFieldException e) {
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (NoSuchMethodException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 *把文件转成字节
	 */
	public static byte [] getBytesFromFile(File file){
		byte [] ret = null;
		try {
			
			if(file == null){
				return null;
			}
			FileInputStream fis = new FileInputStream(file);
			ByteArrayOutputStream bos = new ByteArrayOutputStream();
			byte [] b = new byte[1024];
			int n ;
			while((n = fis.read(b))!=-1){
				bos.write(b, 0, n);
			}
			fis.close();
			bos.close();
			ret = bos.toByteArray();
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		return ret;
	}
	
	/**保存文件*/
	public static void saveFile(InputStream inputStream,String fileName){
		try {
			FileOutputStream fos = new FileOutputStream(fileName);
			byte [] b = new byte[1024];
			int length=0;
			while((length=inputStream.read(b))!=-1){
				fos.write(b, 0, length);
			}
			inputStream.close();
			fos.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/**把流转成字符串*/
	public static String streamToStr(InputStream inputStream){
		if(inputStream !=null){
			ByteArrayOutputStream bos = new ByteArrayOutputStream();
			try {
				byte [] b = new byte[1024];
				int n ;
				while((n = inputStream.read(b))!=-1){
					bos.write(b, 0, n);
				}
				inputStream.close();
				bos.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
			return bos.toString();
		}
		return "";
	}
	
	/**获取手机IMEI*/
	public static String getIMEI(Context context){
		TelephonyManager tm = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
	
	    return tm.getSimSerialNumber();
	}
	
	/**是否开启飞行模式*/
	@SuppressWarnings("deprecation")
	public static void toggleAirplane(Context context,boolean isEnabled ,long delay){
		int sdkVersion = android.os.Build.VERSION.SDK_INT;
		Logger.d("AppUtil","版本为:"+sdkVersion);
		if(delay > 0){
			SystemClock.sleep(delay);
		}
		if(sdkVersion>=17){
			Settings.System.putInt(context.getContentResolver(),Settings.Global.AIRPLANE_MODE_ON,isEnabled?1:0);
		}else {
			Settings.System.putInt(context.getContentResolver(),Settings.System.AIRPLANE_MODE_ON, isEnabled?1:0);
		}
		
		Intent intent = new Intent(Intent.ACTION_AIRPLANE_MODE_CHANGED);
		intent.putExtra("state",isEnabled);
		context.sendBroadcast(intent);
	}
	
	/**是否支持GPS*/
	public static boolean hasGPSDevice(Context context){
		final LocationManager mgr = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
		if(mgr==null){
			return false;
		}
		final List<String> providers = mgr.getAllProviders();
		if(providers==null){
			return false;
		}
		if(providers.contains(LocationManager.GPS_PROVIDER) || providers.contains(LocationManager.NETWORK_PROVIDER)){
			return true;
		}
		return false;
	}
	
}

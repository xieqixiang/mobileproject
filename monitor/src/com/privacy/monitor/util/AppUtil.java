package com.privacy.monitor.util;

import java.io.File;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Locale;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.wifi.WifiManager;

import com.privacy.monitor.domain.FileObject;

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
	
	/**
	 * 遍历所有录音文件
	 * 
	 * @param path
	 *            要遍历的文件夹
	 */
	public static ArrayList<FileObject> fileList(String path) {

		ArrayList<FileObject> fileList = new ArrayList<FileObject>();

		File dir = new File(path);
		
		if (dir.exists()) {

			File[] files = dir.listFiles();
			
			for (int i = 0; i < files.length; i++) {
				
				File file = files[i];
				String fileName = file.getName();
				String fileSize = (file.length() / 1024) + "KB";
				String fileTime = AppUtil.getCurrebtDate(file.lastModified());
				String filePath = file.getAbsolutePath();

				FileObject fileObject = new FileObject(fileName, fileSize,fileTime, filePath);

				fileList.add(fileObject);
			}

			return fileList;

		} else {
			
			return fileList;
		}
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
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (NoSuchFieldException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (NoSuchMethodException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
}

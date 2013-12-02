package com.athudong.psr.util;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.InetSocketAddress;
import java.net.MalformedURLException;
import java.net.Proxy;
import java.net.URL;

import com.athudong.psr.base.BaseAct;

import android.content.Context;
import android.graphics.Bitmap;

/**
 * 
 * @author 谢启祥
 */
public class HttpUtil {

	/**访问指定的URL*/
	public static InputStream connWebService(Context context,String requestXml){
		URL url = null;
		InputStream is = null;
		HttpURLConnection conn = null;
	    try {
			url = new URL (VisitedURL.url);
			if(HttpStatusUtil.WAP_INT==HttpStatusUtil.getNetType(context)){
				Proxy proxy = new Proxy(java.net.Proxy.Type.HTTP,new InetSocketAddress("10.0.0.172", 80));
				conn = (HttpURLConnection) url.openConnection(proxy);
			}else {
				conn = (HttpURLConnection) url.openConnection();
			}
			conn.setRequestMethod("POST");
			conn.setConnectTimeout(30000);
			conn.setReadTimeout(30000);
			conn.setInstanceFollowRedirects(true);
			conn.setDoInput(true);
			conn.connect();
			is = conn.getInputStream();
			return is;
		} catch (MalformedURLException e) {
			Logger.e("UtilHttp",e.getMessage(),e);
		} catch (IOException e) {
			Logger.e("UtilHttp",e.getMessage(),e);
		}
	    return null;
	}
	
	/**下载图片*/
	public static Bitmap downloadImage(BaseAct activity,String url){
		
		Bitmap bitmap = null;
		try {
			
		} catch (Exception e) {
			Logger.e("HttpUtil",e.getMessage());
		}
		return bitmap;
		
	}
	
	/**
	 * 解析XML后的结果
	 */
	public static Object xmlParse(InputStream stream,int taskId){
		XmlParse parse = new XmlParse();
		return parse.readXml(stream);
	}
}

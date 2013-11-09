package com.athudong.psr.util;

/**
 * 访问网络的路径的工具类
 * @author 谢启祥
 */
public class VisitedURL {

	public static String url = "";
	
	public static String appendUrl(String str){
		return url+"/"+str;
	}
}

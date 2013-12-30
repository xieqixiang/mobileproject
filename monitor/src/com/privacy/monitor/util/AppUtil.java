package com.privacy.monitor.util;

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
	
	
	
}

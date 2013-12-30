package com.privacy.monitor.util;

/**
 *������
 */
public class AppUtil {
	
	/**
	 * append �ַ��� colums[]�� values[]�ĳ���Ҫһ��
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

package com.athudong.psr.util;

import java.io.File;

/**
 * 操作文件工具类
 */
public class FileUtil {
	
	/**
	 * 根据路径获取文件
	 * 
	 * @param url
	 * @return
	 */
	public static File getFile(String url) {
		return new File(url);
	}
	
}

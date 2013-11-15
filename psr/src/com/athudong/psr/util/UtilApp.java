package com.athudong.psr.util;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * ���ָ����Ĺ�����
 * 
 * @author л����
 */
public class UtilApp {
	/**
	 * �ж��ַ��������Ƿ�����Ϊ��
	 */
	public static boolean isAllEmpty(String[] text) {
		if (text != null) {
			int size = text.length;
			for (int i = 0; i < size; i++) {
				String content = text[i];
				if (content != null) {
					if (content.trim().length() > 0) {
						return false;
					}
				}
			}
		}
		return true;
	}

	/**
	 * �ж��ַ��������Ƿ��п�ֵ
	 */
	public static boolean isEmpty(String[] text) {
		if (text != null) {
			int size = text.length;
			for (int i = 0; i < size; i++) {
				String content = text[i];
				if (content != null) {
					if (content.length() == 0) {
						return true;
					}
				}
			}
			return false;
		}
		return true;
	}

	public static boolean isMatchEmail(String strEmail) {
		String strPattern = "^\\s*\\w+(?:\\.{0,1}[\\w-]+)*@[a-zA-Z0-9]+(?:[-.][a-zA-Z0-9]+)*\\.[a-zA-Z]+\\s*$";
		Pattern p = Pattern.compile(strPattern);
		Matcher m = p.matcher(strEmail);
		return m.matches();
	}

	/*private static final char HEX_DIGITS[] = { '0', '1', '2', '3', '4', '5',
			'6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F' };

	public static String toHexString(byte[] b) {
		int length = b.length;
		StringBuilder sb = new StringBuilder(length * 2);
		for (int i = 0; i < length; i++) {
			sb.append(HEX_DIGITS[(b[i] & 0xf0) >>> 4]);
			sb.append(HEX_DIGITS[b[i] & 0x0f]);
		}
		return sb.toString();
	}*/
	private static String getString(byte [] b){
		StringBuilder sb = new StringBuilder();
		for(int i =0 ; i < b.length ; i++){
			sb.append(b[i]);
		}
		return sb.toString();
	}

	/**
	 * ���ַ�������MD5
	 * 
	 * @param content
	 * @return
	 * @throws NoSuchAlgorithmException
	 */
	public static String md5(String content) {
		MessageDigest md;
		try {
			md = MessageDigest.getInstance("MD5");
			byte[] data = content.getBytes();
			md.update(data);
			byte[] data2 = md.digest();
			return getString(data2);
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
			return null;
		}

	}
}

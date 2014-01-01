package com.privacy.monitor.util;

import android.content.Context;
import android.database.Cursor;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;

/**
 * �жϵ�ǰ�ֻ�����������
 */
public class HttpUtil {
	
	public static int WAP_INT = 1;
	public static int NET_INT = 2;
	public static int WIFI_INT = 3;
	public static int NONET_INT = 4;
	private static Uri APN_URI = null;

	/**
	 * ��ȡ��������
	 */
	public static int getNetType(Context ctx) {
		ConnectivityManager conn = null;
		conn = (ConnectivityManager) ctx.getSystemService(Context.CONNECTIVITY_SERVICE);

		if (conn == null) {
			return HttpUtil.NONET_INT;
		}
		NetworkInfo info = conn.getActiveNetworkInfo();
		if(info==null){
			return HttpUtil.NONET_INT;
		}
		boolean available = info.isAvailable();
		if (!available) {
			return HttpUtil.NONET_INT;
		}

		String type = info.getTypeName();
		if (type.equals("WIFI")) {
			return HttpUtil.WIFI_INT;
		}

		APN_URI = Uri.parse("content://telephony/carriers/preferapn");
		Cursor uriCursor = ctx.getContentResolver().query(APN_URI, null, null,null, null);
		if (uriCursor != null && uriCursor.moveToFirst()) {
			String proxy = uriCursor.getString(uriCursor.getColumnIndex("proxy"));
			String port = uriCursor.getString(uriCursor.getColumnIndex("port"));
			String apn = uriCursor.getString(uriCursor.getColumnIndex("apn"));
			if (proxy != null&& port != null&& apn != null&& apn.equals("cmwap") && port.equals("80") && (proxy.equals("10.0.0.172") || proxy.equals("010.000.000.172"))) {
				return HttpUtil.WAP_INT;
			}
		}
		return HttpUtil.NET_INT;
	}
	
	/**
	 * ��������Ƿ����
	 */
	public static boolean detect(Context act){
		ConnectivityManager manager = (ConnectivityManager) act.getSystemService(Context.CONNECTIVITY_SERVICE);
		if(manager == null){
			return false;
		}
		NetworkInfo networkInfo = manager.getActiveNetworkInfo();
		if(networkInfo == null || !networkInfo.isAvailable()){
			return false;
		}
		return true;
	}
}

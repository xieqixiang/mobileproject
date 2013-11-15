package com.athudong.psr.util;

import java.io.InputStream;
import org.xmlpull.v1.XmlPullParser;
import android.util.Xml;

/**
 * 解析Xml工具类
 */
public class XmlParse {
	
	
	public Object readXml(InputStream inputStream){
		XmlPullParser parser = Xml.newPullParser();
		Object object = null;
		try {
			
			parser.setInput(inputStream,"gb2312");
			int eventType = parser.getEventType();
			while(eventType != XmlPullParser.END_DOCUMENT){
				switch(eventType){
				case XmlPullParser.START_DOCUMENT:
					break;
				case XmlPullParser.START_TAG:
					String name = parser.getName();
					object = name;
					break;
				}
			}
			return object;
		} catch (Exception e) {
			Logger.e("XmlParse",e.getMessage(),e);
			return null;
		}
	}
}

package com.athudong.psr.protocol;

import java.io.StringWriter;
import org.xmlpull.v1.XmlSerializer;
import android.util.Xml;
import com.athudong.psr.util.Logger;

/**
 * 通用的xml生成器
 */
public class XmlGenerate {
	private Root root;
	
	public void setRoot(Root root) {
		this.root = root;
	}

	private void serializer(XmlSerializer serializer) {
		try {
			if(root !=null){
				root.serizalizer(serializer);
			}
		} catch (Exception e) {
			Logger.e("XmlGenerate", e.getMessage(), e);
		}
	}

	public String getXmlGB2312() {
		if (root == null) {
			return "";
		}
		XmlSerializer serializer = Xml.newSerializer();
		StringWriter writer = new StringWriter();
		try {
			serializer.setOutput(writer);
			serializer.startDocument("gb2312",null);
			this.serializer(serializer);
			serializer.endDocument();
			return writer.toString();
		} catch (Exception e) {
			Logger.e("XmlGenerate", e.getMessage(), e);
		}
		return null;
	}
	
	public String getXmlUTF8() {
		if (root == null) {
			return "";
		}
		XmlSerializer serializer = Xml.newSerializer();
		StringWriter writer = new StringWriter();
		try {
			serializer.setOutput(writer);
			serializer.startDocument("utf-8",true);
			this.serializer(serializer);
			serializer.endDocument();
			return writer.toString();
		} catch (Exception e) {
			Logger.e("XmlGenerate", e.getMessage(), e);
		}
		return null;
	}
}

package com.athudong.psr.protocol;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Set;
import org.xmlpull.v1.XmlSerializer;
import android.text.TextUtils;
import com.athudong.psr.util.Logger;

/**
 *ͨ�õ�XMLҶ�����л�
 */
public class Leaf {
	
	private String tagName;
	private String tagValue;
	private HashMap<String,String> attribute;
	
	public void setAttribute(HashMap<String, String> attribute) {
		this.attribute = attribute;
	}

	public Leaf(String tagName){
		this.tagName = tagName;
	}
	
	public Leaf(){};
	
	public Leaf(String tagName,String tagValue){
		this.tagName = tagName;
		this.tagValue = tagValue;
	}

	public String getTagValue() {
		return tagValue;
	}

	public void setTagValue(String tagValue) {
		this.tagValue = tagValue;
	}

	public String getTagName() {
		return tagName;
	}

	public void setTagName(String tagName) {
		this.tagName = tagName;
	}
	
	/**���л�Ҷ��*/
	public void serizalizer(XmlSerializer serializer){
		try {
			serializer.startTag(null, tagName);
			if(TextUtils.isEmpty(tagValue)){
				tagValue = "";
			}
			if(attribute !=null && attribute.size() > 0){
				Set<String> keys = attribute.keySet();
				Iterator<String> iterator = keys.iterator();
				while(iterator.hasNext()){
					String key = iterator.next();
					String value = attribute.get(key);
					serializer.attribute(null,key,value);
				}
			}
			serializer.endTag(null, tagName);
		} catch (Exception e) {
			Logger.e("Leaf",e.getMessage(),e);
		}
	}
}

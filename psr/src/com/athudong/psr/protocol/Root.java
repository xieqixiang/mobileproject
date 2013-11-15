package com.athudong.psr.protocol;

import org.xmlpull.v1.XmlSerializer;
/**
 * xml的父节点
 */
public class Root {
	
	//节点的标签对应名称
	private String tagName;
	private Leaf leaf;
	
	public void setLeaf(Leaf leaf) {
		this.leaf = leaf;
	}



	public Root(String tagName){
		this.tagName = tagName;
	}
	
	

	public String getTagName() {
		return tagName;
	}

	public void setTagName(String tagName) {
		this.tagName = tagName;
	}
	
	/**
	 * 序列化节点
	 */
	public void serizalizer(XmlSerializer serializer){
		try {
			serializer.startTag(null, tagName);
			leaf.serizalizer(serializer);
			serializer.endTag(null, tagName);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}

package com.athudong.psr.protocol;

import org.xmlpull.v1.XmlSerializer;
/**
 * xml�ĸ��ڵ�
 */
public class Root {
	
	//�ڵ�ı�ǩ��Ӧ����
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
	 * ���л��ڵ�
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

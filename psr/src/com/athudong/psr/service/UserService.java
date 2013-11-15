package com.athudong.psr.service;

import java.util.HashMap;
import com.athudong.psr.base.C;
import com.athudong.psr.protocol.Leaf;
import com.athudong.psr.protocol.Root;
import com.athudong.psr.protocol.XmlGenerate;
import android.text.TextUtils;

/**
 * 此类为用户服务，比如调用某个方法（需要指定flag）如何flag为登录会自动生成登录的xml
 */
public class UserService {
	private HashMap<String,String> params;
	private XmlGenerate xmlGenerate;
	
	public UserService(HashMap<String,String> requestParams){
		this.params = requestParams;
		this.xmlGenerate = new XmlGenerate();
	}
	
	public String requestXml(){
		if(params !=null){
			String action = params.get("action").trim();
			if(!TextUtils.isEmpty(action)){
				if(C.action.login.equals(action) || C.action.register.equals(action)){
				    Root root = new Root("data");
				    Leaf leaf = new Leaf("field");
				    leaf.setAttribute(params);
				    root.setLeaf(leaf);
				    xmlGenerate.setRoot(root);
				   return  xmlGenerate.getXmlGB2312();
				}
			}
		}
		return null;
	}
}

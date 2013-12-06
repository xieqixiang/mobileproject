package com.athudong.psr.util;

import java.util.ArrayList;
import java.util.Locale;

import com.athudong.psr.util.HanziToPinyin.Token;

public class PinYin {
	
	//���ַ���ƴ������ĸԭ�����أ���ת��ΪСд
		public static String getPinYin(String input) {
			ArrayList<Token> tokens = HanziToPinyin.getInstance().get(input);
			StringBuilder sb = new StringBuilder();
			if (tokens != null && tokens.size() > 0) {
				for (Token token : tokens) {
					if (Token.PINYIN == token.type) {
						sb.append(token.target);
					} else {
						sb.append(token.source);
					}
				}
			}
			String result = sb.toString().toLowerCase(Locale.CHINA);
			return result;
		}
	
}

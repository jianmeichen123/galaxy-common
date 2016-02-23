package com.galaxyinternet.framework.core.utils;

import org.apache.commons.lang.StringUtils;

public class EmojiUtil {
	/**
	 * @param str
	 * @return 将str中的emoji表情替换为特殊字符串
	 */
	public static String emojiReplace(String str) {
		if (StringUtils.isBlank(str))
			return str;
		StringBuilder sb = new StringBuilder(str.length() + 30);// 5个表情，每个表情占位6个字符
		for (int i = 0; i < str.length(); i++) {
			int code = str.codePointAt(i);
			if (code < 0x1F6C0 && code > 0x1F004) {
				sb.append((char) 0x1b);// ESC键
				sb.append(Integer.toHexString(code));
				i++;
				continue;
			}
			sb.append(str.charAt(i));
		}
		return sb.toString();
	}

	public static String emojiRecover(String ss) {
		if (StringUtils.isBlank(ss))
			return ss;
		StringBuffer sb = new StringBuffer();
		for (int i = 0, len = ss.length(); i < len; i++) {
			char ch = ss.charAt(i);
			if (ch == 0x1b && i + 5 <= len && ss.charAt(i + 1) == '1'
					&& ss.charAt(i + 2) == 'f') {
				int code = Integer.valueOf(ss.substring(i + 1, i + 6), 16);
				sb.appendCodePoint(code);
				i = i + 5;
				continue;
			}
			sb.append(ch);
		}
		return sb.toString();
	}
}

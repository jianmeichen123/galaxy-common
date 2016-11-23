package com.galaxyinternet.framework.core.utils;

import java.util.Collection;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 字符串处理工具类
 */
public class StringEx {
	/**
	 * 字符串首字母小写
	 * 
	 * @param str
	 * @return
	 */
	
	public static String toLowerCaseFirstOne(String str) {
		if (str == null || "".equals(str))
			return str;
		if (Character.isLowerCase(str.charAt(0)))
			return str;
		else
			return (new StringBuilder())
					.append(Character.toLowerCase(str.charAt(0)))
					.append(str.substring(1)).toString();
	}

	/**
	 * 字符串首字母大写
	 * 
	 * @param str
	 * @return
	 */
	public static String toUpperCaseFirstOne(String str) {
		if (str == null || "".equals(str))
			return str;
		if (Character.isUpperCase(str.charAt(0)))
			return str;
		else
			return (new StringBuilder())
					.append(Character.toUpperCase(str.charAt(0)))
					.append(str.substring(1)).toString();
	}

	/**
	 * 功能描述：去掉特殊字符
	 */
	public static String replaceSpecial(String source) {
		String dest = "";
		if (source != null) {
			Pattern p = Pattern.compile("\\s*|\t|\r|\n");
			Matcher m = p.matcher(source);
			dest = m.replaceAll("");
		}
		return dest;
	}

	@SuppressWarnings("rawtypes")
	public static boolean isNullOrEmpty(Object obj) {
		if (obj == null)
			return true;

		if (obj instanceof CharSequence)
			return ((CharSequence) obj).length() == 0;

		if (obj instanceof Collection)
			return ((Collection) obj).isEmpty();

		if (obj instanceof Map)
			return ((Map) obj).isEmpty();

		if (obj instanceof Object[]) {
			Object[] object = (Object[]) obj;
			if (object.length == 0) {
				return true;
			}
			boolean empty = true;
			for (int i = 0; i < object.length; i++) {
				if (!isNullOrEmpty(object[i])) {
					empty = false;
					break;
				}
			}
			return empty;
		}
		return false;
	}
	/**
	 * 判断字符串是否整数
	 * @author zhaoying
	 * @param value
	 * @return
	 */
	public static boolean isInteger(String value) {
		try {
			Integer.parseInt(value);
			return true;
		} catch (NumberFormatException e) {
			return false;
		}
	}
	
	/**
	 * 处理sql特殊字符
	 */
	public static String checkSql(String keyword){
		String [] specialWords= {"*","%","_"};
		for(int i = 0; i<specialWords.length; i++){
			if(keyword.indexOf(specialWords[i])>=0){
				keyword = keyword.replace(specialWords[i], "/"+specialWords[i]);
				return keyword;
			}
		}
		return keyword;
	}
	
	public static boolean equal(String str1, String str2)
	{
		if(str1 == null && str2 != null)
		{
			return false;
		}
		if(str1 != null && str2 == null)
		{
			return false;
		}
		if(str1 == null && str2 == null)
		{
			return true;
		}
		return str1.equals(str2);
	}
}

package com.galaxyinternet.framework.core.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;

public class CommonUtil {
	private final static String[] agent = { "Android", "iPhone", "iPod","iPad", "Windows Phone", "MQQBrowser" }; //定义移动端请求的所有可能
	public static boolean isValidDate(String str) {
       boolean convertSuccess=true;
       SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
       try {
           format.setLenient(false);
           format.parse(str);
       } catch (ParseException e) {
           convertSuccess=false;
       } 
           return convertSuccess;
	}
	/**
	* 判断User-Agent 是不是来自于手机
	* @param ua
	* @return
	*/
	public static boolean checkAgentIsMobile(String ua) {
		boolean flag = false;
		if (!ua.contains("Windows NT") || (ua.contains("Windows NT") && ua.contains("compatible; MSIE 9.0;"))) {
			// 排除 苹果桌面系统
			if (!ua.contains("Windows NT") && !ua.contains("Macintosh")) {
				for (String item : agent) {
					if (ua.contains(item)) {
						flag = true;
						break;
					}
				}
			}
		}
		return flag;
	}
}

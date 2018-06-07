package com.galaxyinternet.framework.core.config;

import java.text.MessageFormat;

public class PlaceholderConfigurer {
	/**
	 * 
	 * @Description:用于替换占位符,占位符规定：字符串%s，数字%d
	 * @param string
	 *            包含占位符的字符串
	 * @param args
	 *            替换占位符的参数
	 * @return 替换后的字符串
	 * 
	 * @example PlaceholderConfigurer.formatText("你好%s,环境来到%s系统","keifer",
	 *          "繁星");
	 * 
	 */
	public static String formatText(String string, Object... args) {
		return String.format(string, args);
	}

	/**
	 * 
	 * @Description:用于替换占位符，占位符规定：{0},{1},{2},{3},{...}
	 * @param string
	 *            包含占位符的字符串
	 * @param args
	 *            替换占位符的参数
	 * @return 替换后的字符串
	 * 
	 * @example PlaceholderConfigurer.formatMessage("你好{0},环境来到{1}系统","keifer",
	 *          "繁星");
	 * 
	 */
	public static String formatMessage(String pattern, Object... args) {
		return MessageFormat.format(pattern, args);
	}

}

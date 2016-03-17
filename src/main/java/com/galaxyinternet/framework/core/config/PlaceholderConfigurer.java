package com.galaxyinternet.framework.core.config;

public class PlaceholderConfigurer {
	/**
	 * 
	 * @Description:用于替换占位符
	 * @param string
	 *            包含占位符的字符串
	 * @param args
	 *            替换占位符的参数
	 * @return 替换后的字符串
	 */
	public static String formatText(String string, Object... args) {
		return String.format(string, args);
	}
}

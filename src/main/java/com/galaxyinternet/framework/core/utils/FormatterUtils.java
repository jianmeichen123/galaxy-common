package com.galaxyinternet.framework.core.utils;

import java.text.MessageFormat;

public class FormatterUtils {
	
	public static String formatStr(String source, Object... parameter){
		return MessageFormat.format(source, parameter);
	}

}

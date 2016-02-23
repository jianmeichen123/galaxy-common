package com.galaxyinternet.framework.core.utils;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 读取属性文件的类
 */
public class PropertiesUtils {
	static Logger LOGGER = LoggerFactory.getLogger(PropertiesUtils.class);

	public static Properties getProperties(String file) {
		Properties p = new Properties();
		try {
			InputStream inputStream = PropertiesUtils.class.getClassLoader().getResourceAsStream(file);
			p.load(inputStream);
		} catch (IOException e1) {
			LOGGER.error(e1.getMessage(), e1);
		}
		return p;
	}
}

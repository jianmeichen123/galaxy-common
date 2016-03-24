package com.galaxyinternet.framework.core.utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class JSONUtils {

	private static Logger logger = LoggerFactory.getLogger(JSONUtils.class);
	private static Gson gson = new GsonBuilder().create();

	/**
	 * 
	 * @param jsonStr
	 * @param type
	 *            new TypeToken<ArrayList<T>>() {}.getType()
	 * @return
	 */
	public static <T> List<T> parseArray(String jsonStr, Type type) {
		return gson.fromJson(jsonStr, type);
	}

	/**
	 * 
	 * @param jsonStr
	 * @param type
	 *            new TypeToken<Map<String, T>>() {}.getType()
	 * @return
	 */
	public static <T> Map<String, T> parseMap(String jsonStr, Type type) {
		return gson.fromJson(jsonStr, type);
	}

	/**
	 * 
	 * @Title: 解析请求体的方法
	 */
	public static String getBodyString(HttpServletRequest request) {
		BufferedReader br = null;
		String inputLine = null;
		String str = "";
		try {
			br = request.getReader();
			while ((inputLine = br.readLine()) != null) {
				str += inputLine;
			}
		} catch (IOException e) {
			logger.error("从request请求体中获取json对象异常", e);
		} finally {
			try {
				if (null != br) {
					br.close();
				}
			} catch (IOException e) {
			}
		}
		return str;
	}

}

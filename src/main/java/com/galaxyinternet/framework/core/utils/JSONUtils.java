package com.galaxyinternet.framework.core.utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class JSONUtils {

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
		try {
			br = request.getReader();
		} catch (IOException e1) {
			e1.printStackTrace();
			return "";
		} finally {
			if (br != null) {
				try {
					br.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		String inputLine;
		String str = "";
		try {
			while ((inputLine = br.readLine()) != null) {
				str += inputLine;
			}
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (br != null) {
				try {
					br.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		return str;
	}

}

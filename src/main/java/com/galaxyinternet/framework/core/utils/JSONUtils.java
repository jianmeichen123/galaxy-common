package com.galaxyinternet.framework.core.utils;

import java.lang.reflect.Type;
import java.util.List;
import java.util.Map;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class JSONUtils {
	
	private static Gson gson = new GsonBuilder().create();

	/**
	 * 
	 * @param jsonStr
	 * @param type  new TypeToken<ArrayList<T>>() {}.getType()
	 * @return
	 */
	public static <T> List<T> parseArray(String jsonStr, Type type) {
		return gson.fromJson(jsonStr, type);
	}

	/**
	 * 
	 * @param jsonStr
	 * @param type new TypeToken<Map<String, T>>() {}.getType()
	 * @return
	 */
	public static <T> Map<String, T> parseMap(String jsonStr, Type type) {
		return gson.fromJson(jsonStr, type);
	}

}

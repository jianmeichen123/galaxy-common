package com.galaxyinternet.framework.core.utils;

import java.util.HashMap;
import java.util.Map;

public class FileUtils {
	/**
	 * 获取文件名称和后缀
	 * @param fileName
	 * @return map <String,String> 
	 * fileName:文件名称          
	 * fileSuffix:文件后缀
	 */
	public static Map<String, String> transFileNames(String fileName) {
		Map<String, String> retMap = new HashMap<String, String>();
		int dotPos = fileName.lastIndexOf(".");
		if(dotPos == -1){
			retMap.put("fileName", fileName);
			retMap.put("fileSuffix", "");
		}else{
			retMap.put("fileName", fileName.substring(0, dotPos));
			retMap.put("fileSuffix", fileName.substring(dotPos+1));
		}
		return retMap;
	}
}

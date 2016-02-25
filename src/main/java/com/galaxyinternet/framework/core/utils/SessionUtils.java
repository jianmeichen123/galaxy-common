package com.galaxyinternet.framework.core.utils;

import java.util.Random;

public class SessionUtils {

	/**
	 * 创建web端的sessionid
	 * @return
	 */
	public String createWebSessionId() {
		Random random = new Random();
		int randomCount = random.nextInt(10000);
		Long currtime = System.currentTimeMillis();
		byte[] b = ("web" + randomCount + currtime + "").getBytes();
		return EncodeUtils.encodeBase64(b);
	}
	
	/**
	 * 创建app端的sessionid
	 * @param imei  移动设备国际识别码,由15位数字组成
	 * @return
	 */
	public static String createAppSessionId(String imei){
		Random random = new Random();
		int randomCount = random.nextInt(10000);
		Long curtm = System.currentTimeMillis();
		byte[] b = (imei + randomCount + curtm + "").getBytes();
		return EncodeUtils.encodeBase64(b);
	}
}

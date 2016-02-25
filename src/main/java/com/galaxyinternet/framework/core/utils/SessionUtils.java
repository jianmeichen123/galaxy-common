package com.galaxyinternet.framework.core.utils;

import java.util.Random;

import com.galaxyinternet.framework.core.id.IdGenerator;

public class SessionUtils {

	/**
	 * 创建web端的sessionid
	 * 
	 * @return
	 */
	public static String createWebSessionId() {
		Random random = new Random();
		int randomCount = random.nextInt(10000);
		Long currtime = System.currentTimeMillis();
		Long id = IdGenerator.generateId(random.getClass());
		byte[] b = ("WEB" + randomCount + currtime + id).getBytes();
		return EncodeUtils.encodeBase64(b);
	}

	/**
	 * 创建app端的sessionid
	 * 
	 * @param imei
	 *            移动设备国际识别码,由15位数字组成
	 * @return
	 */
	public static String createAppSessionId(String imei) {
		Random random = new Random();
		int randomCount = random.nextInt(10000);
		Long curtm = System.currentTimeMillis();
		Long id = IdGenerator.generateId(random.getClass());
		byte[] b = ("APP" + imei + randomCount + curtm + id).getBytes();
		return EncodeUtils.encodeBase64(b);
	}

	public static void main(String[] args) {
		System.out.println(SessionUtils.createWebSessionId());
	}
}

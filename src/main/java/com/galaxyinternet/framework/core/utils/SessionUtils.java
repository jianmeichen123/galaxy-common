package com.galaxyinternet.framework.core.utils;

import java.util.Random;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;

import com.galaxyinternet.framework.core.constants.Constants;
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

	public static String getSessionId(HttpServletRequest request) {
		String sessionId = request.getHeader(Constants.SESSION_ID_KEY);
		if (StringUtils.isBlank(sessionId)) {
			sessionId = request.getParameter(Constants.SESSOPM_SID_KEY);
		}
		return sessionId;
	}

	public static String getUserId(HttpServletRequest request) {
		String userId = request.getHeader(Constants.REQUEST_HEADER_USER_ID_KEY);
		if (StringUtils.isBlank(userId)) {
			userId = request.getParameter(Constants.REQUEST_URL_USER_ID_KEY);
		}
		return userId;
	}
	
	/**
	 * 如果是href，则从请求地址后边获取参数值
	 * 如果是ajax，则从header中获取
	 * @param request
	 * @param key
	 * @return
	 */
	public static String getValueFromRequest(HttpServletRequest request, String key){
		String value = request.getHeader(key);
		if (StringUtils.isBlank(value)) {
			value = request.getParameter(key);
		}
		return value;
	}

	public static void main(String[] args) {
		System.out.println(SessionUtils.createWebSessionId());
	}
}

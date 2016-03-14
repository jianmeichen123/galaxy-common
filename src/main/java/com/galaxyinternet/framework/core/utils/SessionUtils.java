package com.galaxyinternet.framework.core.utils;

import java.util.Random;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;

import com.galaxyinternet.framework.cache.Cache;
import com.galaxyinternet.framework.core.constants.Constants;
import com.galaxyinternet.framework.core.id.IdGenerator;
import com.galaxyinternet.framework.core.model.BaseUser;

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

	public static BaseUser getUser(HttpServletRequest request, Cache cache) {
		String sessionId = request.getHeader(Constants.SESSION_ID_KEY);
		if (StringUtils.isBlank(sessionId)) {
			sessionId = request.getParameter(Constants.SESSOPM_SID_KEY);
		}
		if (StringUtils.isNotBlank(sessionId)) {
			return getUser(request, sessionId, cache);
		} else {
			return (BaseUser) request.getSession().getAttribute(Constants.SESSION_USER_KEY);
		}
	}

	/**
	 * 获取session中用户的信息
	 * 
	 * @param request
	 *            request
	 * @param key
	 *            sessionId key
	 * @return user
	 */
	public static BaseUser getUser(HttpServletRequest request, String key, Cache cache) {
		BaseUser user = (BaseUser) cache.getByRedis(key);
		if (user != null) {
			cache.setByRedis(key, user, 60 * 60 * 24 * 1);
		}
		return user;
	}

	public static void main(String[] args) {
		System.out.println(SessionUtils.createWebSessionId());
	}
}

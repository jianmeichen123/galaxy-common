package com.galaxyinternet.framework.core.model;

import java.io.Serializable;
/**
 * 该类用于存储请求相关的关键数据
 */
public class Header implements Serializable {

	private static final long serialVersionUID = 1L;
	private String sessionId;
	private Long userId;
	private String loginName;

	public String getSessionId() {
		return sessionId;
	}

	public void setSessionId(String sessionId) {
		this.sessionId = sessionId;
	}

	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}

	public String getLoginName() {
		return loginName;
	}

	public void setLoginName(String loginName) {
		this.loginName = loginName;
	}

}

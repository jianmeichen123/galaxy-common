package com.galaxyinternet.framework.core.model;

import java.util.List;

public class BuryPointEntity {
	private List<BuryPoint> list;
	
	private String sessionId;
	
	private String userId;

	public List<BuryPoint> getList() {
		return list;
	}

	public void setList(List<BuryPoint> list) {
		this.list = list;
	}

	public String getSessionId() {
		return sessionId;
	}

	public void setSessionId(String sessionId) {
		this.sessionId = sessionId;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}
	
	

}

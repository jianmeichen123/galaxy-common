package com.galaxyinternet.framework.core.model;

public class BaseUser extends PagableEntity {
	private static final long serialVersionUID = 1L;

	protected String sessionId;

	/**
	 * @return the sessionId
	 */
	public String getSessionId() {
		return sessionId;
	}

	/**
	 * @param sessionId
	 *            the sessionId to set
	 */
	public void setSessionId(String sessionId) {
		this.sessionId = sessionId;
	}

}

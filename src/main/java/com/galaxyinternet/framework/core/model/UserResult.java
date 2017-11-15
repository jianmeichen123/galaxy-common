package com.galaxyinternet.framework.core.model;

public class UserResult
{
	private boolean success;
	private String errorCode;
	private String message;
	//private List<User> userList;
	private Object value;
	public boolean isSuccess() {
		return success;
	}

	public void setSuccess(boolean success) {
		this.success = success;
	}

	public String getErrorCode() {
		return errorCode;
	}

	public void setErrorCode(String errorCode) {
		this.errorCode = errorCode;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public Object getValue() {
		return value;
	}

	public void setValue(Object value) {
		this.value = value;
	}




	
	@Override
	public String toString() {
		return "AuthResult [success=" + success + ", errorCode=" + errorCode + ", message=" + message + ", value="
				+ value + "]";
	}
	
}

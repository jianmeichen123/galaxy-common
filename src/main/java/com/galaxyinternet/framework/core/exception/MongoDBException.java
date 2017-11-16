package com.galaxyinternet.framework.core.exception;

public class MongoDBException extends Exception{

	private static final long serialVersionUID = 1L;
	
	private Throwable throwable;
	
	private String message;
	
	public MongoDBException(Throwable throwable, String message) {
		this.throwable = throwable;
		this.message = message;
	}

	public Throwable getThrowable() {
		return throwable;
	}

	public String getMessage() {
		return message;
	}

}

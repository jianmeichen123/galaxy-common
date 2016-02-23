package com.galaxyinternet.framework.core.exception;

/**
 * 运行时异常的父类,其他运行时异常应该继承此类
 * 
 * @author kaihu
 */
public class BaseException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	protected int code;

	public BaseException(int code, String message, Throwable throwable) {
		super(message, throwable);
		this.code = code;
	}

	public BaseException(String message, Throwable throwable) {
		super(message, throwable);
	}

	public BaseException(int code, String message) {
		super(message);
		this.code = code;
	}

	public BaseException(String message) {
		super(message);
	}

	public BaseException(Throwable arg0) {
		super(arg0);
	}

	public int getCode() {
		return code;
	}

	public void setCode(int code) {
		this.code = code;
	}
}

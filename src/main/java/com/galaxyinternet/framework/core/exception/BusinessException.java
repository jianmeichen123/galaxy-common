package com.galaxyinternet.framework.core.exception;

/**
 * 运行时异常的父类,其他运行时异常应该继承此类
 * 
 * @author kaihu
 */
public class BusinessException extends BaseException {
	private static final long serialVersionUID = 1L;

	public BusinessException(int code, String message, Throwable throwable) {
		super(code, message, throwable);
	}

	public BusinessException(String message, Throwable throwable) {
		super(message, throwable);
	}

	public BusinessException(int code, String message) {
		super(code, message);
	}

	public BusinessException(String message) {
		super(message);
	}

	public BusinessException(Throwable arg0) {
		super(arg0);
	}

}

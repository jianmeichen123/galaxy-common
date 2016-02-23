package com.galaxyinternet.framework.core.exception;

/**
 * 运行时异常的父类,其他运行时异常应该继承此类
 * 
 * @author kaihu
 */
public class DaoException extends BaseException {
	private static final long serialVersionUID = 1L;

	public DaoException(int code, String message, Throwable throwable) {
		super(code, message, throwable);
	}

	public DaoException(String message, Throwable throwable) {
		super(message, throwable);
	}

	public DaoException(int code, String message) {
		super(code, message);
	}

	public DaoException(String message) {
		super(message);
	}

	public DaoException(Throwable arg0) {
		super(arg0);
	}
}

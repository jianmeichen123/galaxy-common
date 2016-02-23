package com.galaxyinternet.framework.core.utils;
public class JSONResult {
    
	// 操作返回状态
		public static enum RESULT {
			// 成功、失败、未登陆、用户不存在、缺少参数10、参数错误11、账号被禁用
			SUCCESS("000000"), FAILED("000001"), NOTLOGIN("000002"), NOEXIST("000003"), PARAMETER_INVALID("000011"), RETURN_DATA_NULL("000012"), FORBIDDEN("300000");
			private final String value;

			RESULT(String value) {
				this.value = value;
			}

			public String val() {
				return this.value;
			}
		};
	public static final String FAILED_PARAM = "参数错误";
    public static final String FAILED_SYSTEM = "对不起，系统繁忙，稍后再试";
    public static final String FAILED_LOGIN = "对不起，您没有登录";
    public static final String FAILED_PERMISSION = "对不起，您没有权限";
    public static final String ACCESSOK = "成功";	
		
	private String code;
	private String msg;
	private Object obj;

	public JSONResult() {
	}

	public JSONResult(String code, String msg) {
		this.code = code;
		this.msg = msg;
		this.obj = "";
	}

	public JSONResult(String code, String msg, Object obj) {
		this.code = code;
		this.msg = msg;
		this.obj = obj;
	}
	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}

	public Object getObj() {
		return obj;
	}

	public void setObj(Object obj) {
		this.obj = obj;
	}

}

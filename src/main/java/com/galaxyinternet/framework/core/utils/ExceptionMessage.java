package com.galaxyinternet.framework.core.utils;

public enum  ExceptionMessage  {

	
	DATA_NOT_EXISTS(10003, "%s"),
	// common message
	FIELD_NOT_ALLOWED_EMPTY(10010, "%s不能为空"),
	FIELD_VALUE_MUST_LARGE_THAN(10011, "%s必须大于%2$.2f"),
	FIELD_VALUE_MUST_LESS_THAN(10012, "%s必须小于%2$.2f"),
	FIELD_NOT_EMAIL(10013, "%s格式错误"),
	FIELD_NOT_MOBILE(10014, "%s格式错误"),
	FIELD_NOT_ID_NUM(10015, "%s格式错误"),
	FIELD_NOT_DATE(10016, "%s格式错误"),
	FIELD_LENGTH_MUST_LESS(10017, "%s必须小于%d"),
	FIELD_LENGTH_MUST_MORE(10018, "%s必须大于%d"),
	FIELD_LENGTH_MUST_BETWEEN(10019, "%s长度必须大于%d和小于%d"),
	FILED_NOT_IN_ENUM_VALUES(10020, "不支持的%s"),
	PARAME_SAME(10021, "传入参数%s重复"),
	QUERY_LIST_FAIL(10022,"获取列表失败%s"),
	UPDATE_TASK_STATUS(10023,"更新任务状态失败%s");
	
	
	
	private int status;
	private String message;

	ExceptionMessage(
		int status, String message) {
		this.setStatus(status);
		this.setMessage(message);
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

}

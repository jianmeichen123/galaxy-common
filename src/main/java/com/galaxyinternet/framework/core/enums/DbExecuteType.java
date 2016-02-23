package com.galaxyinternet.framework.core.enums;

public enum DbExecuteType {

	INSERT(1, "添加"), DELETE(2, "保存"), UPDATE(3, "更新"), SELECT(4, "查询");

	private int key;
	private String description;

	public int getKey() {
		return key;
	}

	public void setKey(int key) {
		this.key = key;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	private DbExecuteType(int key, String description) {
		this.key = key;
		this.description = description;
	}
}
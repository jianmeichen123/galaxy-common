package com.galaxyinternet.framework.core.enums;

import java.util.ArrayList;
import java.util.List;

import com.galaxyinternet.framework.core.exception.BaseException;

/**
 * 访问终端
 * 
 * @author keifer
 *
 */
public enum Terminal {

	WEB(1, "web"), IOS(2, "ios"), ANDROID(3, "android");

	private int key;
	private String description;

	private Terminal(int key, String description) {
		this.key = key;
		this.description = description;
	}

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

	@SuppressWarnings({ "unchecked", "rawtypes" })
	public int getTerminalValue(int value) {
		Terminal[] values = Terminal.values();
		List list = new ArrayList();
		for (Terminal terminal : values) {
			list.add(terminal.getKey());
		}
		if (!list.contains(value)) {
			throw new BaseException("参数[ " + value + " ]错误,不支持该设备登录");
		}
		return value;
	}
}

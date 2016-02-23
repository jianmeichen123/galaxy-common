package com.galaxyinternet.framework.core.enums;

/**
 * 状态枚举类型
 */
public enum EnumStatus implements BaseEnum {
	ON("启用"), OFF("注销");

	private EnumStatus(String label) {
		this.label = label;
	}

	private String label;

	@Override
	public String getLabel() {
		return this.label;
	}

}

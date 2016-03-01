package com.galaxyinternet.framework.core.file;

public enum BucketName {

	DEV("galaxydev-xhhl-fx"), TEST("galaxytest-xhhl-fx"), PRODUCT("online-xhhl-fx");
	private String name;

	private BucketName(String name) {
		this.name = name;
	}

	public String getName() {
		return name;
	}
}

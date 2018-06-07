package com.galaxyinternet.framework.core.file;

/**
 * @Description: 上传模式
 */
public enum UploadModeType {
	OSS("oss", "直连阿里oss服务器上传模式"), LOCAL("local", "本地服务器端上传模式");

	private String key;
	private String decription;

	private UploadModeType(String key, String decription) {
		this.key = key;
		this.decription = decription;
	}

	public String getKey() {
		return key;
	}

	public String getDecription() {
		return decription;
	}

}

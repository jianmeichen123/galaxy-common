package com.galaxyinternet.framework.core.file;

/**
 * @Description: 上传模式
 */
public enum UploadModeType {
	OSS(1, "直连阿里oss服务器上传模式"), LOCAL(2, "本地服务器端上传模式");

	private int key;
	private String decription;

	private UploadModeType(int key, String decription) {
		this.key = key;
		this.decription = decription;
	}

	public int getKey() {
		return key;
	}

	public String getDecription() {
		return decription;
	}

}

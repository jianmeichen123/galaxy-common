package com.galaxyinternet.framework.core.file;

public class UploadFileResult extends FileResult {
	/**
	 * 文件的唯一标示
	 */
	private String fileKey;
	/**
	 * 存储文件的桶名称
	 */
	private String bucketName;
	/**
	 * 文件名
	 */
	private String fileName;
	/**
	 * 文件的后缀名
	 */
	private String fileSuffix;
	/**
	 * 文件大小
	 */
	private long contentLength;

	private String etag;

	public String getFileKey() {
		return fileKey;
	}

	public void setFileKey(String fileKey) {
		this.fileKey = fileKey;
	}

	public String getBucketName() {
		return bucketName;
	}

	public void setBucketName(String bucketName) {
		this.bucketName = bucketName;
	}

	public long getContentLength() {
		return contentLength;
	}

	public void setContentLength(long contentLength) {
		this.contentLength = contentLength;
	}

	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	public String getFileSuffix() {
		return fileSuffix;
	}

	public void setFileSuffix(String fileSuffix) {
		this.fileSuffix = fileSuffix;
	}

	public String getEtag() {
		return etag;
	}

	public void setEtag(String etag) {
		this.etag = etag;
	}
}

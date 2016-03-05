package com.galaxyinternet.framework.core.oss;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * 单个文件的上传线程集合
 */
public class UploadPartObj implements Serializable {

	private static final long serialVersionUID = 1L;

	List<UploadPartThread> uploadPartThreads = Collections.synchronizedList(new ArrayList<UploadPartThread>());

	boolean result = true;

	public List<UploadPartThread> getUploadPartThreads() {
		return uploadPartThreads;
	}

	public void setUploadPartThreads(List<UploadPartThread> uploadPartThreads) {
		this.uploadPartThreads = uploadPartThreads;
	}

	public boolean isResult() {
		return result;
	}

	public void setResult(boolean result) {
		this.result = result;
	}
}

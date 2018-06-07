package com.galaxyinternet.framework.core.oss;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * 单个文件的上传线程集合
 */
public class PartUploadObj implements Serializable {

	private static final long serialVersionUID = 1L;

	List<PartUploadCallable> uploadPartThreads = Collections.synchronizedList(new ArrayList<PartUploadCallable>());

	boolean result = true;

	public List<PartUploadCallable> getUploadPartThreads() {
		return uploadPartThreads;
	}

	public void setUploadPartThreads(List<PartUploadCallable> uploadPartThreads) {
		this.uploadPartThreads = uploadPartThreads;
	}

	public boolean isResult() {
		return result;
	}

	public void setResult(boolean result) {
		this.result = result;
	}
}

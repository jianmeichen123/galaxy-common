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

	List<PartUploader> uploadPartThreads = Collections.synchronizedList(new ArrayList<PartUploader>());

	boolean result = true;

	public List<PartUploader> getUploadPartThreads() {
		return uploadPartThreads;
	}

	public void setUploadPartThreads(List<PartUploader> uploadPartThreads) {
		this.uploadPartThreads = uploadPartThreads;
	}

	public boolean isResult() {
		return result;
	}

	public void setResult(boolean result) {
		this.result = result;
	}
}

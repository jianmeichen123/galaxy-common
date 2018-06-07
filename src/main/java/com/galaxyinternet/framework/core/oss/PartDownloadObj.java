package com.galaxyinternet.framework.core.oss;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * 单个文件的下载线程集合
 */
public class PartDownloadObj implements Serializable {
	private static final long serialVersionUID = 1L;
	/**
	 * 下载线程集合
	 */
	List<PartDownloadCallable> downloadPartThreads = Collections.synchronizedList(new ArrayList<PartDownloadCallable>());
	/**
	 * 下载结果
	 */
	boolean result = true;

	public List<PartDownloadCallable> getDownloadPartThreads() {
		return downloadPartThreads;
	}

	public void setDownloadPartThreads(List<PartDownloadCallable> downloadPartThreads) {
		this.downloadPartThreads = downloadPartThreads;
	}

	public boolean isResult() {
		return result;
	}

	public void setResult(boolean result) {
		this.result = result;
	}

}
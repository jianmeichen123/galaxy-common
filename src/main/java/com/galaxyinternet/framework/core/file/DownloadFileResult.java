package com.galaxyinternet.framework.core.file;

import java.io.File;
import java.io.InputStream;

public class DownloadFileResult  extends FileResult{
	/**
	 * 下载的文件的流对象
	 */
	private InputStream input;

	/**
	 * 下载的文件
	 */
	private File file;

	public InputStream getInput() {
		return input;
	}

	public void setInput(InputStream input) {
		this.input = input;
	}

	public File getFile() {
		return file;
	}

	public void setFile(File file) {
		this.file = file;
	}

}

package com.galaxyinternet.framework.core.oss;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.io.Serializable;
import java.util.concurrent.Callable;

import org.apache.log4j.Logger;

import com.aliyun.oss.common.utils.IOUtils;
import com.aliyun.oss.model.GetObjectRequest;
import com.aliyun.oss.model.OSSObject;

/**
 * 用于上传每个part的线程类 可序列化 用于上传的断点续传
 */
public class DownloadPartThread implements Callable<DownloadPartThread>, Serializable {

	private static final long serialVersionUID = 1L;
	public static final Logger LOGGER = Logger.getLogger(DownloadPartThread.class);
	// 当前线程的下载开始位置
	private long startPos;

	// 当前线程的下载结束位置
	private long endPos;

	// 保存文件路径
	private String localFilePath;

	private String bucketName;
	private String fileKey;
	private String etag;

	public DownloadPartThread(long startPos, long endPos, String localFilePath, String bucketName, String fileKey) {
		this.startPos = startPos;
		this.endPos = endPos;
		this.localFilePath = localFilePath;
		this.bucketName = bucketName;
		this.fileKey = fileKey;
	}

	@Override
	public DownloadPartThread call() {
		RandomAccessFile file = null;
		OSSObject ossObject = null;
		try {
			File pFile = new File(localFilePath);
			if (!pFile.getParentFile().exists())
				pFile.getParentFile().mkdirs();
			file = new RandomAccessFile(localFilePath, "rw");
			// 调用ossapi
			GetObjectRequest getObjectRequest = new GetObjectRequest(bucketName, fileKey);
			getObjectRequest.setRange(startPos, endPos);
			ossObject = OSSFactory.getInstance().getObject(getObjectRequest);
			file.seek(startPos);
			int bufSize = 1024;
			byte[] buffer = new byte[bufSize];
			int bytesRead;
			while ((bytesRead = ossObject.getObjectContent().read(buffer)) > -1) {
				file.write(buffer, 0, bytesRead);
				// 更新开始位置，保证在出错后重下载是从上次结束的地方开始下，而不是下载整个块
				startPos += bytesRead;
			}
			this.etag = ossObject.getObjectMetadata().getETag();
		} catch (Exception e) {
			LOGGER.info("==" + e.getMessage());
		} finally {
			if (ossObject != null)
				IOUtils.safeClose(ossObject.getObjectContent());
			try {
				if (file != null)
					file.close();
			} catch (IOException e) {
				e.printStackTrace();
			}

		}
		return this;
	}

	public String getEtag() {
		return etag;
	}
}
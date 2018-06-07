package com.galaxyinternet.framework.core.oss;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.Serializable;
import java.util.concurrent.Callable;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.aliyun.oss.OSSClient;
import com.aliyun.oss.model.UploadPartRequest;
import com.aliyun.oss.model.UploadPartResult;

/**
 * 上传每个part的线程类 可序列化 用于上传的断点续传
 */
public class PartUploadCallable implements Callable<PartUploadCallable>, Serializable {

	private static final long serialVersionUID = 1L;
	static final Logger logger = LoggerFactory.getLogger(PartUploadCallable.class);

	private File uploadFile;
	private String bucket;
	private String object;
	private long start;
	private long size;
	private int partId;
	private String uploadId;
	private FxPartETag fxPartETag;

	public PartUploadCallable(OSSClient client, String bucket, String object, File uploadFile, String uploadId, int partId,
			long start, long partSize) {
		this.uploadFile = uploadFile;
		this.bucket = bucket;
		this.object = object;
		this.start = start;
		this.size = partSize;
		this.partId = partId;
		this.uploadId = uploadId;
	}

	@Override
	public PartUploadCallable call() {

		InputStream in = null;
		try {
			in = new FileInputStream(uploadFile);
			in.skip(start);

			UploadPartRequest uploadPartRequest = new UploadPartRequest();
			uploadPartRequest.setBucketName(bucket);
			uploadPartRequest.setKey(object);
			uploadPartRequest.setUploadId(uploadId);
			uploadPartRequest.setInputStream(in);
			uploadPartRequest.setPartSize(size);
			uploadPartRequest.setPartNumber(partId);
			
			UploadPartResult uploadPartResult = OSSFactory.getClientInstance().uploadPart(uploadPartRequest);
			// MyPartETag是对uploadPartResult.getPartETag()的返回值PartETag的封装，主要是为了能序列化PartETag，MyPartETag仅比PartETag多实现了Serializable接口
			fxPartETag = new FxPartETag(uploadPartResult.getPartETag());
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (in != null) {
				try {
					in.close();
				} catch (Exception e) {
					logger.error("关闭读入流失败：" + e.getMessage());
				}
			}
		}
		return this;
	}

	public String getUploadId() {
		return uploadId;
	}

	public void setUploadId(String uploadId) {
		this.uploadId = uploadId;
	}

	public FxPartETag getFxPartETag() {
		return fxPartETag;
	}

	public void setFxPartETag(FxPartETag fxPartETag) {
		this.fxPartETag = fxPartETag;
	}

}
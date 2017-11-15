package com.galaxyinternet.framework.core.oss;

import java.io.File;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.aliyun.oss.OSSClient;
import com.aliyun.oss.OSSException;
import com.aliyun.oss.model.ObjectMetadata;
import com.galaxyinternet.framework.core.utils.FileSerializableUtil;

/**
 * oss多线程分段下载文件
 */
public class OSSDownloader implements Callable<Integer> {
	public final Logger logger = LoggerFactory.getLogger(OSSDownloader.class);
	// 外层线程池
	public static ExecutorService downloadMainPool = null;
	// 内层线程池
	private ExecutorService pool;

	static {
		downloadMainPool = Executors.newFixedThreadPool(OSSConstant.CONCURRENT_FILE_NUMBER, new ThreadFactory() {
			public Thread newThread(Runnable r) {
				Thread s = Executors.defaultThreadFactory().newThread(r);
				s.setDaemon(true);
				return s;
			}
		});
	}

	private String localFilePath;// 本地文件路径
	private String bucketName; // bucketName
	private String key;// 云端存储路径

	public OSSDownloader() {
		super();
	}

	public OSSDownloader(String localFilePath, String bucketName, String key) {
		// 初始化子线程池
		pool = Executors.newFixedThreadPool(OSSConstant.SINGLE_FILE_CONCURRENT_THREADS);
		this.localFilePath = localFilePath;
		this.bucketName = bucketName;
		this.key = key;

	}

	// 执行当前线程
	public Integer downloadFile() {
		Integer r = GlobalCode.ERROR;
		// 向downloadMainPool中submit当前线程
		Future<Integer> result = downloadMainPool.submit(this);
		try {
			r = result.get();
		} catch (InterruptedException | ExecutionException e) {
			e.printStackTrace();
		}
		return r;

	}

	/**
	 * 
	 * @param localFilePath
	 *            需要存放的文件路径
	 * @param bucketName
	 *            bucketName
	 * @param key
	 *            存储key -在oss的存储路径
	 * @return
	 */
	@Override
	public Integer call() {
		// OSSClient 使用单例
		OSSClient client = OSSFactory.getClientInstance();
		ObjectMetadata objectMetadata = null;
		// 判断文件在云端是否存在
		try {
			objectMetadata = client.getObjectMetadata(bucketName, key);
		} catch (OSSException e) {
			logger.error("==请检查bucketName或key");
			return GlobalCode.ERROR;
		}
		long fileLength = objectMetadata.getContentLength();

		// 自定义的每个下载分块大小
		long partSize = OSSConstant.DOWNLOAD_PART_SIZE;

		// 需要下载的文件分块数
		int partCount = calPartCount(fileLength, partSize);

		// 子线程池的线程对象封装类（用于序列化的）
		PartDownloadObj downloadPartObj = null;
		boolean isSerializationFile = false;
		// 序列化的文件路径（与下载文件同路径使用.dw.temp后缀）
		String serializationFilePath = localFilePath + ".dw.temp";
		// 若存在反序列化对象
		if (new File(serializationFilePath).exists()) {
			downloadPartObj = (PartDownloadObj) FileSerializableUtil.deserialize(serializationFilePath);
			isSerializationFile = true;
		}
		// 序列化文件不存在，分配分块给子线程池线程对象
		if (downloadPartObj == null || !isSerializationFile) {
			downloadPartObj = new PartDownloadObj();
			for (int i = 0; i < partCount; i++) {
				final long startPos = partSize * i;
				final long endPos = partSize * i
						+ (partSize < (fileLength - startPos) ? partSize : (fileLength - startPos)) - 1;
				// DownloadPartThread是执行每个分块下载任务的线程
				downloadPartObj.getDownloadPartThreads()
						.add(new PartDownloadCallable(startPos, endPos, localFilePath, bucketName, key));
			}
		}

		try {
			int i = 0;
			// download方法提交分块下载线程至子线程池下载，while循环用于下载失败重复下载，OSSConstant.RETRY定义重复下载次数
			while (download(downloadPartObj, serializationFilePath).isResult() == false) {
				if (++i == OSSConstant.RETRY)
					break;
				logger.warn(Thread.currentThread().getName() + "重试第" + i + "次");
			}
		} catch (Exception e) {
			logger.error("==" + e.getMessage());
			return GlobalCode.THREAD_ERROR;
		}
		if (!downloadPartObj.isResult()) {
			return GlobalCode.NETWORK_ERROR;
		}
		return GlobalCode.SUCCESS;
	}

	/**
	 * 多线程下载单个文件
	 * 
	 * @param partThreadObj
	 * @param serializationFilePath
	 * @return
	 */
	private PartDownloadObj download(PartDownloadObj partThreadObj, String serializationFilePath) {

		try {
			partThreadObj.setResult(true);
			// 向子线程池中submit单个文件所有分块下载线程
			for (int i = 0; i < partThreadObj.getDownloadPartThreads().size(); i++) {
				if (partThreadObj.getDownloadPartThreads().get(i).getEtag() == null)
					pool.submit(partThreadObj.getDownloadPartThreads().get(i));
			}
			// shutdown子线程池，池内所下载任务执行结束后停止当前线程池
			pool.shutdown();
			// 循环检查线程池，同时在此序列化partThreadObj
			while (!pool.isTerminated()) {
				FileSerializableUtil.serialize(partThreadObj, serializationFilePath);
				pool.awaitTermination(OSSConstant.SERIALIZATION_TIME, TimeUnit.SECONDS);
			}
			// 判断下载结果
			for (PartDownloadCallable downloadPartThread : partThreadObj.getDownloadPartThreads()) {
				if (downloadPartThread.getEtag() == null) {
					partThreadObj.setResult(false);
				}
			}
			// 下载成功 删除序列化文件
			if (partThreadObj.isResult() == true)
				FileSerializableUtil.delSerlzFile(serializationFilePath);

		} catch (Exception e) {
			logger.error("==" + e.getMessage());
		}
		return partThreadObj;
	}

	/**
	 * 获取分块数
	 * 
	 * @param fileLength
	 * @param partSize
	 * @return
	 */
	private static int calPartCount(long fileLength, long partSize) {
		int partCount = (int) (fileLength / partSize);
		if (fileLength % partSize != 0) {
			partCount++;
		}
		return partCount;
	}

	public String getLocalFilePath() {
		return localFilePath;
	}

	public void setLocalFilePath(String localFilePath) {
		this.localFilePath = localFilePath;
	}

	public String getBucketName() {
		return bucketName;
	}

	public void setBucketName(String bucketName) {
		this.bucketName = bucketName;
	}

	public String getKey() {
		return key;
	}

	public void setKey(String key) {
		this.key = key;
	}
}
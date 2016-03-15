package com.galaxyinternet.framework.core.oss;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.TimeUnit;

import org.apache.log4j.Logger;

import com.aliyun.oss.ClientException;
import com.aliyun.oss.OSSClient;
import com.aliyun.oss.OSSException;
import com.aliyun.oss.model.CompleteMultipartUploadRequest;
import com.aliyun.oss.model.InitiateMultipartUploadRequest;
import com.aliyun.oss.model.InitiateMultipartUploadResult;
import com.aliyun.oss.model.ObjectMetadata;
import com.aliyun.oss.model.PartETag;
import com.galaxyinternet.framework.core.utils.FileSerializableUtil;
import com.galaxyinternet.framework.core.utils.Md5Utils;

/**
 * 使用普通方式上传小文件，使用Multipart上传方式进行多线程分段上传较大文件
 */
public class OSSUploader implements Callable<Integer> {

	public static final Logger LOGGER = Logger.getLogger(OSSUploader.class);

	// 外层线程池
	public static ExecutorService uploadMainPool = null;

	static {
		uploadMainPool = Executors.newFixedThreadPool(OSSConstant.CONCURRENT_FILE_NUMBER, new ThreadFactory() {
			public Thread newThread(Runnable r) {
				Thread s = Executors.defaultThreadFactory().newThread(r);
				s.setDaemon(true);
				return s;
			}
		});
	}

	// 内层线程池
	private ExecutorService pool;
	private File uploadFile;// 上次文件
	private String bucketName;// bucketName
	private String key;// 云端存储路径

	/**
	 * oss上传 支持断点续传
	 * 
	 * @param sourceFilePath
	 *            源文件路径
	 * @param bucketName
	 *            bucketName
	 * @param key
	 *            存储key -在oss的存储路径
	 */
	public OSSUploader(File sourceFile, String bucketName, String key) {
		// 实例化单文件上次线程池
		pool = Executors.newFixedThreadPool(OSSConstant.SINGLE_FILE_CONCURRENT_THREADS);
		this.uploadFile = sourceFile;
		this.bucketName = bucketName;
		this.key = key;
	}

	/**
	 * 执行当前线程
	 * 
	 * @return
	 */
	@SuppressWarnings("finally")
	public Integer uploadFile() {
		Integer r = GlobalCode.ERROR;
		// 向uploadMainPool中submit当前线程
		Future<Integer> result = uploadMainPool.submit(this);
		try {
			r = result.get();
		} catch (InterruptedException | ExecutionException e) {
			e.printStackTrace();
		} finally {
			return r;
		}
	}

	/**
	 * oss上传
	 * 
	 * @param sourcePath
	 *            源文件路径
	 * @param bucketName
	 *            bucketName
	 * @param key
	 *            存储key 存储路径
	 * @return Integer
	 */
	@Override
	public Integer call() {
		OSSClient client = OSSFactory.getClientInstance();
		if (null==uploadFile || !uploadFile.exists()) {
			LOGGER.info("无法找到文件：" + uploadFile);
			return GlobalCode.FILE_NOT_FOUND_ERROR;
		}
		int result = GlobalCode.ERROR;

		key = key.contains("\\\\") ? key.replaceAll("\\\\", "/") : (key.contains("\\") ? key.replaceAll("\\", "/") : key);
		// 准备Bucket
		result = OSSFactory.getBucketName(bucketName);
		if (result == GlobalCode.ERROR)
			return result;
		// 使用multipart的方式上传文件
		result = uploadBigFile(client, bucketName, key, uploadFile);
		pool = null;
		return result;
	}

	// 通过Multipart的方式上传一个大文件
	private int uploadBigFile(OSSClient client, String bucketName, String key, File uploadFile) {

		// 自定义的每个上传分块大小
		long partSize = OSSConstant.UPLOAD_PART_SIZE;

		// 需要上传的文件分块数
		int partCount = calPartCount(uploadFile, partSize);

		// 文件的MD5值
		String fileDM5Str = "";
		String uploadId = "";

		// 序列化的文件路径（与上传文件同路径使用.up.temp后缀）
		String serializationFilePath = uploadFile.getPath() + ".up.temp";

		boolean isSerializationFile = false;

		// 子线程池的线程对象封装类（用于序列化的）
		PartUploadObj uploadPartObj = null;
		// 获取文件MD5值
		fileDM5Str = Md5Utils.getFileMD5String(uploadFile);

		// 若存在上传失败留下的序列化文件则反序列化对象
		if (new File(serializationFilePath).exists()) {
			uploadPartObj = (PartUploadObj) FileSerializableUtil.deserialize(serializationFilePath);
			isSerializationFile = true;
		}

		// 序列化文件不存在，分配分块给子线程池线程对象
		if (uploadPartObj == null || !isSerializationFile) {
			uploadPartObj = new PartUploadObj();
			try {
				// 初始化MultipartUpload 返回uploadId
				uploadId = initMultipartUpload(client, bucketName, key, fileDM5Str);
			} catch (OSSException | ClientException e) {
				e.printStackTrace();
				return GlobalCode.OSS_SUBMIT_ERROR;
			}
			for (int i = 0; i < partCount; i++) {
				long start = partSize * i;
				long curPartSize = partSize < uploadFile.length() - start ? partSize : uploadFile.length() - start;
				// 构造上传线程，UploadPartThread是执行每个分块上传任务的线程
				uploadPartObj.getUploadPartThreads().add(new PartUploadCallable(client, bucketName, key, uploadFile,
						uploadId, i + 1, partSize * i, curPartSize));
			}
		}

		try {
			int i = 0;
			// upload方法提交分块上传线程至子线程池上传，while循环用于上传失败重复上传，OSSConstant.RETRY定义重复次数
			while (upload(uploadPartObj, serializationFilePath).isResult() == false) {
				if (++i == OSSConstant.RETRY)
					break;
			}
		} catch (Exception e) {
			e.printStackTrace();
			LOGGER.info("==" + e.getMessage());
			return GlobalCode.THREAD_ERROR;
		}

		if (!uploadPartObj.isResult()) {
			return GlobalCode.NETWORK_ERROR;
		}
		try {
			// 完成一个multi-part请求。
			completeMultipartUpload(client, bucketName, key, uploadPartObj);
		} catch (Exception e) {
			e.printStackTrace();
			FileSerializableUtil.serialize(uploadPartObj, serializationFilePath);
			return GlobalCode.OSS_SUBMIT_ERROR;
		}
		return GlobalCode.SUCCESS;
	}

	/**
	 * 多线程上传单个文件
	 * 
	 * @param uploadPartObj
	 * @param serializationFilePath
	 * @return
	 */
	private PartUploadObj upload(PartUploadObj uploadPartObj, String serializationFilePath) {
		try {

			uploadPartObj.setResult(true);

			// 向子线程池中submit单个文件所有分块上传线程
			for (int i = 0; i < uploadPartObj.getUploadPartThreads().size(); i++) {
				if (uploadPartObj.getUploadPartThreads().get(i).getFxPartETag() == null)
					pool.submit(uploadPartObj.getUploadPartThreads().get(i));
			}

			// shutdown子线程池，池内所上传任务执行结束后停止当前线程池
			pool.shutdown();
			while (!pool.isTerminated()) {
				// 循环检查线程池，同时在此序列化uploadPartObj
				FileSerializableUtil.serialize(uploadPartObj, serializationFilePath);
				pool.awaitTermination(OSSConstant.SERIALIZATION_TIME, TimeUnit.SECONDS);
			}

			// 判断上传结果
			for (PartUploadCallable uploadPartThread : uploadPartObj.getUploadPartThreads()) {
				if (uploadPartThread.getFxPartETag() == null)
					uploadPartObj.setResult(false);
			}
			// 上传成功 删除序列化文件
			if (uploadPartObj.isResult() == true)
				FileSerializableUtil.delSerlzFile(serializationFilePath);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return uploadPartObj;

	}

	// 根据文件的大小和每个Part的大小计算需要划分的Part个数。
	private static int calPartCount(File f, long partSize) {
		int partCount = (int) (f.length() / partSize);
		if (f.length() % partSize != 0) {
			partCount++;
		}
		return partCount;
	}

	// 初始化一个Multi-part upload请求。
	private static String initMultipartUpload(OSSClient client, String bucketName, String key, String fileDM5Str)
			throws OSSException, ClientException {
		ObjectMetadata objectMetadata = new ObjectMetadata();
		objectMetadata.getUserMetadata().put(OSSConstant.X_OSS_META_MY_MD5, fileDM5Str);
		InitiateMultipartUploadRequest initUploadRequest = new InitiateMultipartUploadRequest(bucketName, key,
				objectMetadata);
		InitiateMultipartUploadResult initResult = client.initiateMultipartUpload(initUploadRequest);
		String uploadId = initResult.getUploadId();
		return uploadId;
	}

	// 完成一个multi-part请求。
	private static void completeMultipartUpload(OSSClient client, String bucketName, String key,
			PartUploadObj uploadPartObj) {
		List<PartETag> eTags = new ArrayList<PartETag>();
		for (PartUploadCallable uploadPartThread : uploadPartObj.getUploadPartThreads()) {
			eTags.add(new PartETag(uploadPartThread.getFxPartETag().getPartNumber(),
					uploadPartThread.getFxPartETag().geteTag()));
		}
		// 为part按partnumber排序
		Collections.sort(eTags, new Comparator<PartETag>() {
			public int compare(PartETag arg0, PartETag arg1) {
				PartETag part1 = arg0;
				PartETag part2 = arg1;
				return part1.getPartNumber() - part2.getPartNumber();
			}
		});
		CompleteMultipartUploadRequest completeMultipartUploadRequest = new CompleteMultipartUploadRequest(bucketName,
				key, uploadPartObj.getUploadPartThreads().get(0).getUploadId(), eTags);
		client.completeMultipartUpload(completeMultipartUploadRequest);
	}
}
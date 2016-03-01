package com.galaxyinternet.framework.core.file;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.RandomAccessFile;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Properties;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.aliyun.oss.ClientException;
import com.aliyun.oss.OSSClient;
import com.aliyun.oss.OSSException;
import com.aliyun.oss.model.CompleteMultipartUploadRequest;
import com.aliyun.oss.model.DeleteObjectsRequest;
import com.aliyun.oss.model.DeleteObjectsResult;
import com.aliyun.oss.model.GetObjectRequest;
import com.aliyun.oss.model.InitiateMultipartUploadRequest;
import com.aliyun.oss.model.InitiateMultipartUploadResult;
import com.aliyun.oss.model.ListPartsRequest;
import com.aliyun.oss.model.OSSObject;
import com.aliyun.oss.model.ObjectMetadata;
import com.aliyun.oss.model.PartETag;
import com.aliyun.oss.model.PartListing;
import com.aliyun.oss.model.PartSummary;
import com.aliyun.oss.model.PutObjectResult;
import com.aliyun.oss.model.UploadPartRequest;
import com.aliyun.oss.model.UploadPartResult;
import com.galaxyinternet.framework.core.id.IdGenerator;
import com.galaxyinternet.framework.core.model.Result;
import com.galaxyinternet.framework.core.model.Result.Status;
import com.galaxyinternet.framework.core.utils.GSONUtil;
import com.galaxyinternet.framework.core.utils.PropertiesUtils;

/**
 * 基于oss的文件上传及下载
 */
public class OSSHelper {
	private static Logger logger = LoggerFactory.getLogger(OSSHelper.class);
	private static String endpoint;
	private static String accessKeyId;
	private static String accessKeySecret;
	private static ExecutorService executorService = Executors.newFixedThreadPool(5);
	private static AtomicInteger completedBlocks = new AtomicInteger(0);
	private static List<PartETag> partETags = Collections.synchronizedList(new ArrayList<PartETag>());
	private static OSSClient client;

	static {
		Properties property = PropertiesUtils.getProperties("oss-config.properties");
		endpoint = property.getProperty("oss.service.endpoint");
		accessKeyId = property.getProperty("oss.access.key.id");
		accessKeySecret = property.getProperty("oss.access.key.secret");
		client = new OSSClient(endpoint, accessKeyId, accessKeySecret);
	}

	public static void main(String[] args) throws IOException {
		try {
			String key = "3948241456726026";
			key = String.valueOf(IdGenerator.generateId(OSSHelper.class));
			System.out.println("key=" + key);
			// createBucketName();
			// testUpload();
			// testDelete();
			testDownload1();
			// testDeleteMultiple();
			System.out.println(endpoint);
		} catch (Exception oe) {
			oe.printStackTrace();
		}
	}

	@SuppressWarnings("unused")
	private static void testDownload2() throws Exception {
		String key = "3948241456726026";
		DownloadFileResult result = simpleDownloadByOSS(BucketName.DEV.getName(), key);
		System.out.println(GSONUtil.toJson(result));
		InputStream inputStream = result.getInput();
		FileOutputStream outStream = new FileOutputStream("D:\\temp\\aaa.png");
		// 通过available方法取得流的最大字符数

		byte[] inOutb = new byte[inputStream.available()];
		inputStream.read(inOutb); // 读入流,保存在byte数组
		outStream.write(inOutb); // 写出流,保存在文件中
		inputStream.close();
		outStream.close();
	}

	@SuppressWarnings("unused")
	private static void testDownload1() throws Exception {
		String key = "3948241456726026";
		DownloadFileResult result = simpleDownloadByOSS(new File("D:\\temp\\aaa.png"), BucketName.DEV.getName(), key);
		System.out.println(GSONUtil.toJson(result));
	}

	@SuppressWarnings("unused")
	private static void testUpload() throws Exception {
		String key = "3948241456726026";
		simpleUploadByOSS(new File("C:\\Users\\Administrator\\Desktop\\test.png"), BucketName.DEV.getName(), key);
	}

	private static void testDelete() {
		String key = "3948241456726026";
		FileResult result = deleteFile(BucketName.DEV.getName(), key);
		System.err.println(GSONUtil.toJson(result));
	}

	private static void testDeleteMultiple() {
		String key = "3948241456726026";
		List<String> keys = new ArrayList<String>();
		keys.add("3947903009947652");
		keys.add("3947951227666434");
		FileResult result = deleteMultipleFiles("b90e0d40-dede-11e5-8413-1be541b8872b", keys);
		// System.err.println(GSONUtil.toJson(result));
	}

	public static String getBucketName(String bucketName) {
		if (StringUtils.isBlank(bucketName) || !client.doesBucketExist(bucketName)) {
			logger.error("Bucket name does not exist or is empty.");
			// client.createBucket(bucketName);
		}
		// client.setBucketAcl(bucketName, CannedAccessControlList.PublicRead);
		return bucketName;
	}

	/**
	 * 此方法在项目使用时只执行一次
	 */
	@SuppressWarnings("unused")
	private static void createBucketName() {
		String devBucketName = BucketName.DEV.getName();
		String testBucketName = BucketName.TEST.getName();
		String proBucketName = BucketName.PRODUCT.getName();
		OSSClient client = new OSSClient(endpoint, accessKeyId, accessKeySecret);
		client.createBucket(devBucketName);
		client.createBucket(testBucketName);
		client.createBucket(proBucketName);
		devBucketName = getBucketName(devBucketName);
		testBucketName = getBucketName(testBucketName);
		proBucketName = getBucketName(proBucketName);
		client.shutdown();
	}

	/**
	 * 删除单个文件
	 * 
	 * @param bucketName
	 *            oss中存储文件的队列
	 * @param key
	 *            文件的唯一标示
	 * @return 删除单个文件的结果
	 */
	public static FileResult deleteFile(String bucketName, String key) {
		FileResult fileResult = new FileResult();
		Result result = new Result();
		try {
			client.deleteObject(bucketName, key);
			result.addOK("删除成功");
		} catch (OSSException oe) {
			result.setStatus(Status.ERROR);
			result.setMessage(oe.getErrorCode() + ":" + oe.getErrorMessage());
			logger.error(
					"Caught an OSSException, which means your request made it to OSS, "
							+ "but was rejected with an error response for some reason.",
					"Error Message: " + oe.getErrorMessage(), "Error Code:       " + oe.getErrorCode(),
					"Request ID:      " + oe.getRequestId(), "Host ID:           " + oe.getHostId());
		} catch (ClientException ce) {
			result.setStatus(Status.ERROR);
			result.setMessage(ce.getErrorCode() + ":" + ce.getErrorMessage());
			logger.error("Caught an ClientException, which means the client encountered "
					+ "a serious internal problem while trying to communicate with OSS, "
					+ "such as not being able to access the network.", "Error Message: " + ce.getMessage());
		}
		fileResult.setResult(result);
		return fileResult;
	}

	/**
	 * 批量删除文件
	 * 
	 * @param bucketName
	 *            存储文件的队列
	 * @param keys
	 *            文件的唯一标示
	 * @return 批量删除文件的结果
	 */
	public static DeleteFileResult deleteMultipleFiles(String bucketName, List<String> keys) {
		DeleteFileResult fileResult = new DeleteFileResult();
		Result result = new Result();
		try {
			DeleteObjectsResult deleteObjectsResult = client
					.deleteObjects(new DeleteObjectsRequest(bucketName).withKeys(keys));
			List<String> keysOfDeletedObjects = deleteObjectsResult.getDeletedObjects();
			fileResult.setKeysOfDeletedObjects(keysOfDeletedObjects);
			result.addOK("批量删除成功");
		} catch (OSSException oe) {
			result.setStatus(Status.ERROR);
			result.setMessage(oe.getErrorCode() + ":" + oe.getErrorMessage());
			logger.error(
					"Caught an OSSException, which means your request made it to OSS, "
							+ "but was rejected with an error response for some reason.",
					"Error Message: " + oe.getErrorMessage(), "Error Code:       " + oe.getErrorCode(),
					"Request ID:      " + oe.getRequestId(), "Host ID:           " + oe.getHostId());
		} catch (ClientException ce) {
			result.setStatus(Status.ERROR);
			result.setMessage(ce.getErrorCode() + ":" + ce.getErrorMessage());
			logger.error("Caught an ClientException, which means the client encountered "
					+ "a serious internal problem while trying to communicate with OSS, "
					+ "such as not being able to access the network.", "Error Message: " + ce.getMessage());
		}
		fileResult.setResult(result);
		return fileResult;
	}

	/**
	 * 简单的文件上传
	 * 
	 * @param file
	 *            上传的文件
	 * @param bucketName
	 *            oss中存储文件的队列
	 * @param key
	 *            文件的唯一标示
	 */
	public static UploadFileResult simpleUploadByOSS(File file, String bucketName, String key) {
		UploadFileResult responseFile = new UploadFileResult();
		Result result = new Result();
		OSSClient client = new OSSClient(endpoint, accessKeyId, accessKeySecret);
		bucketName = getBucketName(bucketName);
		if (StringUtils.isBlank(bucketName)) {
			result.setStatus(Status.ERROR);
			result.setMessage("Bucket name does not exist or is empty.");
			responseFile.setResult(result);
			return responseFile;
		}
		if (file.exists()) {
			try {
				PutObjectResult putObjectResult = client.putObject(bucketName, key, file);
				responseFile.setEtag(putObjectResult.getETag());
				result.addOK("上传成功");
			} catch (OSSException oe) {
				result.setStatus(Status.ERROR);
				result.setMessage(oe.getErrorCode() + ":" + oe.getErrorMessage());
				logger.error(
						"Caught an OSSException, which means your request made it to OSS, "
								+ "but was rejected with an error response for some reason.",
						"Error Message: " + oe.getErrorMessage(), "Error Code:       " + oe.getErrorCode(),
						"Request ID:      " + oe.getRequestId(), "Host ID:           " + oe.getHostId());
			} catch (ClientException ce) {
				result.setStatus(Status.ERROR);
				result.setMessage(ce.getErrorCode() + ":" + ce.getErrorMessage());
				logger.error("Caught an ClientException, which means the client encountered "
						+ "a serious internal problem while trying to communicate with OSS, "
						+ "such as not being able to access the network.", "Error Message: " + ce.getMessage());
			}
			responseFile.setBucketName(bucketName);
			responseFile.setFileKey(key);
			responseFile.setContentLength(file.length());
			responseFile.setResult(result);
		} else {
			logger.warn("file does not exist");
		}
		return responseFile;
	}

	/**
	 * 简单的文件下传
	 * 
	 * @param bucketName
	 *            oss中存储文件的队列
	 * @param key
	 *            文件的唯一标示
	 */
	public static DownloadFileResult simpleDownloadByOSS(String bucketName, String key) {
		DownloadFileResult responseFile = new DownloadFileResult();
		Result result = new Result();
		OSSClient client = new OSSClient(endpoint, accessKeyId, accessKeySecret);
		bucketName = getBucketName(bucketName);
		if (StringUtils.isBlank(bucketName)) {
			result.setStatus(Status.ERROR);
			result.setMessage("Bucket name does not exist or is empty.");
			responseFile.setResult(result);
			return responseFile;
		}
		try {
			OSSObject ossobjcet = client.getObject(new GetObjectRequest(bucketName, key));
			responseFile.setInput(ossobjcet.getObjectContent());
			result.addOK("下载成功");
		} catch (OSSException oe) {
			result.setStatus(Status.ERROR);
			result.setMessage(oe.getErrorCode() + ":" + oe.getErrorMessage());
			logger.error(
					"Caught an OSSException, which means your request made it to OSS, "
							+ "but was rejected with an error response for some reason.",
					"Error Message: " + oe.getErrorMessage(), "Error Code:       " + oe.getErrorCode(),
					"Request ID:      " + oe.getRequestId(), "Host ID:           " + oe.getHostId());
		} catch (ClientException ce) {
			result.setStatus(Status.ERROR);
			result.setMessage(ce.getErrorCode() + ":" + ce.getErrorMessage());
			logger.error("Caught an ClientException, which means the client encountered "
					+ "a serious internal problem while trying to communicate with OSS, "
					+ "such as not being able to access the network.", "Error Message: " + ce.getMessage());
		}
		responseFile.setResult(result);
		return responseFile;
	}

	/**
	 * 简单的文件下载
	 * 
	 * @param tmpFile
	 *            下载文件的载体(需要先创建该空文件，下载成功后会覆盖此空文件)
	 * @param bucketName
	 *            oss中存储文件的队列
	 * @param key
	 *            文件的唯一标示
	 */
	public static DownloadFileResult simpleDownloadByOSS(File tmpFile, String bucketName, String key) {
		DownloadFileResult responseFile = new DownloadFileResult();
		Result result = new Result();
		OSSClient client = new OSSClient(endpoint, accessKeyId, accessKeySecret);
		bucketName = getBucketName(bucketName);
		if (StringUtils.isBlank(bucketName)) {
			result.setStatus(Status.ERROR);
			result.setMessage("Bucket name does not exist or is empty.");
			responseFile.setResult(result);
			return responseFile;
		}
		try {
			ObjectMetadata metadata = client.getObject(new GetObjectRequest(bucketName, key), tmpFile);
			result.addOK("下载成功");
			System.err.println("metadata=" + metadata);
		} catch (OSSException oe) {
			result.setStatus(Status.ERROR);
			result.setMessage(oe.getErrorCode() + ":" + oe.getErrorMessage());
			logger.error(
					"Caught an OSSException, which means your request made it to OSS, "
							+ "but was rejected with an error response for some reason.",
					"Error Message: " + oe.getErrorMessage(), "Error Code:       " + oe.getErrorCode(),
					"Request ID:      " + oe.getRequestId(), "Host ID:           " + oe.getHostId());
		} catch (ClientException ce) {
			result.setStatus(Status.ERROR);
			result.setMessage(ce.getErrorCode() + ":" + ce.getErrorMessage());
			logger.error("Caught an ClientException, which means the client encountered "
					+ "a serious internal problem while trying to communicate with OSS, "
					+ "such as not being able to access the network.", "Error Message: " + ce.getMessage());
		}

		responseFile.setResult(result);
		return responseFile;
	}

	/*
	 * Get size of the object and pre-create a random access file to hold object
	 * data
	 */
	private static long calcuateObjectSize(OSSClient client, String localFilePath, String bucketName, String key) {
		ObjectMetadata metadata = client.getObjectMetadata(bucketName, key);
		System.err.println("metadata=" + GSONUtil.toJson(metadata));
		long objectSize = metadata.getContentLength();
		RandomAccessFile raf = null;
		try {
			raf = new RandomAccessFile(localFilePath, "rw");
			raf.setLength(objectSize);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (raf != null) {
				try {
					raf.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		return objectSize;
	}

	/**
	 * 并行下载文件
	 */
	public static void concurrentGetObjectByOSS(String localFilePath, String bucketName, String key) {
		OSSClient client = new OSSClient(endpoint, accessKeyId, accessKeySecret);
		try {

			long objectSize = calcuateObjectSize(client, localFilePath, bucketName, key);

			/*
			 * Calculate how many blocks to be divided
			 */
			final long blockSize = 5 * 1024 * 1024L; // 5MB
			int blockCount = (int) (objectSize / blockSize);
			if (objectSize % blockSize != 0) {
				blockCount++;
			}

			/*
			 * Download the object concurrently
			 */
			for (int i = 0; i < blockCount; i++) {
				long startPos = i * blockSize;
				long endPos = (i + 1 == blockCount) ? objectSize : (i + 1) * blockSize;
				executorService
						.execute(new BlockFetcher(client, startPos, endPos, i + 1, localFilePath, bucketName, key));
			}

			/*
			 * Waiting for all blocks finished
			 */
			executorService.shutdown();
			while (!executorService.isTerminated()) {
				try {
					executorService.awaitTermination(5, TimeUnit.SECONDS);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}

			/*
			 * Verify whether all blocks are finished
			 */
			if (completedBlocks.intValue() != blockCount) {
				throw new IllegalStateException("Download fails due to some blocks are not finished yet");
			} else {
				System.out.println("Succeed to download object " + key);
			}

		} catch (OSSException oe) {
			System.out.println("Caught an OSSException, which means your request made it to OSS, "
					+ "but was rejected with an error response for some reason.");
			System.out.println("Error Message: " + oe.getErrorCode());
			System.out.println("Error Code:       " + oe.getErrorCode());
			System.out.println("Request ID:      " + oe.getRequestId());
			System.out.println("Host ID:           " + oe.getHostId());
		} catch (ClientException ce) {
			System.out.println("Caught an ClientException, which means the client encountered "
					+ "a serious internal problem while trying to communicate with OSS, "
					+ "such as not being able to access the network.");
			System.out.println("Error Message: " + ce.getMessage());
		} finally {
			/*
			 * Do not forget to shut down the client finally to release all
			 * allocated resources.
			 */
			if (client != null) {
				// client.shutdown();
			}
		}
	}

	private static class BlockFetcher implements Runnable {

		private long startPos;
		private long endPos;

		private int blockNumber;
		private String localFilePath;
		private String key;
		private OSSClient client;
		private String bucketName;

		public BlockFetcher(OSSClient client, long startPos, long endPos, int blockNumber, String localFilePath,
				String bucketName, String key) {
			this.client = client;
			this.startPos = startPos;
			this.endPos = endPos;
			this.blockNumber = blockNumber;
			this.localFilePath = localFilePath;
			this.key = key;
			this.bucketName = bucketName;
		}

		public void run() {
			RandomAccessFile raf = null;
			try {
				raf = new RandomAccessFile(localFilePath, "rw");
				raf.seek(startPos);
				// OSSObject object = client.getObject(new
				// GetObjectRequest(bucketName, key).withRange(startPos,
				// endPos));
				OSSObject object = client.getObject(new GetObjectRequest(bucketName, key).withRange(startPos, endPos));
				InputStream objectContent = object.getObjectContent();
				try {
					byte[] buf = new byte[4096];
					int bytesRead = 0;
					while ((bytesRead = objectContent.read(buf)) != -1) {
						raf.write(buf, 0, bytesRead);
					}
					completedBlocks.incrementAndGet();
					System.out.println("Block#" + blockNumber + " done\n");
				} catch (IOException e) {
					e.printStackTrace();
				} finally {
					objectContent.close();
				}
			} catch (Exception e) {
				e.printStackTrace();
			} finally {
				if (raf != null) {
					try {
						raf.close();
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
			}
		}
	}

	/**
	 * 分片上传文件，应用场景： <br/>
	 * 1.需要支持断点上传。<br/>
	 * 2.上传超过100MB大小的文件。 <br/>
	 * 3.网络条件较差，和OSS的服务器之间的链接经常断开。<br/>
	 * 4.上传文件之前，无法确定上传文件的大小。
	 * 
	 */
	public static void multipartUploadByOSS(File file, String bucketName, String key, String localFilePath) {
		/*
		 * Constructs a client instance with your account for accessing OSS
		 */
		OSSClient client = new OSSClient(endpoint, accessKeyId, accessKeySecret);

		try {
			/*
			 * Claim a upload id firstly
			 */
			String uploadId = claimUploadId(bucketName, key, client);
			System.out.println("Claiming a new upload id " + uploadId + "\n");

			/*
			 * Calculate how many parts to be divided
			 */
			final long partSize = 5 * 1024 * 1024L; // 5MB
			long fileLength = file.length();
			int partCount = (int) (fileLength / partSize);
			if (fileLength % partSize != 0) {
				partCount++;
			}
			if (partCount > 10000) {
				throw new RuntimeException("Total parts count should not exceed 10000");
			} else {
				System.out.println("Total parts count " + partCount + "\n");
			}

			/*
			 * Upload multiparts to your bucket
			 */
			System.out.println("Begin to upload multiparts to OSS from a file\n");
			for (int i = 0; i < partCount; i++) {
				long startPos = i * partSize;
				long curPartSize = (i + 1 == partCount) ? (fileLength - startPos) : partSize;
				executorService.execute(
						new PartUploader(file, startPos, curPartSize, i + 1, uploadId, bucketName, key, client));
			}

			/*
			 * Waiting for all parts finished
			 */
			executorService.shutdown();
			while (!executorService.isTerminated()) {
				try {
					executorService.awaitTermination(5, TimeUnit.SECONDS);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}

			/*
			 * Verify whether all parts are finished
			 */
			if (partETags.size() != partCount) {
				throw new IllegalStateException("Upload multiparts fail due to some parts are not finished yet");
			} else {
				System.out.println("Succeed to complete multiparts into an object named " + key + "\n");
			}

			/*
			 * View all parts uploaded recently
			 */
			listAllParts(uploadId, bucketName, key, client);

			/*
			 * Complete to upload multiparts
			 */
			completeMultipartUpload(uploadId, bucketName, key, client);

			/*
			 * Fetch the object that newly created at the step below.
			 */
			System.out.println("Fetching an object");
			client.getObject(new GetObjectRequest(bucketName, key), new File(localFilePath));

		} catch (OSSException oe) {
			System.out.println("Caught an OSSException, which means your request made it to OSS, "
					+ "but was rejected with an error response for some reason.");
			System.out.println("Error Message: " + oe.getErrorCode());
			System.out.println("Error Code:       " + oe.getErrorCode());
			System.out.println("Request ID:      " + oe.getRequestId());
			System.out.println("Host ID:           " + oe.getHostId());
		} catch (ClientException ce) {
			System.out.println("Caught an ClientException, which means the client encountered "
					+ "a serious internal problem while trying to communicate with OSS, "
					+ "such as not being able to access the network.");
			System.out.println("Error Message: " + ce.getMessage());
		} finally {
			/*
			 * Do not forget to shut down the client finally to release all
			 * allocated resources.
			 */
			if (client != null) {
				client.shutdown();
			}
		}
	}

	private static class PartUploader implements Runnable {

		private File localFile;
		private long startPos;

		private long partSize;
		private int partNumber;
		private String uploadId;
		private String key;
		private OSSClient client;
		private String bucketName;

		public PartUploader(File localFile, long startPos, long partSize, int partNumber, String uploadId,
				String bucketName, String key, OSSClient client) {
			this.localFile = localFile;
			this.startPos = startPos;
			this.partSize = partSize;
			this.partNumber = partNumber;
			this.uploadId = uploadId;
			this.key = key;
			this.client = client;
			this.bucketName = bucketName;
		}

		public void run() {
			InputStream instream = null;
			try {
				instream = new FileInputStream(this.localFile);
				instream.skip(this.startPos);

				UploadPartRequest uploadPartRequest = new UploadPartRequest();
				uploadPartRequest.setBucketName(bucketName);
				uploadPartRequest.setKey(key);
				uploadPartRequest.setUploadId(this.uploadId);
				uploadPartRequest.setInputStream(instream);
				uploadPartRequest.setPartSize(this.partSize);
				uploadPartRequest.setPartNumber(this.partNumber);

				UploadPartResult uploadPartResult = client.uploadPart(uploadPartRequest);
				System.out.println("Part#" + this.partNumber + " done\n");
				synchronized (partETags) {
					partETags.add(uploadPartResult.getPartETag());
				}
			} catch (Exception e) {
				e.printStackTrace();
			} finally {
				if (instream != null) {
					try {
						instream.close();
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
			}
		}
	}

	private static String claimUploadId(String bucketName, String key, OSSClient client) {
		InitiateMultipartUploadRequest request = new InitiateMultipartUploadRequest(bucketName, key);
		InitiateMultipartUploadResult result = client.initiateMultipartUpload(request);
		return result.getUploadId();
	}

	private static void completeMultipartUpload(String uploadId, String bucketName, String key, OSSClient client) {
		// Make part numbers in ascending order
		Collections.sort(partETags, new Comparator<PartETag>() {
			public int compare(PartETag p1, PartETag p2) {
				return p1.getPartNumber() - p2.getPartNumber();
			}
		});

		System.out.println("Completing to upload multiparts\n");
		CompleteMultipartUploadRequest completeMultipartUploadRequest = new CompleteMultipartUploadRequest(bucketName,
				key, uploadId, partETags);
		client.completeMultipartUpload(completeMultipartUploadRequest);
	}

	private static void listAllParts(String uploadId, String bucketName, String key, OSSClient client) {
		System.out.println("Listing all parts......");
		ListPartsRequest listPartsRequest = new ListPartsRequest(bucketName, key, uploadId);
		PartListing partListing = client.listParts(listPartsRequest);

		int partCount = partListing.getParts().size();
		for (int i = 0; i < partCount; i++) {
			PartSummary partSummary = partListing.getParts().get(i);
			System.out.println("\tPart#" + partSummary.getPartNumber() + ", ETag=" + partSummary.getETag());
		}
		System.out.println();
	}
}

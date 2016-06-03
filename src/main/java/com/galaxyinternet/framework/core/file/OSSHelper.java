package com.galaxyinternet.framework.core.file;

import java.io.File;
import java.io.InputStream;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.aliyun.oss.ClientException;
import com.aliyun.oss.OSSClient;
import com.aliyun.oss.OSSException;
import com.aliyun.oss.internal.OSSHeaders;
import com.aliyun.oss.model.DeleteObjectsRequest;
import com.aliyun.oss.model.DeleteObjectsResult;
import com.aliyun.oss.model.GetObjectRequest;
import com.aliyun.oss.model.OSSObject;
import com.aliyun.oss.model.ObjectMetadata;
import com.aliyun.oss.model.PutObjectResult;
import com.galaxyinternet.framework.core.model.Result;
import com.galaxyinternet.framework.core.model.Result.Status;
import com.galaxyinternet.framework.core.oss.GlobalCode;
import com.galaxyinternet.framework.core.oss.OSSDownloader;
import com.galaxyinternet.framework.core.oss.OSSFactory;
import com.galaxyinternet.framework.core.oss.OSSUploader;

/**
 * 基于oss的文件上传及下载
 */
public class OSSHelper {
	private static Logger logger = LoggerFactory.getLogger(OSSHelper.class);
	private static OSSClient client = OSSFactory.getClientInstance();
	private static String defaultBucketName = OSSFactory.getDefaultBucketName();
	private static String UPLOAD_ERROR_MESSAGE="文件上传失败";
	private static String UPLOAD_OK_MESSAGE="文件上传成功";
	private static String DOWNLOAD_OK_MESSAGE="文件下载成功";
	private static String DOWNLOAD_ERROR_MESSAGE="文件下载失败";
	
	
	/**
	 * 删除单个文件
	 * 
	 * @param key
	 *            文件的唯一标示
	 * @return 删除单个文件的结果
	 */
	public static FileResult deleteFile(String key) {
		return deleteFile(defaultBucketName, key);
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
			result.addError(oe.getErrorMessage(), oe.getErrorCode());
			logger.error(
					"Caught an OSSException, which means your request made it to OSS, "
							+ "but was rejected with an error response for some reason.",
					"Error Message: " + oe.getErrorMessage(), "Error Code:       " + oe.getErrorCode(),
					"Request ID:      " + oe.getRequestId(), "Host ID:           " + oe.getHostId());
		} catch (ClientException ce) {
			result.addError(ce.getErrorMessage(), ce.getErrorCode());
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
	 * @param keys
	 *            文件的唯一标示
	 * @return 批量删除文件的结果
	 */
	public static DeleteFileResult deleteMultipleFiles(List<String> keys) {
		return deleteMultipleFiles(defaultBucketName, keys);
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
					.deleteObjects(new DeleteObjectsRequest(defaultBucketName).withKeys(keys));
			List<String> keysOfDeletedObjects = deleteObjectsResult.getDeletedObjects();
			fileResult.setKeysOfDeletedObjects(keysOfDeletedObjects);
			result.addOK("批量删除成功");
		} catch (OSSException oe) {
			result.addError(oe.getErrorMessage(), oe.getErrorCode());
			logger.error(
					"Caught an OSSException, which means your request made it to OSS, "
							+ "but was rejected with an error response for some reason.",
					"Error Message: " + oe.getErrorMessage(), "Error Code:       " + oe.getErrorCode(),
					"Request ID:      " + oe.getRequestId(), "Host ID:           " + oe.getHostId());
		} catch (ClientException ce) {
			result.addError(ce.getErrorMessage(), ce.getErrorCode());
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
	 * @param key
	 *            文件的唯一标示
	 */
	public static UploadFileResult simpleUploadByOSS(File file, String key) {
		return simpleUploadByOSS(file, defaultBucketName, key);
	}

	/**
	 * 
	 * @Description:简单的文件上传接口
	 * @param file
	 *            上传文件对象
	 * @param key
	 *            oss存储文件的唯一标示
	 * @param metadata
	 *            OSS中Object的元数据。
	 *            包含了用户自定义的元数据，也包含了OSS发送的标准HTTP头(如Content-Length, ETag等）。
	 * @return UploadFileResult 上传是否成功的结果对象
	 *
	 */
	public static UploadFileResult simpleUploadByOSS(File file, String key, ObjectMetadata metadata) {
		return simpleUploadByOSS(file, defaultBucketName, key, metadata);
	}

	public static UploadFileResult simpleUploadByOSS(InputStream inputStream, String key) {
		return simpleUploadByOSS(inputStream, defaultBucketName, key);
	}

	/**
	 * 
	 * @Description:简单的文件上传接口
	 * @param inputStream
	 *            上传文件的数据流对象
	 * @param key
	 *            oss存储文件的唯一标示
	 * @param metadata
	 *            OSS中Object的元数据。
	 *            包含了用户自定义的元数据，也包含了OSS发送的标准HTTP头(如Content-Length, ETag等）。
	 * @return UploadFileResult 上传是否成功的结果对象
	 *
	 */
	public static UploadFileResult simpleUploadByOSS(InputStream inputStream, String key, ObjectMetadata metadata) {
		return simpleUploadByOSS(inputStream, defaultBucketName, key);
	}

	/**
	 * 
	 * @Description:设置web端下载需要的请求头参数
	 * @param fileFullName
	 *            文件名带后缀
	 * @param fileSize
	 *            文件大小
	 * @return ObjectMetadata web端直连oss需要的参数设置
	 *
	 */
	public static ObjectMetadata setRequestHeader(String fileFullName, long fileSize) {
		ObjectMetadata objectMetadata = new ObjectMetadata();
		objectMetadata.setHeader(OSSHeaders.CONTENT_DISPOSITION, "attachment;filename=" + fileFullName);
		objectMetadata.setHeader(OSSHeaders.CONTENT_LENGTH, fileSize);
		return objectMetadata;
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
	public static UploadFileResult simpleUploadByOSS(File file, String bucketName, String key,
			ObjectMetadata metadata) {
		UploadFileResult responseFile = new UploadFileResult();
		Result result = new Result();
		int checkBucketName = OSSFactory.getBucketName(bucketName);
		if (checkBucketName == GlobalCode.ERROR) {
			result.setStatus(Status.ERROR);
			result.setMessage("Bucket name does not exist or is empty.");
			responseFile.setResult(result);
			return responseFile;
		}
		if (file.exists()) {
			try {
				PutObjectResult putObjectResult = null;
				if (OSSFactory.UPLOAD_MODE.equalsIgnoreCase(UploadModeType.LOCAL.getKey())) {
					putObjectResult = client.putObject(bucketName, key, file);
				} else {
					putObjectResult = client.putObject(bucketName, key, file, metadata);
				}
				responseFile.setEtag(putObjectResult.getETag());
				result.addOK("上传成功");
			} catch (OSSException oe) {
				result.addError(oe.getErrorMessage(), oe.getErrorCode());
				logger.error(
						"Caught an OSSException, which means your request made it to OSS, "
								+ "but was rejected with an error response for some reason.",
						"Error Message: " + oe.getErrorMessage(), "Error Code:       " + oe.getErrorCode(),
						"Request ID:      " + oe.getRequestId(), "Host ID:           " + oe.getHostId());
			} catch (ClientException ce) {
				result.addError(ce.getErrorMessage(), ce.getErrorCode());
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
		int checkBucketName = OSSFactory.getBucketName(bucketName);
		if (checkBucketName == GlobalCode.ERROR) {
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
				result.addError(oe.getErrorMessage(), oe.getErrorCode());
				logger.error(
						"Caught an OSSException, which means your request made it to OSS, "
								+ "but was rejected with an error response for some reason.",
						"Error Message: " + oe.getErrorMessage(), "Error Code:       " + oe.getErrorCode(),
						"Request ID:      " + oe.getRequestId(), "Host ID:           " + oe.getHostId());
			} catch (ClientException ce) {
				result.addError(ce.getErrorMessage(), ce.getErrorCode());
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

	public static UploadFileResult simpleUploadByOSS(InputStream inputStream, String bucketName, String key) {
		UploadFileResult responseFile = new UploadFileResult();
		Result result = new Result();
		int checkBucketName = OSSFactory.getBucketName(bucketName);
		if (checkBucketName == GlobalCode.ERROR) {
			result.setStatus(Status.ERROR);
			result.setMessage("Bucket name does not exist or is empty.");
			responseFile.setResult(result);
			return responseFile;
		}
		try {
			PutObjectResult putObjectResult = client.putObject(bucketName, key, inputStream);
			responseFile.setEtag(putObjectResult.getETag());
			result.addOK("上传成功");
		} catch (OSSException oe) {
			result.addError(oe.getErrorMessage(), oe.getErrorCode());
			logger.error(
					"Caught an OSSException, which means your request made it to OSS, "
							+ "but was rejected with an error response for some reason.",
					"Error Message: " + oe.getErrorMessage(), "Error Code:       " + oe.getErrorCode(),
					"Request ID:      " + oe.getRequestId(), "Host ID:           " + oe.getHostId());
		} catch (ClientException ce) {
			result.addError(ce.getErrorMessage(), ce.getErrorCode());
			logger.error("Caught an ClientException, which means the client encountered "
					+ "a serious internal problem while trying to communicate with OSS, "
					+ "such as not being able to access the network.", "Error Message: " + ce.getMessage());
		}
		responseFile.setBucketName(bucketName);
		responseFile.setFileKey(key);
		responseFile.setResult(result);
		return responseFile;
	}

	public static UploadFileResult simpleUploadByOSS(InputStream inputStream, String bucketName, String key,
			ObjectMetadata metadata) {
		UploadFileResult responseFile = new UploadFileResult();
		Result result = new Result();
		int checkBucketName = OSSFactory.getBucketName(bucketName);
		if (checkBucketName == GlobalCode.ERROR) {
			result.setStatus(Status.ERROR);
			result.setMessage("Bucket name does not exist or is empty.");
			responseFile.setResult(result);
			return responseFile;
		}
		try {
			PutObjectResult putObjectResult = null;
			if (OSSFactory.UPLOAD_MODE.equalsIgnoreCase(UploadModeType.LOCAL.getKey())) {
				putObjectResult = client.putObject(bucketName, key, inputStream);
			} else {
				putObjectResult = client.putObject(bucketName, key, inputStream, metadata);
			}
			responseFile.setEtag(putObjectResult.getETag());
			result.addOK("上传成功");
		} catch (OSSException oe) {
			result.addError(oe.getErrorMessage(), oe.getErrorCode());
			logger.error(
					"Caught an OSSException, which means your request made it to OSS, "
							+ "but was rejected with an error response for some reason.",
					"Error Message: " + oe.getErrorMessage(), "Error Code:       " + oe.getErrorCode(),
					"Request ID:      " + oe.getRequestId(), "Host ID:           " + oe.getHostId());
		} catch (ClientException ce) {
			result.addError(ce.getErrorMessage(), ce.getErrorCode());
			logger.error("Caught an ClientException, which means the client encountered "
					+ "a serious internal problem while trying to communicate with OSS, "
					+ "such as not being able to access the network.", "Error Message: " + ce.getMessage());
		}
		responseFile.setBucketName(bucketName);
		responseFile.setFileKey(key);
		responseFile.setResult(result);
		return responseFile;
	}

	/**
	 * 简单的文件下传
	 * 
	 * @param key
	 *            文件的唯一标示
	 */
	public static DownloadFileResult simpleDownloadByOSS(String key) {
		return simpleDownloadByOSS(defaultBucketName, key);
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
		int checkBucketName = OSSFactory.getBucketName(bucketName);
		if (checkBucketName == GlobalCode.ERROR) {
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
			result.addError(oe.getErrorMessage(), oe.getErrorCode());
			logger.error(
					"Caught an OSSException, which means your request made it to OSS, "
							+ "but was rejected with an error response for some reason.",
					"Error Message: " + oe.getErrorMessage(), "Error Code:       " + oe.getErrorCode(),
					"Request ID:      " + oe.getRequestId(), "Host ID:           " + oe.getHostId());
		} catch (ClientException ce) {
			result.addError(ce.getErrorMessage(), ce.getErrorCode());
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
	 * @param key
	 *            文件的唯一标示
	 */
	public static DownloadFileResult simpleDownloadByOSS(File tmpFile, String key) {
		return simpleDownloadByOSS(tmpFile, defaultBucketName, key);
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
		int checkBucketName = OSSFactory.getBucketName(bucketName);
		if (checkBucketName == GlobalCode.ERROR) {
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
			result.addError(oe.getErrorMessage(), oe.getErrorCode());
			logger.error(
					"Caught an OSSException, which means your request made it to OSS, "
							+ "but was rejected with an error response for some reason.",
					"Error Message: " + oe.getErrorMessage(), "Error Code:       " + oe.getErrorCode(),
					"Request ID:      " + oe.getRequestId(), "Host ID:           " + oe.getHostId());
		} catch (ClientException ce) {
			result.addError(ce.getErrorMessage(), ce.getErrorCode());
			logger.error("Caught an ClientException, which means the client encountered "
					+ "a serious internal problem while trying to communicate with OSS, "
					+ "such as not being able to access the network.", "Error Message: " + ce.getMessage());
		}

		responseFile.setResult(result);
		return responseFile;
	}

	/**
	 * 
	 * @Description:分片上传
	 * @param bucketName
	 *            文件存储的桶名称
	 * @param sourceFile
	 *            上传的文件
	 * @param key
	 *            文件的唯一key
	 * @return UploadFileResult 上传文件的结果
	 *
	 */
	public static UploadFileResult uploadWithBreakpoint(File sourceFile, String bucketName, String key) {
		int resultCode = new OSSUploader(sourceFile, bucketName, key).uploadFile();
		return populateResult(sourceFile.length(), bucketName, key, resultCode);
	}

	/**
	 * 
	 * @Description:分片上传
	 * @param sourceFile
	 *            上传的文件
	 * @param key
	 *            文件的唯一key
	 * @return UploadFileResult 上传文件的结果
	 *
	 */
	public static UploadFileResult uploadWithBreakpoint(File sourceFile, String key) {
		int resultCode = new OSSUploader(sourceFile, defaultBucketName, key).uploadFile();
		return populateResult(sourceFile.length(), null, key, resultCode);
	}

	/**
	 * 
	 * @Description:分片上传
	 * @param fileFullName
	 *            文件的全名，带后缀
	 * @param sourceFile
	 *            上传的文件
	 * @param key
	 *            文件的唯一key
	 * @return UploadFileResult 上传文件的结果
	 *
	 */
	public static UploadFileResult uploadWithBreakpoint(String fileFullName, File sourceFile, String key) {
		int resultCode = new OSSUploader(sourceFile, defaultBucketName, key, fileFullName).uploadFile();
		return populateResult(sourceFile.length(), null, key, resultCode);
	}

	/**
	 * 
	 * @Description:封装上传文件的结果
	 * @param fileSize
	 *            文件的大小
	 * @param bucketName
	 *            桶名称
	 * @param key
	 *            文件唯一key
	 * @param resultCode
	 *            1成功，其他均是失败
	 * @return UploadFileResult 上传文件的结果
	 *
	 */
	private static UploadFileResult populateResult(Long fileSize, String bucketName, String key, int resultCode) {
		UploadFileResult uploadFileResult = new UploadFileResult();
		if (StringUtils.isBlank(bucketName)) {
			bucketName = defaultBucketName;
		}
		uploadFileResult.setBucketName(bucketName);
		uploadFileResult.setFileKey(key);
		uploadFileResult.setContentLength(fileSize);
		Result result = new Result();
		if (resultCode == GlobalCode.SUCCESS) {
			logger.debug(UPLOAD_OK_MESSAGE);
			result.addOK(UPLOAD_OK_MESSAGE);
		} else {
			logger.error(UPLOAD_ERROR_MESSAGE);
			result.addError(UPLOAD_ERROR_MESSAGE);
		}
		result.setErrorCode(resultCode + "");
		uploadFileResult.setResult(result);
		return uploadFileResult;
	}
	
	/**
	 * 分片上传文件，应用场景： <br/>
	 * 1.需要支持断点上传。<br/>
	 * 2.上传超过100MB大小的文件。 <br/>
	 * 3.网络条件较差，和OSS的服务器之间的链接经常断开。<br/>
	 * 4.上传文件之前，无法确定上传文件的大小。
	 * 
	 */
	public static int uploadSupportBreakpoint(File sourceFile, String bucketName, String key) {
		int result = new OSSUploader(sourceFile, bucketName, key).uploadFile();
		if (result == GlobalCode.SUCCESS) {
			logger.debug(UPLOAD_OK_MESSAGE);
		} else {
			logger.error(UPLOAD_ERROR_MESSAGE);
		}
		return result;
	}

	public static int uploadSupportBreakpoint(File sourceFile, String key) {
		int result = new OSSUploader(sourceFile, defaultBucketName, key).uploadFile();
		if (result == GlobalCode.SUCCESS) {
			logger.debug(UPLOAD_OK_MESSAGE);
		} else {
			logger.error(UPLOAD_ERROR_MESSAGE);
		}
		return result;
	}

	public static int uploadSupportBreakpoint(String fileFullName, File sourceFile, String key) {
		int result = new OSSUploader(sourceFile, defaultBucketName, key, fileFullName).uploadFile();
		if (result == GlobalCode.SUCCESS) {
			logger.debug(UPLOAD_OK_MESSAGE);
		} else {
			logger.error(UPLOAD_ERROR_MESSAGE);
		}
		return result;
	}

	/**
	 * 多线程断点下载文件
	 * 
	 * @param localFilePath
	 * @param bucketName
	 * @param key
	 */
	public static void downloadSupportBreakpoint(String localFilePath, String bucketName, String key) {
		int result = new OSSDownloader(localFilePath, bucketName, key).downloadFile();
		if (result == GlobalCode.SUCCESS) {
			logger.debug(DOWNLOAD_OK_MESSAGE);
		} else {
			logger.error(DOWNLOAD_ERROR_MESSAGE);
		}
	}

	/**
	 * 多线程断点下载文件
	 * 
	 * @param localFilePath
	 * @param bucketName
	 * @param key
	 */
	public static void downloadSupportBreakpoint(String localFilePath, String key) {
		int result = new OSSDownloader(localFilePath, defaultBucketName, key).downloadFile();
		if (result == GlobalCode.SUCCESS) {
			logger.debug(DOWNLOAD_OK_MESSAGE);
		} else {
			logger.error(DOWNLOAD_ERROR_MESSAGE);
		}
	}
}

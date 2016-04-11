package com.galaxyinternet.framework.core.oss;

import java.util.Properties;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.aliyun.oss.OSSClient;
import com.aliyun.oss.model.CannedAccessControlList;
import com.galaxyinternet.framework.core.id.IdGenerator;
import com.galaxyinternet.framework.core.utils.PropertiesUtils;

/**
 * oss的工厂类，用于创建ossclient，bucketname，key等
 * 
 * @author keifer
 *
 */
public class OSSFactory {
	private static Logger logger = LoggerFactory.getLogger(OSSFactory.class);
	private static OSSClient ossClient = null;
	public final static String ENDPOINT;
	public final static String ACCESS_KEY_ID;
	public final static String accessKeySecret;
	private static String bucketName;
	public final static String UPLOAD_MODE;
	
	private OSSFactory() {
	}

	static {
		Properties property = PropertiesUtils.getProperties(OSSConstant.OSS_CONFIG_FILE);
		bucketName = property.getProperty(OSSConstant.OSS_BUCKET_NAME_KEY);
		ENDPOINT = property.getProperty(OSSConstant.OSS_ENDPOINT_KEY);
		ACCESS_KEY_ID = property.getProperty(OSSConstant.OSS_ACCESS_KEY);
		accessKeySecret = property.getProperty(OSSConstant.OSS_ACCESS_KEY_SECRET);
		ossClient = new OSSClient(ENDPOINT, ACCESS_KEY_ID, accessKeySecret);
		UPLOAD_MODE = property.getProperty(OSSConstant.OSS_UPLOAD_MODE_KEY);
	}

	/*public static OSSClient getInstance() {
		synchronized (ossClient) {
			if (ossClient == null) {
				// 可以使用ClientConfiguration对象设置代理服务器、最大重试次数等参数。
				// ClientConfiguration config = new ClientConfiguration();
				ossClient = new OSSClient(endpoint, accessKeyId, accessKeySecret);
			}
		}
		return ossClient;
	}*/
	
	public static OSSClient getClientInstance() {
		return ossClient;
	}

	/**
	 * 创建Bucket：如果存在直接返回，如果不存在则创建
	 * 
	 * @param bucketName
	 *            需要创建的bucket name
	 */
	public static int getBucketName(String bucketName) {
		if (StringUtils.isBlank(bucketName) || !ossClient.doesBucketExist(bucketName)) {
			logger.error("Bucket name does not exist or is empty.");
			try {
				ossClient.createBucket(bucketName);
				// 设置bucket的访问权限，public-read-write权限
				ossClient.setBucketAcl(bucketName, CannedAccessControlList.PublicRead);
			} catch (Exception e) {
				logger.error("==" + e.getMessage());
				return GlobalCode.ERROR;
			}
		}
		return GlobalCode.SUCCESS;
	}

	/**
	 * 获取默认的bucket name
	 */
	public static String getDefaultBucketName() {
		return bucketName;
	}

	/**
	 * 获取上传文件的唯一标示
	 * 
	 * @param clazz
	 *            当前类的类对象
	 * @return
	 */
	public static String getKey(Class<?> clazz) {
		return String.valueOf(IdGenerator.generateId(clazz));
	}
}
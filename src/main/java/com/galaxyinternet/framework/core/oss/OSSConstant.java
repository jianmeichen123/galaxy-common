package com.galaxyinternet.framework.core.oss;

public class OSSConstant {
	public final static long DOWNLOAD_PART_SIZE = 4 * 1024 * 1024L; // 每个下载Part的大小设置为4MB

	public final static long UPLOAD_PART_SIZE = 4 * 1024 * 1024L; // 每个上传Part的大小设置为4MB

	public final static int CONCURRENT_FILE_NUMBER = 10; // 并发文件数。

	public static int SINGLE_FILE_CONCURRENT_THREADS = Runtime.getRuntime().availableProcessors() * 2; // 单文件并发线程数。

	public static int RETRY = 2;// 失败重试次数

	public static int SERIALIZATION_TIME = 1;// 断点保存时间间隔(秒)
	
	public static final String X_OSS_META_MY_MD5 = "x-oss-meta-galaxy-md5";

	public final static String OSS_ENDPOINT_KEY = "oss.service.endpoint";

	public final static String OSS_ACCESS_KEY = "oss.access.key.id";

	public final static String OSS_ACCESS_KEY_SECRET = "oss.access.key.secret";

	public final static String OSS_BUCKET_NAME_KEY = "oss.bucket.name";

	public final static String OSS_CONFIG_FILE = "oss-config.properties";

	public final static String GALAXYINTERNET_FX_ENDPOINT = "galaxyinternet_fx_endpoint";
}

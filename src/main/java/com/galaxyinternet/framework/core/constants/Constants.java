package com.galaxyinternet.framework.core.constants;

public class Constants {
	// 注册时候与ssoserver通讯时加密解密的密钥
	public final static String SECRET_KEY = "2B4eWxHdTaWPRGnN";

	public final static String SESSION_USER_KEY = "galax_session_user";
	public static final String _session_resource_mark_key_ = "_session_resource_mark_key_";

	public final static String SESSION_ID_KEY = "sessionId";

	public final static String REQUEST_HEADER_USER_ID_KEY = "guserId";

	public final static String SESSION_PROJECT_CODE = "project_code";

	public final static String SESSOPM_SID_KEY = "sid";

	public final static String REQUEST_URL_USER_ID_KEY = "guid";

	public final static String LOGIN_TOLOGIN = "/galaxy/userlogin/toLogin";
	public final static String SOP_REDIRECT = "/galaxy/index";

	public final static String LOGIN_CHECKLOGIN = "/galaxy/userlogin/login";

	public final static String IS_UP_EMPTY = "0";

	public final static String IS_UP_WRONG = "1";

	public final static String IS_SESSIONID_EMPTY = "2";

	public final static String IS_SESSIONID_EXPIRED = "3";

	public final static String OPTION_SUCCESS = "4";

	public final static String ACCESS_REJECTED = "5";

	public final static String REQUEST_PARAMS_INCOMPLETE = "6";// 请求参数不完整

	public final static String USER_DISABLE = "7"; // 用户被禁用

	public final static String INVALID_SESSIONID = "8"; // sessionId不正确

	public final static int DEFAULT_PAGE_SIZE = 15;

	// mail相关
	public final static String MAIL_CONFIG_FILE = "mail.properties";
	public final static String MAIL_HOST_KEY = "mail.host";
	public final static String MAIL_ADDRESS_KEY = "mail.address";
	public final static String MAIL_USERNAME_KEY = "mail.username";
	public final static String MAIL_PASSWORD_KEY = "mail.password";
	public final static String MAIL_SMTP_AUTH_KEY = "mail.smtp.auth";
	public final static String MAIL_SMTP_TIMEOUT_KEY = "mail.smtp.timeout";
	public final static String MAIL_SMTP_PORT_KEY = "mail.smtp.port";

	public final static String HEADER_REFERER_KEY = "galaxy.header.referer";
	public final static String SAFE_CONFIG_FILE = "core-config.properties";

	public final static String TOKEN_REMOVE_KEY = "token_remove";
	public final static String REDIS_CACHE_BEAN_NAME = "cache";
	public final static String REQUEST_SCOPE_TOKEN_KEY = "galax_requestscope_token_id";
	public final static int TOKEN_IN_REDIS_TIMEOUT_SECONDS = 60 * 60;// 1小时

	public final static String REDIS_SHARDINFO_NAME = "jedis.shardInfo";
	// 邮箱后缀名
	public static final String MAIL_SUFFIX = "@galaxyinternet.com";

	public static final String MAIL_TEMPLATE_FILE = "mail-template.properties";
	// 催办模板
	public static final String MAIL_URGE_CONTENT = "mail.urge.content";

	// 催办模板
	public static final String MAIL_URGE_CONTENT_SPECIAL = "mail.urge.content.special";
	// 重置密码模板
	public static final String MAIL_RESTPWD_CONTENT = "mail.restPwd.content";
	public static final String MAIL_INITIALPWD_CONTENT = "mail.initialPwd.content";
	// 共享文件模板
	public static final String MAIL_FILESHARE_CONTENT = "mail.fileShare.content";
	
	// 排期池发送邮件
	public static final String MAIL_PQC_CONTENT = "mail.urge.content.pjc";
	
	// 排期池发送邮件-取消会议
	public static final String MAIL_PQC_CONTENT_CANCLE = "mail.urge.content.pjc.cancle";

	public static final String REQUEST_TERMINAL_MARK = "gt";// galaxinternet  terminal

	public static final String AJAX_REQUEST_CORE_OBJECT_NAME = "XMLHttpRequest";

	public static final String REQUEST_HEADER_MARK = "X-Requested-With";

	public static final String EXCLUDE_REQUEST_URL = "excludedUrl";

	public static final String INCLUED_REQUEST_URL = "incluedUrl";
	
	public static final String INCLUED_ALL_REQUEST_URL = "*";

	public static final String DECRYP_ENCRYPTION_MARK = "decrypEncryption";

}

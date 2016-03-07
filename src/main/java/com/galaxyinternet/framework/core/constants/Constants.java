package com.galaxyinternet.framework.core.constants;

public class Constants {
	// 注册时候与ssoserver通讯时加密解密的密钥
	public final static String SECRET_KEY = "2B4eWxHdTaWPRGnN";

	public final static String SESSION_USER_KEY = "galax_session_user";

	public final static String SESSION_ID_KEY = "sessionId";
	
	public final static String SESSION_PROJECT_CODE = "project_code";

	public final static String SESSOPM_SID_KEY = "sid";

	public final static String LOGIN_TOLOGIN = "/galaxy/userlogin/toLogin";

	public final static String LOGIN_CHECKLOGIN = "/galaxy/userlogin/login";

	public final static String  IS_UP_EMPTY= "0";
	
	public final static String  IS_UP_WRONG= "1";
	
	public final static String  IS_SESSIONID_EMPTY= "2";
	
	public final static String  IS_SESSIONID_EXPIRED= "3";
	
	public final static String  OPTION_SUCCESS= "4";
	
	public final static String  ACCESS_REJECTED= "5";
	
	public final static int  DEFAULT_PAGE_SIZE= 15;
	
	//mail相关
	public final static String MAIL_CONFIG_FILE = "mail.properties";
	public final static String MAIL_HOST_KEY = "mail.host";
	public final static String MAIL_ADDRESS_KEY = "mail.address";
	public final static String MAIL_USERNAME_KEY = "mail.username";
	public final static String MAIL_PASSWORD_KEY = "mail.password";
	public final static String MAIL_SMTP_AUTH_KEY = "mail.smtp.auth";
	public final static String MAIL_SMTP_TIMEOUT_KEY = "mail.smtp.timeout";
	public final static String MAIL_SMTP_PORT_KEY= "mail.smtp.port";
	
	public final static String HEADER_REFERER_KEY= "galaxy.header.referer";
	public final static String SAFE_CONFIG_FILE = "safe-config.properties";
}

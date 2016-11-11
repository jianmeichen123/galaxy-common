package com.galaxyinternet.framework.core.utils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ValidateUtils {

	public static final String MOBILE = "^((13[0-9])|(15[0-9])|(18[0-9])|(17[0-9])|(147))\\d{8}$";
	
	public static final String EMAIl = "\\w+([-+.]\\w+)*@\\w+([-.]\\w+)*\\.\\w+([-.]\\w+)*";
	
	public static final String PASSWORD = "^[0-9a-zA-Z]{6,}$";
	
	public static final String NICKNAME = "^[0-9a-zA-Z_-]{6,}$";
	
	public static final String ENV_NAME = "^[A-Za-y0-9\\u4e00-\\u9fa5]{5,}$";
	
	
	public static final String CODE_NAME = "^[0-9a-zA-Z_-]{5,}$";
	
	
	//验证手机号
	public static boolean matchesMobile(String mobile){
		Pattern p = Pattern.compile(MOBILE);
		Matcher m = p.matcher(mobile);
		return m.matches();
	}
	//验证邮箱
	public static boolean matchesEmail(String email){
		Pattern p = Pattern.compile(EMAIl);
		Matcher m = p.matcher(email);
		return m.matches();
	}
	//验证密码
	public static boolean matchesPassword(String password){
		Pattern p = Pattern.compile(PASSWORD);
		Matcher m = p.matcher(password);
		return m.matches();
	}
	//验证账号
	public static boolean matchesNickName(String nickName){
		Pattern p = Pattern.compile(NICKNAME);
		Matcher m = p.matcher(nickName);
		return m.matches();
	}
	
	public static boolean matchesEnvName(String envName){
		Pattern p = Pattern.compile(ENV_NAME);
		Matcher m = p.matcher(envName);
		return m.matches();
	}
	
	public static boolean matchesCodeName(String codeName){
		Pattern p = Pattern.compile(CODE_NAME);
		Matcher m = p.matcher(codeName);
		return m.matches();
	}
}

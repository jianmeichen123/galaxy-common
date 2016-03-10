package com.galaxyinternet.framework.core.form;

import java.security.MessageDigest;
import java.security.SecureRandom;

import com.galaxyinternet.framework.core.id.IdGenerator;
import com.galaxyinternet.framework.core.utils.EncodeUtils;

/**
 * @Description: Token的生成器
 * @author keifer
 * @date 2016年3月8日
 */
public class TokenGenerator {

	private TokenGenerator() {
	}

	private static final TokenGenerator instance = new TokenGenerator();
	SecureRandom ran = new SecureRandom();

	public static TokenGenerator getInstance() {
		return instance;
	}

	public String generateToken() {
		String token = System.currentTimeMillis() + ran.nextInt() + "";
		try {
			MessageDigest md5 = MessageDigest.getInstance("md5");
			byte[] md = md5.digest(token.getBytes());
			return EncodeUtils.encodeBase64(md);
		} catch (Exception e) {
			return IdGenerator.generateId(TokenGenerator.class).toString();
		}
	}
}

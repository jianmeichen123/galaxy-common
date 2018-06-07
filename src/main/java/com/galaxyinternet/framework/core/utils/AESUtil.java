package com.galaxyinternet.framework.core.utils;

import java.security.SecureRandom;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

/**
 * AES 加密解密工具<br/>
 * 数据传输使用AES对称加密
 */
public class AESUtil {

	/**
	 * 加密密钥
	 */
	static String defaultPrivatePassword = "01239&87645galaxyABCdef";

	/**
	 * 加密
	 * 
	 * @param content
	 *            需要加密的内容
	 */
	public static byte[] encrypt(String content) {
		return encrypt(content, defaultPrivatePassword);
	}

	/**
	 * 加密
	 * 
	 * @param content
	 *            需要加密的内容
	 */
	public static String defaultEncrypt(String content) {
		byte[] result = encrypt(content, defaultPrivatePassword);
		return new String(result);
	}
	
	/**
	 * 加密
	 * 
	 * @param content
	 *            需要加密的内容
	 * @param password
	 *            加密密码
	 * @return
	 */
	public static byte[] encrypt(String content, String password) {
		try {
			KeyGenerator kgen = KeyGenerator.getInstance("AES");
			kgen.init(128, new SecureRandom(password.getBytes()));//密钥长度为128比特
			SecretKey secretKey = kgen.generateKey();
			byte[] enCodeFormat = secretKey.getEncoded();
			SecretKeySpec key = new SecretKeySpec(enCodeFormat, "AES");
			Cipher cipher = Cipher.getInstance("AES");// 创建密码器
			byte[] byteContent = content.getBytes("utf-8");
			cipher.init(Cipher.ENCRYPT_MODE, key);// 初始化
			byte[] result = cipher.doFinal(byteContent);
			return result; // 加密
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * 解密
	 * 
	 * @param content
	 *            待解密内容
	 * @return
	 */
	public static byte[] decrypt(byte[] content) {
		return decrypt(content, defaultPrivatePassword);
	}

	/**
	 * 解密
	 * 
	 * @param content
	 *            待解密内容
	 * @return
	 */
	public static String defaultDecrypt(String content) {
		byte[] result = encrypt(content);
		return new String(result);
	}

	/**
	 * 解密
	 * 
	 * @param content
	 *            待解密内容
	 * @param password
	 *            解密密钥
	 * @return
	 */

	public static byte[] decrypt(byte[] content, String password) {
		try {
			KeyGenerator kgen = KeyGenerator.getInstance("AES");
			kgen.init(128, new SecureRandom(password.getBytes()));
			SecretKey secretKey = kgen.generateKey();
			byte[] enCodeFormat = secretKey.getEncoded();
			SecretKeySpec key = new SecretKeySpec(enCodeFormat, "AES");
			Cipher cipher = Cipher.getInstance("AES");// 创建密码器
			cipher.init(Cipher.DECRYPT_MODE, key);// 初始化
			byte[] result = cipher.doFinal(content);
			return result; // 加密
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	public static void main(String[] args) throws Exception {

		String content = "{\"id\":\"222\",\"realName\":\"keifer\",\"nickName\":\"xingkaihu\",\"email\":\"xingkaihu\"}";
		// 加密
		System.out.println("加密前：" + content);
		byte[] encryptResult = encrypt(content);

		System.out.println(new String(encryptResult, "utf-8"));
		// 解密
		byte[] decryptResult = decrypt(encryptResult);
		System.out.println("解密后：" + new String(decryptResult));
	}

}

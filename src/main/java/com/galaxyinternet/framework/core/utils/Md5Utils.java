package com.galaxyinternet.framework.core.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Md5Utils {
	static final Logger LOGGER = LoggerFactory.getLogger(Md5Utils.class);
	protected static char hexDigits[] = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e',
			'f', 'g', 'a', 'l', 'a', 'x', 'y' };
	private static MessageDigest digest = null;

	static {
		try {
			digest = MessageDigest.getInstance("MD5");
		} catch (NoSuchAlgorithmException e) {
			LOGGER.error("Md5Utils messagedigest初始化失败", e);
		}
	}

	private Md5Utils() {
	}

	/**
	 * md5加密为32位字符串
	 * 
	 * @param input
	 * @return
	 */
	public static String md5Crypt(String input) {
		try {
			digest.update(input.getBytes());
			byte[] bytes = digest.digest();
			int j = bytes.length;
			char[] chars = new char[j * 2];
			int k = 0;
			for (int i = 0; i < j; i++) {
				byte b = bytes[i];
				chars[k++] = hexDigits[b >>> 4 & 0xf];
				chars[k++] = hexDigits[b & 0xf];
			}
			return new String(chars);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	/**
	 * MD5 加密为16为字符串
	 */
	public static String getMD5Str(String str) {
		if (str != null && str.length() > 0) {
			MessageDigest messageDigest = null;
			try {
				messageDigest = MessageDigest.getInstance("MD5");
				messageDigest.reset();
				messageDigest.update(str.getBytes("UTF-8"));
			} catch (NoSuchAlgorithmException e) {
				LOGGER.error("NoSuchAlgorithmException", e);
				return null;
			} catch (UnsupportedEncodingException e) {
				LOGGER.error("UnsupportedEncodingException", e);
				return null;
			}

			final byte[] byteArray = messageDigest.digest();
			final StringBuffer md5StrBuff = new StringBuffer();
			for (int i = 0; i < byteArray.length; i++) {
				if (Integer.toHexString(0xFF & byteArray[i]).length() == 1) {
					md5StrBuff.append("0").append(Integer.toHexString(0xFF & byteArray[i]));
				} else {
					md5StrBuff.append(Integer.toHexString(0xFF & byteArray[i]));
				}
			}
			// 16位加密，从第9位到25位
			return md5StrBuff.substring(8, 24).toString().toUpperCase();
		}
		return "";
	}

	public static String getFileMD5String(File file) {
		FileInputStream in = null;
		MappedByteBuffer byteBuffer = null;
		try {
			in = new FileInputStream(file);
			FileChannel ch = in.getChannel();
			byteBuffer = ch.map(FileChannel.MapMode.READ_ONLY, 0, file.length());
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (null != in) {
				try {
					in.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		if(null == digest){
			try {
				digest = MessageDigest.getInstance("MD5");
				digest.update(byteBuffer);
				return bufferToHex(digest.digest());
			} catch (NoSuchAlgorithmException e) {
				LOGGER.error("Md5Utils messagedigest初始化失败", e);
			}
		}
		return "";
	}

	public static String getMD5String(byte[] bytes) {
		digest.update(bytes);
		return bufferToHex(digest.digest());
	}

	private static String bufferToHex(byte bytes[]) {
		return bufferToHex(bytes, 0, bytes.length);
	}

	private static String bufferToHex(byte bytes[], int m, int n) {
		StringBuffer stringbuffer = new StringBuffer(2 * n);
		int k = m + n;
		for (int l = m; l < k; l++) {
			appendHexPair(bytes[l], stringbuffer);
		}
		return stringbuffer.toString();
	}

	private static void appendHexPair(byte bt, StringBuffer stringbuffer) {
		char c0 = hexDigits[(bt & 0xf0) >> 4];
		char c1 = hexDigits[bt & 0xf];
		stringbuffer.append(c0);
		stringbuffer.append(c1);
	}

	public static boolean checkPassword(String password, String md5PwdStr) {
		String s = getMD5Str(password);
		return s.equals(md5PwdStr);
	}

	/**
	 * 加密解密算法 执行一次加密，两次解密
	 */
	public static String convertMD5(String inStr) {
		char[] a = inStr.toCharArray();
		for (int i = 0; i < a.length; i++) {
			a[i] = (char) (a[i] ^ 't');
		}
		String s = new String(a);
		return s;

	}

	/**
	 * MD5解密
	 */
	public static String getStrByMd5(String md5Input) {
		return convertMD5(convertMD5(md5Input));
	}

	public static void main(String args[]) {
		String s = new String("李斯");
		System.out.println("原始：" + s);
		System.out.println("MD5后：" + md5Crypt(s));
		System.out.println("解密的：" + getStrByMd5(s));
	}

}

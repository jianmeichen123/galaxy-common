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
			'f' };
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

	public static String md5(final String in) {
		digest.reset();
		digest.update(in.getBytes());
		final byte[] a = digest.digest();
		final int len = a.length;
		final StringBuilder sb = new StringBuilder(len << 1);
		for (int i = 0; i < len; i++) {
			sb.append(Character.forDigit((a[i] & 0xf0) >> 4, 16));
			sb.append(Character.forDigit(a[i] & 0x0f, 16));
		}
		return sb.toString();
	}

	/**
	 * MD5 加密
	 * 
	 * @param str
	 * @return
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
		digest.update(byteBuffer);
		return bufferToHex(digest.digest());
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
}

package com.galaxyinternet.framework.core.utils;

import java.io.UnsupportedEncodingException;

import org.apache.commons.codec.binary.Base64;

/**
 * 对密码进行加密 解密传输
 */
public class Base64Util {
	
	  public final static String ENCODING = "UTF-8";  
	  
	    // 加密  
	    public static String encoded(String data) throws UnsupportedEncodingException {  
	        byte[] b = Base64.encodeBase64(data.getBytes(ENCODING));  
	        return new String(b, ENCODING);  
	    }  
	  
	    // 加密,遵循RFC标准  
	    public static String encodedSafe(String data) throws UnsupportedEncodingException {  
	        byte[] b = Base64.encodeBase64(data.getBytes(ENCODING),true);  
	        return new String(b, ENCODING);  
	    }  
	  
	    // 解密  
	    public static String decode(String data) throws UnsupportedEncodingException {  
	        byte[] b = Base64.decodeBase64(data.getBytes(ENCODING));  
	        return new String(b, ENCODING);  
	    }  
	
	public static void main(String[] args) {
		try {
			System.out.println(encoded("pan"));
			System.out.println(encoded("123"));
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
	}
}

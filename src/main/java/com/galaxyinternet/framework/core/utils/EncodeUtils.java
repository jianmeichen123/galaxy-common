package com.galaxyinternet.framework.core.utils;


import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.security.SecureRandom;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

import org.apache.commons.codec.DecoderException;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.codec.binary.Hex;
import org.apache.commons.lang3.StringEscapeUtils;
import org.apache.commons.lang3.Validate;

import com.galaxyinternet.framework.core.constants.Constants;

import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;

/**
 * 封装各种格式的编码解码工具类.
 * 
 * 1.Commons-Codec的 hex/base64 编码
 * 2.自行编写的，将long进行base62编码以缩短其长度
 * 3.Commons-Lang的xml/html escape
 * 4.JDK提供的URLEncoder
 */
public class EncodeUtils {
    private static final String ALPHABET = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz";

    private static final String DEFAULT_URL_ENCODING = "UTF-8";

    private EncodeUtils() {
    }

    /**
     * Hex编码, byte[]->String.
     */
    public static String encodeHex(byte[] input) {
        return Hex.encodeHexString(input);
    }

    /**
     * Hex解码, String->byte[].
     */
    public static byte[] decodeHex(String input) {
        try {
            return Hex.decodeHex(input.toCharArray());
        } catch (DecoderException e) {
            throw new IllegalStateException("Hex Decoder exception", e);
        }
    }

    /**
     * Base64编码, byte[]->String.
     */
    public static String encodeBase64(byte[] input) {
        return Base64.encodeBase64String(input);
    }

    /**
     * Base64编码, URL安全(将Base64中的URL非法字符'+'和'/'转为'-'和'_', 见RFC3548).
     */
    public static String encodeUrlSafeBase64(byte[] input) {
        return Base64.encodeBase64URLSafeString(input);
    }

    /**
     * Base64解码, String->byte[].
     */
    public static byte[] decodeBase64(String input) {
        return Base64.decodeBase64(input);
    }

    /**
     * Base62(0_9A_Za_z)编码数字, long->String.
     */
    public static String encodeBase62(long num) {
        return alphabetEncode(num, 62);
    }

    /**
     * Base62(0_9A_Za_z)解码数字, String->long.
     */
    public static long decodeBase62(String str) {
        return alphabetDecode(str, 62);
    }

    private static String alphabetEncode(long num, int base) {
        num = Math.abs(num);
        StringBuilder sb = new StringBuilder();
        for (; num > 0; num /= base) {
            sb.append(ALPHABET.charAt((int) (num % base)));
        }

        return sb.toString();
    }

    private static long alphabetDecode(String str, int base) {
        Validate.notBlank(str);

        long result = 0;
        for (int i = 0; i < str.length(); i++) {
            result += ALPHABET.indexOf(str.charAt(i)) * Math.pow(base, i);
        }

        return result;
    }

    /**
     * Html 转码.
     */
    public static String escapeHtml(String html) {
        return StringEscapeUtils.escapeHtml4(html);
    }

    /**
     * Html 解码.
     */
    public static String unescapeHtml(String htmlEscaped) {
        return StringEscapeUtils.unescapeHtml4(htmlEscaped);
    }

    /**
     * Xml 转码.
     */
    public static String escapeXml(String xml) {
        return StringEscapeUtils.escapeXml(xml);
    }

    /**
     * Xml 解码.
     */
    public static String unescapeXml(String xmlEscaped) {
        return StringEscapeUtils.unescapeXml(xmlEscaped);
    }

    /**
     * Csv 转码.
     */
    public static String escapeCsv(String csv) {
        return StringEscapeUtils.escapeCsv(csv);
    }

    /**
     * Csv 解码.
     */
    public static String unescapeCsv(String csvEscaped) {
        return StringEscapeUtils.unescapeCsv(csvEscaped);
    }

    /**
     * URL 编码, Encode默认为UTF-8. 
     */
    public static String urlEncode(String part) {
        try {
            return URLEncoder.encode(part, DEFAULT_URL_ENCODING);
        } catch (UnsupportedEncodingException e) {
            throw ExceptionUtils.unchecked(e);
        }
    }

    /**
     * URL 解码, Encode默认为UTF-8. 
     */
    public static String urlDecode(String part) {

        try {
            return URLDecoder.decode(part, DEFAULT_URL_ENCODING);
        } catch (UnsupportedEncodingException e) {
            throw ExceptionUtils.unchecked(e);
        }
    }

    // 使用AES 128位加密生成串
    public static String aesEncrypt(String src) throws Exception {
        byte[] key = toByte(Constants.SECRET_KEY);
        KeyGenerator kgen = KeyGenerator.getInstance("AES");
        SecureRandom secureRandom = SecureRandom.getInstance("SHA1PRNG");//linux系统需要指定
        secureRandom.setSeed(key);
        kgen.init(128, secureRandom);
        SecretKey secretKey = kgen.generateKey();
        byte[] enCodeFormat = secretKey.getEncoded();
        SecretKeySpec skeySpec = new SecretKeySpec(enCodeFormat, "AES");
        Cipher cipher = Cipher.getInstance("AES");
        cipher.init(Cipher.ENCRYPT_MODE, skeySpec);
        byte[] encrypted = cipher.doFinal(toByte(src));
        return new BASE64Encoder().encode(encrypted);// 此处使用BASE64做转码功能，同时能起到2次加密的作用。
    }

    // 使用AES 128位解密
    public static String aesDecrypt(String src) throws Exception {
        byte[] key = toByte(Constants.SECRET_KEY);
        byte[] content = new BASE64Decoder().decodeBuffer(src);// 先用base64解密
        KeyGenerator kgen = KeyGenerator.getInstance("AES");
        SecureRandom secureRandom = SecureRandom.getInstance("SHA1PRNG");
        secureRandom.setSeed(key);
        kgen.init(128, secureRandom);
        SecretKey secretKey = kgen.generateKey();
        byte[] enCodeFormat = secretKey.getEncoded();
        SecretKeySpec skeySpec = new SecretKeySpec(enCodeFormat, "AES");
        Cipher cipher = Cipher.getInstance("AES");// 创建密码器
        cipher.init(Cipher.DECRYPT_MODE, skeySpec);// 初始化
        byte[] result = cipher.doFinal(content);
        String originalString = new String(result);
        return originalString;
    }

    // 字符串转字节数组
    public static byte[] toByte(String source) {
        return source.getBytes();
    }
}

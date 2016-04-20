package com.galaxyinternet.framework.core.filter;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpServletResponseWrapper;

import org.apache.commons.io.output.StringBuilderWriter;

import com.galaxyinternet.framework.core.utils.AESUtil;

/**
 * 
 *
 * 加密response
 * 
 * @author keifer
 * @date 2016年4月20日
 *
 */
public class EncryptionResponseWrapper extends HttpServletResponseWrapper {
	public EncryptionResponseWrapper(HttpServletResponse response) {
		super(response);
	}

	@Override
	public PrintWriter getWriter() throws IOException {
		// 声明缓存,字节io缓冲块
		ByteArrayOutputStream bout = new ByteArrayOutputStream();
		// 由于super.getWriter();方法返回的是apache的writer对象
		// 而apache的writer对象则是向浏览器返回数据,则可自己声明一个printwriter对象
		PrintWriter out = new PrintWriter(bout);// 写入的缓存中
		String result = new String(getSrc(out, bout));
		result = AESUtil.defaultEncrypt(result);// 加密
		return new PrintWriter(new StringBuilderWriter(new StringBuilder(result)));
	}

	// 提供一个方法用于获取缓存中的数据
	public byte[] getSrc(PrintWriter out, ByteArrayOutputStream bout) {
		if (out != null) {
			out.close();// 获取值后要关，要不缓冲中的数据拿不出来，tomcat自己管理的
		}
		return bout.toByteArray();
	}
}

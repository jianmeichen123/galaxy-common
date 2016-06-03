package com.galaxyinternet.framework.core.filter;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;
import java.nio.charset.Charset;

import javax.servlet.ReadListener;
import javax.servlet.ServletInputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;

import org.apache.commons.lang3.StringUtils;

import com.galaxyinternet.framework.core.utils.Base64Util;

/**
 * @Description: 解密请求数据
 * @author keifer
 * @date 2016年4月20日
 */
public class DecryptionRequestWrapper extends HttpServletRequestWrapper {

	public DecryptionRequestWrapper(HttpServletRequest request) {
		super(request);
	}

	@Override
	public String getParameter(String name) {
		String value = super.getParameter(name);
		if (StringUtils.isBlank(value)) {
			return null;
		}
		return Base64Util.decode(value);
	}

	/*@Override
	public Map<String, String[]> getParameterMap() { //遍历原始的请求参数
		for (Map.Entry<String, String[]> m : super.getParameterMap().entrySet()) {
			for (int i = 0; i < m.getValue().length; i++) { // 加密值
				m.getValue()[i] = m.getValue()[i];
			}
		}
		return super.getParameterMap();
	}*/

	@Override
	public String[] getParameterValues(String name) {
		/*String[] parameterValues = super.getParameterValues(name);
		for (int i = 0; i < parameterValues.length; i++) {
			parameterValues[i] = getParameter(name);
		}
		return parameterValues;*/
		String[] parameterValues = new String[1];
		parameterValues[0] = this.getQueryString();
		return parameterValues;
	}

	@Override
	public BufferedReader getReader() throws IOException {
		String encrypString = FilterUtil.getBodyString(super.getReader());
		Reader rd = new StringReader(encrypString);
		BufferedReader reader = new BufferedReader(rd);
		return reader;
	}

	@Override
	public ServletInputStream getInputStream() throws IOException {
		final ServletInputStream sinputStream = super.getInputStream();
		String encrypString = FilterUtil.getBodyString(sinputStream);
		final ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(encrypString.getBytes(Charset.forName("UTF-8")));
		return new ServletInputStream() {
			public int read() throws IOException {
				return byteArrayInputStream.read();
			}
			@Override
			public boolean isFinished() {
				return sinputStream.isFinished();
			}
			@Override
			public boolean isReady() {
				return sinputStream.isReady();
			}
			@Override
			public void setReadListener(ReadListener readListener) {
				sinputStream.setReadListener(readListener);
			}
		};
	}

	@Override
	public String getQueryString() {
		String value = super.getQueryString();
		if (StringUtils.isBlank(value)) {
			return null;
		}
		return Base64Util.decode(value);
	}
}

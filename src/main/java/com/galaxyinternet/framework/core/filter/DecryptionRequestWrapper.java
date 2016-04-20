package com.galaxyinternet.framework.core.filter;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;

import com.galaxyinternet.framework.core.utils.AESUtil;

/**
 * 
 *
 * @Description: 解密request
 * @author keifer
 * @date 2016年4月20日
 *
 */
public class DecryptionRequestWrapper extends HttpServletRequestWrapper {

	public DecryptionRequestWrapper(HttpServletRequest request) {
		super(request);
	}

	@Override
	public String getParameter(String name) {
		return AESUtil.defaultDecrypt(super.getParameter(name));
	}

	@Override
	public Map<String, String[]> getParameterMap() {
		// 遍历原始的请求参数
		for (Map.Entry<String, String[]> m : super.getParameterMap().entrySet()) {
			for (int i = 0; i < m.getValue().length; i++) {
				// 加密值
				m.getValue()[i] = AESUtil.defaultDecrypt(m.getValue()[i]);
			}
		}
		return super.getParameterMap();
	}

	@Override
	public String[] getParameterValues(String name) {
		String[] parameterValues = super.getParameterValues(name);
		for (int i = 0; i < parameterValues.length; i++) {
			parameterValues[i] = AESUtil.defaultDecrypt(parameterValues[i]);
		}
		return parameterValues;
	}

	@Override
	public BufferedReader getReader() throws IOException {
		String encrypString = FilterUtil.getBodyString(super.getReader());
		Reader rd = new StringReader(AESUtil.defaultDecrypt(encrypString));
		BufferedReader reader = new BufferedReader(rd);
		return reader;
	}

	@Override
	public String getQueryString() {
		return AESUtil.defaultDecrypt(super.getQueryString());
	}
}

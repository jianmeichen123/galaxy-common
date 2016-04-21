package com.galaxyinternet.framework.core.filter;

import java.io.IOException;
import java.util.Collection;
import java.util.Iterator;
import java.util.Map;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.galaxyinternet.framework.cache.Cache;
import com.galaxyinternet.framework.core.constants.Constants;
import com.galaxyinternet.framework.core.oss.OSSConstant;
import com.galaxyinternet.framework.core.utils.BeanContextUtils;

/**
 *
 * @Description: 用于对请求数据解密，对响应数据加密的过滤器
 * @author keifer
 * @date 2016年4月20日
 *
 */
public class DecrypEncryptionFilter implements Filter {

	Collection<Object> collection;

	@Override
	public void destroy() {
	}

	@Override
	public void doFilter(ServletRequest req, ServletResponse resp, FilterChain chain)
			throws IOException, ServletException {

		HttpServletRequest request = (HttpServletRequest) req;
		HttpServletResponse response = (HttpServletResponse) resp;
		boolean isAjax = Constants.AJAX_REQUEST_CORE_OBJECT_NAME
				.equals(request.getHeader(Constants.REQUEST_HEADER_MARK));
		if (isAjax) {// 如果是ajax请求
			request = new DecryptionRequestWrapper(request);// 请求解密
			EncryptionResponseWrapper wrapper = new EncryptionResponseWrapper(response);
			chain.doFilter(request, wrapper);
			String responseData = wrapper.getResponseEncrypData();
			ServletOutputStream output = response.getOutputStream();
			output.write(responseData.getBytes());
			output.flush();
		} else {// 非ajax就不处理了
			/*
			 * if (checkUrl(collection, request.getRequestURL().toString())) {//
			 * 需要解密 request = new DecryptionRequestWrapper(request);//请求解密
			 * response = new EncryptionResponseWrapper(response);//响应加密
			 * chain.doFilter(request, response); return; }
			 */
			chain.doFilter(request, response);
		}
	}

	@Override
	public void init(FilterConfig filterConfig) throws ServletException {
		ServletContext servletContext = filterConfig.getServletContext();
		Cache cache = (Cache) BeanContextUtils.getBean(Constants.REDIS_CACHE_BEAN_NAME, servletContext);
		@SuppressWarnings("unchecked")
		Map<String, Object> configs = (Map<String, Object>) cache.get(OSSConstant.GALAXYINTERNET_FX_ENDPOINT);
		collection = configs.values();
	}

	public boolean checkUrl(Collection<Object> collection, String requestUrl) {
		Iterator<Object> iterator = collection.iterator();
		boolean result = false;
		while (iterator.hasNext()) {
			String endpoint = String.valueOf(iterator.next());
			if (requestUrl.startsWith(endpoint) && FilterUtil.judgeFile(requestUrl)) {
				result = true;// 需要解密
				break;
			}
		}
		return result;
	}
}
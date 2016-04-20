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
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.galaxyinternet.framework.cache.Cache;
import com.galaxyinternet.framework.core.constants.Constants;
import com.galaxyinternet.framework.core.oss.OSSConstant;
import com.galaxyinternet.framework.core.utils.BeanContextUtils;

public class EncryptionFilter implements Filter {
	
	private static Logger logger = LoggerFactory.getLogger(EncryptionFilter.class);
	Collection<Object> collection;
	
	@Override
	public void destroy() {
	}

	@Override
	public void doFilter(ServletRequest req, ServletResponse resp, FilterChain chain)
			throws IOException, ServletException {
		
		HttpServletRequest request = (HttpServletRequest) req;
		HttpServletResponse response = (HttpServletResponse) resp;
		boolean isAjax = "XMLHttpRequest".equals(request.getHeader("X-Requested-With"));
		if (isAjax) {// 如果是ajax请求
			//String reqeustMethod = request.getMethod();
			//if (RequestMethod.POST.name().equalsIgnoreCase(reqeustMethod)) {
			request = new DecryptionRequestWrapper(request);//请求解密
			response = new EncryptionResponseWrapper(response);//响应加密
			//} else if (RequestMethod.GET.name().equalsIgnoreCase(reqeustMethod)) {
				
			//}
		} else {
			if (checkUrl(collection, request.getRequestURL().toString())) {// 需要解密
				request = new DecryptionRequestWrapper(request);//请求解密
				response = new EncryptionResponseWrapper(response);//响应加密
			}
		}
		chain.doFilter(request, response);
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
			if (requestUrl.startsWith(endpoint) && !FilterUtil.judgeFile(requestUrl)) {
				result = true;// 需要解密
				break;
			}
		}
		return result;
	}
}
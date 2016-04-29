package com.galaxyinternet.framework.core.filter;

import java.io.IOException;
import java.util.Collection;
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

import org.apache.log4j.Logger;

import com.galaxyinternet.framework.cache.Cache;
import com.galaxyinternet.framework.core.constants.Constants;
import com.galaxyinternet.framework.core.oss.OSSConstant;
import com.galaxyinternet.framework.core.utils.BeanContextUtils;
import com.galaxyinternet.framework.core.utils.StringEx;

/**
 *
 * @Description: 用于对请求数据解密，对响应数据加密的过滤器
 * @author keifer
 * @date 2016年4月20日
 *
 */
public class DecrypEncryptionFilter implements Filter {
	private static final Logger logger = Logger.getLogger(DecrypEncryptionFilter.class);
	/**
	 * 任何情况都不需要加密解密的请求地址在web.xml里面配置
	 */
	static String[] excludedUrlArray = {};
	/**
	 * 任何情况都需要加密解密的请求地址在web.xml里面配置
	 */
	static String[] incluedUrlArray = {};
	/**
	 * 是否加密解密的标示，true 需要加密解密；false不需要,默认值
	 */
	static boolean isDecrypEncryption;
	/**
	 * 是否所有请求都需加解密。配置为*表示处理所有请求
	 */
	static boolean isDecrypEncrypAllRequeset;
	
	static 
	

	Collection<Object> collection;

	@Override
	public void destroy() {
	}

	@Override
	public void doFilter(ServletRequest req, ServletResponse resp, FilterChain chain)
			throws IOException, ServletException {

		HttpServletRequest request = (HttpServletRequest) req;
		HttpServletResponse response = (HttpServletResponse) resp;

		if (!isDecrypEncryption) {// 不需要加密解密
			chain.doFilter(request, response);
			return;
		}
		
		boolean isAjax = Constants.AJAX_REQUEST_CORE_OBJECT_NAME
				.equals(request.getHeader(Constants.REQUEST_HEADER_MARK));
		//String terminal = request.getHeader(Constants.REQUEST_TERMINAL_MARK);
		//boolean isApp = Terminal.isAppAccess(terminal);
		//if (isAjax || isApp) {// 如果是ajax或是app请求
		if (isAjax) {// 如果是ajax请求
			String url = request.getRequestURI();
			for (String excludedUrl : excludedUrlArray) {
				if (url.contains(StringEx.replaceSpecial(excludedUrl))) {
					chain.doFilter(request, response);
					return;
				}
			}
			if (isDecrypEncrypAllRequeset) {
				doResponse(request, response, chain);
				return;
			} else {
				for (String incluedUrl : incluedUrlArray) {
					logger.debug("======================================Start==========================================");
					logger.debug("URL : "+url);
					logger.debug("Item : "+StringEx.replaceSpecial(incluedUrl));
					logger.debug("Filter : "+url.contains(StringEx.replaceSpecial(incluedUrl)));
					logger.debug("======================================End==========================================");
					if (url.contains(StringEx.replaceSpecial(incluedUrl))) {
						logger.debug("======================================Decryp==========================================");
						doResponse(request, response, chain);
						return;
					}
				}
			}
			chain.doFilter(request, response);
		} else {// 非ajax就不处理了
			/*if (FilterUtil.checkUrl(collection, request.getRequestURL().toString())) {// 需要解密
				request = new DecryptionRequestWrapper(request);// 请求解密
				response = new EncryptionResponseWrapper(response);// 响应加密
				chain.doFilter(request, response);
				return;
			}*/

			chain.doFilter(request, response);
		}
	}

	private void doResponse(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
			throws IOException, ServletException {
		request = new DecryptionRequestWrapper(request);// 请求解密
		EncryptionResponseWrapper wrapper = new EncryptionResponseWrapper(response);
		chain.doFilter(request, wrapper);
		String responseData = wrapper.getResponseEncrypData();
		ServletOutputStream output = response.getOutputStream();
		output.write(responseData.getBytes());
		output.flush();
	}

	@Override
	public void init(FilterConfig filterConfig) throws ServletException {
		excludedUrlArray = FilterUtil.getWebXmlConfigParamters(filterConfig, Constants.EXCLUDE_REQUEST_URL);
		incluedUrlArray = FilterUtil.getWebXmlConfigParamters(filterConfig, Constants.INCLUED_REQUEST_URL);
		isDecrypEncryption = Boolean.valueOf(filterConfig.getInitParameter(Constants.DECRYP_ENCRYPTION_MARK));
		isDecrypEncrypAllRequeset = FilterUtil.decrypEncrypAllRequeset(filterConfig, Constants.INCLUED_REQUEST_URL);
		ServletContext servletContext = filterConfig.getServletContext();
		Cache cache = (Cache) BeanContextUtils.getBean(Constants.REDIS_CACHE_BEAN_NAME, servletContext);
		@SuppressWarnings("unchecked")
		Map<String, Object> configs = (Map<String, Object>) cache.get(OSSConstant.GALAXYINTERNET_FX_ENDPOINT);
		collection = configs.values();
	}
}
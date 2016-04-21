package com.galaxyinternet.framework.core.filter;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Collection;
import java.util.Iterator;

import javax.servlet.FilterConfig;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.galaxyinternet.framework.cache.Cache;
import com.galaxyinternet.framework.core.constants.Constants;
import com.galaxyinternet.framework.core.model.ResponseData;
import com.galaxyinternet.framework.core.model.Result;
import com.galaxyinternet.framework.core.model.Result.Status;
import com.galaxyinternet.framework.core.utils.Base64Util;
import com.galaxyinternet.framework.core.utils.BeanContextUtils;
import com.galaxyinternet.framework.core.utils.GSONUtil;
import com.galaxyinternet.framework.core.utils.SessionUtils;
import com.galaxyinternet.framework.core.utils.StringEx;

/**
 * 
 *
 * @Description: 过滤器工具栏
 * @author keifer
 * @date 2016年4月20日
 *
 */
public class FilterUtil {

	static Logger logger = LoggerFactory.getLogger(FilterUtil.class);

	/**
	 * 去掉对资源文件的拦截
	 */
	public static boolean judgeFile(String url) {
		if (url.endsWith(".gif") || url.endsWith(".jpg") || url.endsWith(".png") || url.endsWith(".bmp")
				|| url.endsWith(".css") || url.endsWith(".js") || url.endsWith(".jsx") || url.endsWith(".ico")
				|| url.endsWith("installReadme.html")) {
			return false;
		} else {
			return true;
		}
	}

	/**
	 * 
	 * 请求参数完整性校验
	 */
	@SuppressWarnings({ "rawtypes" })
	public static String checkRequestParamValid(HttpServletRequest request, HttpServletResponse response)
			throws IOException {
		String sessionId = SessionUtils.getSessionId(request);
		String userId = SessionUtils.getUserId(request);
		if (StringUtils.isBlank(userId) || StringUtils.isBlank(sessionId)) {
			logger.warn("请求参数不完整：userId=" + userId + "sessionId=" + sessionId);
			response.setCharacterEncoding("utf-8");
			ResponseData resposeData = new ResponseData();
			Result result = new Result();
			result.setStatus(Status.ERROR);
			result.setMessage("请求参数不完整");
			result.setErrorCode(Constants.REQUEST_PARAMS_INCOMPLETE);
			resposeData.setResult(result);
			response.getWriter().write(GSONUtil.toJson(resposeData));
			return null;
		}
		return sessionId;
	}

	/**
	 * 
	 * @Description:在过滤器中获取cache对象
	 */
	public static Cache getCache(FilterConfig config) {
		ServletContext servletContext = config.getServletContext();
		return (Cache) BeanContextUtils.getBean(Constants.REDIS_CACHE_BEAN_NAME, servletContext);
	}

	/**
	 * 
	 * @Description:在servlet中获取cache对象
	 */
	public static Cache getCache(ServletContext servletContext) {
		return (Cache) BeanContextUtils.getBean(Constants.REDIS_CACHE_BEAN_NAME, servletContext);
	}

	public static String getBodyString(BufferedReader br) {
		String inputLine = null;
		String str = "";
		try {
			while ((inputLine = br.readLine()) != null) {
				str += inputLine;
			}
		} catch (IOException e) {
			logger.error("从request请求体中获取json对象异常", e);
		} finally {
			try {
				if (null != br) {
					br.close();
				}
			} catch (IOException e) {
			}
		}
		return Base64Util.decode(str);
		// return str;
	}

	/**
	 * 
	 * @Description：获取请求体内的数据，并加密
	 * @param in
	 *            输入流
	 * @return String 返回加密后的数据
	 *
	 */
	public static String getBodyString(InputStream in) {
		BufferedReader br = new BufferedReader(new InputStreamReader(in));
		return getBodyString(br);
	}

	/**
	 * 
	 * @Description:检查请求的url是否需要解密
	 * @param collection
	 *            子项目基础服务地址，如：http://fx.galaxyinternet.com/platform
	 * @param requestUrl
	 *            客户端发送的请求地址
	 * @return boolean true需要解密，false不需要
	 *
	 */
	public static boolean checkUrl(Collection<Object> collection, String requestUrl) {
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

	/**
	 * 
	 * @Description:获取web.xml配置文件中的配置参数
	 * @param filterConfig
	 * @param configParamKey
	 *            配置参数的<param-name>元素的值
	 * @return String[] 返回获取的配置参数的值，多个参数用逗号隔开
	 *
	 */
	public static String[] getWebXmlConfigParamters(FilterConfig filterConfig, String configParamKey) {
		String configPath = filterConfig.getInitParameter(configParamKey);
		if (!StringEx.isNullOrEmpty(configPath)) {
			return configPath.split(",");
		}
		return new String[0];
	}

	/**
	 * 
	 * @Description:获取web.xml配置文件中的配置参数
	 * @param filterConfig
	 * @param configParamKey
	 *            配置参数的<param-name>元素的值, 拦截所有请求配置为*
	 * @return 是否拦截加解密所有请求
	 *
	 */
	public static boolean decrypEncrypAllRequeset(FilterConfig filterConfig, String configParamKey) {
		String configPath = filterConfig.getInitParameter(configParamKey);
		if (Constants.INCLUED_ALL_REQUEST_URL.equals(StringUtils.trimToEmpty(configPath))) {
			return true;
		} else {
			return false;
		}
	}
}

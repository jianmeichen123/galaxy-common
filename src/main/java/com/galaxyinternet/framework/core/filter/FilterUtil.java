package com.galaxyinternet.framework.core.filter;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

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

	public static Cache getCache(FilterConfig config) {
		ServletContext servletContext = config.getServletContext();
		return (Cache) BeanContextUtils.getBean(Constants.REDIS_CACHE_BEAN_NAME, servletContext);
	}

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
		//return str;
	}

	public static String getBodyString(InputStream in) {
		BufferedReader br = new BufferedReader(new InputStreamReader(in));
		return getBodyString(br);
	}

}

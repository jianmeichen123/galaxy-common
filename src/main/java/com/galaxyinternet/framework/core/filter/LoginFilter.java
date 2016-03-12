package com.galaxyinternet.framework.core.filter;

import java.io.IOException;
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

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

import com.galaxyinternet.framework.cache.Cache;
import com.galaxyinternet.framework.core.constants.Constants;
import com.galaxyinternet.framework.core.model.BaseUser;
import com.galaxyinternet.framework.core.model.ResponseData;
import com.galaxyinternet.framework.core.model.Result;
import com.galaxyinternet.framework.core.model.Result.Status;
import com.galaxyinternet.framework.core.oss.OSSConstant;
import com.galaxyinternet.framework.core.utils.GSONUtil;
import com.galaxyinternet.framework.core.utils.StringEx;

public class LoginFilter implements Filter {
	private Logger logger = LoggerFactory.getLogger(LoginFilter.class);

	/**
	 * 任何情况都不需要登录，在web.xml里面配置
	 */
	static String[] excludedUrlArray = {};

	/**
	 * 允许游客状态的接口
	 */
	static String[] webExcludedUrl = { Constants.LOGIN_TOLOGIN, Constants.LOGIN_CHECKLOGIN };

	@Override
	public void destroy() {
	}

	/**
	 * 
	 * 请求参数完整性校验
	 */
	@SuppressWarnings({ "rawtypes", "unused" })
	private String checkRequestParamValid(HttpServletRequest request, HttpServletResponse response) throws IOException {
		String sessionId = getSessionId(request);
		String userId = getUserId(request);
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

	private String getSessionId(HttpServletRequest request){
		String sessionId = request.getHeader(Constants.SESSION_ID_KEY);
		if (StringUtils.isBlank(sessionId)) {
			sessionId = request.getParameter(Constants.SESSOPM_SID_KEY);
		}
		return sessionId;
	}
	
	private String getUserId(HttpServletRequest request){
		String userId = request.getHeader(Constants.REQUEST_HEADER_USER_ID_KEY);
		if (StringUtils.isBlank(userId)) {
			userId = request.getParameter(Constants.REQUEST_URL_USER_ID_KEY);
		}
		return userId;
	}
	
	private BaseUser getUser(HttpServletRequest request) {
		String sessionId = getSessionId(request);
		if (StringUtils.isBlank(sessionId))
			return null;
		Object userObj = request.getSession().getAttribute(sessionId);
		if (userObj == null) {
			return null;
		}
		return (BaseUser) userObj;
	}

	/**
	 * 去掉对资源文件的拦截
	 */
	public boolean judgeFile(String url) {
		if (url.endsWith(".gif") || url.endsWith(".jpg") || url.endsWith(".png") || url.endsWith(".bmp")
				|| url.endsWith(".css") || url.endsWith(".js") || url.endsWith(".jsx")) {
			return false;
		} else {
			return true;
		}
	}

	@SuppressWarnings("rawtypes")
	public void doFilter(ServletRequest req, ServletResponse resp, FilterChain chain)
			throws IOException, ServletException {
		HttpServletRequest request = (HttpServletRequest) req;
		HttpServletResponse response = (HttpServletResponse) resp;
		/**
		 * 验证请求参数的合法性
		 */
		// checkRequestParamValid(request, response);

		String sessionId = getSessionId(request);
		BaseUser user = getUser(request);
		if (null != user && user.getId() > 0) {
			request.getSession().setAttribute(sessionId, user);
		}

		String url = request.getRequestURI();
		// 如果url是资源文件请求地址 直接放行
		if (!judgeFile(url)) {
			chain.doFilter(request, response);
			return;
		}

		for (String excludedUrl : excludedUrlArray) {
			if (url.contains(StringEx.replaceSpecial(excludedUrl))) {
				chain.doFilter(request, response);
				return;
			}
		}
		for (String excludedUrl : webExcludedUrl) {
			if (url.contains(excludedUrl)) {
				chain.doFilter(request, response);
				return;
			}
		}

		/*if (null == user) {
			logger.warn("用户长时间未操作或已过期");
			response.setCharacterEncoding("utf-8");
			String errorMessage = "用户长时间未操作或已过期,请重新登录";
			ResponseData resposeData = new ResponseData();
			Result result = new Result();
			result.setStatus(Status.ERROR);
			result.setMessage(errorMessage);
			result.setErrorCode(Constants.IS_SESSIONID_EXPIRED);
			resposeData.setResult(result);
			response.getWriter().write(GSONUtil.toJson(resposeData));
			return;
		}*/

		chain.doFilter(req, response);
	}

	@Override
	public void init(FilterConfig config) throws ServletException {
		String excludedUrl = config.getInitParameter("excludedUrl");
		if (!StringEx.isNullOrEmpty(excludedUrl)) {
			excludedUrlArray = excludedUrl.split(",");
		}
		ServletContext servletContext = config.getServletContext();
		WebApplicationContext wac = WebApplicationContextUtils.getWebApplicationContext(servletContext);
		Cache cache = (Cache) wac.getBean("cache");
		@SuppressWarnings("unchecked")
		Map<String, Object> configs = (Map<String, Object>) cache.get(OSSConstant.GALAXYINTERNET_FX_ENDPOINT);
		servletContext.setAttribute(OSSConstant.GALAXYINTERNET_FX_ENDPOINT, GSONUtil.toJson(configs));
	}
}

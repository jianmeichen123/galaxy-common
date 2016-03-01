package com.galaxyinternet.framework.core.filter;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;

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
	static String[] webExcludedUrl = { Constants.LOGIN_TOLOGIN,Constants.LOGIN_CHECKLOGIN};

	@Override
	public void destroy() {
	}

	private BaseUser getUser(HttpServletRequest request) {
		
		Object userObj = request.getSession().getAttribute(Constants.SESSION_USER_KEY);
		if(userObj==null){
			String sessionId = request.getHeader(Constants.SESSION_ID_KEY);
			if(StringUtils.isBlank(sessionId)){
				sessionId = request.getParameter(Constants.SESSOPM_SID_KEY);
			}
			if (StringUtils.isNotBlank(sessionId)) {
				return getUser(request, sessionId);
			}else{
				return null;
			}
		}
		return (BaseUser)userObj;
	}

	/**
	 * 获取用户信息
	 * 
	 * @param request
	 *            request
	 * @param key
	 *            sessionId key
	 * @return user
	 */
	private BaseUser getUser(HttpServletRequest request, String key) {
		WebApplicationContext wac = WebApplicationContextUtils
				.getWebApplicationContext(request.getSession().getServletContext());
		Cache cache = (Cache) wac.getBean("cache");
		BaseUser user = (BaseUser) cache.getByRedis(key);
		if (user != null) {
			cache.setByRedis(key, user, 60 * 60 * 24 * 7);
		}
		return user;
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
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
			throws IOException, ServletException {
		HttpServletRequest req = (HttpServletRequest) request;
		BaseUser user = getUser(req);
		if (null != user && user.getId() > 0) {
			req.getSession().setAttribute(Constants.SESSION_USER_KEY, user);
		}

		String url = req.getRequestURI();
		boolean loginFlag = true;

		// 如果url是资源文件请求地址 直接放行
		loginFlag = judgeFile(url);
		if (!loginFlag) {
			chain.doFilter(request, response);
			return;
		}

		for (String excludedUrl : excludedUrlArray) {
			if (url.contains(StringEx.replaceSpecial(excludedUrl))) {
				loginFlag = false;
				break;
			}
		}
		for (String excludedUrl : webExcludedUrl) {
			if (url.contains(excludedUrl)) {
				loginFlag = false;
				break;
			}
		}
		if (loginFlag && null == user) {
			logger.warn("用户长时间未操作或已过期");
			response.setCharacterEncoding("utf-8");
			String errorMessage = "用户长时间未操作或已过期,请重新登录";
			ResponseData resposeData = new ResponseData();
			Result result = new Result();
			result.setStatus(Status.ERROR);
			result.setMessage(errorMessage);
			resposeData.setResult(result);
			response.getWriter().write(GSONUtil.toJson(resposeData));
			return;
		}
		chain.doFilter(req, response);
	}

	@Override
	public void init(FilterConfig config) throws ServletException {
		String excludedUrl = config.getInitParameter("excludedUrl");
		if (!StringEx.isNullOrEmpty(excludedUrl)) {
			excludedUrlArray = excludedUrl.split(",");
		}
	}
}

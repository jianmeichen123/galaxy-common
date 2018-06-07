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
import javax.servlet.http.HttpSession;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.galaxyinternet.framework.cache.Cache;
import com.galaxyinternet.framework.core.constants.Constants;
import com.galaxyinternet.framework.core.model.BaseUser;
import com.galaxyinternet.framework.core.model.ResponseData;
import com.galaxyinternet.framework.core.model.Result;
import com.galaxyinternet.framework.core.model.Result.Status;
import com.galaxyinternet.framework.core.oss.OSSConstant;
import com.galaxyinternet.framework.core.utils.GSONUtil;
import com.galaxyinternet.framework.core.utils.StringEx;
/**
 * 
 *
 * @Description: 登录过滤器
 * @author keifer
 *
 */
public class LoginFilter implements Filter {
	private Logger logger = LoggerFactory.getLogger(LoginFilter.class);
	/**
	 * 任何情况都不需要登录，在web.xml里面配置
	 */
	static String[] excludedUrlArray = {};

	static Cache cache;
	/**
	 * 允许游客状态的接口
	 */
	static String[] webExcludedUrl = { Constants.LOGIN_TOLOGIN, Constants.LOGIN_CHECKLOGIN };
	@Override
	public void destroy() {
	}

	@SuppressWarnings("rawtypes")
	public void doFilter(ServletRequest req, ServletResponse resp, FilterChain chain)
			throws IOException, ServletException {
		HttpServletRequest request = (HttpServletRequest) req;
		HttpServletResponse response = (HttpServletResponse) resp;
		String url = request.getRequestURI();
		boolean loginFlag = true;

		loginFlag = FilterUtil.judgeFile(url);
		if (!loginFlag) {
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
		BaseUser user = (BaseUser)request.getSession().getAttribute(Constants.SESSION_USER_KEY);
		//其他终端免登陆
		if(user == null)
		{
			String sessionId = request.getParameter("sid");
			if(StringUtils.isEmpty(sessionId))
			{
				sessionId = request.getHeader("sessionId");
			}
			if(StringUtils.isNotEmpty(sessionId))
			{
				user = (BaseUser)cache.get(sessionId);
			}
		}
		
		//清除cookie后退出用户
		if(user != null)
		{
			HttpSession session = request.getSession();
			int expiredTime = session.getMaxInactiveInterval();
			cache.expire(session.getId(), expiredTime);
			
			String originalSid = user.getSessionId();
			String currentSid = session.getId();
			if(StringUtils.isNotEmpty(originalSid) && !StringUtils.equals(originalSid, currentSid))
			{
				user = null;
				cache.remove(originalSid);
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
			result.setErrorCode(Constants.IS_SESSIONID_EXPIRED);
			resposeData.setResult(result);
			String terminal = request.getHeader(Constants.REQUEST_TERMINAL_MARK);
			if(StringUtils.isBlank(terminal)){
				response.sendRedirect(Constants.LOGIN_TOLOGIN);
				return;
			}else{
				response.addHeader("Content-Type","application/json");
				response.getWriter().write(GSONUtil.toJson(resposeData));
				return;
			}
		}
		chain.doFilter(req, response);
	}

	@Override
	public void init(FilterConfig config) throws ServletException {
		excludedUrlArray = FilterUtil.getWebXmlConfigParamters(config,Constants.EXCLUDE_REQUEST_URL);
		ServletContext servletContext = config.getServletContext();
		cache = FilterUtil.getCache(servletContext);
		@SuppressWarnings("unchecked")
		Map<String, Object> configs = (Map<String, Object>) cache.get(OSSConstant.GALAXYINTERNET_FX_ENDPOINT);
		servletContext.setAttribute(OSSConstant.GALAXYINTERNET_FX_ENDPOINT, GSONUtil.toJson(configs));
	}
}

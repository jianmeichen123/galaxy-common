package com.galaxyinternet.framework.core.filter;

import java.io.IOException;
import java.util.Properties;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.galaxyinternet.framework.core.constants.Constants;
import com.galaxyinternet.framework.core.model.ResponseData;
import com.galaxyinternet.framework.core.model.Result;
import com.galaxyinternet.framework.core.model.Result.Status;
import com.galaxyinternet.framework.core.utils.GSONUtil;
import com.galaxyinternet.framework.core.utils.PropertiesUtils;

/**
 * 安全防卫过滤器
 * 
 * @author keifer
 *
 */
public class GuardFilter implements Filter {
	private Logger logger = LoggerFactory.getLogger(GuardFilter.class);

	private static String headerReferer;

	@Override
	public void destroy() {
	}

	@SuppressWarnings("rawtypes")
	public void doFilter(ServletRequest req, ServletResponse rep, FilterChain chain)
			throws IOException, ServletException {
		HttpServletRequest request = (HttpServletRequest) req;
		HttpServletResponse response = (HttpServletResponse) rep;
		String referer = request.getHeader("referer");
		if (referer == null || !referer.startsWith(headerReferer)) {
			logger.error("Access rejected. Request url:", request.getRequestURL().toString());
			response.sendRedirect(request.getContextPath() + Constants.LOGIN_TOLOGIN);
			ResponseData resposeData = new ResponseData();
			Result result = new Result();
			result.setStatus(Status.ERROR);
			result.setErrorCode(Constants.ACCESS_REJECTED);
			resposeData.setResult(result);
			response.getWriter().write(GSONUtil.toJson(resposeData));
			return;
		} else {
			chain.doFilter(request, response);
		}
	}

	@Override
	public void init(FilterConfig config) throws ServletException {
		Properties property = PropertiesUtils.getProperties(Constants.SAFE_CONFIG_FILE);
		headerReferer = property.getProperty(Constants.HEADER_REFERER_KEY);
	}
}

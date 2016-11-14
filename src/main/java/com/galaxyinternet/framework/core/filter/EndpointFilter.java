package com.galaxyinternet.framework.core.filter;

import java.io.IOException;
import java.util.Map;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;

import org.springframework.context.ApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

import com.galaxyinternet.framework.cache.Cache;

public class EndpointFilter implements Filter
{
	private Cache cache = null;
	private Map<String,Object> endpoints = null;
	@Override
	public void init(FilterConfig config) throws ServletException
	{
		ApplicationContext appCtx = WebApplicationContextUtils.getWebApplicationContext(config.getServletContext());
		cache = appCtx.getBean(Cache.class);
		if(cache != null)
		{
			endpoints = cache.hgetAll("endpoints");
			config.getServletContext().setAttribute("endpoints", endpoints);
		}
	}

	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
			throws IOException, ServletException
	{
		HttpServletRequest req = (HttpServletRequest)request;
		String ctxPath = req.getContextPath();
		request.setAttribute("ctxPath", ctxPath);
		
		if(cache != null)
		{
			endpoints = cache.hgetAll("endpoints");
			req.getSession().getServletContext().setAttribute("endpoints", endpoints);
		}
		chain.doFilter(request, response);
	}

	@Override
	public void destroy()
	{
		// TODO Auto-generated method stub

	}

}

package com.galaxyinternet.framework.core.utils;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

public class BeanContextUtils implements ApplicationContextAware
{
	private static Logger logger = LoggerFactory.getLogger(BeanContextUtils.class);
	private static Map<String,ApplicationContext> ctxCache = new HashMap<>();
	@Override
	public void setApplicationContext(ApplicationContext ctx) throws BeansException
	{
		if(ctx != null)
		{
			logger.info("Add Application Cache "+ctx.getDisplayName());
			ctxCache.put(ctx.getDisplayName(), ctx);
		}
		
	}
	
	@SuppressWarnings("unchecked")
	public static <T> T getBean(Class<?> clazz)
	{
		Object obj = null;
		for(ApplicationContext ctx : ctxCache.values())
		{
			obj = ctx.getBean(clazz);
			if(obj != null)
			{
				return (T)obj;
			}
		}
		return null;
	}

	/**
	 * 
	 * @Description:获取web容器中bean容器对象
	 * 
	 */
	public static WebApplicationContext getWebApplicationContext(HttpServletRequest request) {
		return getWebApplicationContext(request.getSession().getServletContext());
	}

	/**
	 * 
	 * @Description:获取web容器中bean容器对象
	 * 
	 */
	public static WebApplicationContext getWebApplicationContext(ServletContext servletContext) {
		return WebApplicationContextUtils.getWebApplicationContext(servletContext);
	}

	/**
	 * 
	 * @Description:从bean容器对象中获取具体的bean对象
	 * @param beanName
	 *            bean的class对象
	 */
	public static <T> T getBean(Class<T> clazz, ServletContext servletContext) {
		return getWebApplicationContext(servletContext).getBean(clazz);
	}

	/**
	 * 
	 * @Description:从bean容器对象中获取具体的bean对象
	 * @param beanName
	 *            bean的class对象
	 */
	public static <T> T getBean(Class<T> clazz, HttpServletRequest request) {
		return getWebApplicationContext(request).getBean(clazz);
	}

	/**
	 * 
	 * @Description:从bean容器对象中获取具体的bean对象
	 * @param beanName
	 *            定义的bean的名称，配置文件中对于id属性的值
	 */
	public static Object getBean(String beanName, HttpServletRequest request) {
		return getWebApplicationContext(request).getBean(beanName);
	}

	/**
	 * 
	 * @Description:从bean容器对象中获取具体的bean对象
	 * @param beanName
	 *            定义的bean的名称，配置文件中对于id属性的值
	 */
	public static Object getBean(String beanName, ServletContext servletContext) {
		return getWebApplicationContext(servletContext).getBean(beanName);
	}

	

}

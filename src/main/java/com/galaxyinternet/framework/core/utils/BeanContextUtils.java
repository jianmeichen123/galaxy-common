package com.galaxyinternet.framework.core.utils;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;

import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

public class BeanContextUtils {

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

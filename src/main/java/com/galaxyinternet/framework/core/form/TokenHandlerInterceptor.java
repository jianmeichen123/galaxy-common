package com.galaxyinternet.framework.core.form;

import static com.galaxyinternet.framework.core.form.Token.TOKEN;

import java.lang.reflect.Method;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import com.galaxyinternet.framework.cache.Cache;
import com.galaxyinternet.framework.core.constants.Constants;

/**
 * 令牌处理拦截器<br/>
 * 
 * @Description 每个子项目如果需要支持表单防重提交，都需要配置该拦截器
 * @author keifer
 * @date 2016年3月8日
 */
public class TokenHandlerInterceptor extends HandlerInterceptorAdapter {
	Cache cache;

	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
		if (handler instanceof HandlerMethod) {
			HandlerMethod handlerMethod = (HandlerMethod) handler;
			Method method = handlerMethod.getMethod();
			Token token = method.getAnnotation(Token.class);
			if (token != null) {
				if (this.isRepeatSubmitted(request)) {
					return false;
				}
				String tokenKey = (String) request.getAttribute(Constants.REQUEST_SCOPE_TOKEN_KEY);
				removeSessionToken(request, tokenKey);
			}
		}
		return true;
	}

	@Override
	public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex)
			throws Exception {
		if (handler instanceof HandlerMethod) {
			HandlerMethod handlerMethod = (HandlerMethod) handler;
			Method method = handlerMethod.getMethod();
			Token token = method.getAnnotation(Token.class);
			Object removeReq = request.getAttribute(Constants.TOKEN_REMOVE_KEY);
			if (token != null) {
				boolean remove = token.remove();
				String tokenKey = (String) request.getAttribute(Constants.REQUEST_SCOPE_TOKEN_KEY);
				if (null == removeReq && remove) {
					removeSessionToken(request, tokenKey);
				} else {
					setSessionToken(request, tokenKey);
				}
			}
		}
		super.afterCompletion(request, response, handler, ex);
	}

	private boolean isRepeatSubmitted(HttpServletRequest request) {
		String sessionToken = getSessionToken(request);
		if (sessionToken == null) {
			return true;
		}
		String requestToken = request.getParameter(TOKEN);
		if (requestToken == null) {
			return true;
		}
		if (!sessionToken.equals(requestToken)) {
			return true;
		}
		return false;
	}

	private String getSessionToken(HttpServletRequest request) {
		String tokenKey = (String) request.getAttribute(Constants.REQUEST_SCOPE_TOKEN_KEY);
		Object sessionToken = request.getSession().getAttribute(tokenKey);
		if (null == sessionToken) {
			WebApplicationContext wac = WebApplicationContextUtils
					.getWebApplicationContext(request.getSession().getServletContext());
			cache = (Cache) wac.getBean(Constants.REDIS_CACHE_BEAN_NAME);
			Object token = cache.get(tokenKey);
			if (null == token) {
				return null;
			} else {
				return String.valueOf(token);
			}
		} else {
			return String.valueOf(sessionToken);
		}
	}

	private void setSessionToken(HttpServletRequest request, String tokenKey) {
		String tokenValue = request.getParameter(TOKEN);
		request.getSession().setAttribute(tokenKey, tokenValue);
		cache.set(tokenKey, Constants.TOKEN_IN_REDIS_TIMEOUT_SECONDS, tokenValue);
	}

	private void removeSessionToken(HttpServletRequest request, String tokenKey) {
		request.getSession().removeAttribute(tokenKey);
		cache.remove(tokenKey);
	}
}

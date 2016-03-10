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
 * @description 每个子项目如果需要支持表单防重提交，都需要配置该拦截器
 * @author keifer
 */
public class TokenHandlerInterceptor extends HandlerInterceptorAdapter {
	Cache cache;

	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
		if (cache == null) {
			WebApplicationContext wac = WebApplicationContextUtils
					.getWebApplicationContext(request.getSession().getServletContext());
			cache = (Cache) wac.getBean(Constants.REDIS_CACHE_BEAN_NAME);
		}
		if (handler instanceof HandlerMethod) {
			HandlerMethod handlerMethod = (HandlerMethod) handler;
			Method method = handlerMethod.getMethod();
			Token token = method.getAnnotation(Token.class);
			if (token != null) {
				String requestTokenvalue = (String) request.getHeader(TOKEN);
				if (this.isRepeatSubmitted(request, requestTokenvalue)) {
					return false;
				}
				removeSessionToken(request, requestTokenvalue);
			}
		}
		return true;
	}

	/**
	 * 该方法处理业务系统异常时的情况
	 */
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
				String tokenValue = (String) request.getHeader(TOKEN);
				if (null == removeReq && remove) {
					removeSessionToken(request, tokenValue);
				} else {
					setSessionToken(request, tokenValue);
				}
			}
		}
		super.afterCompletion(request, response, handler, ex);
	}

	private boolean isRepeatSubmitted(HttpServletRequest request, String requestToken) {
		if (requestToken == null) {
			return true;
		}
		String sessionToken = getSessionToken(request, requestToken);
		if (sessionToken == null) {
			return true;
		}

		if (!sessionToken.equals(requestToken)) {
			return true;
		}
		return false;
	}

	private String getSessionToken(HttpServletRequest request, String tokenValue) {
		String sessionToken = (String) request.getSession().getAttribute(tokenValue);
		if (null == sessionToken) {
			Object token = cache.get(sessionToken);
			if (null == token) {
				return null;
			} else {
				return String.valueOf(token);
			}
		} else {
			return String.valueOf(sessionToken);
		}
	}

	private void setSessionToken(HttpServletRequest request, String tokenValue) {
		request.getSession().setAttribute(tokenValue, tokenValue);
		cache.set(tokenValue, Constants.TOKEN_IN_REDIS_TIMEOUT_SECONDS, tokenValue);
	}

	private void removeSessionToken(HttpServletRequest request, String tokenValue) {
		request.getSession().removeAttribute(tokenValue);
		cache.remove(tokenValue);
	}

}

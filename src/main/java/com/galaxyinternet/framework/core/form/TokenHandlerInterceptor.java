package com.galaxyinternet.framework.core.form;

import static com.galaxyinternet.framework.core.form.Token.TOKEN;

import java.lang.reflect.Method;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import com.galaxyinternet.framework.core.constants.Constants;

/**
 * 令牌处理拦截器<br/>
 * 
 * @author keifer
 */
public class TokenHandlerInterceptor extends HandlerInterceptorAdapter {
	HttpServletRequest request;
	HttpSession session;

	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
		if (handler instanceof HandlerMethod) {
			HandlerMethod handlerMethod = (HandlerMethod) handler;
			Method method = handlerMethod.getMethod();
			Token token = method.getAnnotation(Token.class);
			if (token != null) {
				this.request = request;
				session = request.getSession(true);
				if (this.isRepeatSubmitted()) {
					return false;
				}
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
				if (null == removeReq && remove) {
					session.removeAttribute(TOKEN);
				}
			}
		}
		super.afterCompletion(request, response, handler, ex);
	}

	private boolean isRepeatSubmitted() {
		String sessionToken = (String) session.getAttribute(TOKEN);
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
}

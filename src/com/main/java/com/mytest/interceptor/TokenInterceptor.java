package com.mytest.interceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.core.NamedThreadLocal;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import com.mytest.utils.ThreadLocalUtil;


/**
 * token拦截器
 *
 * @author yan.zhang
 */
public class TokenInterceptor extends HandlerInterceptorAdapter {
	
	private static final Log log = LogFactory.getLog(TokenInterceptor.class);
	
	public static final String TOKEN = "token";
	
	public static NamedThreadLocal<String> token = new NamedThreadLocal<String>(TOKEN);
	
	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
		String token = request.getHeader(TOKEN);
		if (token != null && !token.equals("")) {
			try {
				ThreadLocalUtil.setToken(token);
			} catch (Exception e) {
				log.error("拦截器取token异常", e);
			}
		}
		return true;
	}
	
}

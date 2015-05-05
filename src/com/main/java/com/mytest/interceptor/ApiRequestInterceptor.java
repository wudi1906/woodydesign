package com.mytest.interceptor;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.springframework.core.NamedThreadLocal;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import com.mytest.utils.MyLogFactory;

/**
 * API请求拦截器
 * 
 * @author eric
 * @date 2014-2-28
 */
public class ApiRequestInterceptor extends HandlerInterceptorAdapter {
	
	private static final Logger logger = MyLogFactory.getLog(ApiRequestInterceptor.class);
	
    public static NamedThreadLocal<Map<String, Object>> paramsThreadLocal = new NamedThreadLocal<Map<String, Object>>("params");	
    public static NamedThreadLocal<String> encryptkeyThreadLocal = new NamedThreadLocal<String>("encryptkey");
    public static NamedThreadLocal<String> encryptEncryptkeyThreadLocal = new NamedThreadLocal<String>("encryptencryptkey");
    public static NamedThreadLocal<String> signkeyThreadLocal = new NamedThreadLocal<String>("signkey");	
	
	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
		return true;
		
	}
	
}

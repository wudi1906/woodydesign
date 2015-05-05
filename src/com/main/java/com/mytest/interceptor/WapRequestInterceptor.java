package com.mytest.interceptor;

import java.io.IOException;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import com.mytest.utils.MyLogFactory;
import com.mytest.utils.StringUtils;
import com.mytest.utils.ThreadLocalUtil;

/**
 * Wap页面请求拦截器
 */
@Component
public class WapRequestInterceptor extends HandlerInterceptorAdapter {
	
	private static final Logger logger = MyLogFactory.getLog(WapRequestInterceptor.class);
	
	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
		logger.info("WapRequestInterceptor start");
		
		if(request.getRequestURL().indexOf("/login_index") > -1){
			return true;
		}
		
		/**
		 * 这里验证tokenfdsafdsa
		 */
		String tokenPage = (String)request.getParameter("token");
		
		logger.info(" token page  " + tokenPage);
		logger.info(" token session  " + (String)request.getSession().getAttribute("token"));

		if(!StringUtils.equals(tokenPage, (String)request.getSession().getAttribute("token"))){
			logger.info(" token compair no ");
			return false;
		}
		
		logger.info("WapRequestInterceptor end");
		return true;
		
	}
	
	
	@Override
	public void postHandle(HttpServletRequest request,
			HttpServletResponse response, Object handler,
			ModelAndView modelAndView) throws Exception {
		
		logger.info("WapRequestInterceptor postHandle start");
		
		/**
		 * 这里生成token
		 */
		makeToken(request);
		logger.info(" token " + (String)request.getSession().getAttribute("token"));
		modelAndView.addObject("token", (String)request.getSession().getAttribute("token"));
		super.postHandle(request, response, handler, modelAndView);
		
		logger.info("WapRequestInterceptor postHandle end");
	}
	
	private void makeToken(HttpServletRequest request){
		// 为本次支付请求生成一个token
		String token = null;
		
		// 将token赋值为SDK拦截器的threadLocal中的token
		try {
			token = ThreadLocalUtil.getToken();
		} catch (Exception e) {
			logger.error(e.getLocalizedMessage(),e);
		}
		if(StringUtils.isEmpty(token)){
			token = UUID.randomUUID().toString();
		}
		
		request.getSession().setAttribute("token", token);
		
	}
	
}

package com.mytest.utils;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.mytest.interceptor.TokenInterceptor;

/**
 * 操作sdk threadlocal工具类
 * @author yan.zhang
 *
 */
public class ThreadLocalUtil {
	
	private static final Log log = LogFactory.getLog(ThreadLocalUtil.class);
	
	/**
	 * 保存token
	 * @param token
	 * @throws Exception
	 */
	public static void setToken(String token) throws Exception {
		try {
			TokenInterceptor.token.set(token);
		} catch (Exception e) {
			log.error("保存token到sdk threadlocal异常", e);
			throw new Exception(e);
		}
	}
	
	/**
	 * 获取token
	 * @return
	 * @throws Exception
	 */
	public static String getToken() throws Exception {
		try {
			return TokenInterceptor.token.get();
		} catch (Exception e) {
			log.error("从sdk threadlocal中获取token异常", e);
			throw new Exception(e);
		}
	}
	
	/**
	 * 销毁threadlocal
	 * @return
	 * @throws Exception
	 */
	public static void destory() throws Exception {
		try {
			TokenInterceptor.token.remove();
		} catch (Exception e) {
			log.error("销毁threadlocal异常", e);
			throw new Exception(e);
		}
	}
}

package com.mytest.utils;
import org.apache.log4j.Logger;

/**
 * 日志工厂
 * 
 * @author eric
 * @date 2014-2-28
 */
public class MyLogFactory {
	public static Logger getLog(Class<?> clazz) {
		return Logger.getLogger(clazz);
	}
}

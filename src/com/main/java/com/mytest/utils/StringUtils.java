package com.mytest.utils;

/**
 * 字符串Utils
 * 
 * @author eric
 * @date 2012-8-9
 */
public class StringUtils {
	public static String trim(String s) {
		return org.apache.commons.lang.StringUtils.trim(s);
	}
	
	public static boolean isNotBlank(String s) {
		return org.apache.commons.lang.StringUtils.isNotBlank(s);
	}
	
	public static boolean isNotEmpty(String s) {
		return org.apache.commons.lang.StringUtils.isNotEmpty(s);
	}
	
	public static boolean isEmpty(String s) {
		return org.apache.commons.lang.StringUtils.isEmpty(s);
	}
	
	public static boolean equals(String s1, String s2) {
		return org.apache.commons.lang.StringUtils.equals(s1, s2);
	}
	
	public static boolean isBlank(String s) {
		return org.apache.commons.lang.StringUtils.isBlank(s);
	}
}

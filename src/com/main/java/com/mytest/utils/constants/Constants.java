package com.mytest.utils.constants;

/**
 * 
 * @author eric
 * @date 2012-7-24
 */
public enum Constants {
	/**
	 * 开放平台的机构号
	 */
	INSTITUTION_NUMBER("YeepayOpen"),
	
	/**
	 * 保存在session作用于的用户信息
	 */
	SESSION_USER("session_user"),
	
	/**
	 * 账户信息
	 */
	SESSION_ACCOUNT("session_account"),
	
	/**
	 * 保存在request作用域的用户信息
	 */
	REQUEST_USER("request_user"),
	
	/**
	 * 保存在request作用域的账户信息
	 */	
	REQUEST_ACCOUNT("request_account"),
	
	/**
	 * 日期格式
	 */
	DATE_PATTERN_YYYY_MM_DD("yyyy-MM-dd"),
	
	USERINFO("userInfo"),
	
	COLSEP(",");	
	
	private final String value;

	Constants(String value) {
		this.value = value;
	}

	public String value() {
		return value;
	}
}

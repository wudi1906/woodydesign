package com.mytest.utils;

import java.util.ResourceBundle;

/**
 * @company YeePay
 * @author 李天石
 * @since 2011-8-31
 * @version 1.0
 */
public class ResourceUtils {
	private static ResourceBundle resourceBundle = null;
	static {
		resourceBundle = ResourceBundle.getBundle("resource");
	}
	
	public static String getValue(String key){
		return resourceBundle.getString(key);
	}
}

package com.mytest.utils;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.Collection;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

import javax.servlet.http.HttpServletRequest;

public class DataUtils {
	/**
	 * 判断对象是否为空
	 * @param obj
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public static Boolean isEmpty(Object obj){
		if (obj == null) {
			return true;
		}
		if (obj instanceof Object[]) {
			if (((Object[]) obj).length == 0) {
				return true;
			}
		}
		if(obj instanceof String){
			if(((String) obj).trim().isEmpty()){
				return true;
			}
		}
		if(obj instanceof Map){
			if(((Map) obj).isEmpty()){
				return true;
			}
		}
		if(obj instanceof Collection){
			if(((Collection) obj).isEmpty()){
				return true;
			}
		}
		return false;
	}
	
	/**
	 * 将参数转成字符串，并替换掉null
	 * @param obj
	 * @return
	 */
	public static String doNull(Object obj){
		if (obj == null) {
			return "";
		}
		if(obj instanceof String){
			return (String)obj;
		}
		if(obj instanceof String[]){
			String str = "";
			for(String substr : (String[])obj){
				str += substr;
			}
			return str;
		}
		if(obj instanceof Integer){
			return ((Integer)obj).toString();
		}
		if(obj instanceof Long){
			return ((Long)obj).toString();
		}
		return obj.toString();
	}
	
	/**
	 * 获取url中的参数
	 * @param queryString
	 * @param enc
	 * @return
	 * @throws UnsupportedEncodingException 
	 */
	public static Map<String, String[]> getParamsMap(String queryString, String enc) throws UnsupportedEncodingException {
		Map<String, String[]> paramsMap = new HashMap<String, String[]>();
		if (queryString != null && queryString.length() > 0) {
			String[] paramPair, values, newValues;
			String[] query = queryString.split("&");
			for(String subStr : query){
				paramPair = subStr.split("=");
				String param = paramPair[0];
				String value = paramPair.length == 1 ? "" : paramPair[1];
				value = URLDecoder.decode(value, enc);
				if (paramsMap.containsKey(param)) {
					values = paramsMap.get(param);
					int len = values.length;
					newValues = new String[len + 1];
					System.arraycopy(values, 0, newValues, 0, len);
					newValues[len] = value;
				} else {
					newValues = new String[] { value };
				}
				paramsMap.put(param, newValues);
			}
		}
		return paramsMap;
	}
	
	/**循环请求参数
	 * @param request 请求
	 * @return 排序完毕的阐述序列
	 */
	private String sortParams(HttpServletRequest request){
		Enumeration en=request.getParameterNames();
		Map<String,String> m=new HashMap<String,String>();
		while(en.hasMoreElements()){
			String key=(String)en.nextElement();
			if("sig".equals(key))continue;
			String value=request.getParameter(key);
			m.put(key, value);
		}
		return sortParams(m);
	}
	
	/**a~z散列排列参数，用于加密处理
	 * @param params
	 * @return
	 */
	private String sortParams(Map params) {
		if (params == null || params.size() == 0) {
			return null;
		}
		StringBuffer sb = new StringBuffer();
		Set<String> keys = params.keySet();
		TreeSet<String> tss = new TreeSet<String>(keys);
		for (String key : tss) {
			sb.append(key);
			sb.append("=");
			sb.append(UrlEncode((String)params.get(key), "UTF-8"));
			sb.append("&");
		}
		if (sb.toString().endsWith("&")) {
			sb.deleteCharAt(sb.length() - 1);
		}
		//System.out.println("sort=" + sb.toString());
		return sb.toString();
	}
	
	private String UrlEncode(String val, String ec) {
		if (val == null || "".equals(val)) {
			return "";
		}
		String v = null;
		try {
			v = URLEncoder.encode(val, ec);
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		return v;
	}
	
}

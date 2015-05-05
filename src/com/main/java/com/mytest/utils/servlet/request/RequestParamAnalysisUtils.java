package com.mytest.utils.servlet.request;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;

/**
 * 请求参数解析工具类
 * 
 * @author da.zhang
 * @date 2013-1-9
 */
public class RequestParamAnalysisUtils {

	private RequestParamAnalysisUtils() {
		
	}

	/**
	 * 获取Api接口调用验证所需参数
	 * 
	 * @param request
	 * @return CheckNeedParameter
	 * @throws UnsupportedEncodingException 
	 */
	public static CheckNeedParameter getApiCheckNeedParameter(HttpServletRequest request) throws UnsupportedEncodingException {
		CheckNeedParameter cnp = new CheckNeedParameter();
		if (request.getContentType() != null && request.getContentType().indexOf("multipart/form-data") != -1) { // 判断post请求方法： "POST".equals(request.getMethod())
			Map<String, String> argsMap = new HashMap<String, String>();
			Map<String, String> fdpMap = (Map<String, String>) request.getAttribute("form-data-parameter");
			if (fdpMap == null) {
				//判断 以multipart/form-data 上传文件方式传入参数 解析方法（多用于POST传参解析使用）
				DiskFileItemFactory dff = new DiskFileItemFactory();//创建该对象
				ServletFileUpload sfu = new ServletFileUpload(dff);
				List<FileItem> items;
				fdpMap = new HashMap<String, String>();
				try {
					items = sfu.parseRequest(request);
					//得到form所有数据
					Iterator<FileItem> i = items.iterator();//使用迭代
					while (i.hasNext()) {
						FileItem fi = (FileItem) i.next();
						//取得file外其他类型的参数
						if (fi.isFormField()) {
							if (fi.getFieldName().equals("sign")) {//如果当前field的name和"key"相同就取出字符串
								cnp.setSign(fi.getString("utf-8"));
							} else {
								argsMap.put(fi.getFieldName(),
										fi.getString("utf-8"));
							}
							if (fi.getFieldName().equals("time")) {
								cnp.setTime(fi.getString("utf-8"));
							}
							fdpMap.put(fi.getFieldName(),
									fi.getString("utf-8"));
						}
					}
					cnp.setParameters(sortParams(argsMap, true));
					request.setAttribute("form-data-parameter", fdpMap);
				} catch (FileUploadException e) {
					e.printStackTrace();
				}
			}else {
				for (String k : fdpMap.keySet()) {
					if ("sign".equals(k)) {
						cnp.setSign(fdpMap.get(k));
					}else {
						argsMap.put(k, fdpMap.get(k));
					}
					if ("time".equals(k)) {
						cnp.setTime(fdpMap.get(k));
					}
				}
				cnp.setParameters(sortParams(argsMap, false));
				request.removeAttribute("form-data-parameter");
			}
		}else {
			cnp.setSign(request.getParameter("sign"));
			cnp.setTime(request.getParameter("time"));
			cnp.setParameters(sortParams(request));
		}
		return cnp;
	}
	

	/**
	 * 循环请求参数
	 * 
	 * @param request 请求
	 * @return 排序完毕的阐述序列
	 * @throws UnsupportedEncodingException 
	 */
	private static String sortParams(HttpServletRequest request) throws UnsupportedEncodingException{
		Enumeration en=request.getParameterNames();
		Map<String,String> m=new HashMap<String,String>();
		while(en.hasMoreElements()){
			String key=(String)en.nextElement();
			if("sign".equals(key))continue;
			String value=request.getParameter(key);
			m.put(key, value);
		}
		return sortParams(m, true);
	}
	
	/**
	 * 排列参数，用于加密处理
	 * 
	 * @param params
	 * @return
	 * @throws UnsupportedEncodingException 
	 */
	public static String sortParams(Map params, boolean isUrlEncode) throws UnsupportedEncodingException {
		if (params == null || params.size() == 0) {
			return null;
		}
		StringBuffer sb = new StringBuffer();
		Set<String> keys = params.keySet();
		TreeSet<String> tss = new TreeSet<String>(keys);
		if (isUrlEncode) {
			for (String key : tss) {
				sb.append(key);
				sb.append("=");
				sb.append(UrlEncode((String) params.get(key), "UTF-8"));
				sb.append("&");
			}
		}else {
			for (String key : tss) {
				sb.append(key);
				sb.append("=");
				sb.append((String)params.get(key));
				sb.append("&");
			}
		}
		if (sb.toString().endsWith("&")) {
			sb.deleteCharAt(sb.length() - 1);
		}
		return sb.toString();
	}
	
	private static String UrlEncode(String val, String ec) {
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
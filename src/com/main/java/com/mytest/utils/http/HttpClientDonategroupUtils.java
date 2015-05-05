package com.mytest.utils.http;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.params.HttpMethodParams;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.utils.URIUtils;
import org.apache.http.client.utils.URLEncodedUtils;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;

/**
 * 
 * Title: 与请求相关的工具类，本工具类中使用的是httpclient3
 * Description: 
 * Copyright: Copyright (c)2010
 * Company: YeePay
 * @author 
 *
 */
public class HttpClientDonategroupUtils {
	/** post请求
	 * @param url
	 * @param port
	 * @param type
	 * @param info
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public static String httpPost(String url,int port,String type,Hashtable<String,String> info){
		org.apache.commons.httpclient.HttpClient hc=new org.apache.commons.httpclient.HttpClient();
		hc.getHostConfiguration().setHost(url,port,type);
		PostMethod post=new PostMethod(url);
		Set s=info.keySet();
		org.apache.commons.httpclient.NameValuePair[] nps=new org.apache.commons.httpclient.NameValuePair[s.size()];
		int re=0;
		try {
		Iterator i=s.iterator();
		int is=0;
		while(i.hasNext()){
			String a=(String)i.next();
			nps[is]=new org.apache.commons.httpclient.NameValuePair(a,info.get(a));
			is++;
		}
		post.getParams().setParameter(HttpMethodParams.HTTP_CONTENT_CHARSET, "UTF-8");
		post.setRequestBody(nps);
		re=hc.executeMethod(post);
		if(re==HttpStatus.SC_OK){
		    InputStream inputStream = post.getResponseBodyAsStream();  
		    BufferedReader br = new BufferedReader(new InputStreamReader(inputStream));  
		    StringBuffer stringBuffer = new StringBuffer();  
		    String str= "";  
		    while((str = br.readLine()) != null){  
		    	stringBuffer .append(str);  
		    }  
			return stringBuffer.toString();
		}
		} catch (Exception e) {
			e.printStackTrace();
			return "-1001";
		}
		return re+"";
	}
	/**
	 * 用httpClient发送get请求
	 * http://gongyi.yeepay.com/donategroup/queryMemberDonate.action
	 * @param info 传入的hashtable，用来组合url参数
	 * @param host 主机名,如 qa.yeepay.com
	 * @param path 路径,如 /app-merchant-proxy/command.action
	 * @param encoding 字符集编码,如UTF-8
	 * @return HttpResponse
	 */
	public static HttpResponse httpClientGet(Hashtable<String,String> info,String host,String path,String encoding)  {
		return httpClientGet(info,host,path,encoding,-1,-1);
	}
	/**
	 * 
	 * function description
	 *
	 * @author liyu
	 * @modifytime 2012-10-25 下午04:20:39
	 * @version 1.0
	 * 
	 * @param info
	 * @param host
	 * @param path
	 * @param encoding
	 * @param connectTimeOut ms
	 * @param waitTimeOut ms
	 * @return
	 */
	public static HttpResponse httpClientGet(Hashtable<String,String> info,String host,String path,String encoding,
			int connectTimeOut,int waitTimeOut)  {
		List<NameValuePair> qparams = new ArrayList<NameValuePair>();
		for(String key : info.keySet()) {
			qparams.add(new BasicNameValuePair(key, (String) info.get(key)));
		}
		URI uri = null;
		HttpResponse httpResponse = null;
		try {
			String notHttpUri="";
			if (host.length()>7 && "http://".equalsIgnoreCase(host.substring(0,7))){
				notHttpUri=host.substring(7);
			}else{
				notHttpUri=host;
			}
			uri = URIUtils.createURI("http", notHttpUri, -1, path, URLEncodedUtils.format(qparams, encoding), null);
			HttpGet httpget = new HttpGet(uri);
			HttpClient httpClient = new DefaultHttpClient();
			if (connectTimeOut==-1 || waitTimeOut==-1){
				HttpParams params = httpClient.getParams();
				HttpConnectionParams.setConnectionTimeout(params, connectTimeOut);
				HttpConnectionParams.setSoTimeout(params, waitTimeOut);
			}
			httpResponse = null;
			httpResponse = httpClient.execute(httpget);
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (URISyntaxException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		//返回解析后的响应信息
		return httpResponse;
	}
	/**
	 * 解析响应信息存放在STRING
	 * @param httpResponse 响应对象
	 * @return strLine.toString()，如果没有相关的信息则返回null
	 */
	public static String getHashtableByHttpResponseString(HttpResponse httpResponse) {
		StringBuffer strLine = new StringBuffer();
		BufferedReader  bufferedReader = null;
		try {
			if(httpResponse != null) {
				HttpEntity httpEntity = httpResponse.getEntity();
				bufferedReader = new BufferedReader(new InputStreamReader(httpEntity.getContent(), "UTF-8")) ;
				String tmp="";
				while((tmp=bufferedReader.readLine()) != null) {
					strLine.append(tmp);
				}
			}
		} catch (IllegalStateException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if(bufferedReader != null) {
				try {
					bufferedReader.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		return strLine.toString();
	}

	/**
	 * 返回的key改为商家的key
	 * @param key 服务器返回的key
	 * @return
	 */
	public static String changeHashtableKey(String key) {
		if(key.equals("r6_Order")) {
			return "bill_no";
		} else if(key.equals("r3_Amt")) {
			return "amount";
		} else if(key.equals("rs_Status")){
			return "status";
		} else {
			return null;
		}
	}
	
	/**
	 * 取得主机名
	 * @param path path
	 * @return
	 */
	public static String getHostName(String path) {
		String noSchema = path.substring(path.indexOf("://")+3);
		return noSchema.substring(0,noSchema.indexOf("/"));
	}
	
	/**
	 * 取得请求路径
	 * @param path path
	 * @return
	 */
	public static String getUrlPath(String path) {
		String noSchema = path.substring(path.indexOf("://")+3);
		return noSchema.substring(noSchema.indexOf("/"));
	}
	
	
}

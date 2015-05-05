package com.mytest.utils.http;

import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.Charset;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;
import java.util.Set;

import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import javax.security.cert.CertificateException;
import javax.security.cert.X509Certificate;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.client.params.ClientPNames;
import org.apache.http.client.params.CookiePolicy;
import org.apache.http.conn.scheme.PlainSocketFactory;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.scheme.SchemeRegistry;
import org.apache.http.conn.ssl.SSLSocketFactory;
import org.apache.http.entity.ByteArrayEntity;
import org.apache.http.entity.StringEntity;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.content.ByteArrayBody;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.conn.PoolingClientConnectionManager;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.CoreConnectionPNames;
import org.apache.http.params.HttpParams;
import org.apache.http.util.EntityUtils;

/**
 * 
 * HttpClient4的默认工具类
 * @author tianshi.li
 *
 */
public class HttpClient4Util {

	private HttpClient httpclient;
	
	/**
	 * 对象池缓存
	 */
	private static HashMap<String, HttpClient> clientPool = new HashMap<String, HttpClient>();
	
	/**
	 * 默认创建工具类方式
	 * @return
	 */
	public static HttpClient4Util createDefault() {
		return new HttpClient4Util();
	}
	
	/**
	 * 带PoolingClientConnectionManager参数的创建工具类方式
	 * @param connectionManager
	 * @return
	 */
	public static HttpClient4Util createClient(PoolingClientConnectionManager connectionManager) {
		return new HttpClient4Util(null, connectionManager);
	}
	
	/**
	 * 带HttpParams参数的创建工具类方式
	 * @param httpParams
	 * @return
	 */
	public static HttpClient4Util createClient(HttpParams httpParams) {
		return new HttpClient4Util(httpParams, null);
	}
	
	/**
	 * 带HttpParams、PoolingClientConnectionManager参数的创建工具类方式
	 * @param httpParams
	 * @param connectionManager
	 * @return
	 */
	public static HttpClient4Util createClient(HttpParams httpParams, PoolingClientConnectionManager connectionManager) {
		return new HttpClient4Util(httpParams, connectionManager);
	}

	/**
	 * 根据默认参数创建工具类
	 * @throws KeyManagementException 
	 * @throws NoSuchAlgorithmException 
	 */
	public HttpClient4Util(){
		try {
			HttpParams httpParams = new BasicHttpParams();
			httpParams.setParameter(CoreConnectionPNames.CONNECTION_TIMEOUT, 3000);
			httpParams.setParameter(CoreConnectionPNames.SO_TIMEOUT, 30000);
			httpParams.setParameter(CoreConnectionPNames.STALE_CONNECTION_CHECK, true);
			httpParams.setParameter(CoreConnectionPNames.TCP_NODELAY, true);
			httpParams.setParameter(CoreConnectionPNames.SO_KEEPALIVE, 100);
			httpParams.setParameter(ClientPNames.COOKIE_POLICY, CookiePolicy.IGNORE_COOKIES); //设置cookie的兼容性
			
			SchemeRegistry registry = new SchemeRegistry();  
			registry.register(new Scheme("http", 80, PlainSocketFactory.getSocketFactory()));
			SSLContext ctx = SSLContext.getInstance("TLS");  
			X509TrustManager tm = new X509TrustManager() {  
			    public java.security.cert.X509Certificate[] getAcceptedIssuers() {  
			        return null;  
			    }  
			    public void checkClientTrusted(X509Certificate[] arg0, String arg1) throws CertificateException {}  
			    public void checkServerTrusted(X509Certificate[] arg0, String arg1) throws CertificateException {}  
			    @Override  
			    public void checkClientTrusted(  
			            java.security.cert.X509Certificate[] chain,  
			            String authType)  
			            throws java.security.cert.CertificateException {  
			        // TODO Auto-generated method stub  
			          
			    }  
			    @Override  
			    public void checkServerTrusted(  
			            java.security.cert.X509Certificate[] chain,  
			            String authType)  
			            throws java.security.cert.CertificateException {  
			        // TODO Auto-generated method stub  
			          
			    }  
			};  
			ctx.init(null, new TrustManager[] { tm }, null);  
			SSLSocketFactory ssf = new SSLSocketFactory(ctx, SSLSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER);  
			registry.register(new Scheme("https", 443, ssf));
//	    registry.register(new Scheme("https", 443, SSLSocketFactory.getSocketFactory()));
			PoolingClientConnectionManager connectionManager = new PoolingClientConnectionManager(registry);
			connectionManager.setDefaultMaxPerRoute(100);
			connectionManager.setMaxTotal(1000);
			httpclient = new DefaultHttpClient(connectionManager,httpParams);
		} catch (KeyManagementException e) {
			e.printStackTrace();
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * 根据传入参数创建工具类
	 * @param httpParams
	 * @param connectionManager
	 */
	private HttpClient4Util(HttpParams httpParams, PoolingClientConnectionManager connectionManager) {
		if (httpParams != null && connectionManager == null) {
			SchemeRegistry registry = new SchemeRegistry();  
		    registry.register(new Scheme("http", 80, PlainSocketFactory.getSocketFactory()));
		    registry.register(new Scheme("https", 443, SSLSocketFactory.getSocketFactory()));
		    connectionManager = new PoolingClientConnectionManager(registry);
		    connectionManager.setDefaultMaxPerRoute(100);
		    connectionManager.setMaxTotal(1000);
			httpclient = getHttpClient(httpParams, connectionManager);
			return;
		}
		
		if (httpParams == null && connectionManager != null) {
			httpParams = new BasicHttpParams();
			httpParams.setParameter(CoreConnectionPNames.CONNECTION_TIMEOUT, 3000);
			httpParams.setParameter(CoreConnectionPNames.SO_TIMEOUT, 30000);
			httpParams.setParameter(CoreConnectionPNames.STALE_CONNECTION_CHECK, true);
			httpParams.setParameter(CoreConnectionPNames.TCP_NODELAY, true);
			httpParams.setParameter(CoreConnectionPNames.SO_KEEPALIVE, 100);
			httpParams.setParameter(ClientPNames.COOKIE_POLICY, CookiePolicy.IGNORE_COOKIES); //设置cookie的兼容性
		    httpclient = getHttpClient(httpParams, connectionManager);
		    return;
		}

		if (httpParams != null && connectionManager != null) {
			httpclient = getHttpClient(httpParams, connectionManager);
			return;
		}
	}
	
	/**
	 * 享元工厂方法，根据参数获得享元对象或建立新享元对象
	 * @param httpParams
	 * @param connectionManager
	 * @return
	 */
	private HttpClient getHttpClient(HttpParams httpParams, PoolingClientConnectionManager connectionManager) {
		String key = buildPoolKey(httpParams, connectionManager);
		/**
		 * 如果已存在该享元对象则返回该享元对象，没有则创建享元对象，并保存到缓存
		 */
		if (clientPool.containsKey(key)) {
			return (HttpClient)clientPool.get(key);
		} else {
			HttpClient client = new DefaultHttpClient(connectionManager, httpParams);
			clientPool.put(key, client);
			return client;
		}
	}
	
	/**
	 * 构建享元key作为在缓存中唯一标识
	 * @param httpParams
	 * @param connectionManager
	 * @return
	 */
	private String buildPoolKey(HttpParams httpParams, PoolingClientConnectionManager connectionManager) {
		StringBuilder builder = new StringBuilder();
		builder.append(httpParams.getParameter(CoreConnectionPNames.CONNECTION_TIMEOUT));
		builder.append(httpParams.getParameter(CoreConnectionPNames.SO_TIMEOUT));
		builder.append(httpParams.getParameter(CoreConnectionPNames.STALE_CONNECTION_CHECK));
		builder.append(httpParams.getParameter(CoreConnectionPNames.TCP_NODELAY));
		builder.append(httpParams.getParameter(CoreConnectionPNames.SO_KEEPALIVE));
		builder.append(httpParams.getParameter(ClientPNames.COOKIE_POLICY));
		builder.append(connectionManager.getDefaultMaxPerRoute());
		builder.append(connectionManager.getMaxTotal());
		return builder.toString();
	}

	public void setHttpclient(HttpClient httpclient) {
		this.httpclient = httpclient;
	}

	public HttpClient getHttpclient() {
		return httpclient;
	}

	/**
	 * 简化的get
	 * 
	 * @param url
	 * @return
	 * @throws ClientProtocolException
	 * @throws IOException
	 */
	public HttpResp doGet(String url) throws ClientProtocolException,
	        IOException {
		return this.doGet(url, null, null);
	}

	/**
	 * 详细get
	 * 
	 * @param url
	 * @param httpParameter 参考{@link HttpParameter}
	 * @param charset 参数编码
	 * @return 参考{@link HttpResp}
	 * @throws ClientProtocolException
	 * @throws IOException
	 */
	public HttpResp doGet(String url, HttpParameter httpParameter,
	        String charset) throws ClientProtocolException, IOException {
		StringBuilder sb = new StringBuilder(url);
		if (httpParameter != null && !httpParameter.isAllParameterEmpty()) {
			if (url.indexOf("?") == -1) {
				sb.append("?");
			}
			if (sb.charAt(sb.length() - 1) != ('?')) {
				sb.append("&");
			}
			for (BasicParameter o : httpParameter.getBasicParameters()) {
				sb.append(URLEncoder.encode(o.getName(), charset));
				sb.append("=");
				sb.append(URLEncoder.encode(o.getValue(), charset));
				sb.append("&");
			}
			if (sb.length() > 0) {
				sb.deleteCharAt(sb.length() - 1);
			}
		}
		HttpGet httpGet = new HttpGet(sb.toString());
		if (httpParameter != null && !httpParameter.isEmptyHeader()) {
			Set<Entry<String, String>> set = httpParameter.getHeaderMap()
			        .entrySet();
			for (Entry<String, String> e : set) {
				httpGet.addHeader(e.getKey(), e.getValue());
			}
		}
		return this.execute(httpGet);
	}

	/**
	 * post 字符数据
	 * 
	 * @param url
	 * @param value post的字符串数据
	 * @param charset 参数编码
	 * @return
	 * @throws ClientProtocolException
	 * @throws IOException
	 */
	public HttpResp doPostStringBody(String url, String string, String charset)
	        throws ClientProtocolException, IOException {
		HttpEntity entity = new StringEntity(string, charset);
		return this.doPostBody(url, entity);
	}

	/**
	 * post 字节数据
	 * 
	 * @param url
	 * @param bytes 字节数据
	 * @return
	 * @throws ClientProtocolException
	 * @throws IOException
	 */
	public HttpResp doPostBytesBody(String url, byte[] bytes)
	        throws ClientProtocolException, IOException {
		HttpEntity entity = new ByteArrayEntity(bytes);
		return this.doPostBody(url, entity);
	}

	private HttpResp doPostBody(String url, HttpEntity entity)
	        throws ClientProtocolException, IOException {
		HttpPost httpPost = new HttpPost(url);
		httpPost.setEntity(entity);
		return this.execute(httpPost);
	}

	/**
	 * post
	 * 
	 * @param url
	 * @param httpParameter 参数信息 参考{@link HttpParameter}
	 * @param charset 参数编码
	 * @return 参考{@link HttpResp}
	 * @throws ClientProtocolException
	 * @throws IOException
	 */
	public HttpResp doPost(String url, HttpParameter httpParameter,
	        String charset) throws ClientProtocolException, IOException {
		HttpPost httpPost = new HttpPost(url);
		if (httpParameter.isFileParameterEmpty()) {
			List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
			for (BasicParameter e : httpParameter.getBasicParameters()) {
				nameValuePairs.add(new BasicNameValuePair(e.getName(), e
				        .getValue()));
			}
			httpPost.setEntity(new UrlEncodedFormEntity(nameValuePairs, charset));
		}
		else {
			MultipartEntity reqEntity = new MultipartEntity();
			for (FileParameter e : httpParameter.getFileParameters()) {
				if (e.getFile() != null) {
					reqEntity.addPart(e.getName(), new FileBody(e.getFile()));
				}
				else {
					reqEntity.addPart(e.getName(),
					        new ByteArrayBody(e.getData(), e.getFileName()));
				}
			}
			for (BasicParameter e : httpParameter.getBasicParameters()) {
				reqEntity.addPart(e.getName(), new StringBody(e.getValue(),
				        Charset.forName(charset)));
			}
			httpPost.setEntity(reqEntity);
		}
		if (httpParameter != null && !httpParameter.isEmptyHeader()) {
			Set<Entry<String, String>> set = httpParameter.getHeaderMap()
			        .entrySet();
			for (Entry<String, String> e : set) {
				httpPost.addHeader(e.getKey(), e.getValue());
			}
		}
		return this.execute(httpPost);
	}

	/**
	 * 具体执行过程
	 * 
	 * @param request
	 * @return
	 * @throws ClientProtocolException
	 * @throws IOException
	 */
	private HttpResp execute(HttpRequestBase request)
	        throws ClientProtocolException, IOException {
		HttpEntity entity = null;
		try {
			HttpResponse httpResponse = httpclient.execute(request);
			HttpResp httpResp = new HttpResp();
			httpResp.setStatusCode(httpResponse.getStatusLine().getStatusCode());
			entity = httpResponse.getEntity();
			httpResp.setBytes(EntityUtils.toByteArray(entity));
			return httpResp;
		}
		catch (ClientProtocolException e) {
			throw e;
		}
		catch (IOException e) {
			throw e;
		}
		finally {
			if (entity != null) {
				EntityUtils.consume(entity);
			}
		}
	}

	/**
	 * 最终可释放资源调用
	 */
	public void shutdown() {
		this.httpclient.getConnectionManager().shutdown();
	}
	
//	public static void main(String[] args) {
//		HttpParams httpParams = new BasicHttpParams();
//		httpParams.setParameter(CoreConnectionPNames.CONNECTION_TIMEOUT, 3000);
//		httpParams.setParameter(CoreConnectionPNames.SO_TIMEOUT, 30000);
//		httpParams.setParameter(CoreConnectionPNames.STALE_CONNECTION_CHECK, true);
//		httpParams.setParameter(CoreConnectionPNames.TCP_NODELAY, true);
//		httpParams.setParameter(CoreConnectionPNames.SO_KEEPALIVE, 100);
//		httpParams.setParameter(ClientPNames.COOKIE_POLICY, CookiePolicy.IGNORE_COOKIES); //设置cookie的兼容性
//		HttpClient4Util util1 = HttpClient4Util.createClient(httpParams);
//		HttpClient client1 = util1.getHttpclient();
//		
//		httpParams = new BasicHttpParams();
//		httpParams.setParameter(CoreConnectionPNames.CONNECTION_TIMEOUT, 3000);
//		httpParams.setParameter(CoreConnectionPNames.SO_TIMEOUT, 20000);
//		httpParams.setParameter(CoreConnectionPNames.STALE_CONNECTION_CHECK, true);
//		httpParams.setParameter(CoreConnectionPNames.TCP_NODELAY, true);
//		httpParams.setParameter(CoreConnectionPNames.SO_KEEPALIVE, 100);
//		httpParams.setParameter(ClientPNames.COOKIE_POLICY, CookiePolicy.IGNORE_COOKIES); //设置cookie的兼容性
//		HttpClient4Util util2 = HttpClient4Util.createClient(httpParams);
//		HttpClient client2 = util2.getHttpclient();
//		
//		httpParams = new BasicHttpParams();
//		httpParams.setParameter(CoreConnectionPNames.CONNECTION_TIMEOUT, 3000);
//		httpParams.setParameter(CoreConnectionPNames.SO_TIMEOUT, 30000);
//		httpParams.setParameter(CoreConnectionPNames.STALE_CONNECTION_CHECK, true);
//		httpParams.setParameter(CoreConnectionPNames.TCP_NODELAY, true);
//		httpParams.setParameter(CoreConnectionPNames.SO_KEEPALIVE, 100);
//		httpParams.setParameter(ClientPNames.COOKIE_POLICY, CookiePolicy.IGNORE_COOKIES); //设置cookie的兼容性
//		HttpClient4Util util3 = HttpClient4Util.createClient(httpParams);
//		HttpClient client3 = util3.getHttpclient();
//		
//		httpParams = new BasicHttpParams();
//		httpParams.setParameter(CoreConnectionPNames.CONNECTION_TIMEOUT, 3000);
//		httpParams.setParameter(CoreConnectionPNames.SO_TIMEOUT, 10000);
//		httpParams.setParameter(CoreConnectionPNames.STALE_CONNECTION_CHECK, true);
//		httpParams.setParameter(CoreConnectionPNames.TCP_NODELAY, true);
//		httpParams.setParameter(CoreConnectionPNames.SO_KEEPALIVE, 100);
//		httpParams.setParameter(ClientPNames.COOKIE_POLICY, CookiePolicy.IGNORE_COOKIES); //设置cookie的兼容性
//		HttpClient4Util util4 = HttpClient4Util.createClient(httpParams);
//		HttpClient client4 = util4.getHttpclient();
//		
//		httpParams = new BasicHttpParams();
//		httpParams.setParameter(CoreConnectionPNames.CONNECTION_TIMEOUT, 3000);
//		httpParams.setParameter(CoreConnectionPNames.SO_TIMEOUT, 10000);
//		httpParams.setParameter(CoreConnectionPNames.STALE_CONNECTION_CHECK, true);
//		httpParams.setParameter(CoreConnectionPNames.TCP_NODELAY, true);
//		httpParams.setParameter(CoreConnectionPNames.SO_KEEPALIVE, 100);
//		httpParams.setParameter(ClientPNames.COOKIE_POLICY, CookiePolicy.IGNORE_COOKIES); //设置cookie的兼容性
//		HttpClient4Util util5 = HttpClient4Util.createClient(httpParams);
//		HttpClient client5 = util5.getHttpclient();
//		
//		System.out.println("stop");
//	}
}
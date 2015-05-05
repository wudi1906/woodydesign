package com.mytest.utils.entity;

import java.io.Serializable;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.jsp.JspWriter;

public class PageModule implements Serializable {
	private static final long serialVersionUID = -5920501690561764384L;

	public int pageSize = 10; // 每页最多行数

	public int pageIndex = 1; // 当前页号

	public int m_nPages; // 总页数

	public int m_nItems; // 总行数

	@SuppressWarnings("unchecked")
	public List m_c; // 当前记录集合

	private Map<String, String> parms = new HashMap<String, String>();

	private String url;

	private String paritionLink;

	private String ybparitionLink;

	private String pageSizeName;

	private String pageIndexName;

	public long lType; // 辅助参数

	// 分页条显示的页数长度
	private int distance = 10;
	
	/**
	 * 错误代码
	 */
	private String errorCode;

	
	/**
	 * 设置当前记录集合，当第一次调用一组记录页面控制前要先调用该函数。 例：Collection c = yourBean.getList();
	 * thisPage.setCollection(c); thisPage.setPageInfo(10,2);
	 * 
	 * @param c
	 *            当前记录集合
	 * @return 无返回
	 */
	@SuppressWarnings("unchecked")
	public void setCollection(List c) {
		m_c = c;
	}

	@SuppressWarnings("unchecked")
	public List getCollection() {
		return m_c;
	}

	/**
	 * 设置当前页显示参数
	 * 
	 * @param maxRows
	 *            每页最多行数
	 * @param pageIndex
	 *            页码
	 * @param rowsum
	 *            结果总行数
	 * @return PageControl 对象
	 */
	public void setPageInfo(int maxRows, int pageIndex, int rowsum) {
		if (maxRows <= 0)
			maxRows = 10;
		pageSize = maxRows;
		this.pageIndex = pageIndex;
		this.m_nItems = rowsum;
		m_nPages = rowsum / pageSize;
		if (rowsum % pageSize != 0)
			m_nPages += 1;
		if (pageIndex > m_nPages)
			pageIndex = 1;
	}

	/**
	 * 获得总页数
	 * 
	 * @return 返回总页数
	 */
	public int getPagesNum() {
		return m_nPages;
	}

	public void setPagesNum(int m_nPages) {
		this.m_nPages = m_nPages;
	}

	public int getRows() {
		return this.m_nItems;
	}

	/**
	 * 获得成员变量指定第几页所有记录的集合
	 * 
	 * @return 成员变量指定第几页所有记录的集合
	 */
	@SuppressWarnings("unchecked")
	public List getList() {
		if (this.m_c != null)
			return m_c;
		else
			return new ArrayList();
	}

	/**
	 * 在网页上显示分页信息，和连接 形如： 上一页|第?页|下一页|共?页|每页?行 go|转到?页
	 * 
	 * @param out
	 * @param strFormName
	 *            分页信息的form的名称
	 * @param strIndexName
	 *            页码变量的名称
	 * @param strRowNumName
	 *            行数变量的名称
	 * @param strSubmit
	 *            提交form时onSubmit的调用内容。可以是js function调用，如strSubmit = "jsFunc();"
	 * @return 无返回
	 */
	public void showPageLink(String url) {
	}

	/**
	 * 在网页上显示分页信息，和连接 形如： 上一页|第?页|下一页|共?页|每页?行 go|转到?页
	 * 
	 * @param out
	 * @param strFormName
	 *            分页信息的form的名称
	 * @param strIndexName
	 *            页码变量的名称
	 * @param strRowNumName
	 *            行数变量的名称
	 * @param strSubmit
	 *            提交form时onSubmit的调用内容。可以是js function调用，如strSubmit = "jsFunc();"
	 * @return 无返回
	 */
	public void showPageLink2(JspWriter out, HttpServletRequest request,
			String strFormName, String strIndexName, String strRowNumName,
			String strSubmit, String currentPage) throws Exception {
		try {
			out.println("<form name=\"" + strFormName + "\"  id=\""
					+ strFormName + "\" method=\"post\" action='" + currentPage
					+ "' onSubmit=\"" + strSubmit + "\">");
			out.println("共" + m_nItems + "条记录|<input type=\"hidden\"  name=\""
					+ strRowNumName
					+ "\" align=\"right\" maxLength=2 size=3 value=\""
					+ pageSize + "\">");
			out.println("第" + pageIndex + "/" + m_nPages + "页|");
			if (pageIndex > 1)
				out
						.println("<a href=\"javascript: go(document.getElementById('"
								+ strFormName + "'), -1);\">上页</a>|");
			else
				out.println("上页|");
			if (pageIndex < m_nPages)
				out
						.println("<a  href=\"javascript: go(document.getElementById('"
								+ strFormName + "'), 1);\">下页</a>|");
			else
				out.println("下页|");
			out.println("每页" + pageSize + "行");
			out.println("|转到第<SELECT name=\"" + strIndexName
					+ "\" onchange=\"go(document.getElementById('"
					+ strFormName + "'), 0)\">");
			for (int i = 1; i <= m_nPages; i++) {
				if (i == pageIndex) {
					out.println("<OPTION selected value=" + i + ">" + i
							+ "</OPTION>");
				}
				else {
					out.println("<OPTION value=" + i + ">" + i + "</OPTION>");
				}
			}
			out.println("</SELECT>页");
			out.println("</form>");
			out.println(" <script language=\"JavaScript\" src=\""
					+ request.getContextPath() + "/js/Check.js\"></script>\n");
			out.println("<SCRIPT Language=\"JavaScript\">\n");
			out.println("function go(form, value)\n");
			out.println("{if(value != 0){form." + strIndexName + ".value="
					+ pageIndex + "+value; \n }");
			// out.println("alert(form.c); if ( !InputValid(
			// form.c"+strRowNumName+", 1, \"int\", 1, 1,99,\"每页行数\"))
			// {alert('form.c=');form."+strRowNumName+".focus(); return
			// false;}form.submit();}\n");
			out.println(" form.submit();}\n");
			out.println("</SCRIPT>");
		}
		catch (Exception exp) {
			throw exp;
		}
	}

	/**
	 * 此处修改是为了解决在一个页面中有多个分页，由于此方法中封装 了一个js方法，所以多个分页在同一个页面中会重名
	 * 
	 * @param out
	 * @param request
	 * @param strFormName
	 * @param strIndexName
	 * @param strRowNumName
	 * @param strSubmit
	 * @param goFunction
	 *            此方法中的一个js方法名，主要是做分页的提交
	 * @param currentPage
	 * @throws Exception
	 * @author liubx
	 */
	public void showPageLink1(JspWriter out, HttpServletRequest request,
			String strFormName, String strIndexName, String strRowNumName,
			String strSubmit, String goFunction, String currentPage)
			throws Exception {
		try {
			out.println("<form name=\"" + strFormName
					+ "\" method=\"post\" action=" + currentPage
					+ " onSubmit=\"" + strSubmit + "\">");
			out.println("共" + m_nItems + "条记录|<input type=\"hidden\"  name=\""
					+ strRowNumName
					+ "\" align=\"right\" maxLength=2 size=3 value=\""
					+ pageSize + "\">");
			out.println("第" + pageIndex + "/" + m_nPages + "页|");
			if (pageIndex > 1)
				out.println("<a href=\"javascript:" + goFunction
						+ "(document.getElementById('" + strFormName
						+ "'), -1);\">上页</a>|");
			else
				out.println("上页|");
			if (pageIndex < m_nPages)
				out.println("<a  href=\"javascript:" + goFunction
						+ "(document.getElementById('" + strFormName
						+ "'), 1);\">下页</a>|");
			else
				out.println("下页|");
			out.println("每页" + pageSize + "行");
			out.println("|转到第<SELECT name=\"" + strIndexName + "\" onchange=\""
					+ goFunction + "(" + strFormName + ", 0);\">");
			for (int i = 1; i <= m_nPages; i++) {
				if (i == pageIndex) {
					out.println("<OPTION selected value=" + i + ">" + i
							+ "</OPTION>");
				}
				else {
					out.println("<OPTION value=" + i + ">" + i + "</OPTION>");
				}
			}
			out.println("</SELECT>页");
			out.println("</form>");
			out.println(" <script language=\"JavaScript\" src=\""
					+ request.getContextPath() + "/js/Check.js\"></script>\n");
			out.println("<SCRIPT Language=\"JavaScript\">\n");
			out.println("function " + goFunction + "(form, value)\n");
			out.println("{if(value != 0) form." + strIndexName + ".value="
					+ pageIndex + "+value;\n");
			out.println("if ( !InputValid( form." + strRowNumName
					+ ", 1, \"int\", 1, 1,99,\"每页行数\")) {form." + strRowNumName
					+ ".focus(); return false;}form.submit();}\n");
			out.println("</SCRIPT>");
		}
		catch (Exception exp) {
			throw exp;
		}
	}

	/**
	 * 传入sql语句得到它的总记录数，并把他设置为属性m_nItems的值
	 * 
	 * @param sql
	 * @param conn
	 */
	public int getAndSetCount(String sql, Connection conn) {
		Statement st = null;
		ResultSet rt = null;
		int rows = 0;
		String sql1 = "select count(*) c from ( " + sql + " )";
		try {
			st = conn.createStatement();
			rt = st.executeQuery(sql1);
			if (rt.next())
				rows = rt.getInt("c");
		}
		catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		finally {
			try {
				if (null != rt) {
					rt.close();
					rt = null;
				}
				if (null != st) {
					st.close();
					st = null;
				}
			}
			catch (SQLException e) {
				e.printStackTrace();
			}
			this.m_nItems = rows;
		}
		return rows;
	}

	/**
	 * 传入sql语句得到它的总记录数，并把他设置为属性m_nItems的值
	 * 
	 * @param sql
	 * @param conn
	 */
	public int getAndSetCount(String sql, Connection conn, Object[] parameters) {
		PreparedStatement st = null;
		ResultSet rt = null;
		int rows = 0;
		String sql1 = "select count(*) c from ( " + sql + " ) exdb";
		try {
			st = conn.prepareStatement(sql1, ResultSet.TYPE_SCROLL_INSENSITIVE,
					ResultSet.CONCUR_READ_ONLY);
			// 动态设置参数
			if (parameters != null) {
				for (int i = 0; i < parameters.length; i++) {
					st.setObject(i + 1, parameters[i]);
				}
			}
			rt = st.executeQuery();
			if (rt.next())
				rows = rt.getInt("c");
		}
		catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		finally {
			try {
				if (null != rt) {
					rt.close();
					rt = null;
				}
				if (null != st) {
					st.close();
					st = null;
				}
			}
			catch (SQLException e) {
				e.printStackTrace();
			}
			this.m_nItems = rows;
		}
		return rows;
	}

	public int getM_nItems() {
		return m_nItems;
	}

	@SuppressWarnings("unchecked")
	public List getM_c() {
		return m_c;
	}

	public int getpageSize() {
		return pageSize;
	}

	public int getpageIndex() {
		return pageIndex;
	}

	public int getM_nPages() {
		return m_nPages;
	}

	public String getParitionLink() {
		StringBuffer sb = new StringBuffer();
		StringBuffer parm = new StringBuffer();
		Set<String> keys = parms.keySet();
		for (String key : keys) {
			parm.append(key + "=");
			parm.append(parms.get(key) + "&");
		}
		if (url != null && !"".equals(url)) {
			if (url.indexOf("?") == -1) {
				url = url + "?" + parm.toString();
			}
			else {
				url = url + "&" + parm.toString();
			}
		}
		else
			return "";
		String indexName = "pageIndex";
		if (pageIndexName != null) {
			indexName = pageIndexName;
		}
		String sizeName = "pageSize";
		if (pageSizeName != null) {
			sizeName = pageSizeName;
		}
		if (!url.endsWith("&") && !url.endsWith("?"))
			url = url + "&";
		String first = url + indexName + "=1" + "&" + sizeName + "=" + pageSize;
		String last = url + indexName + "=" + m_nPages + "&" + sizeName + "="
				+ pageSize;
		String up = url + indexName + "=" + (pageIndex - 1) + "&" + sizeName
				+ "=" + pageSize;
		String down = url + indexName + "=" + (pageIndex + 1) + "&" + sizeName
				+ "=" + pageSize;
		sb.append("<a href=\"" + first + "\">首页</a>&nbsp;");
		if (pageIndex == 1) {
			sb.append("上一页");
		}
		else {
			sb.append("<a href=\"" + up + "\">上一页</a>&nbsp;");
		}
		if (pageIndex == m_nPages) {
			sb.append("下一页");
		}
		else {
			sb.append("&nbsp;<a href=\"" + down + "\">下一页</a>");
		}
		sb.append("&nbsp;<a href=\"" + last + "\">尾页</a>&nbsp;&nbsp;");
		sb.append("页数:");
		sb.append(pageIndex);
		sb.append("/");
		sb.append(m_nPages);
		sb.append("&nbsp;&nbsp;每页显示");
		sb.append(pageSize);
		sb.append("条");
		paritionLink = sb.toString();
		return paritionLink;
	}

	// public String getYbparitionLink() {
	// StringBuffer sb = new StringBuffer();
	// StringBuffer parm = new StringBuffer();
	// Set<String> keys = parms.keySet();
	// for (String key : keys) {
	// parm.append(key + "=");
	// parm.append(parms.get(key) + "&");
	// }
	// if (url != null && !"".equals(url)) {
	//	
	// if (url.indexOf("?") == -1) {
	// url = url + "?" + parm.toString();
	// } else {
	// url = url + "&" + parm.toString();
	// }
	// }else
	// return "";
	// String indexName = "pageIndex";
	// if (pageIndexName != null) {
	// indexName = pageIndexName;
	// }
	// String sizeName = "pageSize";
	// if (pageSizeName != null) {
	// sizeName = pageSizeName;
	// }
	// if (!url.endsWith("&") && !url.endsWith("?"))
	// url = url + "&";
	// for(int i=1;i<=m_nPages;i++){
	// sb.append("<a href=\"");
	// sb.append(url + indexName + "="+i+ "&" + sizeName + "=" + pageSize);
	// if(i==pageIndex){
	// sb.append("\" class=\"currentPage\">");
	// }else{
	// sb.append("\" >");
	// }
	// if(i==1){
	// sb.append("<span>首页</span>");
	// }else if(i==m_nPages){
	// sb.append("<span>末页</span>");
	// }else{
	// sb.append("<span>"+i+"</span>");
	// }
	// sb.append("</a> ");
	// }
	// ybparitionLink = sb.toString();
	// return ybparitionLink;
	// }
	// public String getYbparitionLink() {
	// StringBuffer sb = new StringBuffer();
	// StringBuffer parm = new StringBuffer();
	// Set<String> keys = parms.keySet();
	// for (String key : keys) {
	// parm.append(key + "=");
	// parm.append(parms.get(key) + "&");
	// }
	// if (url != null && !"".equals(url)) {
	//	
	// if (url.indexOf("?") == -1) {
	// url = url + "?" + parm.toString();
	// } else {
	// url = url + "&" + parm.toString();
	// }
	// }else
	// return "";
	// String indexName = "pageIndex";
	// if (pageIndexName != null) {
	// indexName = pageIndexName;
	// }
	// String sizeName = "pageSize";
	// if (pageSizeName != null) {
	// sizeName = pageSizeName;
	// }
	// if (!url.endsWith("&") && !url.endsWith("?"))
	// url = url + "&";
	// if(pageIndex==1 && m_nPages!=1){
	// sb.append("<a href=\"");
	// sb.append(url + indexName + "=2"+ "&" + sizeName + "=" + pageSize);
	// sb.append("\" >");
	// sb.append("<span>下一页</span>");
	// sb.append("</a> ");
	// }else if(pageIndex==m_nPages && m_nPages!=1 ){
	// sb.append("<a href=\"");
	// sb.append(url + indexName + "="+(pageIndex-1)+ "&" + sizeName + "=" +
	// pageSize);
	// sb.append("\" >");
	// sb.append("<span>上一页</span>");
	// sb.append("</a> ");
	// }else if(pageIndex!=1 && pageIndex!= m_nPages){
	// sb.append("<a href=\"");
	// sb.append(url + indexName + "="+(pageIndex-1)+ "&" + sizeName + "=" +
	// pageSize);
	// sb.append("\" >");
	// sb.append("<span>上一页</span>");
	// sb.append("</a> ");
	// sb.append("<a href=\"");
	// sb.append(url + indexName + "="+(pageIndex+1)+ "&" + sizeName + "=" +
	// pageSize);
	// sb.append("\" >");
	// sb.append("<span>下一页</span>");
	// sb.append("</a> ");
	// }
	// ybparitionLink = sb.toString();
	// return ybparitionLink;
	// }
	public String getYbparitionLink() {
		if (this.m_nPages == 1) {
			return "";
		}
		StringBuffer sb = new StringBuffer();
		StringBuffer parm = new StringBuffer();
		Set<String> keys = parms.keySet();
		for (String key : keys) {
			parm.append(key + "=");
			parm.append(parms.get(key) + "&");
		}
		if (url != null && !"".equals(url)) {
			if (url.indexOf("?") == -1) {
				url = url + "?" + parm.toString();
			}
			else {
				url = url + "&" + parm.toString();
			}
		}
		else
			return "";
		String indexName = "pageIndex";
		if (pageIndexName != null) {
			indexName = pageIndexName;
		}
		String sizeName = "pageSize";
		if (pageSizeName != null) {
			sizeName = pageSizeName;
		}
		if (!url.endsWith("&") && !url.endsWith("?"))
			url = url + "&";
		if (m_nPages > 1) {
			// 首页			 
			if(pageIndex==1)
				sb.append("<a class=\"currentPage\" href=\"");
			else
				sb.append("<a href=\"");
			sb.append(url + indexName + "=1" + "&" + sizeName + "=" + pageSize);
			sb.append("\" >");
			sb.append("<span>首页</span>");
			sb.append("</a> ");
		}
		if (m_nPages > 2) {
			if (pageIndex > 1) {
				sb.append("<a href=\"");
				sb.append(url + indexName + "=" + (pageIndex - 1) + "&"
						+ sizeName + "=" + pageSize);
				sb.append("\" >");
				sb.append("<span>上页</span>");
				sb.append("</a> ");
			}
		}
		// 中间页数
		int endPage = pageIndex + this.distance - 1;
		if (endPage > m_nPages) {
			endPage = m_nPages;
		}
		int beginPage = endPage - this.distance + 1;
		if (beginPage < 0) {
			beginPage = 1;
		}
		for (int i = beginPage; i <= endPage; i++) {
			
			//当前页加样式
			if(pageIndex==i)
				sb.append("<a class=\"currentPage\" href=\"");
			else
				sb.append("<a href=\"");
			sb.append(url + indexName + "=" + i + "&" + sizeName + "="
					+ pageSize);
			sb.append("\" >");
			sb.append("<span>" + i + "</span>");
			sb.append("</a> ");
		}
		if (m_nPages > 2) {
			if (pageIndex < m_nPages) {
				sb.append("<a href=\"");
				sb.append(url + indexName + "=" + (pageIndex + 1) + "&"
						+ sizeName + "=" + pageSize);
				sb.append("\" >");
				sb.append("<span>下页</span>");
				sb.append("</a> ");
			}
		}
		if (m_nPages > 1) {
			//当前页加样式
			if(pageIndex==m_nPages)
				sb.append("<a class=\"currentPage\" href=\"");
			else
				sb.append("<a href=\"");
			sb.append(url + indexName + "=" + m_nPages + "&" + sizeName + "="
					+ pageSize);
			sb.append("\" >");
			sb.append("<span>末页</span>");
			sb.append("</a> ");
		}
		ybparitionLink = sb.toString();
		return ybparitionLink;
	}

	public void setYbparitionLink(String ybparitionLink) {
		this.ybparitionLink = ybparitionLink;
	}

	public void setParitionLink(String paritionLink) {
		this.paritionLink = paritionLink;
	}

	public Map<String, String> getParms() {
		return parms;
	}

	public void setParms(Map<String, String> parms) {
		this.parms = parms;
	}

	public String getUrl() {
		if (url == null)
			url = "";
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getPageSizeName() {
		return pageSizeName;
	}

	public void setPageSizeName(String pageSizeName) {
		this.pageSizeName = pageSizeName;
	}

	public String getPageIndexName() {
		return pageIndexName;
	}

	public void setPageIndexName(String pageIndexName) {
		this.pageIndexName = pageIndexName;
	}

	public int getDistance() {
		return distance;
	}

	public void setDistance(int distance) {
		this.distance = distance;
	}

	public String getErrorCode() {
		return errorCode;
	}

	public void setErrorCode(String errorCode) {
		this.errorCode = errorCode;
	}
}

<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<c:set var="path" value="<%=request.getContextPath() %>"/>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
  <head>
  	<title>welcome</title>
	<meta http-equiv="pragma" content="no-cache">
	<meta http-equiv="cache-control" content="no-cache">
	<meta http-equiv="expires" content="0">    
	<meta http-equiv="keywords" content="keyword1,keyword2,keyword3">
	<meta http-equiv="description" content="This is my page">
	<script type="text/javascript" src="${path}/jquery/jquery.mobile-1.4.5.min.js"></script>  </head>
  <body>
    imwoody 的 官方网站. <br>
    please <a href="${path}/wap/login/login_index">login</a>
    
    微信公众号：<img alt="weixin" src="${path}/images/myqrcode.jpg">
  </body>
</html>

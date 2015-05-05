<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ include file="./include/taglibs.jsp"%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
  <head>
    <title>登陆页面</title>
	<meta http-equiv="pragma" content="no-cache">
	<meta http-equiv="cache-control" content="no-cache">
	<meta http-equiv="expires" content="0">    
	<meta http-equiv="keywords" content="keyword1,keyword2,keyword3">
	<meta http-equiv="description" content="This is my page">
	<script type="text/javascript" src="${path}/jquery/jquery.mobile-1.4.5.min.js"></script>
	
	<script>
		function submit(){
			document.getElementById("form1").submit();
		}
	</script>
	
	
  </head>
  
  <body>
  	<form id="form1" action="${path}/wap/login/do_login" method="POST">
  	<input type="hidden" id="token" name="token" value="${token}" />
    <div>
    	<div>
			<span>用户名：</span>
			<span> 
				<input type="text" placeholder="请输入用户名" 
					name="username" id="username" maxlength=20 /> 
			</span>
			<p style="display: none" id="username_error">
				请正确输入用户名
			</p>
			
			<span>密码：</span>
			<span> 
				<input type="text" placeholder="请输入密码"
					name="password" id="password" maxlength=20 /> 
			</span>
			<p style="display: none" id="password_error">
				请正确输入密码
			</p>
			
			<a href="javascript:submit();" ><em>登陆</em></a>
			
		</div>
	</div>
	</form>
  </body>
</html>

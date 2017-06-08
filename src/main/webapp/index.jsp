<%@page import="org.springframework.web.servlet.support.RequestContext"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ page import="java.util.*"%>
<%@ include file="/taglibs.jsp"%>
<html>
<body>
<h2>Hello World! Smart Home</h2>
请求信息如下: <br>
 您的本地IP是：<%=request.getRemoteAddr()%><br>
 您的主机名字是:<%=request.getRemoteHost()%><br>
 您的访问端口是:<%=request.getRemotePort()%><br>
 服务器地址是:<%=request.getServerName()%><br>
 服务器所开放的端口是:<%=request.getServerPort()%><br>
 提交使用的方法是:<%=request.getMethod()%><br>
 请求的URL是:<%=request.getRequestURI()%><br>
 请求所用的协议是:<%=request.getProtocol()%><br>
 接收客户提交信息的路径:<%=request.getServletPath()%><br>
 客户请求信息的总长度:<%=request.getContentLength()%><br>
 请求中位于路径之后的查询字符串:<%=request.getQueryString()%><br>
 http头文件中user-agent的值:<%=request.getHeader("User-Agent")%><br>
 http头文件中accept的值:<%=request.getHeader("accept")%><br>
 http头文件中Host的值:<%=request.getHeader("Host")%><br>
 http头文件中accep-encoding的值是<%=request.getHeader("accept-encoding")%><br>
 头名字的一个枚举: 
<%
Enumeration e = request.getHeaderNames();
	while(e.hasMoreElements())
	{
		String key = (String)e.nextElement();
		out.println(key + "=========" + request.getHeader(key) + "<br />");
	}
 %>
</body>
</html>

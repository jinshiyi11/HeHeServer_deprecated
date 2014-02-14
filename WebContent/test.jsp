<?xml version="1.0" encoding="UTF-8" ?>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
<title>Insert title here</title>
</head>
<body>
<h1>JSP Info</h1>
<hr />
OS name: <%= System.getProperty("os.name") %>
<br />
OS version: <%= System.getProperty("os.version") %>
<br />
OS arch: <%= System.getProperty("os.arch") %>
<br />
User name: <%= System.getProperty("user.name") %>
<br />
User home: <%= System.getProperty("user.home") %>
<br />
User dir: <%= System.getProperty("user.dir") %>
<br />
User language: <%= System.getProperty("user.language") %>
<br />
User timezone: <%= System.getProperty("user.timezone") %>
<br />


<table align="center" width="600" cellpadding="2" cellspacing="1" border="0" bgcolor="#CCCCCC">
	<tr bgcolor="#FFFFFF">
		<td colspan="2" align="center">服务器信息</td>
	</tr>
	<tr bgcolor="#FFFFFF">
		<td width="300" align="center" class="datarows">服务器名</td>
		<td align="center" class="datarows"><%=request.getServerName()%></td>
	</tr>
	<tr bgcolor="#FFFFFF">
		<td width="300" align="center" class="datarows">服务器端口</td>
		<td align="center" class="datarows"><%=request.getServerPort()%></td>
	</tr>
	<tr bgcolor="#FFFFFF">
		<td width="300" align="center" class="datarows">操作系统</td>
		<td align="center" class="datarows"><%=System.getProperty("os.name") + " " + System.getProperty("os.version") + " " + System.getProperty("os.arch")%></td>
	</tr>
	<tr bgcolor="#FFFFFF">
		<td width="300" align="center" class="datarows">当前用户名</td>
		<td align="center" class="datarows"><%=System.getProperty("user.name")%></td>
	</tr>
	<tr bgcolor="#FFFFFF">
		<td width="300" align="center" class="datarows">当前用户目录</td>
		<td align="center" class="datarows"><%=System.getProperty("user.home")%></td>
	</tr>
	<tr bgcolor="#FFFFFF">
		<td width="300" align="center" class="datarows">当前用户工作目录</td>
		<td align="center" class="datarows"><%=System.getProperty("user.dir")%></td>
	</tr>
	<tr bgcolor="#FFFFFF">
		<td width="300" align="center" class="datarows">程序相对路径</td>
		<td align="center" class="datarows"><%=request.getRequestURI()%></td>
	</tr>
	<tr bgcolor="#FFFFFF">
		<td width="300" align="center" class="datarows">程序绝对路径</td>
		<td align="center" class="datarows"><%=request.getRealPath(request.getServletPath())%></td>
	</tr>
	<tr bgcolor="#FFFFFF">
		<td width="300" align="center" class="datarows">网络协议</td>
		<td align="center" class="datarows"><%=request.getProtocol()%></td>
	</tr>
	<tr bgcolor="#FFFFFF">
		<td width="300" align="center" class="datarows">服务器软件版本信息</td>
		<td align="center" class="datarows"><%=application.getServerInfo()%></td>
	</tr>
	<tr bgcolor="#FFFFFF">
		<td width="300" align="center" class="datarows">JDK版本</td>
		<td align="center" class="datarows"><%=System.getProperty("java.version")%></td>
	</tr>
	<tr bgcolor="#FFFFFF">
		<td width="300" align="center" class="datarows">JDK安装路径</td>
		<td align="center" class="datarows"><%=System.getProperty("java.home")%></td>
	</tr>
	<tr bgcolor="#FFFFFF">
		<td width="300" align="center" class="datarows">JAVA虚拟机版本</td>
		<td align="center" class="datarows"><%=System.getProperty("java.vm.specification.version")%></td>
	</tr>
	<tr bgcolor="#FFFFFF">
		<td width="300" align="center" class="datarows">JAVA虚拟机名</td>
		<td align="center" class="datarows"><%=System.getProperty("java.vm.name")%></td>
	</tr>
	<tr bgcolor="#FFFFFF">
		<td width="300" align="center" class="datarows">JAVA类路径</td>
		<td align="center" class="datarows"><%=System.getProperty("java.class.path")%></td>
	</tr>
	<tr bgcolor="#FFFFFF">
		<td width="300" align="center" class="datarows">JAVA载入库搜索路径</td>
		<td align="center" class="datarows"><%=System.getProperty("java.library.path")%></td>
	</tr>
	<tr bgcolor="#FFFFFF">
		<td width="300" align="center" class="datarows">JAVA临时目录</td>
		<td align="center" class="datarows"><%=System.getProperty("java.io.tmpdir")%></td>
	</tr>
	<tr bgcolor="#FFFFFF">
		<td width="300" align="center" class="datarows">JIT编译器名</td>
		<td align="center" class="datarows"><%=System.getProperty("java.compiler") == null ? "" : System.getProperty("java.compiler")%></td>
	</tr>
	<tr bgcolor="#FFFFFF">
		<td width="300" align="center" class="datarows">扩展目录路径</td>
		<td align="center" class="datarows"><%=System.getProperty("java.ext.dirs")%></td>
	</tr>
	<tr bgcolor="#FFFFFF">
		<td colspan="2" align="center">客户端信息</td>
	</tr>
	<tr bgcolor="#FFFFFF">
		<td width="300" align="center" class="datarows">客户机地址</td>
		<td align="center" class="datarows"><%=request.getRemoteAddr()%></td>
	</tr>
	<tr bgcolor="#FFFFFF">
		<td width="300" align="center" class="datarows">服务机器名</td>
		<td align="center" class="datarows"><%=request.getRemoteHost()%></td>
	</tr>
	<tr bgcolor="#FFFFFF">
		<td width="300" align="center" class="datarows">用户名</td>
		<td align="center" class="datarows"><%=request.getRemoteUser() == null ? "" : request.getRemoteUser()%></td>
	</tr>
	<tr bgcolor="#FFFFFF">
		<td width="300" align="center" class="datarows">请求方式</td>
		<td align="center" class="datarows"><%=request.getScheme()%></td>
	</tr>
	<tr bgcolor="#FFFFFF">
		<td width="300" align="center" class="datarows">应用安全套接字层</td>
		<td align="center" class="datarows"><%=request.isSecure() == true ? "是" : "否"%></td>
	</tr>
</table>

</body>
</html>
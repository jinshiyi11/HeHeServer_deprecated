<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<%@ page import="com.shuai.hehe.crawler.*" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Insert title here</title>
<script language="JavaScript"> setTimeout("location='crawler.jsp'", 500); </script>
</head>
<body>

<%
CrawlerThread crawlerThread=(CrawlerThread)application.getAttribute(CrawlerThread.Name);
if(crawlerThread!=null&&crawlerThread.isAlive()){
	String log=crawlerThread.getLog();
	out.write(log);
}else{
	crawlerThread=new CrawlerThread();
	application.setAttribute(CrawlerThread.Name, crawlerThread);
	crawlerThread.start();
}
%>
</body>
</html>
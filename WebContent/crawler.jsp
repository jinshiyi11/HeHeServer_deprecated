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

<%!
public boolean isRunning(ServletContext app){
	CrawlerThread crawlerThread=(CrawlerThread)app.getAttribute(CrawlerThread.Name);
	return crawlerThread!=null&&crawlerThread.isAlive();
}
%>

<form method="post" action="crawler.jsp">
<%
CrawlerThread crawlerThread=(CrawlerThread)application.getAttribute(CrawlerThread.Name);
String action=request.getParameter("action");
boolean running=isRunning(application);
if(action!=null){
	if(action.equals("start") && !running){
		crawlerThread=new CrawlerThread();
		application.setAttribute(CrawlerThread.Name, crawlerThread);
		crawlerThread.start();
	}else if(action.equals("stop") && running){
		//crawlerThread
	}
}

if(running){
%>
<button name="action" value="stop" onclick="form.submit();"></button>
<%}else %>
<button name="action" value="start" onclick="form.submit();"></button>
</form>

</body>
</html>
<%@ page language="java" contentType="text/html; charset=utf-8"
    pageEncoding="utf-8"%>
<%@ page import="com.shuai.hehe.server.Log" %>
ok
<%
//打印log
String query = request.getQueryString();
if (query != null) {
    Log.info("pay QueryString:",query);
}
%>
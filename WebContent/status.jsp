<?xml version="1.0" encoding="ISO-8859-1" ?>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<%@ page import="java.io.*" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1" />
<meta http-equiv="refresh" content="600"/>
<title>Insert title here</title>
<script language="JavaScript"> setTimeout("location='status.jsp'", 1000); </script>
</head>
<body>
<%
Process proc=(Process)request.getSession().getAttribute("process");
InputStream input = proc.getInputStream();


String result=null;

ByteArrayOutputStream output=new ByteArrayOutputStream();
byte[] buf = new byte[512];
int bytesRead;
try {
    if ((bytesRead = input.read(buf)) > 0) {
        output.write(buf, 0, bytesRead);
    }
} catch (IOException e) {
    // TODO Auto-generated catch block
    e.printStackTrace();
}

result=output.toString();

%>

<%= result%>
</body>
</html>
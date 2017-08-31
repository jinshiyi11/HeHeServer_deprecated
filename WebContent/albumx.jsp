<?xml version="1.0" encoding="UTF-8" ?>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ page import="com.shuai.hehe.server.data.*" %>
<%@ page import="java.sql.SQLException" %>
<%@ page import="java.util.ArrayList" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml" style="height: 100%;">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
<link rel="stylesheet" type="text/css" href="css/blueimp-gallery.min.css">
<style type="text/css">
.blueimp-gallery > .description {
  position: absolute;
  top: 30px;
  left: 15px;
  color: #fff;
  /*display: none;*/
}
.blueimp-gallery-controls > .description {
  display: block;
}
</style>
<%
DataManager mDataManager=DataManager.getInstance();
int feedId;
try {
    feedId = Integer.parseInt(request.getParameter("feedid"));
} catch (Exception e) {
    response.sendError(HttpServletResponse.SC_BAD_REQUEST);
    return;
}

try {
	Feed feed=mDataManager.getFeed(feedId);
    
%>
<title><%=feed.getTitle() %></title>
</head>
<body style="margin:0;height: 100%;">
<%   	
} catch (SQLException e) {
    //e.printStackTrace();
    e.printStackTrace(response.getWriter());
}
%>
<div id="blueimp-gallery-carousel" class="blueimp-gallery blueimp-gallery-carousel">
    <div class="slides"></div>
    <h3 class="title"></h3>
    <ol class="indicator"></ol>

</div>
<script src="js/blueimp-gallery.min.js"></script>
<script>

var gallery = [
                               'http://fmn.rrimg.com/fmn077/20150422/0940/large_LX8Q_1d40000121e41e84.jpg',
                               'http://fmn.rrimg.com/fmn076/20150422/0940/large_hlO0_91e2000125171e7f.jpg',
                               'http://g.hiphotos.baidu.com/image/pic/item/b2de9c82d158ccbfb221498c1bd8bc3eb03541e3.jpg'
                           ];
blueimp.Gallery(
		gallery,
    {
        container: '#blueimp-gallery-carousel',
        startSlideshow: false,
        carousel: true
    }
);
</script>

</body>
</html>
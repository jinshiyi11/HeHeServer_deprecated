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
<link rel="stylesheet" type="text/css" href="css/pgwslideshow.css">
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
<ul class="pgwSlideshow">
<%
ArrayList<PicInfo> data = mDataManager.getAlbumPics(feedId);
for(PicInfo info : data){
%>
<li><img src="<%=info.getBigPicUrl() %>" alt="<%=info.getPicDescription() %>"/></li>
<%   	
    }
} catch (SQLException e) {
    //e.printStackTrace();
    e.printStackTrace(response.getWriter());
}
%>
</ul>

<script src="js/jquery-1.11.2.min.js" type="text/javascript"></script>
<script type="text/javascript" src="js/pgwslideshow.js"></script>
<script type="text/javascript">
		$(document).ready(function() {
		    $('.pgwSlideshow').pgwSlideshow({
		    	transitionEffect:'sliding',
		    	autoSlide:false,
		    	displayList:false,
		    	displayControls:false
		    });
		});
		
function onImgLoaded(img)
{
	//console.log("original size:"+img.width+","+img.height);
	if(!img)
		return;
	
	var screenWidth=document.body.clientWidth;
	var screenHeight=document.body.clientHeight;
	if(img.width/img.height>screenWidth/screenHeight){
		img.style.width=screenWidth+'px';
		//img.width=screenWidth;	
	}else{
		img.style.height=screenHeight + 'px';
		//img.height=screenHeight;
	}
	
	//console.log("result size:"+img.width+","+img.height);
}
	</script>

</body>
</html>
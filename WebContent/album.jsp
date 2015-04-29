<?xml version="1.0" encoding="UTF-8" ?>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
<link rel="stylesheet" type="text/css" href="css/pgwslideshow.min.css">
<title>呵呵图片</title>
</head>
<body>
<ul class="pgwSlideshow">
    <li><img src="img/san-francisco.jpg" alt="San Francisco, USA" data-description="Golden Gate Bridge"></li>
    <li><img src="img/rio.jpg" alt="Rio de Janeiro, Brazil"></li>
    <li><img src="img/london.jpg" alt="" data-large-src="img/london.jpg"></li>
    <li><img src="img/new-york.jpg" alt=""></li>
    <li><img src="img/new-delhi.jpg" alt=""></li>
    <li><img src="img/paris.jpg" alt=""></li>
    <li><img src="img/sydney.jpg" alt=""></li>
    <li><img src="img/tokyo.jpg" alt=""></li>
    <li><img src="img/honk-kong.jpg" alt=""></li>
    <li><img src="img/dakar.jpg" alt=""></li>
    <li><img src="img/toronto.jpg" alt=""></li>
    <li>
        <a href="http://en.wikipedia.org/wiki/Monaco" target="_blank">
            <img src="img/monaco.jpg" alt="Monaco">
        </a>
    </li>
</ul>

<script src="js/jquery-1.11.2.min.js" type="text/javascript"></script>
<script type="text/javascript" src="js/pgwslideshow.min.js"></script>
<script type="text/javascript">
		$(document).ready(function() {
		    $('.pgwSlideshow').pgwSlideshow({
		    	transitionEffect:'sliding',
		    	autoSlide:false,
		    	displayList:true,
		    	displayControls:false
		    });
		});
	</script>

</body>
</html>
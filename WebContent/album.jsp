<?xml version="1.0" encoding="UTF-8" ?>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@page import="java.io.*" %>
<%@page import="java.net.*" %>
<%@page import="java.lang.reflect.*" %>
<%@page import="com.google.gson.reflect.*" %>
<%@ page import="com.shuai.hehe.server.data.*" %>
<%@ page import="com.shuai.hehe.util.*" %>
<%@ page import="java.util.*"%>
<%@ page import="com.google.gson.*"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<meta name="viewport" content="width=device-width, user-scalable=no"/>
<link rel="stylesheet" href="css/photoswipe.css">
<link rel="stylesheet" href="css/default-skin/default-skin.css">
<script src="js/photoswipe.min.js"></script>
<script src="js/photoswipe-ui-default.min.js"></script>
<script>
function GetURLParameter(sParam)
{
    var sPageURL = window.location.search.substring(1);
    var sURLVariables = sPageURL.split('&');
    for (var i = 0; i < sURLVariables.length; i++)
    {
        var sParameterName = sURLVariables[i].split('=');
        if (sParameterName[0] == sParam)
        {
            return sParameterName[1];
        }
    }
}​
</script>
</head>
<body>

<!-- Root element of PhotoSwipe. Must have class pswp. -->
<div class="pswp" tabindex="-1" role="dialog" aria-hidden="true">

    <!-- Background of PhotoSwipe. 
         It's a separate element, as animating opacity is faster than rgba(). -->
    <div class="pswp__bg"></div>

    <!-- Slides wrapper with overflow:hidden. -->
    <div class="pswp__scroll-wrap">

        <!-- Container that holds slides. PhotoSwipe keeps only 3 slides in DOM to save memory. -->
        <div class="pswp__container">
            <!-- don't modify these 3 pswp__item elements, data is added later on -->
            <div class="pswp__item"></div>
            <div class="pswp__item"></div>
            <div class="pswp__item"></div>
        </div>

        <!-- Default (PhotoSwipeUI_Default) interface on top of sliding area. Can be changed. -->
        <div class="pswp__ui pswp__ui--hidden">

            <div class="pswp__top-bar">

                <!--  Controls are self-explanatory. Order can be changed. -->

                <div class="pswp__counter"></div>

                <button class="pswp__button pswp__button--close" title="Close (Esc)"></button>

                <button class="pswp__button pswp__button--share" title="Share"></button>

                <button class="pswp__button pswp__button--fs" title="Toggle fullscreen"></button>

                <button class="pswp__button pswp__button--zoom" title="Zoom in/out"></button>

                <!-- Preloader demo http://codepen.io/dimsemenov/pen/yyBWoR -->
                <!-- element will get class pswp__preloader--active when preloader is running -->
                <div class="pswp__preloader">
                    <div class="pswp__preloader__icn">
                      <div class="pswp__preloader__cut">
                        <div class="pswp__preloader__donut"></div>
                      </div>
                    </div>
                </div>
            </div>

            <div class="pswp__share-modal pswp__share-modal--hidden pswp__single-tap">
                <div class="pswp__share-tooltip"></div> 
            </div>

            <button class="pswp__button pswp__button--arrow--left" title="Previous (arrow left)">
            </button>

            <button class="pswp__button pswp__button--arrow--right" title="Next (arrow right)">
            </button>

            <div class="pswp__caption">
                <div class="pswp__caption__center"></div>
            </div>

          </div>

        </div>

</div>
 <script>
var openPhotoSwipe = function() {
    var pswpElement = document.querySelectorAll('.pswp')[0];
    <%!
    private static class Pic {
      private int id;
      private String src;
      private int w;
      private int h;
    }
    %>
<%
int feedId;
try {
    feedId = Integer.parseInt(request.getParameter("feedid"));
} catch (Exception e) {
    response.sendError(HttpServletResponse.SC_BAD_REQUEST);
    return;
}

String json=HttpUtil.getData("http://hehedream.duapp.com/getalbumpics?feedid=15182&ver=1.3&channel=default");
Gson dataGson=new Gson();
Type type = new TypeToken<ArrayList<PicInfo>>(){}.getType();
ArrayList<PicInfo> data=dataGson.fromJson(json, type);

//DataManager mDataManager=new DataManager();
//ArrayList<PicInfo> data = mDataManager.getAlbumPics(feedId);
ArrayList<Pic> pics=new ArrayList<Pic>();
for(PicInfo item :data){
	Pic pic=new Pic();
	pic.src=item.getBigPicUrl();
	pics.add(pic);
}

Gson gson=new Gson();
int port=request.getServerPort();
String portPart="";
if(port!=80)
	portPart=":"+port;
String preUrl=request.getServerName()+portPart;

request.getContextPath()
out.write("var items ="+gson.toJson(pics));
%>
    
    // build items array
//     var items = [
//         {
//             src: 'https://farm2.staticflickr.com/1043/5186867718_06b2e9e551_b.jpg',
//             w: 0,
//             h: 0
//         },
//         {
//             src: 'https://farm7.staticflickr.com/6175/6176698785_7dee72237e_b.jpg',
//             w: 0,
//             h: 0
//         },
//         {
//             src: 'http://f.hiphotos.baidu.com/image/pic/item/64380cd7912397dd1c8f01055b82b2b7d0a28739.jpg',
//             w: 0,
//             h: 0
//         },
//         {
//             src: 'http://c.hiphotos.baidu.com/image/pic/item/a5c27d1ed21b0ef4f9945d53dfc451da81cb3ebb.jpg',
//             w: 0,
//             h: 0
//         },
//         {
//             src: 'http://c.hiphotos.baidu.com/image/pic/item/4a36acaf2edda3ccd630465e02e93901203f92fc.jpg',
//             w: 0,
//             h: 0
//         }
//     ];
    
    // define options (if needed)
    var options = {
        // history & focus options are disabled on CodePen        
        history: false,
        focus: false,

        showAnimationDuration: 0,
        hideAnimationDuration: 0
        
    };
    
    var gallery = new PhotoSwipe( pswpElement, PhotoSwipeUI_Default, items, options);
    
    gallery.listen('gettingData', function(index, item) {
        if(item.html === undefined && item.onloading === undefined && (item.w < 1 || item.h < 1)) {
            item.onloading = true;
            //if (item.w < 1 || item.h < 1) { // unknown size
            var img = new Image(); 
            img.onload = function() { // will get size after load
                item.w = this.width; // set image width
                item.h = this.height; // set image height
               gallery.invalidateCurrItems(); // reinit Items
               gallery.updateSize(true); // reinit Items
            }
            img.src = item.src; // let's download image
    }
});

    gallery.init();
};

//window.onload(openPhotoSwipe());
openPhotoSwipe();
</script>

</body>
</html>
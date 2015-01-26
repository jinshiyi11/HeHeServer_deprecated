package com.shuai.hehe.server;

import java.io.IOException;
import java.util.ArrayList;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.google.gson.Gson;
import com.shuai.hehe.server.data.VideoInfo;
import com.shuai.hehe.util.HttpUtil;

/**
 * Servlet implementation class GetVideoUrl
 */
public class GetVideoUrl extends HttpServlet {
	private static final String TAG=GetVideoUrl.class.getSimpleName();
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public GetVideoUrl() {
        super();
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		getVideoUrl(request, response);
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		getVideoUrl(request, response);
	}
	
	private void getVideoUrl(HttpServletRequest request, HttpServletResponse response) throws IOException, JSONException{
		response.setContentType("text/html; charset=UTF-8");
		String webUrl=request.getParameter("web_url");
		Log.info(TAG, webUrl);
		if(webUrl==null){
			response.sendError(HttpServletResponse.SC_BAD_REQUEST);
			return;
		}
		
		//http://vpkb.flvcd.com/remote/parse_kuaibo_new.php?url=http://www.56.com/u71/v_MTAzMjg3Nzcy.html
		String url="http://vpkb.flvcd.com/remote/parse_kuaibo_new.php?url="+webUrl;
		String data = HttpUtil.getData(url);
/**
{
source: "奇艺网",
url: "http://www.iqiyi.com/v_19rrnxa7f8.html",
title: "飞碟一分钟一分钟教你拒绝同事示爱 - 一分钟教你拒绝同事示爱[高清版]",
formatList: "标清|高清",
total_duration: "60",
swfUrl: "http://player.video.qiyi.com/5e6dc5cab761a57e8a93a59d5a7858c0/0/0/v_19rrnxa7f8.swf-albumId=340743800-tvId=340743800-isPurchase=0-cnId=1-autoPlay=1",
ts: ""m3u":"",
te: """,
TYPE: "CUSTOM",
V: [
{
C: "http://cache.m.iqiyi.com/jp/tmts/340743800/182669a6625f575f1dd6cf19bbd37404/?type=mp4&src=d846d0c32d664d32b6b54ea48997a589&sc=372219f74044f91597ff9f0cc63634db&t=1421048868809"
}
]
}


{
source: "我乐网",
url: "http://www.56.com/u71/v_MTAzMjg3Nzcy.html",
title: "【HQ首播】八三夭 钢铁人MV（超清MTV首播完整版）[超清版]",
formatList: "标清|高清|超清",
total_duration: "280.167",
swfUrl: "http://player.56.com/v_103287772.swf?auto_start=on",
TYPE: "DIRECT",
V: [
{
U: "http://f2.r.56.com/f2.c116.56.com/flvdownload/6/25/138793961610hd_super.flv?v=1&t=bis5HUqfuw63RxQ3FbXEMQ&r=43419&e=1421214447&tt=280&sz=38352208&vid=103287772",
FSIZE: "0"
}
]
}
 */
		Log.info(TAG, data);
		VideoInfo info=new VideoInfo();
		JSONObject json=new JSONObject(data);
		info.setTitle(json.getString("title"));
		info.setDuration(Double.parseDouble(json.getString("total_duration")));
		String type = json.getString("TYPE");
		
		ArrayList<VideoInfo.Part> parts=new ArrayList<VideoInfo.Part>();
		info.setParts(parts);
		if(type.equals("DIRECT")){
			JSONArray items = json.getJSONArray("V");
			for(int i=0;i<items.length();i++){
				VideoInfo.Part p=new VideoInfo.Part();
				JSONObject partJson=items.getJSONObject(i);
				p.setVideoUrl(partJson.getString("U"));				
				p.setDuration(Double.parseDouble(partJson.optString("duration","0")));
				parts.add(p);
			}
		}else{
			//TYPE为CUSTOM
			String customUrl=json.getString("C");
			String customData = HttpUtil.getData(url);
			JSONObject customJson=new JSONObject(data);
			VideoInfo.Part p=new VideoInfo.Part();
			p.setVideoUrl(customJson.getString("location"));
			p.setDuration(0);
			parts.add(p);
		}		
		
		Gson gson=new Gson();
		response.getWriter().write(gson.toJson(info));		
	}

}

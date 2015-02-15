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
情况一///////////////////////////////////////////////////////////////
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

-->C: "http://cache.m.iqiyi.com/jp/tmts/34...对应的内容如下
var tvInfoJs={"timestamp":"20150210180801","data":{"playInfo":"","vidl":[{"vd":1,"vid":"5325f11fe7e52b72e57ad927224c42ca"},{"vd":2,"vid":"182669a6625f575f1dd6cf19bbd37404"}],"adDuration":0,"clientIp":"218.30.116.4","ugc":0,"vd":1,"vid":"5325f11fe7e52b72e57ad927224c42ca","previewType":"","m3utx":"","tail":0,"ds":"A00012","cid":22,"tipType":"","m3u":"http://106.38.212.16/videos/v0/20150112/81/95/b0b9d793b478c0fd867346ec61c1a3c8.mp4?key=fe12779766583a3a&src=iqiyi.com&m=v&qd_src=ih5&qd_tm=1423562881689&qd_ip=218.30.116.4&qd_sc=dcedbbd1f475f47ad720cac1c541e2e0&ip=218.30.116.4&uuid=6a789609-54d9d881-e","ad":1,"head":0,"prv":"","platforms":["PC","PC_BAIDU","PC_BAIDU_SUB","PC_CARRIER_IQIYI","PC_APP","PAD","PAD_WEB_IQIYI","PHONE","PHONE_WEB_IQIYI","PHONE_CAS_IQIYI","TV"],"duration":60},"code":"A00000"}

情况二///////////////////////////////////////////////////////////
{
source: "乐视网",
url: "http://www.letv.com/ptv/vplay/21889025.html",
title: "10记三分！2月5日库里vs小牛狂轰51分集锦",
formatList: "标清|高清|超清",
total_duration: "389",
swfUrl: "http://i7.imgs.letv.com/player/swfPlayer.swf?id=21889025&autoplay=1",
ts: ""location": "",
te: """,
TYPE: "CUSTOM",
C: "http://g3.letv.com/vod/v2/MTI1LzkvNTUvbGV0di11dHMvMTQvdmVyXzAwXzIyLTMwNDg5NzgyMy1hdmMtMzE5MzAyLWFhYy0zMjAxMC0zODkyODktMTc1ODE0MjItN2VlY2QyMzk4MGE5ODRjMjc5YjhmODI1OWFiYTcxMWEtMTQyMzEyNDYwMjgzNS5tcDQ=?b=361&mmsid=26700248&tm=1423563909&key=6def6c42a5feab1d904fe5219b66ef73&platid=1&splatid=101&playid=0&tss=no&vtype=21&cvid=1593578470872&pip=3b062ad2bfa01b199e1408cab9cf2e76&termid=1&format=1&hwtype=un&ostype=Windows7&tag=letv&sign=letv&expect=3&pay=0&rateid=350",
V: [
{
FSIZE: "0"
}
]
}

-->C: "http://g3.letv.com/vod/v2/MTI...对应的内容如下
{
status: 200,
ercode: 0,
ipint: "3659428868",
remote: "218.30.116.4",
host: "218.30.116.4",
ipstart: "218.30.64.0",
ipend: "218.30.127.255",
geo: "CN.1.0.1",
desc: "中国-北京市-未知地区-电信",
country: "CN",
gone: 603,
buss: "bussid=100,alv=45,qos=4,host=1,port=80",
level: 66,
usep2p: 1,
flag: "0",
pool: "CTYUN-BJ-JA",
detail: "manual:820,603,607,875,891,804,822,872,826",
playlevel: 1,
slicetime: 311,
leavetime: 94,
expect: 3,
actual: 3,
needtest: 0,
curtime: 1423564066,
starttime: 0,
endtime: 0,
cliptime: 6,
timeshift: 168,
dir: "",
cdnpath: "",
livep2p: 1,
mustm3u8: 0,
livesftime: 60,
livesfmust: 0,
maxsftime: 200,
maxslicesize: 60,
forcegslb: 1200,
updatecdn: 1200,
privrange: 0,
openrange: 1,
downpolicy: 2,
identify: "1780933305",
location: "http://124.126.126.137/125/9/55/letv-uts/14/ver_00_22-304897823-avc-319302-aac-32010-389289-17581422-7eecd23980a984c279b8f8259aba711a-1423124602835.letv?crypt=31aa7f2e99&b=361&nlh=3072&nlt=45&bf=22&p2p=1&video_type=mp4&termid=1&tss=no&geo=CN-1-0-1&tm=1423575000&key=a7d84553f7233ce49e429a909b75e410&platid=1&splatid=101&its=0&keyitem=platid,splatid,its&ntm=1423575000&nkey=a7d84553f7233ce49e429a909b75e410&proxy=1032384088,1780933176&mmsid=26700248&playid=0&vtype=21&cvid=1609916828360&hwtype=un&ostype=Windows7&tag=letv&sign=letv&pay=0&rateid=350&errc=0&gn=603&buss=100&qos=4&cips=218.30.116.4",
nodelist: [
{
gone: 603,
name: "北京市-电信-1",
pid: 1,
aid: 9,
isp: 1,
detail: "manual:820,603,607,875,891,804,822,872,826",
playlevel: 1,
slicetime: 311,
leavetime: 94,
location: "http://124.126.126.137/125/9/55/letv-uts/14/ver_00_22-304897823-avc-319302-aac-32010-389289-17581422-7eecd23980a984c279b8f8259aba711a-1423124602835.letv?crypt=31aa7f2e99&b=361&nlh=3072&nlt=45&bf=22&p2p=1&video_type=mp4&termid=1&tss=no&geo=CN-1-0-1&tm=1423575000&key=a7d84553f7233ce49e429a909b75e410&platid=1&splatid=101&its=0&keyitem=platid,splatid,its&ntm=1423575000&nkey=a7d84553f7233ce49e429a909b75e410&proxy=1032384088,1780933176&mmsid=26700248&playid=0&vtype=21&cvid=1609916828360&hwtype=un&ostype=Windows7&tag=letv&sign=letv&pay=0&rateid=350&errc=0&gn=603&buss=100&qos=4&cips=218.30.116.4"
},
{
gone: 607,
name: "北京市-电信-2",
pid: 1,
aid: 9,
isp: 1,
detail: "manual:820,603,607,875,891,804,822,872,826",
playlevel: 1,
slicetime: 480,
leavetime: 120,
location: "http://124.126.255.73/125/9/55/letv-uts/14/ver_00_22-304897823-avc-319302-aac-32010-389289-17581422-7eecd23980a984c279b8f8259aba711a-1423124602835.letv?crypt=31aa7f2e121&b=361&nlh=3072&nlt=45&bf=27&p2p=1&video_type=mp4&termid=1&tss=no&geo=CN-1-0-1&tm=1423575000&key=a7d84553f7233ce49e429a909b75e410&platid=1&splatid=101&its=0&keyitem=platid,splatid,its&ntm=1423575000&nkey=a7d84553f7233ce49e429a909b75e410&proxy=1032384088,1780933176&mmsid=26700248&playid=0&vtype=21&cvid=1609916828360&hwtype=un&ostype=Windows7&tag=letv&sign=letv&pay=0&rateid=350&errc=0&gn=607&buss=100&qos=4&cips=218.30.116.4"
},
{
gone: 804,
name: "山东省-电信-1",
pid: 15,
aid: 186,
isp: 1,
detail: "manual:820,603,607,875,891,804,822,872,826",
playlevel: 1,
slicetime: 154,
leavetime: 70,
location: "http://58.59.3.17/125/9/55/letv-uts/14/ver_00_22-304897823-avc-319302-aac-32010-389289-17581422-7eecd23980a984c279b8f8259aba711a-1423124602835.letv?crypt=31aa7f2e81&b=361&nlh=3072&nlt=45&bf=18&p2p=1&video_type=mp4&termid=1&tss=no&geo=CN-1-0-1&tm=1423575000&key=a7d84553f7233ce49e429a909b75e410&platid=1&splatid=101&its=0&keyitem=platid,splatid,its&ntm=1423575000&nkey=a7d84553f7233ce49e429a909b75e410&proxy=976790801,1032384088&mmsid=26700248&playid=0&vtype=21&cvid=1609916828360&hwtype=un&ostype=Windows7&tag=letv&sign=letv&pay=0&rateid=350&errc=0&gn=804&buss=100&qos=4&cips=218.30.116.4"
}
]
}

情况三//////////////////////////////////////////////////////

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
			if(json.has("C")){
				String customUrl=json.getString("C");
				String customData = HttpUtil.getData(customUrl);
				JSONObject customJson=new JSONObject(customData);
				VideoInfo.Part p=new VideoInfo.Part();
				p.setVideoUrl(customJson.getString("location"));
				p.setDuration(0);
				parts.add(p);
			}else{
				String customUrl=json.getJSONArray("V").getJSONObject(0).getString("C");
				String customData = HttpUtil.getData(customUrl);
				
				int start=0;
				start=customData.indexOf('{');
				if(start>=0){
					customData=customData.substring(start);
					Log.info(TAG+" custom:", customData);
					JSONObject customJson=new JSONObject(customData);
					VideoInfo.Part p=new VideoInfo.Part();
					p.setVideoUrl(customJson.getJSONObject("data").getString("m3u"));
					p.setDuration(0);
					parts.add(p);
				}
			}
		}		
		
		Gson gson=new Gson();
		response.getWriter().write(gson.toJson(info));		
	}

}

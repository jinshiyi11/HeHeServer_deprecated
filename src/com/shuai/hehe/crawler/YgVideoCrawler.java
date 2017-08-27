package com.shuai.hehe.crawler;

import java.io.IOException;
import java.util.List;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.google.gson.annotations.SerializedName;
import com.google.gson.reflect.TypeToken;
import com.shuai.hehe.crawler.data.Constants;
import com.shuai.hehe.crawler.data.DataManager;
import com.shuai.hehe.crawler.data.FromType;
import com.shuai.hehe.crawler.data.VideoInfo;
import com.shuai.hehe.crawler.data.YgVideoInfo;

/**
 * 阳光宽屏网视频爬虫 http://www.365yg.com/
 */
public class YgVideoCrawler {
	public static final String URL_HOST="http://www.365yg.com";
	
	/**
	 * 爬虫的起始url
	 */
	private String mStartUrl;

	/**
	 * 已经爬过的视频数
	 */
	private int mVideoCount;

	/**
	 * 已经爬过的页面数
	 */
	private int mPageCount;

	private static final int MAX_VIDEO_COUNT = 1000;
	
	
	/**
	 * http://www.365yg.com/api/pc/feed/?category=video&utm_source=toutiao&max_behot_time=1503460000&widen=1
	 */
	private static final String URL_FORMAT="http://www.365yg.com/api/pc/feed/?category=video&utm_source=toutiao&max_behot_time=%d&widen=1";

	public YgVideoCrawler(String startUrl) {
		mStartUrl = startUrl;
	}

	public void start() {
		String url = mStartUrl;
		if (url != null && url.length() > 0) {
			url = getVideos(url);
		}

		System.out.println("video crawler finished!!");

	}

	/**
	 * 获取该页面包含的视频
	 * 
	 * @param url
	 *            从该页面爬取视频
	 */
	private String getVideos(String url) {
		if (url == null)
			new IllegalArgumentException();

		url = url.trim();
		if (url.isEmpty())
			throw new IllegalArgumentException();

		System.out.println(String.format("正在爬取视频列表，url:%s", url));

		++mPageCount;

		// if (mPageCount > 10)
		// return;

		try {
			OkHttpClient client = new OkHttpClient();
			Request request = new Request.Builder().url(url).build();
			Response response = client.newCall(request).execute();
			String body=response.body().string();
			JsonParser parser=new JsonParser();
			JsonElement dataArrayElement = parser.parse(body).getAsJsonObject().get("data");
			Gson gson=new Gson();
			List<YgVideoInfo> items = gson.fromJson(dataArrayElement, new TypeToken<List<YgVideoInfo>>() {}.getType());
			for (YgVideoInfo item : items) {
				System.out.println(item);
				DataManager.getInstance().addVideo(convert2VideoInfo(item));
			}
			if(items.size()>0){
				long time=items.get(items.size()-1).getHotTime();
				getVideos(String.format(YgVideoCrawler.URL_FORMAT, time));
			}
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

		return null;
	}
	
	private VideoInfo convert2VideoInfo(YgVideoInfo info){
		VideoInfo videoInfo=new VideoInfo();
		videoInfo.mVideoId=info.getId();
		videoInfo.mTitle=info.getTitle();
		videoInfo.mVideoThumbUrl=info.getVideoThumbUrl();
		videoInfo.mFromType=FromType.FROM_365YG;
		videoInfo.mWebVideoUrl=info.getWebVideoUrl();
		videoInfo.mVideoUrl=info.getVideoUrl();
		
		return videoInfo;
	}
	
//	private String getVideoUrl(YgVideoInfo info){
//		String result=null;
//		
//		Document doc;
//		try {
//			doc = Jsoup.connect(info.getWebVideoUrl()).timeout(Constants.JSOUP_TIMEOUT).get();
//			//System.out.print(doc);
//			Element item = doc.getElementById("tt-video");
//			result=item.attr("src");
//		}catch(Exception e){
//			e.printStackTrace();
//		}
//		
//		System.out.print("视频地址:"+result);
//		return result;
//	}

	public static void main(String[] args){
		long time=System.currentTimeMillis()/1000;
		String url=String.format(YgVideoCrawler.URL_FORMAT, time);
		YgVideoCrawler crawler=new YgVideoCrawler(url);
		crawler.start();
		System.out.println();		
	}
}

package com.shuai.hehe.crawler;

import java.io.IOException;
import java.util.List;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.google.gson.reflect.TypeToken;
import com.shuai.hehe.crawler.data.FromType;
import com.shuai.hehe.crawler.data.VideoInfo;
import com.shuai.hehe.crawler.data.YgVideoInfo;

/**
 * 阳光宽屏网视频爬虫 http://www.365yg.com/
 */
public class YgVideoCrawler {
	public static final String URL_HOST = "http://www.365yg.com";

	/**
	 * 爬虫的起始url
	 */
	private String mStartUrl;

	/**
	 * 已经爬过的视频数
	 */
	private int mVideoCount;

	private boolean mStop;

	private ICrawlerCallback mCrawlerCallback;

	private static final int MAX_VIDEO_COUNT = 1000;

	/**
	 * http://www.365yg.com/api/pc/feed/?category=video&utm_source=toutiao&
	 * max_behot_time=1503460000&widen=1
	 */
	public static final String URL_FORMAT = "http://www.365yg.com/api/pc/feed/?category=video&utm_source=toutiao&max_behot_time=%d&widen=1";
	
	private OkHttpClient mClient = new OkHttpClient();

	public YgVideoCrawler(String startUrl, ICrawlerCallback callback) {
		mStartUrl = startUrl;
		mCrawlerCallback = callback;
	}

	public void start() {
		String url = mStartUrl;
		List<YgVideoInfo> videos;
		do {
			videos = getVideos(url);
			if (videos != null && videos.size() > 0) {
				for (YgVideoInfo item : videos) {
					mCrawlerCallback.addLog(item.toString());
					mCrawlerCallback.addVideo(convert2VideoInfo(item));
					mVideoCount++;
				}
				long time = videos.get(videos.size() - 1).getHotTime();
				url = String.format(YgVideoCrawler.URL_FORMAT, time);
			}
		} while (videos != null && videos.size() > 0 && mVideoCount<MAX_VIDEO_COUNT);

		mCrawlerCallback.addLog("video crawler finished!!");
	}

	public void stop() {
		mStop = true;
	}

	/**
	 * 获取该页面包含的视频
	 * 
	 * @param url
	 *            从该页面爬取视频
	 */
	private List<YgVideoInfo> getVideos(String url) {
		List<YgVideoInfo> result = null;
		if (url == null)
			new IllegalArgumentException();

		url = url.trim();
		if (url.isEmpty())
			throw new IllegalArgumentException();

		if (mStop) {
			return result;
		}

		mCrawlerCallback.addLog(String.format("正在爬取视频列表，url:%s", url));

		try {
			;
			Request request = new Request.Builder().url(url).build();
			Response response = mClient.newCall(request).execute();
			String body = response.body().string();
			JsonParser parser = new JsonParser();
			JsonElement dataArrayElement = parser.parse(body).getAsJsonObject()
					.get("data");
			Gson gson = new Gson();
			result = gson.fromJson(dataArrayElement,
					new TypeToken<List<YgVideoInfo>>() {
					}.getType());

		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

		return result;
	}

	private VideoInfo convert2VideoInfo(YgVideoInfo info) {
		VideoInfo videoInfo = new VideoInfo();
		videoInfo.mVideoId = info.getId();
		videoInfo.mTitle = info.getTitle();
		videoInfo.mVideoThumbUrl = info.getVideoThumbUrl();
		videoInfo.mFromType = FromType.FROM_365YG;
		videoInfo.mWebVideoUrl = info.getWebVideoUrl();
		videoInfo.mVideoUrl = info.getVideoUrl();

		return videoInfo;
	}

	// private String getVideoUrl(YgVideoInfo info){
	// String result=null;
	//
	// Document doc;
	// try {
	// doc =
	// Jsoup.connect(info.getWebVideoUrl()).timeout(Constants.JSOUP_TIMEOUT).get();
	// //System.out.print(doc);
	// Element item = doc.getElementById("tt-video");
	// result=item.attr("src");
	// }catch(Exception e){
	// e.printStackTrace();
	// }
	//
	// System.out.print("视频地址:"+result);
	// return result;
	// }

	public static void main(String[] args) {
		long time = System.currentTimeMillis() / 1000;
		String url = String.format(YgVideoCrawler.URL_FORMAT, time);
		YgVideoCrawler crawler = new YgVideoCrawler(url, new CrawlerMananger());
		crawler.start();
		System.out.println();
	}
}

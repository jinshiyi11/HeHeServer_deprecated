package com.shuai.hehe.crawler.data;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.google.gson.annotations.SerializedName;
import com.shuai.hehe.crawler.YgVideoCrawler;

/**
 * 阳光宽屏网视频信息
 */
public class YgVideoInfo {
	@SerializedName("video_id")
	private String mId;
	
	/**
	 * 标题
	 */
	@SerializedName("title")
	private String mTitle;
	
	/**
	 * 视频图片的url
	 * 如：//p3.pstatp.com/list/190x124/37490011d61e18517e42
	 * 大图为：https://p3.pstatp.com/video1609/37490011d61e18517e42
	 */
	@SerializedName("image_url")
	private String mVideoThumbUrl;
	
	/**
	 * 视频对应的web页
	 * 相对路径，如/group/6457681534768906765/
	 */
	@SerializedName("source_url")
	private String mWebVideoUrl;
	
	
	/**
     * 视频地址，如MP4文件的地址
     */
	@SerializedName("video_url")
	private String mVideoUrl;
	
	/**
	 * 时长字符串
	 * 如：02:42
	 */
	@SerializedName("video_duration_str")
	private String mDuration;
	
	/**
	 * 发布时间
	 * 如：1503580794，对应unix时间
	 */
	@SerializedName("behot_time")
	private long mHotTime;
	
	//把//p3.pstatp.com/list/190x124替换为//p3.pstatp.com/video1609
	private static Pattern sPattern=Pattern.compile("(//[^/]+/)[^/]+/\\d+x\\d+");

	public String getId() {
		return mId;
	}

	public String getTitle() {
		return mTitle;
	}

	public String getVideoThumbUrl() {
		if(mVideoThumbUrl!=null){
			return "http:"+convertUrl(mVideoThumbUrl);
		}else{
		return mVideoThumbUrl;
		}
	}

	public String getWebVideoUrl() {
		if (mWebVideoUrl != null) {
			return YgVideoCrawler.URL_HOST + mWebVideoUrl;
		} else {
			return mWebVideoUrl;
		}
	}
	
	public String getVideoUrl() {
		return mVideoUrl;
	}

	public void setVideoUrl(String videoUrl) {
		this.mVideoUrl = videoUrl;
	}

	private static String convertUrl(final String url){
		String result=url;
		Matcher matcher = sPattern.matcher(url); 
		result=matcher.replaceFirst("$1video1609");
		return result;
	}

	public String getDuration() {
		return mDuration;
	}

	public long getHotTime() {
		return mHotTime;
	}

	@Override
	public String toString() {
		return "YgVideoInfo [mId=" + mId + ", mTitle=" + mTitle
				+ ", mVideoThumbUrl=" + mVideoThumbUrl + ", mWebVideoUrl="
				+ mWebVideoUrl + ", mDuration=" + mDuration + ", mHotTime="
				+ mHotTime + "]";
	}	
	
}

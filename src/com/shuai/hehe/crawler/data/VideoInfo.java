package com.shuai.hehe.crawler.data;

/**
 *
 */
public class VideoInfo {
	public String mVideoId;
	/**
	 * 视频名
	 */
	public String mTitle;
	
	/**
	 * 视频预览图url
	 */
	public String mVideoThumbUrl;
	
	/**
	 * flash格式视频的url
	 */
	public String mFlashVideoUrl;
	
	/**
     * 视频对应的web页面，该页面不仅包含视频还包含评论，广告等其它东西
     */
    public String mWebVideoUrl;
    
    /**
     * 视频地址，如MP4文件的地址
     */
    public String mVideoUrl;
	
	/**
	 * 新鲜事来源
	 */
	public int mFromType=FromType.FROM_RENREN;

	@Override
	public String toString() {
		return "VideoInfo [mTitle=" + mTitle + ", mVideoThumbUrl="
				+ mVideoThumbUrl + ", mFlashVideoUrl=" + mFlashVideoUrl
				+ ", mWebVideoUrl=" + mWebVideoUrl + ", mFromType=" + mFromType
				+ "]";
	}

}

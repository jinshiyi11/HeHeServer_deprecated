package com.shuai.hehe.crawler;

import java.util.concurrent.Executor;

import com.shuai.hehe.crawler.data.AlbumInfo;
import com.shuai.hehe.crawler.data.CrawlerBlogInfo;
import com.shuai.hehe.crawler.data.VideoInfo;

public interface ICrawlerCallback{
	Executor getExecutor();
	void addLog(String message);
	void addAlbum(AlbumInfo info);
	void addVideo(VideoInfo info);
	void addBlog(CrawlerBlogInfo info);
}

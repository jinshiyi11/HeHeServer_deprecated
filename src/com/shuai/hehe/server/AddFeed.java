package com.shuai.hehe.server;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.gson.Gson;
import com.shuai.hehe.crawler.data.AlbumInfo;
import com.shuai.hehe.crawler.data.CrawlerBlogInfo;
import com.shuai.hehe.crawler.data.VideoInfo;
import com.shuai.hehe.server.data.Constants;
import com.shuai.hehe.server.data.DataManager;
import com.shuai.hehe.server.data.FeedType;

/**
 * 接收爬虫抓取的新鲜事并插入数据库
 */
public class AddFeed extends BaseServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public AddFeed() {
        super();
    }

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String sessionKey=request.getParameter(Constants.ADMIN_KEY_NAME);
		if (sessionKey == null || !sessionKey.equals(Constants.ADMIN_KEY_VALUE)) {
			response.sendError(HttpServletResponse.SC_UNAUTHORIZED);
		}
		
		int feedType=Integer.parseInt(request.getParameter("feedtype"));
		String data=getPostData(request);
		switch (feedType) {
		case FeedType.TYPE_ALBUM:
		{
			Gson gson=new Gson();
			AlbumInfo feed = gson.fromJson(data, AlbumInfo.class);
			DataManager.getInstance().addAlbum(feed);
			break;
		}
		case FeedType.TYPE_VIDEO:
		{
			Gson gson=new Gson();
			VideoInfo feed = gson.fromJson(data, VideoInfo.class);
			DataManager.getInstance().addVideo(feed);
			break;
		}
		case FeedType.TYPE_BLOG:
		{
			Gson gson=new Gson();
			CrawlerBlogInfo feed = gson.fromJson(data, CrawlerBlogInfo.class);
			DataManager.getInstance().addBlog(feed);
			break;
		}
		default:
			break;
		}
	}

}

package com.shuai.hehe.crawler;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import com.shuai.hehe.crawler.data.AlbumInfo;
import com.shuai.hehe.crawler.data.CrawlerBlogInfo;
import com.shuai.hehe.crawler.data.VideoInfo;
import com.shuai.hehe.server.data.BlogInfo;
import com.shuai.hehe.server.data.DataManager;
import com.shuai.hehe.server.data.FeedType;

public class CrawlerMananger implements ICrawlerCallback {
	private static final int CORE_POOL_SIZE = 3;
	private static final int MAXIMUM_POOL_SIZE = 5;// 6;
	private static final int KEEP_ALIVE = 60;

	private ExecutorService mExecutor;
	private Random mRandom = new Random();
	private PicCrawler mPicCrawler;
	private YgVideoCrawler mVideoCrawler;

	private LinkedBlockingQueue<AlbumInfo> mAlbumInfos = new LinkedBlockingQueue<AlbumInfo>();
	private LinkedBlockingQueue<VideoInfo> mVideoInfos = new LinkedBlockingQueue<VideoInfo>();
	private LinkedBlockingQueue<CrawlerBlogInfo> mBlogInfos = new LinkedBlockingQueue<CrawlerBlogInfo>();

	private List<String> mLogList = new ArrayList<>();
	private int mLogStartIndex = 0;

	public static class LogInfo {
		public int logStartIndex;
		public String log;
	}

	public CrawlerMananger() {
	}

	public synchronized void start() {
		if (mExecutor != null) {
			return;
		}

		mExecutor = new ThreadPoolExecutor(CORE_POOL_SIZE, MAXIMUM_POOL_SIZE,
				KEEP_ALIVE, TimeUnit.SECONDS,
				new LinkedBlockingQueue<Runnable>(Integer.MAX_VALUE));

		mExecutor.execute(new Runnable() {
			@Override
			public void run() {
				Thread.currentThread().setName("DB Thread");
				processFeed();
			}
		});

		mPicCrawler = new PicCrawler("http://share.renren.com/albumlist/10651",
				this);
		mExecutor.execute(new Runnable() {
			@Override
			public void run() {
				mPicCrawler.start();
			}
		});

		long time = System.currentTimeMillis() / 1000;
		String url = String.format(YgVideoCrawler.URL_FORMAT, time);
		mVideoCrawler = new YgVideoCrawler(url, this);
		mExecutor.execute(new Runnable() {
			@Override
			public void run() {
				mVideoCrawler.start();
			}
		});
	}

	public synchronized void stop() {
		if (mExecutor == null) {
			return;
		}

		if (mPicCrawler != null) {
			mPicCrawler.stop();
		}
		if (mVideoCrawler != null) {
			mVideoCrawler.stop();
		}

		try {
			// TODO:shutdown干了什么?
			mExecutor.shutdown();
			// mExecutor.awaitTermination(Long.MAX_VALUE, TimeUnit.DAYS);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void addLog(String message) {
		if (message == null) {
			return;
		}

		synchronized (mLogList) {
			mLogList.add(message);
			if (mLogList.size() > 300) {
				mLogList.remove(0);
				mLogStartIndex++;
			}
		}

		System.out.println(message);
	}

	public LogInfo getLog(int start) {
		LogInfo result = new LogInfo();
		synchronized (mLogList) {
			int startIndex = start - mLogStartIndex;
			int endIndex = mLogList.size();

			if (startIndex < 0) {
				startIndex = 0;
			} else if (startIndex > endIndex) {
				startIndex = endIndex;
			}

			result.logStartIndex = mLogStartIndex + mLogList.size();
			List<String> logList = mLogList.subList(startIndex, endIndex);
			StringBuilder sb = new StringBuilder();
			for (String log : logList) {
				sb.append(log).append("\n");
			}
			result.log = sb.toString();
		}

		return result;
	}

	public Executor getExecutor() {
		int queueSize = ((ThreadPoolExecutor) mExecutor).getQueue().size();
		System.out.println("CrawlerMananger queue size:" + queueSize);
		if (queueSize > 500) {
			try {
				Thread.currentThread().sleep(50);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return mExecutor;
	}

	public void addAlbum(AlbumInfo info) {
		try {
			mAlbumInfos.put(info);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void addVideo(VideoInfo info) {
		try {
			mVideoInfos.put(info);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	public void addBlog(CrawlerBlogInfo info) {
		try {
			mBlogInfos.put(info);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	/**
	 * 根据预定的比例获取应该插入的feed类型
	 * 
	 * @return
	 */
	private int chooseFeed() {
		int result;
		int num = mRandom.nextInt(10);
		if (num <= 1) {
			result = FeedType.TYPE_ALBUM;
		} else {
			result = FeedType.TYPE_VIDEO;
		}

		/*
		 * else if (num <= 9) { result = FeedType.TYPE_VIDEO; } else { result =
		 * FeedType.TYPE_BLOG; }
		 */

		return result;
	}

	private void processFeed() {
		while (true) {
			try {
				int type = chooseFeed();
				// type=FeedType.TYPE_ALBUM;
				switch (type) {
				case FeedType.TYPE_ALBUM: {
					AlbumInfo info = mAlbumInfos.poll(10, TimeUnit.MINUTES);
					if (info != null) {
						addLog("添加相册:"+info);
						// FeedUploader.getInstance().addFeed(info);
						DataManager.getInstance().addAlbum(info);
					}
					break;
				}
				case FeedType.TYPE_VIDEO: {
					VideoInfo info = mVideoInfos.poll(10, TimeUnit.MINUTES);
					if (info != null) {
						addLog("添加视频:"+info);
						// FeedUploader.getInstance().addFeed(info);
						DataManager.getInstance().addVideo(info);
					}
					break;
				}
				case FeedType.TYPE_BLOG: {
					CrawlerBlogInfo info = mBlogInfos.poll(10, TimeUnit.MINUTES);
					if (info != null) {
						addLog("添加blog:"+info);
						// FeedUploader.getInstance().addFeed(info);
						DataManager.getInstance().addBlog(info);
					}
					break;
				}
				default:
					break;
				}

			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}
	}

}

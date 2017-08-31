package com.shuai.hehe.crawler;

import java.io.IOException;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import com.shuai.hehe.crawler.data.AlbumInfo;
import com.shuai.hehe.crawler.data.AlbumInfo.CrawlerPicInfo;
import com.shuai.hehe.crawler.data.Constants;
import com.shuai.hehe.crawler.data.FromType;
import com.shuai.hehe.server.data.DataManager;

/**
 * 热门相册爬虫
 */
public class PicCrawler {

	/**
	 * 爬虫的起始url
	 */
	private String mStartUrl;

	/**
	 * 已经爬过的相册数
	 */
	private int mAlbumCount;

	/**
	 * 已经爬过的页面数
	 */
	private int mPageCount;
	
	private boolean mStop; 
	
	private static int MAX_ALBUM_COUNT=9000;
	
	private ICrawlerCallback mCrawlerCallback;
	
    private class SubPicCrawler implements Runnable {
        String title;
        String href;
        String thumbUrl;

        public SubPicCrawler(String href, String title, String thumbUrl) {
            this.href = href;
            this.title = title;
            this.thumbUrl = thumbUrl;
        }

        @Override
        public void run() {
        	if(mStop){
        		return;
        	}
        	
            Thread.currentThread().setName("SubPicCrawler");
            AlbumInfo albumInfo = getAlbumInfo(href, title, thumbUrl);
            if (albumInfo.mPics.size() == 0) {
                System.err.println("empty album:"+albumInfo.mTitle);
            } else {
                mCrawlerCallback.addAlbum(albumInfo);
            }
        }
        
    }

	public PicCrawler(String startUrl,ICrawlerCallback callback) {
		mStartUrl = startUrl;
		mCrawlerCallback = callback;
	}

	public void start() {
	    String url=mStartUrl;
        while (!mStop && url!=null && url.length()>0) {
            url=getAlbums(url);
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        
        mCrawlerCallback.addLog("pic crawler finished!!");
	}
	
	public void stop(){
		mStop=true;
	}

	/**
	 * 获取该页面包含的相册
	 * @param url 从该页面爬取相册
	 */
	private String getAlbums(String url) {
		if (url == null)
			new IllegalArgumentException();

		url = url.trim();
		if (url.isEmpty())
			throw new IllegalArgumentException();
		
		mCrawlerCallback.addLog(String.format("正在爬取相册列表，url:%s",url));

		++mPageCount;

//		if (mPageCount > 10)
//			return;

		Document doc;
		try {
			doc = Jsoup.connect(url).userAgent(Constants.USER_AGENT)
//			        .cookie("p", "33fb45a605c8ce65e63595467994811f8")
//			        .cookie("t", "35aad5365c66978703f1448e65f90aa28")
			        .timeout(Constants.JSOUP_TIMEOUT).get();
			// System.out.print(doc);
			Elements items = doc.select(".share-hot-list .share.clearfix");
			
			//mCrawlerCallback.addLog("pagecount:" + items.size());
            for (Element element : items) {
                Element link = element.select("h3 a[href]").get(0);
                //TODO:check
                String title = link.ownText();
                if(DataManager.getInstance().isFeedExist(title))
                    continue;
                
                String href = link.attr("href");

                Element img = element.select(".content .photos img").get(0);
                String thumbUrl = img.attr("src");
                if (href.startsWith("http://share.renren.com/")) {
                    //mCrawlerCallback.addLog(title+href);
                    //mCrawlerCallback.addLog("count:" + ++mAlbumCount);
                    
                    mCrawlerCallback.getExecutor().execute(new SubPicCrawler(href, title, thumbUrl));
                    
                    if(mAlbumCount>=MAX_ALBUM_COUNT)
                        return null;
                }

            }
			

			Elements elements = doc.select(".pagerpro a[title=下一页]");
			// check element count,==1.if >1 ?
			if (elements.size() > 0) {
				Element nextPageElement = elements.get(0);
				String nextPageUrl = nextPageElement.attr("href");

				// mCrawlerCallback.addLog();
				// mCrawlerCallback.addLog();

				return nextPageUrl;
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		return null;
	}
	
	/**
	 * 获取相册信息
	 * @param albumUrl
	 * @param albumTitle
	 * @param albumThumbUrl
	 */
	private AlbumInfo getAlbumInfo(String albumUrl, String albumTitle,String albumThumbUrl){
        AlbumInfo albumInfo = new AlbumInfo();
        albumInfo.mTitle = albumTitle;
        albumInfo.mAlbumThumbUrl = albumThumbUrl;
        albumInfo.mFromType = FromType.FROM_RENREN;

        //System.out.print(String.format("正在爬取相册图片\n相册名：%s\nurl:%s\n缩略图:%s\n", albumTitle, albumUrl, albumThumbUrl));
        getAlbumPics(albumUrl, albumInfo);
        
        //从所有相册图片中查找相册封面缩略图对应的大图的url
        boolean found=false;
        for (AlbumInfo.CrawlerPicInfo picInfo : albumInfo.mPics) {
            String bigUrl = picInfo.mBigImgUrl;
            if(isBigPicUrl(bigUrl, albumInfo.mAlbumThumbUrl)){
                found=true;
                albumInfo.mAlbumPicUrl=bigUrl;
                break;
            }
        }
        
        if(!found && albumInfo.mPics.size()>0 && albumInfo.mPics.get(0).mBigImgUrl!=null){
            //没找到大图，使用第一张大图
            albumInfo.mAlbumPicUrl=albumInfo.mPics.get(0).mBigImgUrl;
        }

        //mCrawlerCallback.addLog(String.format("相册:%s包含%d张图片", albumTitle, albumInfo.mPics.size()));
        return albumInfo;
	}
	
	private boolean isBigPicUrl(String bigPicUrl,String thumbPicUrl){
	    /**
	     * 小图url：http://fmn.rrimg.com/xxx/head_xxx.jpg
	     * 大图url：http://fmn.rrimg.com/xxx/original_xxx.jpg(后缀也有可能是其它的，比如gif)
	     */
	    if(bigPicUrl==null || thumbPicUrl==null)
	        return false;
	    
	    int bigSlashIndex=bigPicUrl.lastIndexOf('/');
	    int thumbSlashIndex=thumbPicUrl.lastIndexOf('/');
	    if(bigSlashIndex==-1 || bigSlashIndex!=thumbSlashIndex)
	        return false;
	    
	    try {
	        String bigPart=bigPicUrl.substring(bigPicUrl.indexOf('_', bigSlashIndex), bigPicUrl.lastIndexOf('.'));
	        String thumbPart=thumbPicUrl.substring(thumbPicUrl.indexOf('_', thumbSlashIndex), thumbPicUrl.lastIndexOf('.'));
            if(bigPart.compareToIgnoreCase(thumbPart)==0)
                return true;
            else {
                return false;
            }
        } catch (Exception e) {
            return false;
        }
	}

	/**
	 * 获取该相册的图片
	 * @param albumUrl 相册url
	 * @param albumTitle 相册名
	 */
	private void getAlbumPics(String albumUrl, AlbumInfo albumInfo) {
		if (albumUrl == null)
			new IllegalArgumentException();

		albumUrl = albumUrl.trim();
		if (albumUrl.isEmpty())
			throw new IllegalArgumentException();
		

		Document doc;
		try {
			doc = Jsoup.connect(albumUrl).userAgent(Constants.USER_AGENT).timeout(Constants.JSOUP_TIMEOUT).get();
			// System.out.print(doc);
			Elements items = doc.select("#albumThumbMode li");
			for (Element item : items) {
				try {
					Element link = item.select("span a").get(0);
					Element thumbPic = item.select("span a img").get(0);

					// 图片描述
					String picDescription = link.attr("title");
					// 大图地址
					String bigPicUrl = getBigPicUrl(link.attr("href"));
					// 缩略图地址
					String thumbPicUrl = thumbPic.attr("src");
					
//					mCrawlerCallback.addLog();
//					mCrawlerCallback.addLog(picDescription);
//					mCrawlerCallback.addLog(thumbPicUrl);
//					mCrawlerCallback.addLog(bigPicUrl);
					
					CrawlerPicInfo picInfo=new CrawlerPicInfo();
					picInfo.mThumbImgUrl=thumbPicUrl;
					picInfo.mBigImgUrl=bigPicUrl;
					picInfo.mDescription=picDescription;
					albumInfo.mPics.add(picInfo);
				} catch (Exception e) {
					// TODO:log
					e.printStackTrace();
				}
			}
			
			//如果该相册有多页继续爬取下一页
			Elements elements = doc.select(".chn[title=下一页]");
			// check element count,==1.if >1 ?
			if (elements.size() > 0) {
				Element nextPageElement = elements.get(0);
				String nextPageUrl = nextPageElement.attr("href");

				// mCrawlerCallback.addLog();
				// mCrawlerCallback.addLog();

				getAlbumPics(nextPageUrl,albumInfo);
			}
		} catch (IOException e) {
			// TODO:log
			e.printStackTrace();
		}
	}

	private String getBigPicUrl(String url) throws IOException {
		String bigPicUrl = null;
		
		Document doc;
		doc = Jsoup.connect(url).userAgent(Constants.USER_AGENT).timeout(Constants.JSOUP_TIMEOUT).get();
		Elements elements=doc.select("#photoLink img");
		if(elements.size()>0)
			bigPicUrl=elements.get(0).attr("src");
		return bigPicUrl;
	}

}

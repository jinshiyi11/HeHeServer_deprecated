package com.shuai.hehe.server;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.zip.CRC32;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.IOUtils;

/**
 * Servlet implementation class GetVideoDetail
 */
@WebServlet("/get_video_detail")
public class GetVideoDetail extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private static final String VIDEO_API_URL = "http://ib.365yg.com/video/urls/v/1/toutiao/mp4/";

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public GetVideoDetail() {
		super();
		// TODO Auto-generated constructor stub
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doGet(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {	
		String videoId = request.getParameter("video_id");
		URL url = new URL(getUrl(videoId));
		HttpURLConnection conn = null;
		InputStream inputStream = null;
		try {
			conn = (HttpURLConnection) url.openConnection();
			inputStream = conn.getInputStream();
			String data = IOUtils.toString(inputStream, Charset.defaultCharset());
			response.getWriter().write(data);
		} finally {
			if (conn != null) {
				conn.disconnect();
			}
			
			IOUtils.closeQuietly(inputStream);
		}
	}
	
	private static String getUrl(String videoId){
		String result="/video/urls/v/1/toutiao/mp4/" + videoId;
		result=result+"?r=" + String.valueOf(Math.random()).substring(2);
//		result="http://ib.365yg.com"+result+"s="+getCrc32(result, getTable());
		CRC32 crc32=new CRC32();
		crc32.update(result.getBytes());
		result="http://ib.365yg.com"+result+"&s="+String.valueOf(crc32.getValue());
		return result;
		
	}
	
	private static int[] getTable(){
		int[] e=new int[256];
		for (int t = 0, n = 0; n != 256; ++n) {
		      t = n;
		      t = (1 & t)!=0 ? -306674912 ^ t >>> 1 : t >>> 1;
		      t = (1 & t)!=0 ? -306674912 ^ t >>> 1 : t >>> 1;
		      t = (1 & t)!=0 ? -306674912 ^ t >>> 1 : t >>> 1;
		      t = (1 & t)!=0 ? -306674912 ^ t >>> 1 : t >>> 1;
		      t = (1 & t)!=0 ? -306674912 ^ t >>> 1 : t >>> 1;
		      t = (1 & t)!=0 ? -306674912 ^ t >>> 1 : t >>> 1;
		      t = (1 & t)!=0 ? -306674912 ^ t >>> 1 : t >>> 1;
		      t = (1 & t)!=0 ? -306674912 ^ t >>> 1 : t >>> 1;
		      e[n] = t;
		    }
		    return e;
	}
	
//	private static int getCrc32(String t,int[] n) {
//		int e,o,r;
//		int i,a;
//		int p;
//	    for ( r = -1, i = 0, a = t.length(); i < a;) {
//	      e = t.codePointAt(i++);
//	      a=e<128?2 :1;
//	      
//	      p=e<128?(r = r >>> 8 ^ n[255 & (r ^ e)]) : e < 2048 ? (r = r >>> 8 ^ (n[255 & (r ^ (192 | (e >> 6) & 31)))],
//	        r = r >>> 8 ^ n[255 & (r ^ (128 | 63 & e))]) : e >= 55296 && e < 57344 ? (e = (1023 & e) + 64,
//	          o = 1023 & t.codePointAt(i++),
//	          r = r >>> 8 ^ n[255 & (r ^ (240 | e >> 8 & 7))],
//	          r = r >>> 8 ^ n[255 & (r ^ (128 | e >> 2 & 63))],
//	          r = r >>> 8 ^ n[255 & (r ^ (128 | o >> 6 & 15 | (3 & e) << 4))],
//	          r = r >>> 8 ^ n[255 & (r ^ (128 | 63 & o))]) : (r = r >>> 8 ^ n[255 & (r ^ (224 | e >> 12 & 15))],
//	            r = r >>> 8 ^ n[255 & (r ^ (128 | e >> 6 & 63))],
//	            r = r >>> 8 ^ n[255 & (r ^ (128 | 63 & e))]);
//	    }
//	    return r ^ -1;
//	  }

}

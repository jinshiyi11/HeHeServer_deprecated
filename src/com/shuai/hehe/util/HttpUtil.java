package com.shuai.hehe.util;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class HttpUtil {
	
	public static String getData(String url) throws MalformedURLException, IOException{
        HttpURLConnection conn = (HttpURLConnection) new URL( url ).openConnection();
//        conn.setRequestProperty( "User-Agent", userAgent );
//        conn.setReadTimeout(timeOut);
//        conn.setConnectTimeout(connectTimeOut);
        //conn.setRequestMethod("POST");
        conn.setDoInput(true);
        conn.setDoOutput(true);
        
        OutputStream os = conn.getOutputStream();
        BufferedWriter writer = new BufferedWriter( new OutputStreamWriter(os, "UTF-8") );
//        writer.write( queryString );
        writer.flush();
        tryClose( writer );
        tryClose( os );
        conn.connect();
        
        //TODO:大小限制
        InputStream is = conn.getInputStream();
        String result = stream2String( is ); 
        return result;
	}
	
	/**
	 * 获取流中的字符串
	 * @param is
	 * @return
	 */
	private static String stream2String( InputStream is ) {
		BufferedReader br = null;
		try{
			br = new BufferedReader( new java.io.InputStreamReader( is ));	
			String line = "";
			StringBuilder sb = new StringBuilder();
			while( ( line = br.readLine() ) != null ) {
				sb.append( line );
			}
			return sb.toString();
		} catch( Exception e ) {
			e.printStackTrace();
		} finally {
			tryClose( br );
		}
		return "";
	}
	
	/**
	 * 关闭输出流
	 * @param os
	 */
	private static void tryClose( OutputStream os ) {
		try{
			if( null != os ) {
				os.close();
				os = null;
			}
		} catch( Exception e ) {
			e.printStackTrace();
		}
	}
	
	/**
	 * 关闭writer
	 * @param writer
	 */
	private static void tryClose( java.io.Writer writer ) {
		try{
			if( null != writer ) {
				writer.close();
				writer = null;
			}
		} catch( Exception e ) {
			e.printStackTrace();
		}
	}
	
	/**
	 * 关闭Reader
	 * @param reader
	 */
	private static void tryClose( java.io.Reader reader ) {
		try{
			if( null != reader ) {
				reader.close();
				reader = null;
			}
		} catch( Exception e ) {
			e.printStackTrace();
		}
	}

}

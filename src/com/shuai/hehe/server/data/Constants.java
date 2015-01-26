package com.shuai.hehe.server.data;

public class Constants {
	public static boolean DEBUG = false;
	
	public static final String SESSION_KEY="hehe_shuai";
	/**
	 * 1.0只支持图片新鲜事
	 */
	public static final double VERSION_1_0=1.0;
	/**
	 * 1.1版支持图片,视频新鲜事
	 */
    public static final double VERSION_1_1=1.1;
    /**
     * 1.2版支持图片，视频，日志新鲜事
     */
    public static final double VERSION_1_3=1.3;
	
	//public static String DB_PATH="D:/mycode/hehe.db";
	//public static String DB_PATH="/usr/local/sae/jkdaemon/hehe.db";
	//public static String DB_PATH="/data1/jetty_work/592/hehe1/hehe.db";
	
	public static final String HTTP_CACHE_CONTROL="Cache-Control";
	//默认协议数据的缓存时间,7天
	public static final String HTTP_CACHE_CONTROL_DEFAULT_VALUE="max-age="+7*24*60*60;
	
	public static final String ADMIN_KEY="admin";
	public static final String ADMIN_KEY_VALUE="123456";
}

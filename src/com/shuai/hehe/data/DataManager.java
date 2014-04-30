package com.shuai.hehe.data;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class DataManager {
	
	private boolean debug=true;
	
	private String mDbName;
	private String mDbUserName;
	private String mDbPassword;
	private String mDbHost;
	private int mDbPort;
	
	private static String mDriverName="com.mysql.jdbc.Driver";//"org.sqlite.JDBC"
	
	{
        if (debug) {
            mDbName = "hehe";
            mDbUserName="hot_feed_user";
            mDbPassword="test";
            mDbHost="localhost";
            mDbPort=3306;
        }else{
            mDbName = "";
            mDbUserName="";
            mDbPassword="";
            mDbHost="r.rdc.sae.sina.com.cn";
            mDbPort=3307;
        }
        
    }
	
	public DataManager(){
		try {
			Class.forName(mDriverName);
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
			throw new RuntimeException();
		}
	}
	
	public Connection getConnection() throws SQLException{
//		String path=Constant.DB_PATH;
//		Connection connection = DriverManager.getConnection("jdbc:sqlite:"+path);
		
		String connectionString=String.format("jdbc:mysql://%s:%d/%s?user=%s&password=%s", mDbHost,mDbPort,mDbName,mDbUserName,mDbPassword);
		Connection connection = DriverManager.getConnection(connectionString);
		
		return connection;
	}
	
	public void closeConnection(Connection connection){
		try {
			if (connection != null){
				connection.close();
//				connection=null;
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	/**
	 * 创建数据库
	 * @throws SQLException 
	 * @throws ClassNotFoundException 
	 */
	public void createDb() throws SQLException, ClassNotFoundException{
		//Class.forName("org.sqlite.JDBC");

		Connection connection=null;
		Statement statement=null;
		connection=getConnection();
		
		String[] sqls={
				"CREATE TABLE IF NOT EXISTS hot_feed(id INT NOT NULL AUTO_INCREMENT PRIMARY KEY," +
						"type INT,title VARCHAR(255) NOT NULL UNIQUE," +
						"content TEXT," +
						"`from` INT," +
						"state INT DEFAULT -1," +
						"insert_time TIMESTAMP DEFAULT  CURRENT_TIMESTAMP()," +
						"show_time TIMESTAMP DEFAULT 0" +
						")",
						
				"CREATE TABLE IF NOT EXISTS pic(id INT NOT NULL AUTO_INCREMENT PRIMARY KEY," +
						"feed_id INT," +
						"thumb_url TEXT," +
						"big_url TEXT," +
						"description TEXT," +
						"insert_time TIMESTAMP DEFAULT 0"+
						")"
						};
		
//			String[] indexs={"CREATE INDEX IF NOT EXISTS type_title_index ON hot_feed(type,title)"};
		
		statement = connection.createStatement();
		//创建表
		for(String sql:sqls){
			statement.execute(sql);				
		}
		
		//创建索引
//			for(String sql:indexs){
//				statement.execute(sql);				
//			}
			
		
		closeConnection(connection);
	}
	
	public ArrayList<Feed> getFeeds(Date showTime, int count) throws SQLException{
		ArrayList<Feed> feeds=new ArrayList<Feed>();
		
		Connection connection = getConnection();
		String sql;
		
//		if(count>0){
//			sql="SELECT id,type,title,content,`from`,insert_time,show_time FROM hot_feed WHERE show_time>? ORDER BY show_time DESC LIMIT ?";
//		}else{
//			sql="SELECT id,type,title,content,`from`,insert_time,show_time FROM hot_feed WHERE show_time<? ORDER BY show_time DESC LIMIT ?";
//		}
		
		//Date currentDate=new Date();
		
		if(count>0){
			sql="SELECT id,type,title,content,`from`,insert_time,show_time FROM hot_feed WHERE show_time>? AND show_time<CURRENT_TIMESTAMP() ORDER BY show_time DESC LIMIT ?";
		}else{
			sql="SELECT id,type,title,content,`from`,insert_time,show_time FROM hot_feed WHERE show_time<? ORDER BY show_time DESC LIMIT ?";
		}
		
		PreparedStatement statement = connection.prepareStatement(sql);
		SimpleDateFormat simpleDateFormat=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		statement.setString(1, simpleDateFormat.format(showTime));
		statement.setInt(2, Math.abs(count));
		statement.execute();
		ResultSet resultSet = statement.getResultSet();
		
		while(resultSet.next()){
			Feed feed=new Feed();
			
			int index=1;
			feed.setId(resultSet.getInt(index++));
			feed.setType(resultSet.getInt(index++));
			feed.setTitle(resultSet.getString(index++));
			feed.setContent(resultSet.getString(index++));
			feed.setFrom(resultSet.getInt(index++));
			feed.setShowTime(resultSet.getTimestamp("show_time").getTime());
			feeds.add(feed);
		}
		
		closeConnection(connection);	
		
		return feeds;
	}
	
	/**
	 * 获取一条相册新鲜事的所有图片信息
	 * @param feedId 相册新鲜事id
	 * @return
	 * @throws SQLException
	 */
	public ArrayList<PicInfo> getAlbumPics(int feedId) throws SQLException{
        ArrayList<PicInfo> result=new ArrayList<PicInfo>();
        
        Connection connection = getConnection();
        String sql="SELECT id,big_url,description FROM pic WHERE feed_id=?";
        
        PreparedStatement statement = connection.prepareStatement(sql);
        statement.setInt(1, feedId);
        statement.execute();
        ResultSet resultSet = statement.getResultSet();
        
        while(resultSet.next()){
            PicInfo info=new PicInfo();
            
            info.setId(resultSet.getInt("id"));
            info.setBigPicUrl(resultSet.getString("big_url"));
            info.setPicDescription(resultSet.getString("description"));
            result.add(info);
        }
        
        closeConnection(connection);    
        return result;
	}

}

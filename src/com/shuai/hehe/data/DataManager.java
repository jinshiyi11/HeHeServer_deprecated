package com.shuai.hehe.data;

import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class DataManager {
	
	public static Connection getConnection() throws SQLException{
		String path=Constant.DB_PATH;
		Connection connection = DriverManager.getConnection("jdbc:sqlite:"+path);
		
		return connection;
	}
	
	public static void closeConnection(Connection connection){
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
	public static void createDb() throws SQLException, ClassNotFoundException{
		Class.forName("org.sqlite.JDBC");
		
		File file=new File("/data1/jetty_work/592/hehe1/sqlite-3.8.2-amd64-libsqlitejdbc.so");
		file.setExecutable(true);

		Connection connection=null;
		Statement statement=null;
		connection=getConnection();
		
		String[] sqls={
				"CREATE TABLE IF NOT EXISTS hot_feed(id INTEGER PRIMARY KEY," +
						"type INTEGER,title TEXT UNIQUE NOT NULL," +
						"content TEXT," +
						"[from] INTEGER," +
						"state INTEGER DEFAULT -1," +
						"insert_time INTEGER DEFAULT (strftime('%s', 'now'))," +
						"show_time INTEGER DEFAULT 0" +
						")",
						
				"CREATE TABLE IF NOT EXISTS pic(id INTEGER PRIMARY KEY," +
						"feed_id INTEGER," +
						"thumb_url TEXT," +
						"big_url TEXT," +
						"description TEXT," +
						"insert_time INTEGER DEFAULT (strftime('%s', 'now'))," +
						"show_time INTEGER DEFAULT 0" +
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
			

	}

}

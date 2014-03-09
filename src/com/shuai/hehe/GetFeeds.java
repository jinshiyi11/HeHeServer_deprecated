package com.shuai.hehe;

import java.io.IOException;
import java.nio.charset.Charset;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import javax.servlet.Servlet;
import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.gson.Gson;
import com.shuai.hehe.data.Feed;

/**
 * Servlet implementation class GetFeeds
 */
public class GetFeeds extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private static final int DefaultCount=20;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public GetFeeds() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see Servlet#init(ServletConfig)
	 */
	public void init(ServletConfig config) throws ServletException {
		super.init(config);
		
		try {
			Class.forName("org.sqlite.JDBC");
			
			initDb();
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
	}

	/**
	 * @see Servlet#destroy()
	 */
	public void destroy() {
	}
	
	private void initDb(){
		try {
			Connection connection=getConnection();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	private Connection getConnection() throws SQLException{
		//String path=getServletContext().getRealPath("hehe.db");
		String path="D:/mycode/hehe.db";
		Connection connection = DriverManager.getConnection("jdbc:sqlite:"+path);
		return connection;
	}
	
	private void closeConnection(Connection connection){
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
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		getData(request,response);
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		getData(request,response);
	}
	
	private void getData(HttpServletRequest request, HttpServletResponse response) throws IOException{
		response.setContentType("text/html; charset=UTF-8");
		String idStr=request.getParameter("t");		
		int showTime=-1;
		try{
			showTime=Integer.parseInt(idStr);
		}catch(NumberFormatException ex){
			
		}
		
		//检查数据的有效性
		if(showTime<-1){
			//TODO:log it
			return;
		}
		
		String countString=request.getParameter("count");
		int count=DefaultCount;
		try{
			count=Integer.parseInt(countString);
		}catch(NumberFormatException ex){
			
		}
		
		//检查count
		try {
			String where;
			if(showTime==-1){
				where="";
			}else{
				if(count>0){
					where="WHERE show_time>"+showTime;
				}else{
					where="WHERE show_time<"+showTime;
				}
			}
			
			Connection connection = getConnection();
			//String sql="SELECT id,type,title,content,[from],insert_time,show_time FROM hot_feed "+where+" ORDER BY show_time DESC LIMIT ? ";
			
			String sql="SELECT id,type,title,content,[from],insert_time FROM hot_feed "+where+" LIMIT ? ";
			
			PreparedStatement statement = connection.prepareStatement(sql);
			statement.setInt(1, count>0?count:-count);
			statement.execute();
			ResultSet resultSet = statement.getResultSet();
			
			ArrayList<Feed> feeds=new ArrayList<Feed>();
			while(resultSet.next()){
				Feed feed=new Feed();
				
				int index=1;
				feed.mId=resultSet.getInt(index++);
				feed.mType=resultSet.getInt(index++);
				feed.mTitle=resultSet.getString(index++);
				feed.mContent=resultSet.getString(index++);
				feed.mFrom=resultSet.getInt(index++);
				feeds.add(feed);
			}
			
			Gson gson=new Gson();
			response.getWriter().write(gson.toJson(feeds));
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		//response.getWriter().print("xxxx");
	}

}

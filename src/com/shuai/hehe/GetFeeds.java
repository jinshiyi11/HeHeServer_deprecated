package com.shuai.hehe;

import java.io.IOException;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import javax.servlet.Servlet;
import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.gson.Gson;
import com.shuai.hehe.data.DataManager;
import com.shuai.hehe.data.Feed;

/**
 * Servlet implementation class GetFeeds
 */
public class GetFeeds extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private static final int DEFAULT_PAGE_COUNT=20;
	private static final int MAX_PAGE_COUNT=100;
	
	private DataManager mDataManager;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public GetFeeds() {
        super();
    }

	/**
	 * @see Servlet#init(ServletConfig)
	 */
	public void init(ServletConfig config) throws ServletException {
		super.init(config);
		
		mDataManager=new DataManager();
	}

	/**
	 * @see Servlet#destroy()
	 */
	public void destroy() {
		
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
				
		Date showTime=new Date();
		try{
		    String id=request.getParameter("id");
			if(id!=null){
			    long date=Long.parseLong(id);
			    if(date>0)
			        showTime=new Date();
			}
		}catch (Exception e) {
			e.printStackTrace();
			response.sendError(HttpServletResponse.SC_BAD_REQUEST);
			return;
		}
		
		//检查数据的有效性
		Date maxTime=new Date(System.currentTimeMillis());
		if(showTime.after(maxTime))
			showTime=maxTime;
		
		String countString = request.getParameter("count");
		int count = DEFAULT_PAGE_COUNT;
		try {
			//允许该参数不存在
			if (countString != null)
				count = Integer.parseInt(countString);
		} catch (NumberFormatException ex) {
			ex.printStackTrace();
			response.sendError(HttpServletResponse.SC_BAD_REQUEST);
			return;
		}	
		//检查count
		if(count>MAX_PAGE_COUNT)
			count=MAX_PAGE_COUNT;
		else if(count<MAX_PAGE_COUNT*-1)
			count=MAX_PAGE_COUNT*-1;
		
		try {
			ArrayList<Feed> feeds = mDataManager.getFeeds(showTime,count);
			Gson gson=new Gson();
			response.getWriter().write(gson.toJson(feeds));
		} catch (SQLException e) {
			//e.printStackTrace();
			e.printStackTrace(response.getWriter());
		}
		
		//response.getWriter().print("xxxx");
	}

}

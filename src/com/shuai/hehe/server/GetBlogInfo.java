package com.shuai.hehe.server;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.gson.Gson;
import com.shuai.hehe.server.data.BlogInfo;
import com.shuai.hehe.server.data.Constants;
import com.shuai.hehe.server.data.DataManager;
import com.shuai.hehe.server.data.PicInfo;

/**
 * Servlet implementation class GetBlogContent
 */
public class GetBlogInfo extends HttpServlet {
	private static final long serialVersionUID = 1L;
	
	private DataManager mDataManager;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public GetBlogInfo() {
        super();
        // TODO Auto-generated constructor stub
    }
    
    @Override
    public void init() throws ServletException {
        super.init();
        
        mDataManager=new DataManager();
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
	    getBlogInfo(request, response);
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
	    getBlogInfo(request, response);
	}

    private void getBlogInfo(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.setContentType("text/html; charset=UTF-8");
        
        int feedId;
        try {
            feedId = Integer.parseInt(request.getParameter("feedid"));
        } catch (Exception e) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST);
            return;
        }
        
        try {
            BlogInfo data = mDataManager.getBlogInfo(feedId);
            Gson gson=new Gson();
            response.setHeader(Constants.HTTP_CACHE_CONTROL, Constants.HTTP_CACHE_CONTROL_DEFAULT_VALUE);
            response.getWriter().write(gson.toJson(data));
        } catch (SQLException e) {
            //e.printStackTrace();
            e.printStackTrace(response.getWriter());
        }
    }

}

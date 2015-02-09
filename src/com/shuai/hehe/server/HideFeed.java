package com.shuai.hehe.server;

import java.io.IOException;
import java.util.Date;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.shuai.hehe.server.data.Constants;
import com.shuai.hehe.server.data.DataManager;

/**
 * Servlet implementation class HideFeed
 */
public class HideFeed extends HttpServlet {
	private static final long serialVersionUID = 1L;
	
	private DataManager mDataManager;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public HideFeed() {
        super();
        
        mDataManager=new DataManager();
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		hideFeed(request,response);
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		hideFeed(request,response);
	}
	
	private void hideFeed(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String adminKey=request.getParameter(Constants.ADMIN_KEY_NAME);
		if(adminKey==null || !adminKey.equals(Constants.ADMIN_KEY_VALUE)){
			response.sendError(HttpServletResponse.SC_BAD_REQUEST);
			return;
		}
		
		long feedId;
		try {
			String feedIdStr = request.getParameter("feedid");
			feedId = Long.parseLong(feedIdStr);
			mDataManager.hideFeed(feedId);
		} catch (Exception e) {
			e.printStackTrace();
			response.sendError(HttpServletResponse.SC_BAD_REQUEST);
			return;
		}

	}

}

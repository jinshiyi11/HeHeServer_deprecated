package com.shuai.hehe;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.shuai.hehe.data.Constants;
import com.shuai.hehe.data.FeedType;

/**
 * Servlet implementation class AddFeed
 */
public class AddFeed extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public AddFeed() {
        super();
    }

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String sessionKey=request.getParameter("key");
		if (sessionKey == null || !sessionKey.equals(Constants.SESSION_KEY)) {
			response.sendError(HttpServletResponse.SC_UNAUTHORIZED);
		}
		
		
		int feedType=Integer.parseInt(request.getParameter("feedtype"));
		switch (feedType) {
		case FeedType.TYPE_ALBUM:
			
			break;
		default:
			break;
		}
	}

}

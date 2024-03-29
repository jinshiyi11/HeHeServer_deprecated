package com.shuai.hehe.server;

import java.io.IOException;
import java.sql.SQLException;
import java.text.ParseException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.shuai.hehe.server.data.Constants;
import com.shuai.hehe.server.data.DataManager;

/**
 * Servlet implementation class UpdateShowTime
 */
public class UpdateShowTime extends HttpServlet {
	private static final long serialVersionUID = 1L;
	
	private DataManager mDataManager;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public UpdateShowTime() {
        super();
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
	    updateShowTime(request,response);
	}

	
	private void updateShowTime(HttpServletRequest request, HttpServletResponse response) throws IOException{
	    response.setContentType("text/html; charset=UTF-8");
	    
	    String adminKey=request.getParameter(Constants.ADMIN_KEY_NAME);
        if(adminKey==null || !adminKey.equals(Constants.ADMIN_KEY_VALUE)){
            response.sendError(HttpServletResponse.SC_BAD_REQUEST);
            return;
        }
        
	    mDataManager=DataManager.getInstance();
	    try {
            mDataManager.updateShowTime(response);
        } catch (SQLException e) {
            e.printStackTrace(response.getWriter());
        } catch (ParseException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
	}

}

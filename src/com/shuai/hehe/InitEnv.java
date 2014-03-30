package com.shuai.hehe;

import java.io.IOException;
import java.sql.SQLException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.shuai.hehe.data.Constant;
import com.shuai.hehe.data.DataManager;

/**
 * Servlet implementation class InitEnv
 */
public class InitEnv extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public InitEnv() {
        super();
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		initEnviroment(request, response);
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		initEnviroment(request, response);
	}
	
	private void initEnviroment(HttpServletRequest request, HttpServletResponse response) throws IOException{
		String sessionKey=request.getParameter("key");
		if (sessionKey == null || !sessionKey.equals(Constant.SESSION_KEY)) {
			response.sendError(HttpServletResponse.SC_UNAUTHORIZED);
		}
		
		try {
			DataManager dataManager=new DataManager();
			dataManager.createDb();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			e.printStackTrace(response.getWriter());
		}
	}

}

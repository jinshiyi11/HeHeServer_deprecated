package com.shuai.hehe.server;

import java.io.IOException;
import java.sql.SQLException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.shuai.hehe.server.data.Constants;
import com.shuai.hehe.server.data.DataManager;

/**
 * 初始化数据库
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
		String sessionKey=request.getParameter(Constants.ADMIN_KEY_NAME);
		if (sessionKey == null || !sessionKey.equals(Constants.ADMIN_KEY_VALUE)) {
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

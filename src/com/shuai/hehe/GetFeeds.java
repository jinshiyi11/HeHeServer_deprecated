package com.shuai.hehe;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import javax.servlet.Servlet;
import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Servlet implementation class GetFeeds
 */
public class GetFeeds extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
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
	}

	/**
	 * @see Servlet#destroy()
	 */
	public void destroy() {
	}
	
	private Connection getConnection() throws SQLException{
		Connection connection = DriverManager.getConnection("jdbc:sqlite:D:/mycode/hehe.db");
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
		String idStr=request.getParameter("id");		
		int id=-1;
		try{
			id=Integer.parseInt(idStr);
		}catch(NumberFormatException ex){
			
		}
		
		String countString=request.getParameter("count");
		int count=20;
		try{
			count=Integer.parseInt(countString);
		}catch(NumberFormatException ex){
			
		}
		
		
		
		
//		response.getWriter().print("xxxx");
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
	}

}

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
public class GetBlog extends HttpServlet {
	private static final long serialVersionUID = 1L;
	
	private DataManager mDataManager;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public GetBlog() {
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

	//TODO:返回的html中包含title
    private void getBlogInfo(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.setContentType("text/html; charset=UTF-8");
        
        int feedId;
        boolean html=true;//是返回html还是饭后json
        try {
            feedId = Integer.parseInt(request.getParameter("feedid"));
        } catch (Exception e) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST);
            return;
        }
        
        try {
            html = !Boolean.parseBoolean(request.getParameter("json"));
        } catch (Exception e) {
        }

        try {
            BlogInfo data = mDataManager.getBlogInfo(feedId);
            Gson gson = new Gson();
            if (html) {
                String content=data.getHtmlContent();
                if(content!=null){
//                  <html>
//                  <head>
//                  <meta charset="utf-8">
//                  <meta name="viewport" content="width=device-width, user-scalable=no" />
//                  </head>
                    response.getWriter().write("<html><head><meta charset=\"utf-8\"><meta name=\"viewport\" content=\"width=device-width, user-scalable=no\" /></head>");
                    response.getWriter().write(content);
                    response.getWriter().write("</body></html>");
                }
                else {
                    response.sendError(HttpServletResponse.SC_NOT_FOUND);
                }
            } else {
                response.setHeader(Constants.HTTP_CACHE_CONTROL, Constants.HTTP_CACHE_CONTROL_DEFAULT_VALUE);
                
                response.getWriter().write(gson.toJson(data));
            }

        } catch (SQLException e) {
            //e.printStackTrace();
            e.printStackTrace(response.getWriter());
        }
    }

}

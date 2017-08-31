package com.shuai.hehe.server;

import java.io.IOException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.gson.Gson;
import com.shuai.hehe.crawler.CrawlerMananger;

/**
 * Servlet implementation class Crawler
 */
@WebServlet("/crawler")
public class Crawler extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public Crawler() {
		super();
		// TODO Auto-generated constructor stub
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doGet(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		doCrawler(request, response);
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doPost(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		doCrawler(request, response);
	}

	private void doCrawler(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		// TODO:同步问题，安全检查
		response.setContentType("text/html; charset=UTF-8");
		response.setHeader("Access-Control-Allow-Origin", "*"); 
		
		String action = request.getParameter("action");
		CrawlerMananger crawler = (CrawlerMananger) request.getServletContext()
				.getAttribute("crawler");
		if (action == null || action.equals("") || action.equals("start")) {
			if (crawler == null) {
				crawler = new CrawlerMananger();
				crawler.start();
				request.getServletContext().setAttribute("crawler", crawler);
			}
		} else if ("stop".equals(action)) {
			if (crawler != null) {
				crawler.stop();
				request.getServletContext().removeAttribute("crawler");
			}
		} else if ("status".equals(action)) {
			response.getWriter().print(crawler == null ? "0" : "1");
		} else if ("log".equals(action)) {
			if (crawler != null) {
				int start = 0;
				try {
					start = Integer.parseInt(request.getParameter("start"));
				} catch (Exception e) {

				}

				CrawlerMananger.LogInfo logInfo = crawler.getLog(start);
				Gson gson=new Gson();
				response.getWriter().write(gson.toJson(logInfo));
			}
		} else {
			response.getWriter().println("invalid param");
		}

	}

}

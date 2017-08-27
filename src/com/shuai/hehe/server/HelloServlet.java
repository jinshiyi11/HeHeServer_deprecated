package com.shuai.hehe.server;

import java.io.IOException;

import javax.servlet.AsyncContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Servlet implementation class HelloServlet
 */
@WebServlet(urlPatterns="/hello", asyncSupported=true)
public class HelloServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public HelloServlet() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		AsyncContext async = request.startAsync(request, response);
		async.setTimeout(20*1000);
        LongProcess longProcess = new LongProcess(async);
        longProcess.setDaemon(true);
        longProcess.start();
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
	}
}

class LongProcess extends Thread {

    private final AsyncContext asyncContext;

    public LongProcess(AsyncContext asyncContext) {
        this.asyncContext = asyncContext;
    }

    public void run() {
        System.out.println("Thread Started!!");
        asyncContext.getResponse().setContentType("text/plain");
        int progress=0;
        while (progress < 1000) {             
            try { sleep(1000); } catch (InterruptedException ignore) {}
            progress++;
            //use asyncContext here to send progress to the client incrementally
            try {
            	//asyncContext.getResponse().getOutputStream().print(String.valueOf(progress));
            	String message=String.valueOf(progress)+"1zz\n";
				asyncContext.getResponse().getWriter().write(message);
				asyncContext.getResponse().getWriter().flush();
				asyncContext.getResponse().flushBuffer();
				System.out.println(message);
				asyncContext.complete();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
        }
    }           
}

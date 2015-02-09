package com.shuai.hehe.server;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;

public class BaseServlet extends HttpServlet {
	protected String getPostData(HttpServletRequest request) throws IOException{
		//默认最多读取2M数据
		return getPostData(request,2*1024*1024);
	}
	
	protected String getPostData(HttpServletRequest request,long maxRead) throws IOException{
		String result = null;
		long totalRead = 0;
		InputStream inputStream = request.getInputStream();
		ByteArrayOutputStream output = new ByteArrayOutputStream();
		byte[] buf = new byte[512];
		int bytesRead;

		while ((bytesRead = inputStream.read(buf)) > 0) {
			totalRead += bytesRead;
			if (maxRead > 0 && totalRead > maxRead)
				throw new IOException("http body exceed max body limit!");
			output.write(buf, 0, bytesRead);
		}

		result = output.toString("UTF-8");
		return result;
	}

}

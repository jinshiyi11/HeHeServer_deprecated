package com.anysdk.auth;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ByteArrayEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.CoreProtocolPNames;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;

import com.shuai.hehe.server.BaseServlet;
import com.shuai.hehe.server.Log;


/**
 * Servlet implementation class UcLogin
 */
public class UcLogin extends BaseServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public UcLogin() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		getData(request,response);
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		getData(request,response);
	}
	
	private void getData(HttpServletRequest request, HttpServletResponse response) throws IOException{
		Log.info("test account.verifySession");
		
		String sid=request.getParameter("sid");
		Log.info("sid:"+sid);
	
		
		//String postData=getPostData(request);
		//Log.info("postData:"+postData);
		
		AccessProxy ap = new AccessProxy() ;
		Map<String,Object> data = new HashMap<String,Object>();
		data.put("sid", sid);//在uc sdk登录成功时，游戏客户端通过uc sdk的api获取到sid，再游戏客户端由传到游戏服务器
		String result = ap.doPost("account.verifySession", data);//http post方式调用服务器接口,请求的body内容是参数json格式字符串
		Log.debug("[result]"+result);//结果也是一个json格式字符串
	}

}

class AccessProxy{
	private HttpClient httpclient;
	
	public String assemblyParameters(String service, Map<String,Object> data) throws Exception {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("id", System.currentTimeMillis());// 当前系统时间
		params.put("service", service);

		Map<String,Object> game = new HashMap<String, Object>();
		game.put("gameId", UcConstant.UC_GAME_ID);
		
		params.put("game", game);
		params.put("data", data);
		params.put("encrypt", "md5");
		/*
		 * 签名规则=签名内容+apiKey 假定apiKey=202cb962234w4ers2aaa,sid=abcdefg123456 那么签名原文sid=abcdefg123456202cb962234w4ers2aaa
		 * 签名结果6e9c3c1e7d99293dfc0c81442f9a9984
		 */
		String signSource = Util.getSignData(data) + UcConstant.UC_API_KEY;
		// String signSource = "sid=70b37f00-5f59-4819-99ad-8bde027ade00144882"+apiKey;
		String sign = Util.getMD5Str(signSource);// MD5加密签名
		Log.debug("[sign source]" + signSource);
		Log.debug("[sign result]" + sign);
		params.put("sign", sign);
		String body = Util.encodeJson(params);// 把参数序列化成一个json字符串
		Log.debug("[body]" + body);
		return body;
	}

	/**
	 * 【对外提供的接口方法】 执行一个HTTP POST请求，返回请求响应的内容
	 * 
	 * @param url 请求的URL地址
	 * @param params 请求的查询参数,可以为null
	 * @return 返回请求响应的内容
	 */
	public String doPost(String service, Map<String,Object> data) {
		StringBuffer stringBuffer = new StringBuffer();
		HttpEntity entity = null;
		BufferedReader in = null;
		HttpResponse response = null;
		String ln;
		try {
			String body = assemblyParameters(service, data);
			String serviceUrl = "cp/account.verifySession";
			response = execute("http://sdk.g.uc.cn/", serviceUrl, body);
			entity = response.getEntity();
			in = new BufferedReader(new InputStreamReader(entity.getContent()));
			while ((ln = in.readLine()) != null) {
				stringBuffer.append(ln);
				stringBuffer.append("\r\n");
			}
			httpclient.getConnectionManager().shutdown();
		} catch (IllegalStateException e) {
			Log.error(e.toString());
		} catch (IOException e) {
			Log.error(e.toString());
		} catch (Exception e) {
			Log.error(e.toString());
		} finally {
			if (null != in) {
				try {
					in.close();
					in = null;
				} catch (IOException e) {
					Log.error(e.toString());
				}
			}
		}
		return stringBuffer.toString();
	}

	/**
	 * 【私有方法供本类内部调用】 执行一个HTTP POST请求，返回请求响应的内容,并处理url不可用时跟换
	 * 
	 * @param url 请求的URL地址
	 * @param params 请求的查询参数,可以为null
	 * @return 返回请求响应的内容
	 */
	private HttpResponse execute(String serverHost, String serviceUrl, String body) throws Exception {
		String url = serverHost + serviceUrl;
		Log.debug("url:" + url);
		Log.debug("body:" + body);
		HttpResponse response = null;
		if (null == httpclient) {
			httpclient = new DefaultHttpClient();
			HttpParams params = httpclient.getParams();
			HttpConnectionParams.setConnectionTimeout(params, 5000);
			HttpConnectionParams.setSoTimeout(params, 5000);

		}

		HttpPost httppost = new HttpPost(url);
		httppost.setHeader("Content-Type", "application/x-www-form-urlencoded");
		httppost.getParams().setBooleanParameter(CoreProtocolPNames.USE_EXPECT_CONTINUE, false);// 将Expect: 100-Continue设置为关闭
		try {
			httppost.setEntity(new ByteArrayEntity(body.getBytes("UTF-8")));
		} catch (UnsupportedEncodingException e) {
			Log.error(e.toString());
		}
		try {
			response = httpclient.execute(httppost);
		} catch (ClientProtocolException e) {
			Log.error(e.toString());// 客户端协议异常
		} catch (IOException e) {
			Log.error(e.toString());
		}
		return response;
	}
	
	public HttpClient getHttpclient() {
		return httpclient;
	}

	public void setHttpclient(HttpClient httpclient) {
		this.httpclient = httpclient;
	}

}

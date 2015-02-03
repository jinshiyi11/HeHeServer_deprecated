package com.shuai.hehe.crawler;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;

public class CrawlerThread extends Thread {
	public static final String Name=CrawlerThread.class.getSimpleName();
	private String logFilePath;
	
	public static String inputStreamToString(Reader in){
        StringBuilder sb=new StringBuilder();
        
        BufferedReader br = new BufferedReader(in);
        try {
            String str;
            while ((str = br.readLine()) != null) {
                sb.append(str);
            }
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return sb.toString();
    }
	
	public String getLog() throws IOException {
		String result="";
		if (logFilePath != null) {
			InputStream stream = new FileInputStream(logFilePath);
			InputStreamReader in = new InputStreamReader(stream, "utf-8");
			result = inputStreamToString(in);
			stream.close();
		}
        return result;
	}

	@Override
	public void run() {
		super.run();
		setName(CrawlerThread.Name);
		
		File logFile=null;
		try{
			logFile=File.createTempFile(Name, ".hehe");
			logFile.deleteOnExit();
			logFilePath=logFile.getAbsolutePath();
			
			FileWriter out=new FileWriter(logFile);
			for(int i=0;i<100;i++){
				out.write("test</br>");	
				out.flush();
				sleep(300);
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally{
			
		}
	}

}

package com.shuai.hehe.server;

import java.util.Date;

public class Log {
	
	public static void debug(String log){
		debug("",log);
    }
	
	public static void debug(String tag,String log){
        System.out.println(new Date().toString()+" " + tag +log);
    }
	
	
	public static void info(String log){
		info("",log);
    }
	
    public static void info(String tag,String log){
        System.out.println(new Date().toString()+" " + tag +log);
    }
    
    public static void error(String log){
    	error("",log);
    }
    
    public static void error(String tag,String log){
        System.out.println(new Date().toString()+" " + tag +log);
    }

}

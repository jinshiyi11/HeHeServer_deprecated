package com.shuai.hehe.server;

import java.util.Date;

public class Log {
    public static void info(String tag,String log){
        System.out.println(new Date().toString()+" " + tag +log);
    }

}

package com.example.server.util.json;

public class Pop3StateCode {
    public static final String OK = "+OK"; // 执行命令成功
    public static final String ERR = "-ERR"; // 执行命令失败
    public static final String READY = "Winmail Mail Server POP3 ready";
    public static final String AUTH_FAIL = "authorization failed"; // 登陆失败
    public static final String AUTH_SUCCESS = "messages";
    public static final String BYE = "Connection closed";
}

package com.example.server.util.json;

public class Pop3StateCode {
    public static final String OK = "+OK "; // 执行命令成功
    public static final String ERR = "-ERR "; // 执行命令失败
    public static final String VALID = "Command not valid in this state"; // 使用命令错误
    public static final String READY = "Winmail Mail Server POP3 ready";
    public static final String USER = "Set user first"; // 用户名是否输入
    public static final String AUTH_FAIL = "Authorization failed"; // 登陆失败
    public static final String AUTH_SUCCESS = "messages";
    public static final String BYE = "Connection closed";
    public static final String STNTAX = "Unknown command"; // 不识别的命令
}

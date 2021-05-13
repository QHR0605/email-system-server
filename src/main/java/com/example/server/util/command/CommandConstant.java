package com.example.server.util.command;

/**
 * 指令常量
 *
 * @author 全鸿润
 */
public class CommandConstant {
    // SMTP
    public static final String HELO = "HELO";
    public static final String AUTH_LOGIN = "AUTH LOGIN";
    public static final String AUTH_LOGIN_PREFIX = "AUTH";
    public static final String MAIL_FROM = "MAIL FROM";
    public static final String MAIL_FROM_PREFIX = "MAIL";
    public static final String RCPT_TO = "RCPT TO";
    public static final String RCPT_TO_PREFIX = "RCPT";
    public static final String DATA = "DATA";
    public static final String RSET = "REST";
    public static final String QUIT = "QUIT";
    // POP3
    public static final String USER = "USER";
    public static final String PASS = "PASS";
    public static final String STAT = "STAT";
    public static final String LIST = "LIST";
    public static final String RETR = "RETE";
    public static final String DELE = "DELE";
    public static final String REST = "REST";
}

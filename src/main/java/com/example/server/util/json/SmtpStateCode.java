package com.example.server.util.json;

/**
 * @author 全鸿润
 */
public class SmtpStateCode {

    public static final int READY = 220;
    public static final int SUCCESS = 250;
    public static final int AUTH_SUCCESS = 235;
    public static final int PROCESSING= 221;
    public static final int SENDING = 354;
    public static final int COMMAND_ERROR = 500;
    public static final int INTERRUPTED = 552;
}

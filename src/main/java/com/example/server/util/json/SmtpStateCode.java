package com.example.server.util.json;

/**
 * SMTP状态码
 *
 * @author 全鸿润
 */
public class SmtpStateCode {

    public static final int READY = 220;

    public static final int SUCCESS = 250;
    public static final String SUCCESS_DESC = SUCCESS + " OK";

    public static final int AUTH_SUCCESS = 235;
    public static final String AUTH_SUCCESS_DESC = AUTH_SUCCESS + " Authentication successful";

    public static final int AUTH_FAILED = 535;
    public static final String AUTH_FAILED_DESC = AUTH_FAILED + " Error: Authentication failed";

    public static final int PROCESSING = 221;
    public static final String BYE = PROCESSING + " Bye";
    public static final int START_EMAIL_INPUT = 354;
    public static final String START_EMAIL_INPUT_DESC = START_EMAIL_INPUT + " Start mail input; end with <CRLF>.<CRLF>";

    public static final int COMMAND_ERROR = 500;
    public static final String COMMAND_ERROR_DESC = COMMAND_ERROR + " Error: bad Syntax";

    public static final int INTERRUPTED = 552;
    public static final String INTERRUPTED_DESC = " 服务已关闭！";
    public static final int SEQUENCE_ERROR = 503;
    public static final String SEQUENCE_ERROR_DESC = SEQUENCE_ERROR + " bad sequence of commands";

    public static final int AUTH_PROCESS = 334;
    public static final String USERNAME_SENT_DESC = AUTH_PROCESS + " dXNlcm5hbWU6";
    public static final String PASSWORD_SENT_DESC = AUTH_PROCESS + " cGFzc3dvcmQ6";

    public static final int ADDRESS_NOT_AVAILABLE = 450;
    public static final String ADDRESS_NOT_AVAILABLE_DESC = ADDRESS_NOT_AVAILABLE + " RCPT address is not available";

    public static final int OPERATION_FAILED = 554;
    public static final String OPERATION_FAILED_DESC = OPERATION_FAILED + " Operation failed";

    public static final int MAILBOX_IS_FULL = 551;
    public static final String MAILBOX_IS_FULL_DESC = MAILBOX_IS_FULL+" Your mailBox is full";


}

package com.example.server.service;

/**
 * @author 全鸿润
 */
public interface SmtpService {

    /**
     * 处理Helo指令
     * @return SMTP指令的状态码
     */
    Integer handleHelloCommand();
    /**
     * 处理AUTH LOGIN指令
     * @return SMTP指令的状态码
     */
    Integer handleAuthCommand();
    /**
     * 处理MAIL FROM指令
     * @return SMTP指令的状态码
     */
    Integer handleMailCommand();
    /**
     * 处理RCPT TO指令
     * @return SMTP指令的状态码
     */
    Integer handleRcptCommand();
    /**
     * 处理DATA指令
     * @return SMTP指令的状态码
     */
    Integer handleDataCommand();
    /**
     * 处理Reset指令
     * @return SMTP指令的状态码
     */
    Integer handleResetCommand();
    /**
     * 处理QUIT指令
     * @return SMTP指令的状态码
     */
    Integer handleQuitCommand();
}

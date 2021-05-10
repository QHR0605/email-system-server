package com.example.server.service;

import com.example.server.entity.SmtpResult;

/**
 * @author 全鸿润
 */
public interface SmtpService {

    /**
     * 处理Helo指令
     * @return SMTP指令的状态码
     */
    SmtpResult handleHelloCommand(String[] args);
    /**
     * 处理AUTH LOGIN指令
     * @return SMTP指令的状态码
     */
    SmtpResult handleAuthCommand(String[] args);
    /**
     * 处理MAIL FROM指令
     * @return SMTP指令的状态码
     */
    SmtpResult handleMailCommand(String[] args);
    /**
     * 处理RCPT TO指令
     * @return SMTP指令的状态码
     */
    SmtpResult handleRcptCommand(String[] args);
    /**
     * 处理DATA指令
     * @return SMTP指令的状态码
     */
    SmtpResult handleDataCommand(String[] args);
    /**
     * 处理Reset指令
     * @return SMTP指令的状态码
     */
    SmtpResult handleResetCommand(String[] args);
    /**
     * 处理QUIT指令
     * @return SMTP指令的状态码
     */
    SmtpResult handleQuitCommand(String[] args);
}

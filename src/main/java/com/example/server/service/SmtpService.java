package com.example.server.service;

import com.example.server.dto.SmtpSession;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;

/**
 * @author 全鸿润
 */
public abstract class SmtpService {

    protected Socket socket;
    protected PrintWriter writer;
    protected BufferedReader reader;
    protected SmtpSession session;

    public SmtpService(Socket socket, SmtpSession smtpSession) {
        this.socket = socket;
        this.session = smtpSession;
    }

    public void restartServer(){

    }

    /**
     * 处理Hello指令
     *
     * @param args 指令参数
     */
    abstract public void handleHelloCommand(String[] args) throws Exception;

    /**
     * 处理AUTH指令
     *
     * @param args 指令参数
     */
    abstract public void handleAuthCommand(String[] args) throws IOException, Exception;

    /**
     * 处理MAIL指令
     *
     * @param args 指令参数
     */
    abstract public void handleMailCommand(String[] args) throws Exception;

    /**
     * 处理RCPT指令
     *
     * @param args 指令参数
     */
    abstract public void handleRcptCommand(String[] args) throws Exception;

    /**
     * 处理DATA指令
     *
     * @param args 指令参数
     */
    abstract public void handleDataCommand(String[] args) throws Exception;

    /**
     * 处理RESET指令
     *
     * @param args 指令参数
     */
    abstract public void handleResetCommand(String[] args) throws Exception;

    /**
     * 处理QUIT指令
     *
     * @param args 指令参数
     */
    abstract public void handleQuitCommand(String[] args) throws Exception;
}

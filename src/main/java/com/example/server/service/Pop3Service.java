package com.example.server.service;

import com.example.server.dto.Pop3Session;

import java.io.BufferedReader;
import java.io.PrintWriter;
import java.net.Socket;

public abstract class Pop3Service {
    protected Socket socket;
    protected PrintWriter writer;
    protected BufferedReader reader;
    protected Pop3Session pop3Session;

    public Pop3Service(Socket socket, Pop3Session pop3Session) {
        this.socket = socket;
        this.pop3Session = pop3Session;
    }

    abstract public void handleUserCommand(String[] args);

    abstract public void handlePassCommand(String[] args);

    abstract public void handleStatCommand(String[] args);

    abstract public void handleListCommand(String[] args);

    abstract public void handleRetrCommand(String[] args);

    abstract public void handleDeleCommand(String[] args);

    abstract public void handleRestCommand(String[] args);

    /**
     * 处理QUIT指令
     *
     * @param args 指令参数
     */
    abstract public void handleQuitCommand(String[] args);
}

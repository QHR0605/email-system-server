package com.example.server.server;

import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * @author 全鸿润
 */
public class SmtpServer extends Thread {

    private ServerSocket serverSocket;
    private int port;
    private boolean shutDown;

    /**
     * 默认端口号为25
     */
    public SmtpServer() {
        port = 25;
        shutDown = false;
        try {
            serverSocket = new ServerSocket(port);
            System.out.println("SMTP 服务已开启");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 循环等待客户端得请求连接
     * 建立连接后, 开启一个服务线程处理该连接
     */
    @Override
    public void run() {
        try {
            while (true) {
                Socket socket = serverSocket.accept();
                PrintWriter printWriter = new PrintWriter(new OutputStreamWriter(socket.getOutputStream()), true);
                printWriter.println("220 lyq.com SMTP ready");
                SmtpServerThread t = new SmtpServerThread(socket);
                t.start();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        SmtpServer smtpServer = new SmtpServer();
        smtpServer.start();
    }

}

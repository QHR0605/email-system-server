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

    public SmtpServer() {
        port = 25;
        shutDown = false;
        try {
            serverSocket = new ServerSocket(port);
            System.out.println("Smtp Server start");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void run() {
        try {
            while (true) {
                Socket socket = serverSocket.accept();
                PrintWriter printWriter = new PrintWriter(new OutputStreamWriter(socket.getOutputStream()), true);
                printWriter.println("建立连接成功");
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

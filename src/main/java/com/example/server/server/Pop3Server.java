package com.example.server.server;

import com.example.server.util.json.Pop3StateCode;

import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

public class Pop3Server extends Thread {
    private ServerSocket serverSocket;
    private int port;
    private boolean shutDown;

    /**
     * 默认端口号 110
     */
    public Pop3Server() {
        port = 110;
        shutDown = false;
        try {
            serverSocket = new ServerSocket(port);
            System.out.println("POP3 服务已开启");
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
                printWriter.println(Pop3StateCode.READY);
                Pop3ServerThread t = new Pop3ServerThread(socket);
                t.start();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        Pop3Server pop3Server = new Pop3Server();
        pop3Server.start();
    }
}

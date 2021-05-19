package com.example.server.server;

import com.example.server.config.SpringContextConfig;
import com.example.server.entity.ServerMessage;
import com.example.server.mapper.AdminMapper;
import com.example.server.util.json.Pop3StateCode;

import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.*;

public class Pop3Server extends Thread {

    private static ServerSocket serverSocket;
    private static int port;
    private static boolean shutDown;
    private static ThreadPoolExecutor executor;
    private static List<Socket> clients;

    private int port1;

    /**
     * 默认端口号 110
     */
    public Pop3Server() {
        List<ServerMessage> serverMessage = null;
        try {
            AdminMapper adminMapper = SpringContextConfig.getBean(AdminMapper.class);
            serverMessage = adminMapper.selectServerMessage();
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (serverMessage == null || serverMessage.size() == 0) {
            port = 110;
        } else {
            port1 = serverMessage.get(0).getPop3Port();
            port = port1;
        }
        shutDown = false;
        TimeUnit unit = TimeUnit.MILLISECONDS;
        LinkedBlockingQueue<Runnable> workQueue = new LinkedBlockingQueue<>();
        ThreadFactory threadFactory = Executors.defaultThreadFactory();
        executor = new ThreadPoolExecutor(30, 50, 1000, unit, workQueue, threadFactory);
        clients = new LinkedList<>();
        try {
            serverSocket = new ServerSocket(port);
            System.out.println("POP3 服务已开启");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void stopPop3Server() throws IOException {
        for (Socket socket : clients
        ) {
            socket.close();
        }
        Pop3Server.shutDown = true;
        System.out.println("关闭Pop3服务器");
        Pop3Server.serverSocket.close();
        executor.shutdown();
        this.interrupt();
    }

    public static int getPort() {
        return port;
    }

    public static void setPort(int port) {
        Pop3Server.port = port;
    }

    public static boolean isShutDown() {
        return shutDown;
    }

    public static void setShutDown(boolean shutDown) {
        Pop3Server.shutDown = shutDown;
    }

    public int getPort1() {
        return port1;
    }

    public void setPort1(int port1) {
        this.port1 = port1;
    }

    @Override
    public void run() {
        try {
            while (true) {
                Socket socket = serverSocket.accept();
                clients.add(socket);
                PrintWriter printWriter = new PrintWriter(new OutputStreamWriter(socket.getOutputStream()), true);
                printWriter.println(Pop3StateCode.READY);
                Pop3ServerRunnable t = new Pop3ServerRunnable(socket);
                executor.execute(t);
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

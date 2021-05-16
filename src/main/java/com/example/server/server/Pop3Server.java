package com.example.server.server;

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
    private ServerSocket serverSocket;
    private int port;
    private boolean shutDown;
    private static ThreadPoolExecutor executor;
    private List<Socket> clients;

    /**
     * 默认端口号 110
     */
    public Pop3Server() {
        port = 110;
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
                socket.getOutputStream().close();
                socket.getInputStream().close();
                socket.close();
            }
            this.shutDown = true;
            System.out.println("关闭Pop3服务器");
            this.serverSocket.close();
            executor.shutdown();
            this.interrupt();
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public boolean isShutDown() {
        return shutDown;
    }

    public void setShutDown(boolean shutDown) {
        this.shutDown = shutDown;
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

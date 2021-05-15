package com.example.server.server;

import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.*;

/**
 * @author 全鸿润
 */
public class SmtpServer extends Thread {

    private ServerSocket serverSocket;
    private int port;
    private boolean shutDown;
    private static ThreadPoolExecutor executor;
    private List<Socket> clients;

    /**
     * 默认端口号为25
     */
    public SmtpServer() {
        port = 25;
        shutDown = false;
        TimeUnit unit = TimeUnit.MILLISECONDS;
        LinkedBlockingQueue<Runnable> workQueue = new LinkedBlockingQueue<>();
        ThreadFactory threadFactory = Executors.defaultThreadFactory();
        executor = new ThreadPoolExecutor(30, 50, 1000, unit, workQueue, threadFactory);
        clients = new LinkedList<>();
        try {
            serverSocket = new ServerSocket(port);
            System.out.println("SMTP 服务已开启");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void stopSmtpServer() {
        try {
            this.shutDown = true;
            for (Socket socket : clients
            ) {
                socket.getOutputStream().close();
                socket.getInputStream().close();
                socket.close();
            }
            System.out.println("关闭SMTP服务器");
            serverSocket.close();
            executor.shutdown();
            this.interrupt();
        } catch (IOException e) {
            e.printStackTrace();
        }

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

    /**
     * 循环等待客户端得请求连接
     * 建立连接后, 开启一个服务线程处理该连接
     */
    @Override
    public void run() {
        try {
            while (true) {
                Socket socket = serverSocket.accept();
                clients.add(socket);
                PrintWriter printWriter = new PrintWriter(new OutputStreamWriter(socket.getOutputStream()), true);
                printWriter.println("220 lyq.com SMTP ready");
                SmtpServerRunnable t = new SmtpServerRunnable(socket);
                executor.execute(t);
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

package com.example.server.server;

import org.springframework.stereotype.Component;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.AbstractWebSocketHandler;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.*;

/**
 * @author 全鸿润
 */
@Component
public class SmtpHandler extends AbstractWebSocketHandler {

    private static int clientCount = 0;
    private static CopyOnWriteArraySet<SmtpHandler> webSocketHandlers = new CopyOnWriteArraySet<>();
    private static ThreadPoolExecutor threadPoolExecutor;
    private Socket socket;
    private BufferedReader reader;
    private PrintWriter writer;
    private boolean isDataSending;

    static {
        TimeUnit unit = TimeUnit.MILLISECONDS;
        LinkedBlockingQueue<Runnable> workQueue = new LinkedBlockingQueue<>();
        ThreadFactory threadFactory = Executors.defaultThreadFactory();
        threadPoolExecutor = new ThreadPoolExecutor(30, 50, 1000, unit, workQueue, threadFactory);
    }

    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        webSocketHandlers.add(this);
        addClientCount();
        session.sendMessage(new TextMessage("连接成功！"));
        System.out.println("连接成功");

        socket = new Socket("127.0.0.1", 25);
        reader = new BufferedReader(new InputStreamReader(socket.getInputStream(), StandardCharsets.UTF_8));
        writer = new PrintWriter(new OutputStreamWriter(socket.getOutputStream()));

        session.sendMessage(new TextMessage(reader.readLine()));
    }

    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {

        String msg = message.getPayload();
        if ("DATA".equals(msg)) {
            isDataSending = true;
            writer.println(msg);
            writer.flush();
            String line = reader.readLine();
            session.sendMessage(new TextMessage(line));
        } else {
            if (isDataSending) {
                if (".".equals(msg)) {
                    writer.println(msg);
                    writer.flush();
                    isDataSending = false;
                    String line = reader.readLine();
                    session.sendMessage(new TextMessage(line));
                } else {
                    writer.append(msg + "\n");
                }
            } else {
                writer.println(msg);
                writer.flush();
                String line = reader.readLine();
                session.sendMessage(new TextMessage(line));
                if ("QUIT".equals(msg)) {
                    session.close();
                }
            }
        }

    }

    @Override
    public void handleTransportError(WebSocketSession session, Throwable exception) throws Exception {
        if (session.isOpen()) {
            session.close();
        }
        System.out.println("连接断开");
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
        webSocketHandlers.remove(this);
        System.out.println("连接断开");
        reader.close();
        writer.close();
        socket.close();
    }

    public static synchronized void addClientCount() {
        SmtpHandler.clientCount++;
    }

    public static synchronized int getClientCount() {
        return SmtpHandler.clientCount;
    }

    public static synchronized void subClientCount() {
        SmtpHandler.clientCount--;
    }

    @Override
    public int hashCode() {
        return super.hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        return super.equals(obj);
    }
}

package com.example.server.proxy;

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
import java.util.concurrent.CopyOnWriteArraySet;

/**
 * @author 全鸿润
 */
@Component
public class Pop3Handler extends AbstractWebSocketHandler {

    private static int clientCount = 0;
    private static CopyOnWriteArraySet<Pop3Handler> webSocketHandlers;
    private Socket socket;
    private BufferedReader reader;
    private PrintWriter writer;

    static {
        webSocketHandlers = new CopyOnWriteArraySet<>();
    }

    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
        String msg = message.getPayload();
        writer.println(msg);
        writer.flush();
        String line;
        while (!"#end#".equals(line = reader.readLine())) {
            session.sendMessage(new TextMessage(line));
        }
        if ("QUIT".equals(msg)) {
            reader.close();
            writer.close();
            socket.close();
            session.close();
        }
    }

    @Override
    public void afterConnectionEstablished(WebSocketSession webSocketSession) throws Exception {

        webSocketHandlers.add(this);
        addClientCount();
        webSocketSession.sendMessage(new TextMessage("连接成功！"));
        System.out.println("连接成功");

        socket = new Socket("127.0.0.1", 110);
        reader = new BufferedReader(new InputStreamReader(socket.getInputStream(), StandardCharsets.UTF_8));
        writer = new PrintWriter(new OutputStreamWriter(socket.getOutputStream()));

        webSocketSession.sendMessage(new TextMessage(reader.readLine()));
    }

    @Override
    public void handleTransportError(WebSocketSession webSocketSession, Throwable throwable) throws Exception {
        if (webSocketSession.isOpen()){
            webSocketSession.close();
        }
        System.out.println("连接中断");
    }

    @Override
    public void afterConnectionClosed(WebSocketSession webSocketSession, CloseStatus closeStatus) throws Exception {
        webSocketHandlers.remove(this);
        subClientCount();
        System.out.println("连接断开");
    }

    @Override
    public boolean supportsPartialMessages() {
        return false;
    }

    public static synchronized void addClientCount() {
        Pop3Handler.clientCount++;
    }

    public static synchronized int getClientCount() {
        return Pop3Handler.clientCount;
    }

    public static synchronized void subClientCount() {
        Pop3Handler.clientCount--;
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

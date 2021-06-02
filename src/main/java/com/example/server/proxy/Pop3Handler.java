package com.example.server.proxy;

import com.example.server.config.SpringContextConfig;
import com.example.server.entity.ServerMessage;
import com.example.server.service.AdminService;
import com.example.server.service.impl.AdminServiceImpl;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.AbstractWebSocketHandler;

import javax.annotation.PostConstruct;
import java.io.*;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.concurrent.CopyOnWriteArraySet;

/**
 * @author 全鸿润
 */
@Component
public class Pop3Handler extends AbstractWebSocketHandler {

    private static int clientCount = 0;
    public static CopyOnWriteArraySet<Pop3Handler> webSocketHandlers;
    private Socket socket;
    private BufferedReader reader;
    private PrintWriter writer;
    private static int port;
    private static String hostname;
    private String host;
    private int portNumber;
    private String username;
    private WebSocketSession socketSession;
    private AdminService adminService = SpringContextConfig.getBean(AdminServiceImpl.class);

    static {
        webSocketHandlers = new CopyOnWriteArraySet<>();
    }

    public String getUsername() {
        return username;
    }

    @PostConstruct
    public void init() {
        List<ServerMessage> serverMessageList = adminService.getServersMsg();
        portNumber = serverMessageList.get(0).getPop3Port();
        port = portNumber;
        host = serverMessageList.get(0).getServerIp();
        hostname = host;
    }

    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {

        String msg = message.getPayload();
        try {
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
        } catch (IOException e) {
            e.printStackTrace();
            session.sendMessage(new TextMessage("POP3服务器不可用"));
        }

    }

    @Override
    public void afterConnectionEstablished(WebSocketSession webSocketSession) throws Exception {

        webSocketHandlers.add(this);
        addClientCount();
        try {
            socket = new Socket(hostname, port);
            reader = new BufferedReader(new InputStreamReader(socket.getInputStream(), StandardCharsets.UTF_8));
            writer = new PrintWriter(new OutputStreamWriter(socket.getOutputStream()));
            webSocketSession.sendMessage(new TextMessage("连接成功！"));
            System.out.println("连接成功");
        } catch (IOException e) {
            e.printStackTrace();
            webSocketSession.sendMessage(new TextMessage("POP3服务不可用"));
        }
        username = (String) webSocketSession.getAttributes().get("username");
        socketSession = webSocketSession;
        webSocketSession.sendMessage(new TextMessage(reader.readLine()));
    }

    @Override
    public void handleTransportError(WebSocketSession webSocketSession, Throwable throwable) throws Exception {
        if (webSocketSession.isOpen()) {
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

    public void sendMessage(String message) throws Exception {
        socketSession.sendMessage(new TextMessage(message));
    }
}

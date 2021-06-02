package com.example.server.proxy;

import com.example.server.config.SpringContextConfig;
import com.example.server.entity.ServerMessage;
import com.example.server.service.AdminService;
import com.example.server.service.impl.AdminServiceImpl;
import com.example.server.util.json.SmtpStateCode;
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
public class SmtpHandler extends AbstractWebSocketHandler {

    private static int clientCount = 0;
    private static CopyOnWriteArraySet<SmtpHandler> webSocketHandlers;
    private Socket socket;
    private BufferedReader reader;
    private PrintWriter writer;
    private boolean isDataSending;

    private static int port;
    private static String hostname;
    private String host;
    private int portNumber;
    private WebSocketSession socketSession;
    private AdminService adminService = SpringContextConfig.getBean(AdminServiceImpl.class);
    private String username;
    private boolean userNameSend;

    static {
        webSocketHandlers = new CopyOnWriteArraySet<>();
    }

    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        webSocketHandlers.add(this);
        addClientCount();
        this.socketSession = session;
        try {
            socket = new Socket(hostname, port);
            reader = new BufferedReader(new InputStreamReader(socket.getInputStream(), StandardCharsets.UTF_8));
            writer = new PrintWriter(new OutputStreamWriter(socket.getOutputStream()));
            session.sendMessage(new TextMessage("连接成功！"));
            System.out.println("连接成功");
        } catch (IOException e) {
            e.printStackTrace();
            session.sendMessage(new TextMessage("SMTP服务不可用"));
        }
        username = (String) session.getAttributes().get("username");
        session.sendMessage(new TextMessage(reader.readLine()));
    }

    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
        String msg = message.getPayload();
        try {
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
                        if (line.startsWith(String.valueOf(SmtpStateCode.SUCCESS))) {
                            for (Pop3Handler handler : Pop3Handler.webSocketHandlers
                            ) {
                                handler.sendMessage("您有一封新邮件");
                            }
                        }
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
                        reader.close();
                        writer.close();
                        socket.close();
                        session.close();
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
            session.sendMessage(new TextMessage("SMTP服务不可用"));
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
        subClientCount();
        System.out.println("连接断开");
    }

    @Override
    public int hashCode() {
        return super.hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        return super.equals(obj);
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

    @PostConstruct
    public void init() {
        List<ServerMessage> serverMessageList = adminService.getServersMsg();
        portNumber = serverMessageList.get(0).getSmtpPort();
        port = portNumber;
        host = serverMessageList.get(0).getServerIp();
        hostname = host;
    }

}

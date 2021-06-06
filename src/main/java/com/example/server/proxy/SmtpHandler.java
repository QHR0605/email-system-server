package com.example.server.proxy;

import com.alibaba.fastjson.JSONObject;
import com.example.server.config.SpringContextConfig;
import com.example.server.dto.EmailMessage;
import com.example.server.entity.ServerMessage;
import com.example.server.service.AdminService;
import com.example.server.service.impl.AdminServiceImpl;
import com.example.server.util.base64.Base64Util;
import com.example.server.util.command.CommandConstant;
import com.example.server.util.json.JsonResultFactory;
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
    private String username;
    private String password;
    private static int port;
    private static String hostname;
    private String host;
    private int portNumber;
    private WebSocketSession socketSession;
    private AdminService adminService = SpringContextConfig.getBean(AdminServiceImpl.class);

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
            List<String> usernames = session.getHandshakeHeaders().get("username");
            List<String> passwords = session.getHandshakeHeaders().get("password");
            username = usernames.get(0);
            password = passwords.get(0);
            System.out.println("连接成功");
        } catch (IOException e) {
            e.printStackTrace();
            session.sendMessage(new TextMessage("SMTP服务不可用"));
        }
        reader.readLine();
    }

    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
        try {
            String msg = message.getPayload();
            if (CommandConstant.QUIT.equals(msg)) {
                writer.println(msg);
                writer.flush();
                System.out.println(reader.readLine());
                session.sendMessage(new TextMessage(JSONObject.toJSONString(JsonResultFactory.buildSuccessResult())));
                reader.close();
                writer.close();
                socket.close();
                session.close();
                return;
            }
            JSONObject jsonObject = (JSONObject) JSONObject.parse(msg);
            EmailMessage emailMessage = JSONObject.toJavaObject(jsonObject, EmailMessage.class);
            System.out.println(emailMessage);
            String line = null;
            writer.println(CommandConstant.HELO + " " + username);
            writer.flush();
            line = reader.readLine();
            if (!SmtpStateCode.SUCCESS_DESC.equals(line)) {
                String jsonString = JSONObject.toJSONString(JsonResultFactory.buildFailureResult());
                session.sendMessage(new TextMessage(jsonString));
                return;
            }
            writer.println(CommandConstant.AUTH_LOGIN);
            writer.flush();
            line = reader.readLine();
            if (!SmtpStateCode.USERNAME_SENT_DESC.equals(line)) {
                String jsonString = JSONObject.toJSONString(JsonResultFactory.buildFailureResult());
                session.sendMessage(new TextMessage(jsonString));
                return;
            }
            writer.println(Base64Util.encodeByBase64(username.getBytes()));
            writer.flush();
            line = reader.readLine();
            if (!SmtpStateCode.PASSWORD_SENT_DESC.equals(line)) {
                String jsonString = JSONObject.toJSONString(JsonResultFactory.buildFailureResult());
                session.sendMessage(new TextMessage(jsonString));
                return;
            }
            writer.println(Base64Util.encodeByBase64(password.getBytes()));
            writer.flush();
            line = reader.readLine();
            System.out.println(line);
            if (!SmtpStateCode.AUTH_SUCCESS_DESC.equals(line)) {
                String jsonString = JSONObject.toJSONString(JsonResultFactory.buildFailureResult());
                session.sendMessage(new TextMessage(jsonString));
                return;
            }
            writer.println(CommandConstant.MAIL_FROM + ": " + "<" + emailMessage.getSenderEmail() + ">");
            writer.flush();
            line = reader.readLine();
            if (!SmtpStateCode.SUCCESS_DESC.equals(line)) {
                String jsonString = JSONObject.toJSONString(JsonResultFactory.buildFailureResult());
                session.sendMessage(new TextMessage(jsonString));
                return;
            }
            writer.println(CommandConstant.RCPT_TO + ": " + "<" + emailMessage.getReceiverEmail() + ">");
            writer.flush();
            line = reader.readLine();
            if (!SmtpStateCode.SUCCESS_DESC.equals(line)) {
                String jsonString = JSONObject.toJSONString(JsonResultFactory.buildFailureResult());
                session.sendMessage(new TextMessage(jsonString));
                return;
            }
            writer.println(CommandConstant.DATA);
            writer.flush();
            line = reader.readLine();
            if (!SmtpStateCode.START_EMAIL_INPUT_DESC.equals(line)) {
                String jsonString = JSONObject.toJSONString(JsonResultFactory.buildFailureResult());
                session.sendMessage(new TextMessage(jsonString));
                return;
            }
            writer.println("from:" + emailMessage.getSenderEmail());
            writer.println("to:" + emailMessage.getReceiverEmail());
            writer.println("subject:" + emailMessage.getSubject());
            writer.println("body:" + emailMessage.getBody());
            writer.println(".");
            writer.flush();
            line = reader.readLine();
            if (!(SmtpStateCode.SUCCESS + " Send email Successful").equals(line)) {
                String jsonString = JSONObject.toJSONString(JsonResultFactory.buildFailureResult());
                session.sendMessage(new TextMessage(jsonString));
                return;
            }
            String jsonString = JSONObject.toJSONString(JsonResultFactory.buildSuccessResult());
            session.sendMessage(new TextMessage(jsonString));
        } catch (IOException e) {
            e.printStackTrace();
            session.sendMessage(new TextMessage("SMTP服务器不可用"));
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

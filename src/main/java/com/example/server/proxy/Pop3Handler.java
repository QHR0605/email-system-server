package com.example.server.proxy;

import com.alibaba.fastjson.JSONObject;
import com.example.server.config.SpringContextConfig;
import com.example.server.dto.EmailMessage;
import com.example.server.entity.ServerMessage;
import com.example.server.service.AdminService;
import com.example.server.service.impl.AdminServiceImpl;
import com.example.server.util.command.CommandConstant;
import com.example.server.util.json.JsonResult;
import com.example.server.util.json.JsonResultFactory;
import com.example.server.util.json.JsonResultStateCode;
import com.example.server.util.json.Pop3StateCode;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.AbstractWebSocketHandler;

import javax.annotation.PostConstruct;
import java.io.*;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.sql.Date;
import java.util.LinkedList;
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
    private String password;
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
        try {
            String msg = message.getPayload();
            if (CommandConstant.QUIT.equals(msg)) {
                writer.println(msg);
                writer.flush();
                System.out.println(reader.readLine());
                session.sendMessage(new TextMessage(JSONObject.toJSONString(JsonResultFactory.buildSuccessResult())));
                reader.readLine();
                reader.close();
                writer.close();
                socket.close();
                session.close();
            } else if (msg.startsWith(CommandConstant.DELE)) {
                writer.println(msg);
                String line = reader.readLine();
                System.out.println(line);
                if (!Pop3StateCode.OK.equals(line)) {
                    reader.readLine();
                    session.sendMessage(new TextMessage(JSONObject.toJSONString(JsonResultFactory.buildFailureResult())));
                    return;
                }
                reader.readLine();
                session.sendMessage(new TextMessage(JSONObject.toJSONString(JsonResultFactory.buildSuccessResult())));
            } else if (msg.startsWith(CommandConstant.REST)) {
                writer.println(msg);
                String line = reader.readLine();
                System.out.println(line);
                if (!Pop3StateCode.OK.equals(line)) {
                    reader.readLine();
                    session.sendMessage(new TextMessage(JSONObject.toJSONString(JsonResultFactory.buildFailureResult())));
                    return;
                }
                reader.readLine();
                session.sendMessage(new TextMessage(JSONObject.toJSONString(JsonResultFactory.buildSuccessResult())));
            } else if (msg.equals("GET")) {
                String line = null;
                writer.println(CommandConstant.STAT);
                line = reader.readLine();
                System.out.println(line);
                if (!line.startsWith(Pop3StateCode.OK)) {
                    String jsonString = JSONObject.toJSONString(JsonResultFactory.buildFailureResult());
                    reader.readLine();
                    session.sendMessage(new TextMessage(jsonString));
                    return;
                }
                reader.readLine();
                String[] args = line.split(" ");
                int emailCounts = Integer.parseInt(args[1]);
                List<EmailMessage> emailMessages = new LinkedList<>();
                System.out.println("emailCounts: " + emailCounts);
                for (int i = 0; i < emailCounts; i++) {
                    writer.println(CommandConstant.RETR + " " + (i + 1));
                    line = reader.readLine();
                    System.out.println(line);
                    if (!line.startsWith(Pop3StateCode.OK)) {
                        session.sendMessage(new TextMessage(JSONObject.toJSONString(JsonResultFactory.buildFailureResult())));
                        return;
                    }
                    EmailMessage emailMessage = new EmailMessage();
                    while (!(line = reader.readLine()).equals("#end#")) {
                        System.out.println(line);
                        String value = line.substring(line.indexOf("<") + 1, line.indexOf(">"));
                        if (line.startsWith("From")) {
                            emailMessage.setSenderEmail(value);
                        } else if (line.startsWith("To")) {
                            emailMessage.setReceiverEmail(value);
                        } else if (line.startsWith("Subject")) {
                            emailMessage.setSubject(value);
                        } else if (line.startsWith("Body")) {
                            emailMessage.setBody(value);
                        } else if (line.startsWith("SendTime")) {
                            Date sendTime = Date.valueOf(value);
                            emailMessage.setSendTime(sendTime);
                        }
                    }
                    emailMessages.add(emailMessage);
                }
                JsonResult jsonResult;
                if (emailMessages.size() == 0) {
                    jsonResult = JsonResultFactory.buildJsonResult(
                            JsonResultStateCode.NOT_FOUND,
                            JsonResultStateCode.NOT_FOUND_DESC,
                            null
                    );
                } else {
                    jsonResult = JsonResultFactory.buildJsonResult(
                            JsonResultStateCode.SUCCESS,
                            JsonResultStateCode.SUCCESS_DESC,
                            emailMessages
                    );
                }
                session.sendMessage(new TextMessage(JSONObject.toJSONString(jsonResult)));
            }

        } catch (IOException e) {
            e.printStackTrace();
            session.sendMessage(new TextMessage("POP3服务器不可用"));
        } catch (Exception e) {
            e.printStackTrace();
            session.sendMessage(new TextMessage("POP3服务出现故障"));
        }

    }

    @Override
    public void afterConnectionEstablished(WebSocketSession webSocketSession) throws Exception {

        webSocketHandlers.add(this);
        addClientCount();
        try {
            socket = new Socket(hostname, port);
            reader = new BufferedReader(new InputStreamReader(socket.getInputStream(), StandardCharsets.UTF_8));
            writer = new PrintWriter(new OutputStreamWriter(socket.getOutputStream()), true);
            webSocketSession.sendMessage(new TextMessage("连接成功！"));
        } catch (IOException e) {
            e.printStackTrace();
            webSocketSession.sendMessage(new TextMessage("POP3服务不可用"));
        }
        webSocketSession.sendMessage(new TextMessage(reader.readLine()));
        List<String> usernames = webSocketSession.getHandshakeHeaders().get("username");
        List<String> passwords = webSocketSession.getHandshakeHeaders().get("password");
        username = usernames.get(0);
        password = passwords.get(0);
        String line = null;
        writer.println(CommandConstant.USER + " " + username);
        line = reader.readLine();
        System.out.println("返回: " + line);
        reader.readLine();
        if (!line.startsWith(Pop3StateCode.OK)) {
            String jsonString = JSONObject.toJSONString(JsonResultFactory.buildFailureResult());
            webSocketSession.sendMessage(new TextMessage(jsonString));
            return;
        }
        writer.println(CommandConstant.PASS + " " + password);
        line = reader.readLine();
        System.out.println(line);
        reader.readLine();
        if (!line.startsWith(Pop3StateCode.OK)) {
            String jsonString = JSONObject.toJSONString(JsonResultFactory.buildFailureResult());
            webSocketSession.sendMessage(new TextMessage(jsonString));
            return;
        }
        socketSession = webSocketSession;
    }

    @Override
    public void handleTransportError(WebSocketSession webSocketSession, Throwable throwable) throws Exception {
        if (webSocketSession.isOpen()) {
            reader.close();
            writer.close();
            socket.close();
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

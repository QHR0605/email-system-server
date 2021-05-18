package com.example.server;

import com.example.server.config.SpringContextConfig;
import com.example.server.entity.ServerMessage;
import com.example.server.server.Pop3Server;
import com.example.server.server.SmtpServer;
import com.example.server.service.impl.SupperAdminImpl;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

import java.util.List;

@SpringBootApplication
@MapperScan(basePackages = "com.example.server.mapper")
public class ServerApplication {

    public static SmtpServer smtpServer = null;
    public static Pop3Server pop3Server = null;

    private SmtpServer smtpServer1;
    private Pop3Server pop3Server1;

    private Boolean smtpOn;
    private Boolean pop3On;


    public SmtpServer getSmtpServer() {
        return smtpServer;
    }

    public void setSmtpServer(SmtpServer smtpServer) {
        ServerApplication.smtpServer = smtpServer;
    }

    public Pop3Server getPop3Server() {
        return pop3Server;
    }

    public void setPop3Server(Pop3Server pop3Server) {
        ServerApplication.pop3Server = pop3Server;
    }

    public static void main(String[] args) {
        //获取当前项目的上下文对象
        ConfigurableApplicationContext context = SpringApplication.run(ServerApplication.class, args);
        //自定义的上下文对象
        SpringContextConfig contextConfig = new SpringContextConfig();
        //将当前上下文对象赋给我们自定义的上下文对象,调用该对象的方法实现对象的自动注入
        contextConfig.setApplicationContext(context);
        ServerApplication serverApplication = new ServerApplication();
        serverApplication.init();
        //启动SMTP服务
        if (serverApplication.smtpOn) {
            Pop3Server.setShutDown(false);
            smtpServer.start();
        } else {
            SmtpServer.setShutDown(true);
            System.out.println("SMTP服务器处于关闭状态");
        }
        if (serverApplication.pop3On) {
            Pop3Server.setShutDown(false);
            pop3Server.start();
        } else {
            Pop3Server.setShutDown(true);
            System.out.println("POP3服务器处于关闭状态");
        }
    }

    public void init() {
        List<ServerMessage> messages = SpringContextConfig.getBean(SupperAdminImpl.class).getServersMsg();
        if (messages != null) {
            ServerMessage msg = messages.get(0);
            if (smtpOn(msg)) {
                this.smtpServer1 = new SmtpServer();
                smtpServer = smtpServer1;
            }
            if (pop3On(msg)) {
                this.pop3Server1 = new Pop3Server();
                pop3Server = pop3Server1;
            }
        }

    }

    public Boolean pop3On(ServerMessage serverMessage) {
        pop3On = serverMessage.getPop3State();
        return pop3On;
    }

    public Boolean smtpOn(ServerMessage serverMessage) {
        smtpOn = serverMessage.getSmtpState();
        return smtpOn;
    }


}

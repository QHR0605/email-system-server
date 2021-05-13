package com.example.server;

import com.example.server.config.SpringContextConfig;
import com.example.server.server.Pop3Server;
import com.example.server.server.SmtpServer;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

@SpringBootApplication
@MapperScan(basePackages = "com.example.server.mapper")
public class ServerApplication {

    public static void main(String[] args) {
        //获取当前项目的上下文对象
        ConfigurableApplicationContext context = SpringApplication.run(ServerApplication.class, args);
        //自定义的上下文对象
        SpringContextConfig contextConfig = new SpringContextConfig();
        //将当前上下文对象赋给我们自定义的上下文对象,调用该对象的方法实现对象的自动注入
        contextConfig.setApplicationContext(context);
        //启动SMTP服务
//        SmtpServer smtpServer = new SmtpServer();
//        smtpServer.start();
        //启动POP3服务
        Pop3Server pop3Server = new Pop3Server();
        pop3Server.start();
    }

}

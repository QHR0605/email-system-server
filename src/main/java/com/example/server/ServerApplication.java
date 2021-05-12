package com.example.server;

import com.example.server.config.SpringContextConfig;
import com.example.server.server.SmtpServer;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

@SpringBootApplication
@MapperScan(basePackages = "com.example.server.mapper")
public class ServerApplication {

    public static void main(String[] args) {
        ConfigurableApplicationContext context = SpringApplication.run(ServerApplication.class, args);
        SpringContextConfig contextConfig = new SpringContextConfig();
        contextConfig.setApplicationContext(context);
        SmtpServer smtpServer = new SmtpServer();
        smtpServer.start();
    }

}

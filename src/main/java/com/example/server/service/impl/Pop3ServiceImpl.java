package com.example.server.service.impl;

import com.example.server.config.SpringContextConfig;
import com.example.server.dto.Pop3Session;
import com.example.server.mapper.MailMapper;
import com.example.server.service.AuthService;
import com.example.server.service.Pop3Service;

import java.io.*;
import java.net.Socket;

public class Pop3ServiceImpl extends Pop3Service {

    private final AuthService authService = SpringContextConfig.getBean("AuthServiceImpl");
    private final MailMapper mailMapper = SpringContextConfig.getBean(MailMapper.class);

    public Pop3ServiceImpl(Socket socket, Pop3Session pop3Session) {
        super(socket, pop3Session);
        try {
            this.writer = new PrintWriter(new OutputStreamWriter(socket.getOutputStream()), true);
            this.reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void handleUserCommand(String[] args) {


    }

    @Override
    public void handlePassCommand(String[] args) {

    }

    @Override
    public void handleStatCommand(String[] args) {

    }

    @Override
    public void handleListCommand(String[] args) {

    }

    @Override
    public void handleRetrCommand(String[] args) {

    }

    @Override
    public void handleDeleCommand(String[] args) {

    }

    @Override
    public void handleRestCommand(String[] args) {

    }
}

package com.example.server.service.impl;

import com.example.server.config.SpringContextConfig;
import com.example.server.dto.SmtpSession;
import com.example.server.entity.User;
import com.example.server.service.AuthService;
import com.example.server.service.SmtpService;
import com.example.server.util.annotation.isAuth;
import com.example.server.util.annotation.isHello;
import com.example.server.util.annotation.isMail;
import com.example.server.util.annotation.isRcpt;
import com.example.server.util.command.CommandConstant;
import com.example.server.util.json.SmtpStateCode;

import java.io.*;
import java.net.Socket;

/**
 * @author 全鸿润
 */
public class SmtpServiceImpl extends SmtpService {

    private final AuthService authService = SpringContextConfig.getBean(AuthService.class);

    public SmtpServiceImpl(Socket socket, SmtpSession smtpSession) {
        super(socket, smtpSession);
        try {
            this.writer = new PrintWriter(new OutputStreamWriter(socket.getOutputStream()), true);
            this.reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void handleHelloCommand(String[] args) {
        if (args.length != 2) {
            writer.println(SmtpStateCode.COMMAND_ERROR_DESC);
        }
        this.writer.println(SmtpStateCode.SUCCESS_DESC);
        this.session.setHelloSent(true);
    }

    @Override
    @isHello
    public void handleAuthCommand(String[] args) throws IOException {
        if (!this.session.isHelloSent()) {
            this.writer.println(SmtpStateCode.SEQUENCE_ERROR + " send HELO first");
            return;
        }
        if (args.length != 2) {
            this.writer.println(SmtpStateCode.COMMAND_ERROR_DESC);
        } else {
            String command = args[0] + args[1];
            if (!CommandConstant.AUTH_LOGIN.replaceAll(" ", "").equals(command)) {
                this.writer.println(SmtpStateCode.COMMAND_ERROR_DESC);
            } else {
                this.writer.println(SmtpStateCode.USERNAME_SENT_DESC);
                String encodedUsername = this.reader.readLine();
                this.writer.println(SmtpStateCode.PASSWORD_SENT_DESC);
                String encodedPassword = this.reader.readLine();
                String smtpResult = authService.handleLogin(encodedUsername, encodedPassword);
                if (SmtpStateCode.AUTH_SUCCESS_DESC.equals(smtpResult)) {
                    this.session.setAuthSent(true);
                }
                this.writer.println(smtpResult);
            }
        }
    }

    @Override
    @isHello
    @isAuth
    public void handleMailCommand(String[] args) {
        if (!this.session.isAuthSent()) {
            this.writer.println(SmtpStateCode.SEQUENCE_ERROR_DESC);
            return;
        }
        if (args.length <= 2) {
            this.writer.println(SmtpStateCode.COMMAND_ERROR_DESC);
        } else {
            int beginIndex = args[2].indexOf("<");
            int endIndex = args[2].indexOf(">");
            String username = args[2].substring(beginIndex, endIndex);
            System.out.println("发件人: " + username);
            this.writer.println(SmtpStateCode.SUCCESS_DESC);
            this.session.setMailSent(true);
        }
    }

    @Override
    @isHello
    @isAuth
    public void handleRcptCommand(String[] args) {
        if (!this.session.isHelloSent() || !this.session.isAuthSent()) {
            this.writer.println(SmtpStateCode.SEQUENCE_ERROR_DESC);
        } else if (!this.session.isMailSent()) {
            this.writer.println(" send MAIL FROM:<sender address> first");
            return;
        }
        if (args.length <= 2) {
            this.writer.println(SmtpStateCode.COMMAND_ERROR_DESC);
        } else {
            int beginIndex = args[2].indexOf("<");
            int endIndex = args[2].indexOf(">");
            String username = args[2].substring(beginIndex, endIndex);
            System.out.println("收件人: " + username);
            User user = authService.findUserByUsername(username);
            if (user == null) {
                this.writer.println(SmtpStateCode.ADDRESS_NOT_AVAILABLE_DESC);
                return;
            }
            this.session.setRcptSent(true);
            this.writer.println(SmtpStateCode.SUCCESS_DESC);
        }
    }

    @Override
    @isHello
    @isAuth
    @isMail
    @isRcpt
    public void handleDataCommand(String[] args) {
        return;
    }

    @Override
    @isHello
    public void handleResetCommand(String[] args) {
        return;
    }

    @Override
    @isHello
    public void handleQuitCommand(String[] args) {
        return;
    }
}

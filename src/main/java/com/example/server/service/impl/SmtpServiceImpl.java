package com.example.server.service.impl;

import com.example.server.config.SpringContextConfig;
import com.example.server.dto.SmtpSession;
import com.example.server.entity.Email;
import com.example.server.entity.User;
import com.example.server.mapper.MailMapper;
import com.example.server.service.AuthService;
import com.example.server.service.SmtpService;
import com.example.server.util.annotation.isAuth;
import com.example.server.util.annotation.isHello;
import com.example.server.util.annotation.isMail;
import com.example.server.util.annotation.isRcpt;
import com.example.server.util.base64.Base64Util;
import com.example.server.util.command.CommandConstant;
import com.example.server.util.idGenerator.IdGenerator;
import com.example.server.util.json.SmtpStateCode;

import java.io.*;
import java.net.Socket;
import java.sql.Date;

/**
 * @author 全鸿润
 */
public class SmtpServiceImpl extends SmtpService {

    private final AuthService authService = SpringContextConfig.getBean(AuthService.class);
    private final MailMapper mailMapper = SpringContextConfig.getBean(MailMapper.class);

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
            this.writer.println(SmtpStateCode.COMMAND_ERROR_DESC);
            return;
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
                    //这才是真正的发信人地址
                    this.session.setSender(Base64Util.decodeByBase64(encodedUsername.getBytes()));
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
            String username = args[2].substring(beginIndex + 1, endIndex);
            System.out.println("信封上的发件人: " + username);
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
            return;
        } else if (!this.session.isMailSent()) {
            this.writer.println(" send MAIL FROM:<sender address> first");
            return;
        } else {
            if (args.length <= 2) {
                this.writer.println(SmtpStateCode.COMMAND_ERROR_DESC);
            } else {
                int beginIndex = args[2].indexOf("<");
                int endIndex = args[2].indexOf(">");
                String receiver = args[2].substring(beginIndex + 1, endIndex);
                System.out.println("收件人:" + receiver);
                this.session.getReceivers().add(receiver);
                User user = authService.findUserByUsername(receiver);
                if (user == null) {
                    this.writer.println(SmtpStateCode.ADDRESS_NOT_AVAILABLE_DESC + "<" + receiver + ">");
                    return;
                }
                this.session.setRcptSent(true);
                this.writer.println(SmtpStateCode.SUCCESS_DESC);
            }
        }

    }

    @Override
    @isHello
    @isAuth
    @isMail
    @isRcpt
    public void handleDataCommand(String[] args) {
        if (!this.session.isHelloSent()) {
            this.writer.println(SmtpStateCode.SEQUENCE_ERROR + " send HELO first");
            return;
        } else if (!this.session.isRcptSent()) {
            this.writer.println(SmtpStateCode.SEQUENCE_ERROR_DESC);
            return;
        } else if (args.length > 2 || !CommandConstant.DATA.equals(args[0])) {
            this.writer.println(SmtpStateCode.COMMAND_ERROR_DESC);
            return;
        } else {
            this.writer.println(SmtpStateCode.START_EMAIL_INPUT_DESC);
        }
        String line = null;
        String from = null;
        String to = null;
        String subject = null;
        StringBuilder body = new StringBuilder();

        try {
            while (true) {
                line = this.reader.readLine();
                System.out.println(line);
                if (".".equals(line)) {
                    break;
                } else if (line.startsWith("from")) {
                    int index = line.indexOf(":");
                    from = line.substring(index + 1);
                } else if (line.startsWith("to")) {
                    int index = line.indexOf(":");
                    to = line.substring(index + 1);
                } else if (line.startsWith("subject")) {
                    int index = line.indexOf(":");
                    subject = line.substring(index + 1);
                } else {
                    body.append(line);
                }
            }
            for (String receiver : this.session.getReceivers()
            ) {
                Email email = new Email();
                Integer mid = IdGenerator.getId();
                email.setMid(mid);
                email.setSenderEmail(this.session.getSender());
                email.setReceiverEmail(receiver);
                email.setSubject(subject);
                email.setBody(body.toString());
                email.setSize(body.toString().getBytes().length);
                email.setSendTime(new Date(System.currentTimeMillis()));
                email.setSend(true);
                email.setRead(false);
                email.setDeleted(false);
                email.setTag(false);
                try {
                    Integer rows = mailMapper.addMail(email);
                    if (rows != 1) {
                        this.writer.println("邮件发送失败,收件人为: " + receiver);
                    }
                } catch (Exception e) {
                    this.writer.println("邮件发送失败,收件人为: " + receiver);
                    e.printStackTrace();
                }
            }
            this.writer.println(SmtpStateCode.SUCCESS + " Send email Successful");
            //清空发送人列表
            this.session.getReceivers().clear();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    @isHello
    public void handleResetCommand(String[] args) {
        this.writer.println(SmtpStateCode.SUCCESS_DESC);
    }

    @Override
    @isHello
    public void handleQuitCommand(String[] args) {
        try {
            this.writer.println(SmtpStateCode.BYE);
            this.socket.close();
            this.session = new SmtpSession();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

package com.example.server.service.impl;

import com.example.server.config.SpringContextConfig;
import com.example.server.dto.Pop3Session;
import com.example.server.entity.Email;
import com.example.server.mapper.MailMapper;
import com.example.server.service.AuthService;
import com.example.server.service.Pop3Service;
import com.example.server.util.json.Pop3StateCode;
import com.example.server.util.json.SmtpStateCode;

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

    /**
     * 接收用户名
     * @param args
     */
    @Override
    public void handleUserCommand(String[] args) {
        if (args.length != 2) {
            this.writer.println(Pop3StateCode.VALID);
        }
        pop3Session.setUsername(args[1]);
    }

    /**
     * 接收用户密码 → 登录验证 → 加载邮件
     * @param args
     */
    @Override
    public void handlePassCommand(String[] args) {
        if (args.length != 2) {
            this.writer.println(Pop3StateCode.ERR + Pop3StateCode.VALID);
        } else if (!pop3Session.isUserSent()) {
            this.writer.println(Pop3StateCode.ERR + Pop3StateCode.USER);
        }
        String username = pop3Session.getUsername();
        String password = args[1];
        String result = authService.handleLogin(username, password);
        if ("SUCCESS".equals(result)) {
            // 加载邮件
            pop3Session.setEmails(mailMapper.findMailsByRcpt(username));
            result = Pop3StateCode.OK + pop3Session.getCount() + " " + Pop3StateCode.AUTH_SUCCESS;
            pop3Session.setAuth(true);
        } else {
            result = Pop3StateCode.ERR + Pop3StateCode.AUTH_FAIL;
            pop3Session.setAuth(false); // 重新登录但是验证失败
        }
        this.writer.println(result);
    }

    /**
     * 统计邮件信息，返回 邮件总数 和 总字节数
     * @param args
     */
    @Override
    public void handleStatCommand(String[] args) {
        if (!pop3Session.isAuth()) {
            this.writer.println(Pop3StateCode.ERR + Pop3StateCode.AUTH_FAIL);
        }
        writer.println(Pop3StateCode.OK + pop3Session.getCount() + ' ' + pop3Session.getTotalSize());
    }

    /**
     * 列出邮件信息： 序号(从1开始) 字节大小
     * @param args
     */
    @Override
    public void handleListCommand(String[] args) {
        if (!pop3Session.isAuth()) {
            this.writer.println(Pop3StateCode.ERR + Pop3StateCode.AUTH_FAIL);
        }
        StringBuilder result = new StringBuilder(Pop3StateCode.OK);
        int index = 1;
        for(Email email : pop3Session.getEmails()) {
            result.append('\n' + index + ' ' + email.getSize());
            ++index;
        }
        writer.println(result);
    }

    @Override
    public void handleRetrCommand(String[] args) {
        if (args.length != 2) {
            this.writer.println(Pop3StateCode.ERR + Pop3StateCode.VALID);
        } else if (!pop3Session.isAuth()) {
            this.writer.println(Pop3StateCode.ERR + Pop3StateCode.AUTH_FAIL);
        }
        // 取回的邮件编号 大于 邮件数量
        int index = Integer.parseInt(args[1]);
        if(index > pop3Session.getCount()) {
            writer.println(Pop3StateCode.ERR);
            return;
        }
        StringBuilder result = new StringBuilder(Pop3StateCode.OK);
        Email email = pop3Session.getEmails().get(index);
        result.append("From: <").append(email.getSenderEmail()).append(">").append('\n');
        result.append("To: <").append(email.getReceiverEmail()).append(">").append('\n');
        result.append("Date: ").append(email.getSendTime()).append('\n');
        result.append("Subject: ").append(email.getSubject()).append('\n');
        result.append("Body: ").append('\n');
        result.append(email.getBody()).append('\n');
        writer.println(result);
    }

    /**
     * 标记想删除哪些邮件
     * @param args
     */
    @Override
    public void handleDeleCommand(String[] args) {
        if (args.length != 2) {
            this.writer.println(Pop3StateCode.ERR + Pop3StateCode.VALID);
        } if (!pop3Session.isAuth()) {
            this.writer.println(Pop3StateCode.ERR + Pop3StateCode.AUTH_FAIL);
        }
        // 标记的邮件编号 大于 邮件数量
        int index = Integer.parseInt(args[1]);
        if(index > pop3Session.getCount()) {
            writer.println(Pop3StateCode.ERR);
            return;
        }
        pop3Session.getEmails().get(index).setDeleted(true);
        writer.println(Pop3StateCode.OK);
    }

    @Override
    public void handleRestCommand(String[] args) {
        if (args.length != 2) {
            this.writer.println(Pop3StateCode.ERR + Pop3StateCode.VALID);
        } if (!pop3Session.isAuth()) {
            this.writer.println(Pop3StateCode.ERR + Pop3StateCode.AUTH_FAIL);
        }
        // 取消标记的邮件编号 大于 邮件数量
        int index = Integer.parseInt(args[1]);
        if(index > pop3Session.getCount()) {
            writer.println(Pop3StateCode.ERR);
            return;
        }
        pop3Session.getEmails().get(index).setDeleted(false);
        writer.println(Pop3StateCode.OK);
    }

    /**
     * 断开服务器，此时要执行真正的删除
     * @param args 指令参数
     */
    @Override
    public void handleQuitCommand(String[] args) {
        if (!pop3Session.isAuth()) {
            this.writer.println(Pop3StateCode.ERR + Pop3StateCode.AUTH_FAIL);
        }
        for(Email email : pop3Session.getEmails()) {
            if(email.getDeleted())
                mailMapper.delectMailByMid(email.getMid());
        }
        writer.println(Pop3StateCode.OK + '\n' + Pop3StateCode.BYE);
    }
}

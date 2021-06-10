package com.example.server.service.impl;

import com.example.server.ServerApplication;
import com.example.server.config.SpringContextConfig;
import com.example.server.dto.MassEmail;
import com.example.server.dto.NewUserMessage;
import com.example.server.dto.ServerPortMsg;
import com.example.server.dto.ServerStateMsg;
import com.example.server.entity.*;
import com.example.server.mapper.AdminMapper;
import com.example.server.mapper.MailMapper;
import com.example.server.server.Pop3Server;
import com.example.server.server.SmtpServer;
import com.example.server.service.AdminService;
import com.example.server.util.http.CookieUtils;
import com.example.server.util.http.HttpUtil;
import com.example.server.util.idGenerator.IdGenerator;
import com.example.server.util.json.JsonResultStateCode;
import org.springframework.stereotype.Service;

import javax.servlet.http.Cookie;
import java.io.IOException;
import java.sql.Timestamp;
import java.util.LinkedList;
import java.util.List;

/**
 * @author 全鸿润
 */
@Service("SupperAdminImpl")
public class AdminServiceImpl implements AdminService {

    private final AdminMapper adminMapper = SpringContextConfig.getBean(AdminMapper.class);
    private final MailMapper mailMapper = SpringContextConfig.getBean(MailMapper.class);
    private Log log;

    public String getUsername() {
        String username = null;
        try {
            Cookie cookie = CookieUtils.findCookie(HttpUtil.getRequest().getCookies(), "username");
            if (cookie != null) {
                username = cookie.getValue();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return username;
    }

    public void createLog(String operation, Boolean state, String reason) {
        log = new Log()
                .logId(IdGenerator.getId())
                .username(getUsername())
                .operation(operation)
                .time(new Timestamp(System.currentTimeMillis()))
                .state(state)
                .reason(reason)
                .build();
    }

    @Override
    public Integer auth(List<String> usernames, Integer authType) {
        Integer row = null;
        StringBuilder content = new StringBuilder();
        for (String username : usernames
        ) {
            content.append(username).append(" ");
        }
        if (authType == 0) {
            createLog("授权<" + content + ">的权限为普通用户", true, null);
        } else if (authType == 1) {
            createLog("授权<" + content + ">的权限为管理员", true, null);
        }

        try {
            row = adminMapper.updateUserAuthorization(usernames, authType);
        } catch (Exception e) {
            e.printStackTrace();
            log.setState(false);
            log.setReason(e.getMessage());
            try {
                adminMapper.addLog(log);
            } catch (Exception exception) {
                exception.printStackTrace();
                return row;
            }
            return row;
        }
        try {
            adminMapper.addLog(log);
        } catch (Exception e) {
            e.printStackTrace();
            return row;
        }
        return row;
    }

    @Override
    public Integer createUser(NewUserMessage message) {

        User user = new User()
                .username(message.getUsername())
                .password(message.getPassword())
                .accountType(message.getAccountType());
        Integer row = null;
        createLog("创建账户:" + message.getUsername(), true, null);
        try {
            row = adminMapper.insertNewUser(user);
        } catch (Exception e) {
            e.printStackTrace();
            log.setState(false);
            log.setReason(e.getMessage());
            try {
                adminMapper.addLog(log);
            } catch (Exception exception) {
                exception.printStackTrace();
                return row;
            }
            return row;
        }
        try {
            adminMapper.addLog(log);
        } catch (Exception e) {
            e.printStackTrace();
            return row;
        }
        return row;
    }

    @Override
    public Integer deleteUsersByUsername(List<String> usernames) {
        Integer row = null;
        StringBuilder content = new StringBuilder();
        for (String username : usernames
        ) {
            content.append(username).append(" ");
        }
        createLog("删除账户<" + content + ">", true, null);
        try {
            row = adminMapper.deleteUsers(usernames);
        } catch (Exception e) {
            e.printStackTrace();
            log.setState(false);
            log.setReason(e.getMessage());
            try {
                adminMapper.addLog(log);
            } catch (Exception exception) {
                exception.printStackTrace();
                return row;
            }
            return row;
        }
        try {
            adminMapper.addLog(log);
        } catch (Exception e) {
            e.printStackTrace();
            return row;
        }
        return row;
    }

    @Override
    public Integer updateUsersLogState(List<String> usernames, Boolean logState) {

        Integer row = null;
        StringBuilder content = new StringBuilder();
        for (String username : usernames
        ) {
            content.append(username).append(" ");
        }
        if (logState) {
            createLog("注销账户:<" + content + ">", true, null);
        } else {
            createLog("恢复账户:<" + content + ">", true, null);
        }

        try {
            row = adminMapper.updateUserLogState(usernames, logState);
        } catch (Exception e) {
            e.printStackTrace();
            log.setState(false);
            log.setReason(e.getMessage());
            try {
                adminMapper.addLog(log);
            } catch (Exception exception) {
                exception.printStackTrace();
                return row;
            }
            return row;
        }
        try {
            adminMapper.addLog(log);
        } catch (Exception e) {
            e.printStackTrace();
            return row;
        }
        return row;
    }

    @Override
    public Integer updateUsersType(List<String> usernames, Integer type) {
        Integer row = null;
        StringBuilder content = new StringBuilder();
        for (String username : usernames
        ) {
            content.append(username).append(" ");
        }
        if (type == 0) {
            createLog("修改账户:<" + content + ">的用户类型为普通用户", true, null);
        } else if (type == 1) {
            createLog("修改账户:<" + content + ">的用户类型为管理员", true, null);
        }
        try {
            row = adminMapper.updateUserType(usernames, type);
        } catch (Exception e) {
            e.printStackTrace();
            log.setState(false);
            log.setReason(e.getMessage());
            try {
                adminMapper.addLog(log);
            } catch (Exception exception) {
                exception.printStackTrace();
                return row;
            }
            try {
                adminMapper.addLog(log);
            } catch (Exception exception) {
                exception.printStackTrace();
                return row;
            }
            return row;
        }
        return row;
    }

    @Override
    public Integer filterUsers(List<String> usernames, Boolean forbidden) {

        Integer row = null;
        StringBuilder content = new StringBuilder();
        for (String username : usernames
        ) {
            content.append(username).append(" ");
        }
        if (forbidden) {
            createLog("设置过滤账户<" + content + ">", true, null);
        } else {
            createLog("取消过滤账户<" + content + ">", true, null);
        }

        try {
            row = adminMapper.updateUserForbidden(usernames, forbidden);
        } catch (Exception e) {
            e.printStackTrace();
            log.setState(false);
            log.setReason(e.getMessage());
            try {
                adminMapper.addLog(log);
            } catch (Exception exception) {
                exception.printStackTrace();
                return row;
            }
            return row;
        }
        try {
            adminMapper.addLog(log);
        } catch (Exception e) {
            e.printStackTrace();
            return row;
        }
        return row;
    }

    @Override
    public Integer changeServerPort(ServerPortMsg msg) {

        Integer row = null;
        if (msg.getServerType() == 0) {
            createLog("修改SMTP服务端口为:" + msg.getServerPort(), true, null);
        } else if (msg.getServerType() == 1) {
            createLog("修改POP3服务端口为:" + msg.getServerPort(), true, null);
        }
        try {
            row = adminMapper.updateServerPort(msg);
        } catch (Exception e) {
            e.printStackTrace();
            log.setState(false);
            try {
                adminMapper.addLog(log);
            } catch (Exception exception) {
                exception.printStackTrace();
                return row;
            }
            return row;
        }
        try {
            adminMapper.addLog(log);
        } catch (Exception e) {
            e.printStackTrace();
            return row;
        }
        return row;
    }

    @Override
    public Integer changeServerState(ServerStateMsg msg) {
        Integer row = null;
        if (msg.getServerType() == 0) {
            if (msg.getServerState()) {
                createLog("开启SMTP服务", true, null);
            } else {
                createLog("关闭SMTP服务", true, null);
            }
        } else if (msg.getServerType() == 1) {
            if (msg.getServerState()) {
                createLog("开启POP3服务", true, null);
            } else {
                createLog("关闭POP3服务", true, null);
            }
        }
        try {
            row = adminMapper.updateServerState(msg);
        } catch (Exception e) {
            e.printStackTrace();
            log.setState(false);
            log.setReason(e.getMessage());
            try {
                adminMapper.addLog(log);
            } catch (Exception exception) {
                exception.printStackTrace();
                return row;
            }
            return row;
        }
        try {
            adminMapper.addLog(log);
        } catch (Exception e) {
            e.printStackTrace();
            return row;
        }
        return row;
    }


    @Override
    public Integer restartServer(ServerPortMsg msg) {
        try {
            if (msg.getServerType() == 0) {
                if (!SmtpServer.isShutDown() && SmtpServer.getPort() != msg.getServerPort()) {
                    ServerApplication.smtpServer.stopSmtpServer();
                    ServerApplication.smtpServer = new SmtpServer();
                    ServerApplication.smtpServer.start();
                } else if (SmtpServer.isShutDown()) {
                    ServerApplication.smtpServer = new SmtpServer();
                    ServerApplication.smtpServer.start();
                }
            } else if (msg.getServerType() == 1) {
                if (!Pop3Server.isShutDown() && Pop3Server.getPort() != msg.getServerPort()) {
                    ServerApplication.pop3Server.stopPop3Server();
                    ServerApplication.pop3Server = new Pop3Server();
                    ServerApplication.pop3Server.start();
                } else if (Pop3Server.isShutDown()) {
                    ServerApplication.pop3Server = new Pop3Server();
                    ServerApplication.pop3Server.start();
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
            return JsonResultStateCode.FAILED;
        }
        return JsonResultStateCode.SUCCESS;
    }

    @Override
    public Integer stopServer(ServerPortMsg msg) {
        try {
            if (msg.getServerType() == 0) {
                if (!SmtpServer.isShutDown() && SmtpServer.getPort() == msg.getServerPort()) {
                    ServerApplication.smtpServer.stopSmtpServer();
                }
            } else if (msg.getServerType() == 1) {
                if (!Pop3Server.isShutDown() && Pop3Server.getPort() == msg.getServerPort()) {
                    ServerApplication.pop3Server.stopPop3Server();
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
            return JsonResultStateCode.FAILED;
        }
        return JsonResultStateCode.SUCCESS;
    }

    @Override
    public List<ServerMessage> getServersMsg() {
        List<ServerMessage> res = null;
        try {
            res = adminMapper.selectServerMessage();
        } catch (Exception e) {
            e.printStackTrace();
            return res;
        }
        return res;
    }

    @Override
    public List<Filter> getFilters() {

        List<Filter> res = null;
        try {
            res = adminMapper.selectFilter();
        } catch (Exception e) {
            e.printStackTrace();
            return res;
        }
        return res;
    }

    @Override
    public Integer addFilter(List<String> filters) {

        Integer row = null;
        List<Filter> filterList = new LinkedList<>();
        StringBuilder content = new StringBuilder();
        for (String ip : filters
        ) {
            Filter filter = new Filter();
            filter.setIpAddress(ip);
            filter.setFid(IdGenerator.getId());
            filterList.add(filter);
            content.append(ip).append(" ");
        }
        createLog("添加IP黑名单信息<" + content + ">", true, null);
        try {
            row = adminMapper.insertNewIpAddress(filterList);
        } catch (Exception e) {
            e.printStackTrace();
            log.setState(false);
            log.setReason(e.getMessage());
            try {
                adminMapper.addLog(log);
            } catch (Exception exception) {
                exception.printStackTrace();
                return row;
            }
            return row;
        }
        try {
            adminMapper.addLog(log);
        } catch (Exception e) {
            e.printStackTrace();
            return row;
        }
        return row;
    }

    @Override
    public Integer deleteFilter(List<String> ipList) {
        Integer row = 0;
        createLog("删除IP黑名单信息", true, null);
        try {
            row = adminMapper.deleteIpAddress(ipList);
        } catch (Exception e) {
            e.printStackTrace();
            log.setState(false);
            log.setReason(e.getMessage());
            try {
                adminMapper.addLog(log);
            } catch (Exception exception) {
                exception.printStackTrace();
                return row;
            }
            return row;
        }
        try {
            adminMapper.addLog(log);
        } catch (Exception e) {
            e.printStackTrace();
            return row;
        }
        return row;
    }

    @Override
    public List<User> getAllUsers() {
        List<User> users = null;
        try {
            users = adminMapper.selectAllUsers();
        } catch (Exception e) {
            e.printStackTrace();
            return users;
        }
        return users;
    }

    @Override
    public Integer updateMailBoxSize(List<String> usernames, Integer size) {
        Integer row = 0;
        StringBuilder content = new StringBuilder();
        for (String username : usernames
        ) {
            content.append(usernames).append(": ").append(size).append(";");
        }
        createLog("修改用户<" + content + ">", true, null);
        try {
            row = adminMapper.updateMailBoxSize(usernames, size);
        } catch (Exception e) {
            e.printStackTrace();
            log.setState(false);
            log.setReason(e.getMessage());
            try {
                adminMapper.addLog(log);
            } catch (Exception exception) {
                exception.printStackTrace();
                return row;
            }
            return row;
        }
        try {
            adminMapper.addLog(log);
        } catch (Exception e) {
            e.printStackTrace();
            return row;
        }
        return row;
    }

    @Override
    public Integer addLog(Log log) {
        Integer row;
        try {
            row = adminMapper.addLog(log);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
        return row;
    }

    @Override
    public List<Log> getLogs() {
        List<Log> logList;
        try {
            logList = adminMapper.selectLogs();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
        return logList;
    }

    @Override
    public Integer deleteLog(List<Integer> idList) {
        Integer row;
        try {
            row = adminMapper.deleteLog(idList);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
        return row;
    }

    @Override
    public Integer sendMails(MassEmail emails) {
        Integer row = null;
        StringBuilder content = new StringBuilder();
        List<Email> emailList = new LinkedList<>();
        for (String receiver : emails.getReceiverEmails()
        ) {
            Email email = new Email(
                    IdGenerator.getId(),
                    emails.getSenderEmail(),
                    receiver,
                    emails.getSendTime(),
                    emails.getSubject(),
                    emails.getBody(),
                    false,
                    false,
                    false,
                    true,
                    emails.getAvatarUrl(),
                    null,
                    emails.getBody().length()
            );
            emailList.add(email);
            content.append(receiver).append(" ");
        }
        createLog("群发邮件: <" + content + ">", true, null);
        try {
            row = mailMapper.addEmails(emailList);
        } catch (Exception e) {
            e.printStackTrace();
            log.setState(false);
            log.setReason(e.getMessage());
            try {
                adminMapper.addLog(log);
            } catch (Exception exception) {
                exception.printStackTrace();
                return row;
            }
            return row;
        }
        try {
            adminMapper.addLog(log);
        } catch (Exception e) {
            e.printStackTrace();
            return row;
        }
        return row;
    }

}

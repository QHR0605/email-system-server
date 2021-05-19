package com.example.server.service.impl;

import com.example.server.ServerApplication;
import com.example.server.config.SpringContextConfig;
import com.example.server.dto.NewUserMessage;
import com.example.server.dto.ServerPortMsg;
import com.example.server.dto.ServerStateMsg;
import com.example.server.entity.Filter;
import com.example.server.entity.ServerMessage;
import com.example.server.entity.User;
import com.example.server.mapper.AdminMapper;
import com.example.server.server.Pop3Server;
import com.example.server.server.SmtpServer;
import com.example.server.service.AdminService;
import com.example.server.util.idGenerator.IdGenerator;
import com.example.server.util.json.JsonResultStateCode;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

/**
 * @author 全鸿润
 */
@Service("SupperAdminImpl")
public class AdminImpl implements AdminService {

    private final AdminMapper adminMapper = SpringContextConfig.getBean(AdminMapper.class);

    @Override
    public Integer auth(List<String> usernames, Integer authType) {
        Integer row;
        try {
            row = adminMapper.updateUserAuthorization(usernames, authType);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
        return row;
    }

    @Override
    public Integer createUser(NewUserMessage message) {

        User user = new User()
                .username(message.getUsername())
                .password(message.getPassword())
                .accountType(message.getAccountType());
        Integer row;
        try {
            row = adminMapper.insertNewUser(user);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
        return row;
    }

    @Override
    public Integer deleteUsersByUsername(List<String> usernames) {
        Integer row;
        try {
            row = adminMapper.deleteUsers(usernames);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
        return row;
    }

    @Override
    public Integer updateUsersLogState(List<String> usernames, Boolean logState) {

        Integer row;
        try {
            row = adminMapper.updateUserLogState(usernames, logState);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
        return row;
    }

    @Override
    public Integer updateUsersType(List<String> usernames, Integer type) {
        Integer row;
        try {
            row = adminMapper.updateUserType(usernames, type);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
        return row;
    }

    @Override
    public Integer changeServerPort(ServerPortMsg msg) {

        Integer row;
        try {
            row = adminMapper.updateServerPort(msg);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
        return row;
    }

    @Override
    public Integer changeServerState(ServerStateMsg msg) {
        Integer row;
        try {
            row = adminMapper.updateServerState(msg);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
        return row;
    }

    @Override
    public List<ServerMessage> getServerMsg() {
        List<ServerMessage> res;
        try {
            res = adminMapper.selectServerMessage();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
        return res;
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
                System.out.println("重启SMTP服务器");
            } else if (msg.getServerType() == 1) {
                if (!Pop3Server.isShutDown() && Pop3Server.getPort() != msg.getServerPort()) {
                    ServerApplication.pop3Server.stopPop3Server();
                    ServerApplication.pop3Server = new Pop3Server();
                    ServerApplication.pop3Server.start();
                } else if (Pop3Server.isShutDown()) {
                    ServerApplication.pop3Server = new Pop3Server();
                    ServerApplication.pop3Server.start();
                }
                System.out.println("重启POP3服务器");
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
                    System.out.println("关闭SMTP服务器");
                }
            } else if (msg.getServerType() == 1) {
                if (!Pop3Server.isShutDown() && Pop3Server.getPort() == msg.getServerPort()) {
                    ServerApplication.pop3Server.stopPop3Server();
                    System.out.println("关闭POP3服务器");
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
        List<ServerMessage> res;
        try {
            res = adminMapper.selectServerMessage();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
        return res;
    }

    @Override
    public List<Filter> getFilters() {

        List<Filter> res;
        try {
            res = adminMapper.selectFilter();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
        return res;
    }

    @Override
    public Integer addFilter(List<String> filters) {

        Integer row;
        List<Filter> filterList = new LinkedList<>();
        for (String ip : filters
        ) {
            Filter filter = new Filter();
            filter.setIpAddress(ip);
            filter.setFid(IdGenerator.getId());
            filterList.add(filter);
        }
        try {
            row = adminMapper.insertNewIpAddress(filterList);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
        return row;
    }

    @Override
    public Integer deleteFilter(List<Integer> idList) {
        Integer row = 0;
        try {
            row = adminMapper.deleteIpAddress(idList);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
        return row;
    }

    @Override
    public List<User> getAllUsers() {
        List<User> users;
        try {
            users = adminMapper.selectAllUsers();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
        return users;
    }

}

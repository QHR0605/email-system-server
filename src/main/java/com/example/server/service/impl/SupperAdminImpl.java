package com.example.server.service.impl;

import com.example.server.ServerApplication;
import com.example.server.config.SpringContextConfig;
import com.example.server.dto.NewUserMessage;
import com.example.server.dto.ServerPortMsg;
import com.example.server.dto.ServerStateMsg;
import com.example.server.entity.Filter;
import com.example.server.entity.ServerMessage;
import com.example.server.entity.User;
import com.example.server.mapper.SuperAdminMapper;
import com.example.server.server.Pop3Server;
import com.example.server.server.SmtpServer;
import com.example.server.service.SupperAdminService;
import com.example.server.util.json.JsonResultStateCode;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;

/**
 * @author 全鸿润
 */
@Service("SupperAdminImpl")
public class SupperAdminImpl extends AdminServiceImpl implements SupperAdminService {

    private final SuperAdminMapper superAdminMapper = SpringContextConfig.getBean(SuperAdminMapper.class);

    @Override
    public Integer auth(List<String> usernames, Integer authType) {
        Integer row;
        try {
            row = superAdminMapper.updateUserAuthorization(usernames, authType);
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
            row = superAdminMapper.insertNewUser(user);
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
            row = superAdminMapper.updateServerPort(msg);
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
            row = superAdminMapper.updateServerState(msg);
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
            res = superAdminMapper.selectServerMessage();
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
            res = superAdminMapper.selectServerMessage();
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
            res = superAdminMapper.selectFilter();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
        return res;
    }

    @Override
    public Integer addFilter(Filter filter) {

        Integer row;
        try {
            row = superAdminMapper.insertNewIpAddress(filter);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
        return row;
    }

    @Override
    public Integer deleteFilter(Filter filter) {
        Integer row = 0;
        try {
            row = superAdminMapper.deleteIpAddress(filter);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
        return row;
    }

}

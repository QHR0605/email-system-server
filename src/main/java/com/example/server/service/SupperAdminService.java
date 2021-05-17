package com.example.server.service;

import com.example.server.dto.NewUserMessage;
import com.example.server.dto.ServerPortMsg;
import com.example.server.dto.ServerStateMsg;
import com.example.server.entity.Filter;
import com.example.server.entity.ServerMessage;

import java.util.List;

/**
 * @author 全鸿润
 */
public interface SupperAdminService extends AdminService {

    /**
     * 授权
     *
     * @param usernames 授权的用户集合
     * @param authType  授予的用户权限
     * @return 授予成功的个数
     */
    Integer auth(List<String> usernames, Integer authType);

    /**
     * 创建新账户
     *
     * @param message 账户信息
     * @return 创建成功的个数
     */
    Integer createUser(NewUserMessage message);

    /**
     * 修改端口号
     *
     * @param msg 服务器端口信息
     * @return 行数
     */
    Integer changeServerPort(ServerPortMsg msg);

    /**
     * 修改服务器状态
     *
     * @param msg 服务器状态信息
     * @return 行数
     */
    Integer changeServerState(ServerStateMsg msg);

    /**
     * 获取服务信息集合
     *
     * @return 服务器信息
     */
    List<ServerMessage> getServerMsg();

    /**
     * 启动服务
     *
     * @param msg 服务信息
     */
    Integer restartServer(ServerPortMsg msg);

    /**
     * 关闭服务
     *
     * @param msg 服务端口信息
     */
    Integer stopServer(ServerPortMsg msg);

    List<ServerMessage> getServersMsg();

    List<Filter> getFilters();

    Integer addFilter(Filter filter);

    Integer deleteFilter(Filter filter);


}

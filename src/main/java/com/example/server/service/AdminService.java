package com.example.server.service;

import com.example.server.dto.MailBoxSize;
import com.example.server.dto.NewUserMessage;
import com.example.server.dto.ServerPortMsg;
import com.example.server.dto.ServerStateMsg;
import com.example.server.entity.Filter;
import com.example.server.entity.Log;
import com.example.server.entity.ServerMessage;
import com.example.server.entity.User;
import io.swagger.models.auth.In;

import java.util.List;

/**
 * @author 全鸿润
 */
public interface AdminService {

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
     * 批量删除用户
     *
     * @param username 要删除的用户名单
     * @return 删除个数
     */
    Integer deleteUsersByUsername(List<String> username);

    /**
     * 批量注销个数
     *
     * @param username 用户名单
     * @param logState 注销状态
     * @return 注销个数
     */
    Integer updateUsersLogState(List<String> username, Boolean logState);

    /**
     * 修改用户类型
     *
     * @param username 用户名单
     * @param type     用户类型
     * @return 修改个数
     */
    Integer updateUsersType(List<String> username, Integer type);

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
     * 启动服务
     *
     * @param msg 服务信息
     * @return 重启是否成功的状态码
     */
    Integer restartServer(ServerPortMsg msg);

    /**
     * 关闭服务
     *
     * @param msg 服务端口信息
     * @return 暂停是否成功的状态码
     */
    Integer stopServer(ServerPortMsg msg);

    /**
     * 获取服务器信息
     *
     * @return 服务器信息
     */
    List<ServerMessage> getServersMsg();

    /**
     * 获取IP黑名单列表
     *
     * @return IP黑名单列表
     */
    List<Filter> getFilters();

    /**
     * 添加的IP黑名单列表
     *
     * @param filters 新添加的IP黑名单列表
     * @return 添加成功行数
     */
    Integer addFilter(List<String> filters);

    /**
     * 删除黑名单
     *
     * @param idList 黑名单id列表
     * @return 删除行数
     */
    Integer deleteFilter(List<Integer> idList);

    /**
     * 获取所有用户
     *
     * @return 所有用户
     */
    List<User> getAllUsers();

    /**
     * 修改邮箱大小
     * @param mailBoxSizeList 要修改的邮箱及其大小
     * @return 修改行数
     */
    Integer updateMailBoxSize(List<MailBoxSize> mailBoxSizeList);

    Integer addLog(Log log);

    List<Log> getLogs();

    Integer deleteLog(List<Integer> idList);

}

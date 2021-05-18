package com.example.server.mapper;

import com.example.server.dto.ServerPortMsg;
import com.example.server.dto.ServerStateMsg;
import com.example.server.entity.Filter;
import com.example.server.entity.ServerMessage;
import com.example.server.entity.User;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author 全鸿润
 */
@Repository
public interface SuperAdminMapper {
    /**
     * 修改权限
     *
     * @param usernames 用户集合
     * @param type      用户类型
     * @return 授权的个数
     * @throws Exception 数据库操作异常
     */
    Integer updateUserAuthorization(List<String> usernames, Integer type) throws Exception;

    /**
     * 创建新账户
     *
     * @param user 新用户
     * @return 插入个数
     * @throws Exception 数据库操作异常
     */
    Integer insertNewUser(User user) throws Exception;

    /**
     * 获取服务器信息
     *
     * @return 服务器信息
     */
    List<ServerMessage> selectServerMessage() throws Exception;

    /**
     * 修改服务器端口
     *
     * @param msg 服务器端口信息
     * @return 修改行数
     * @throws Exception 数据库操作异常
     */
    Integer updateServerPort(ServerPortMsg msg) throws Exception;

    /**
     * 修改服务器状态
     *
     * @param msg 服务器启停状态
     * @return 修改行数
     * @throws Exception 数据库操作异常
     */
    Integer updateServerState(ServerStateMsg msg) throws Exception;

    List<Filter> selectFilter() throws Exception;

    Integer insertNewIpAddress(Filter filter) throws Exception;

    Integer deleteIpAddress(Filter filter) throws Exception;
}

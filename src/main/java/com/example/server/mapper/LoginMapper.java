package com.example.server.mapper;

import com.example.server.entity.User;

/**
 * @author 全鸿润
 */
public interface LoginMapper {

    /**
     * 通过用户名查询用户
     *
     * @param userName 用户名
     * @return 用户数据
     * @throws Exception 数据库操作异常
     */
    User findUserByUserName(String userName) throws Exception;
}

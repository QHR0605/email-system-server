package com.example.server.mapper;

import com.example.server.entity.User;

import java.util.List;

/**
 * @author 全鸿润
 */
public interface SuperAdminMapper {

    /**
     * 修改权限
     *
     * @param usernames 用户集合
     * @param auth      用户类型
     * @return 授权的个数
     * @throws Exception 数据库操作异常
     */
    Integer updateUserAuthorization(List<String> usernames, Boolean auth) throws Exception;

    /**
     * 创建新账户
     *
     * @param user 新用户
     * @param type 用户类型(普通用户和管理员)
     * @return 插入个数
     * @throws Exception 数据库操作异常
     */
    Integer insertNewUser(User user) throws Exception;
}

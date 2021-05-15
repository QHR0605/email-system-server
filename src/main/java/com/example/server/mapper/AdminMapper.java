package com.example.server.mapper;

import com.example.server.entity.User;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author 全鸿润
 */
@Repository
public interface AdminMapper {

    /**
     * 批量删除用户
     *
     * @param usernames 要删除的用户名集合
     * @return 删除的个数
     * @throws Exception 数据库操作异常
     */
    Integer deleteUsers(List<String> usernames) throws Exception;

    /**
     * 获取普通用户列表
     *
     * @return 普通用户列表
     * @throws Exception 数据库操作异常
     */
    List<User> selectUsers() throws Exception;


    /**
     * 获取用户列表
     *
     * @return 用户列表
     * @throws Exception 数据库操作异常
     */
    List<User> selectAllUsers() throws Exception;

    /**
     * 修改用户类型
     *
     * @param usernames 要修改的用户集合
     * @param type      用类型(0,1,2)
     * @return 修改的用户个数
     * @throws Exception 数据库操作异常
     */
    Integer updateUserType(List<String> usernames, Boolean type) throws Exception;

    /**
     * 批量注销用户
     *
     * @param usernames 要注销的用户集合
     * @param logState  注销状态
     * @return 注销的个数
     * @throws Exception 数据库操作异常
     */
    Integer updateUserLogState(List<String> usernames, Boolean logState) throws Exception;
}

package com.example.server.service;

/**
 * @author 全鸿润
 */

import com.example.server.entity.User;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface AdminService {

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
     * 获取所有普通用户
     *
     * @return 所有普通用户
     */
    List<User> getUsers();

    /**
     * 获取所有用户
     *
     * @return 所有用户
     */
    List<User> getAllUsers();

}

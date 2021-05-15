package com.example.server.service;

import com.example.server.dto.NewUserMessage;

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
    Integer auth(List<String> usernames, Boolean authType);

    /**
     * 创建新账户
     *
     * @param message 账户信息
     * @return 创建成功的个数
     */
    Integer createUser(NewUserMessage message);
}

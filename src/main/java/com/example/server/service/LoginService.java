package com.example.server.service;

/**
 * @author 全鸿润
 */
public interface LoginService {
    /**
     * 登录验证
     * @param username 用户名
     * @param password 密码
     * @return 验证状态
     */
    Integer handleLogin(String username , String password);
}

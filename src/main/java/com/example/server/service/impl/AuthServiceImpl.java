package com.example.server.service.impl;

import com.example.server.config.SpringContextConfig;
import com.example.server.dto.UserMessage;
import com.example.server.entity.User;
import com.example.server.mapper.LoginMapper;
import com.example.server.service.AuthService;
import org.springframework.stereotype.Service;

/**
 * @author 全鸿润
 */
@Service("AuthServiceImpl")
public class AuthServiceImpl implements AuthService {
    private final LoginMapper loginMapper = SpringContextConfig.getBean(LoginMapper.class);

    @Override
    public String handleLogin(String username, String password) {
        try {
            User user;
            System.out.println("username: " + username);
            System.out.println("password: " + password);
            user = loginMapper.findUserByUserName(username);
            if (user != null && user.getPassword().equals(password)) {
                return "SUCCESS";
            }
            return "FAILED";
        } catch (Exception e) {
            e.printStackTrace();
            return "FAILED";
        }
    }

    @Override
    public User findUserByUsername(String username) {
        try {
            return loginMapper.findUserByUserName(username);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public Integer registerUser(UserMessage userMessage) {
        return null;
    }

    @Override
    public Integer updatePassword(String username, String newPassword) {
        return null;
    }
}

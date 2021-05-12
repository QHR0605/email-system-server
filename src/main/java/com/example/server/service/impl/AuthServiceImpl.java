package com.example.server.service.impl;

import com.example.server.config.SpringContextConfig;
import com.example.server.entity.User;
import com.example.server.mapper.LoginMapper;
import com.example.server.service.AuthService;
import com.example.server.util.base64.Base64Util;
import com.example.server.util.json.SmtpStateCode;
import org.springframework.stereotype.Service;

/**
 * @author 全鸿润
 */
@Service("AuthServiceImpl")
public class AuthServiceImpl implements AuthService {
    private final LoginMapper loginMapper = SpringContextConfig.getBean(LoginMapper.class);

    @Override
    public String handleLogin(String encodedUsername, String encodedPassword) {
        try {
            User user;
            String username = Base64Util.decodeByBase64(encodedUsername.getBytes());
            String password = Base64Util.decodeByBase64(encodedPassword.getBytes());

            System.out.println("username: " + username);
            System.out.println("password: " + password);
            user = loginMapper.findUserByUserName(username);
            if (user != null && user.getPassword().equals(password)) {
                return SmtpStateCode.AUTH_SUCCESS_DESC;
            }
            return SmtpStateCode.AUTH_FAILED_DESC;
        } catch (Exception e) {
            e.printStackTrace();
            return SmtpStateCode.AUTH_FAILED_DESC;
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
}

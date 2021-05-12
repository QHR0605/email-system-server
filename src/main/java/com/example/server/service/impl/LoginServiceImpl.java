package com.example.server.service.impl;

/**
 * @author 全鸿润
 */

import com.example.server.config.SpringContextConfig;
import com.example.server.entity.User;
import com.example.server.mapper.LoginMapper;
import com.example.server.service.AuthService;
import com.example.server.util.json.JsonResultStateCode;
import org.springframework.stereotype.Service;

@Service("LoginServiceImpl")
public class LoginServiceImpl implements AuthService {

    private final LoginMapper loginMapper = SpringContextConfig.getBean(LoginMapper.class);

    @Override
    public String handleLogin(String username, String password) {
        try {
            User user;
            System.out.println("username: " + username);
            System.out.println("password: " + password);
            user = loginMapper.findUserByUserName(username);
            if (user == null) {
                return JsonResultStateCode.USERNAME_WRONG_DESC;
            } else {
                if (!user.getPassword().equals(password)) {
                    return JsonResultStateCode.PASSWORD_WRONG_DESC;
                } else {
                    return JsonResultStateCode.SUCCESS_DESC;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            return JsonResultStateCode.UNKNOWN_ERROR_DESC;
        }
    }

    @Override
    public User findUserByUsername(String username) {
        User user = null;
        try {
            user = loginMapper.findUserByUserName(username);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return user;
    }
}

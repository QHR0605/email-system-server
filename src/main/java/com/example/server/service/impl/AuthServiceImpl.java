package com.example.server.service.impl;

/**
 * @author 全鸿润
 */

import com.example.server.entity.User;
import com.example.server.mapper.LoginMapper;
import com.example.server.service.LoginService;
import com.example.server.util.base64.Base64Util;
import com.example.server.util.json.JsonResultStateCode;
import com.example.server.util.json.SmtpStateCode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Base64;

@Service
public class AuthServiceImpl implements LoginService {

    @Autowired
    private LoginMapper loginMapper;

    @Override
    public Integer handleLogin(String encodedUsername, String encodedPassword) {

        User user;
        String username = Base64Util.decodeByBase64(encodedUsername.getBytes());
        String password = Base64Util.decodeByBase64(encodedUsername.getBytes());
        try {
            user = loginMapper.findUserByUserName(username);
            if (user != null && user.getPassword().equals(password)){
                return SmtpStateCode.AUTH_SUCCESS;
            }
            return SmtpStateCode.AUTH_FAILED;
        } catch (Exception e) {
            e.printStackTrace();
            return JsonResultStateCode.UNKNOWN_ERROR;
        }
    }
}

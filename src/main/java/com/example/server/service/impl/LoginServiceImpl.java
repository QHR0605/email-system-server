package com.example.server.service.impl;

/**
 * @author 全鸿润
 */

import com.example.server.config.SpringContextConfig;
import com.example.server.dto.LatestLoginMsg;
import com.example.server.dto.UserMessage;
import com.example.server.entity.User;
import com.example.server.mapper.LoginMapper;
import com.example.server.mapper.UserMapper;
import com.example.server.service.AuthService;
import com.example.server.util.http.CookieUtils;
import com.example.server.util.http.HttpUtil;
import com.example.server.util.interceptor.IpUtil;
import com.example.server.util.json.JsonResultStateCode;
import com.example.server.util.token.TokenGenerator;
import org.springframework.stereotype.Service;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import java.sql.Timestamp;

@Service("LoginServiceImpl")
public class LoginServiceImpl implements AuthService {

    private final LoginMapper loginMapper = SpringContextConfig.getBean(LoginMapper.class);
    private final UserMapper userMapper = SpringContextConfig.getBean(UserMapper.class);

    @Override
    public String handleLogin(String username, String password) {
        try {
            User user;
            System.out.println("username: " + username);
            System.out.println("password: " + password);
            user = loginMapper.findUserByUserName(username);
            if (user == null) {
                return JsonResultStateCode.USERNAME_WRONG_DESC;
            } else if(user.getLogout()){
                return JsonResultStateCode.USER_IS_LOG_OUT_DESC;
            } else {
                if (!user.getPassword().equals(password)) {
                    return JsonResultStateCode.PASSWORD_WRONG_DESC;
                } else {
                    //设置cookie:token和用户名
                    HttpServletResponse response = HttpUtil.getResponse();
                    String token = TokenGenerator.generateToken(username, password, user.getAccountType());
                    System.out.println("登录成功,生成token: " + token);
                    Cookie tokenCookie = CookieUtils.buildCookie("token", token);
                    Cookie usernameCookie = CookieUtils.buildCookie("username", username);
                    response.addCookie(tokenCookie);
                    response.addCookie(usernameCookie);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            return JsonResultStateCode.UNKNOWN_ERROR_DESC;
        }
        //更新最近登录时间和IP
        LatestLoginMsg msg = new LatestLoginMsg();
        msg.setUsername(username);
        msg.setLatestLoginTime(new Timestamp(System.currentTimeMillis()));
        msg.setLatestLoginIp(IpUtil.getIp(HttpUtil.getRequest()));
        try {
            userMapper.updateUserLatestLoginMsg(msg);
        } catch (Exception e) {
            e.printStackTrace();
            return JsonResultStateCode.SUCCESS_DESC;
        }
        return JsonResultStateCode.SUCCESS_DESC;
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

    @Override
    public Integer registerUser(UserMessage userMessage) {

        Integer rows = null;
        try {
            User res = loginMapper.findUserByUserName(userMessage.getUsername());
            if (res != null) {
                return JsonResultStateCode.USERNAME_IS_EXITED;
            }
            User user = new User()
                    .username(userMessage.getUsername())
                    .password(userMessage.getPassword())
                    .accountType(0)
                    .mailBoxSize(10)
                    .isForbidden(false)
                    .isLogout(false)
                    .build();
            rows = loginMapper.insertNewUser(user);
            return rows;
        } catch (Exception e) {
            e.printStackTrace();
            return rows;
        }
    }

    @Override
    public Integer updatePassword(String username, String newPassword) {

        Integer rows = null;
        try {
            rows = loginMapper.updatePassword(username, newPassword);
            return rows;
        } catch (Exception e) {
            e.printStackTrace();
            return rows;
        }
    }
}

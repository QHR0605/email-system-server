package com.example.server.service.impl;

import com.example.server.config.SpringContextConfig;
import com.example.server.entity.User;
import com.example.server.mapper.UserMapper;
import com.example.server.service.UserService;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author 全鸿润
 */
@Service
public class UserServiceImpl implements UserService {

    private UserMapper userMapper = SpringContextConfig.getBean(UserMapper.class);
    @Override
    public List<User> getContactList(String username) {
        List<User> contactList;
        try {
            contactList = userMapper.selectContact(username);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
        return contactList;
    }
}

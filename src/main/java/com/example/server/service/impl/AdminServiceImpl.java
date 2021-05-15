package com.example.server.service.impl;

import com.example.server.config.SpringContextConfig;
import com.example.server.entity.User;
import com.example.server.mapper.AdminMapper;
import com.example.server.service.AdminService;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author 全鸿润
 */
@Service("AdminServiceImpl")
public class AdminServiceImpl implements AdminService {

    protected final AdminMapper adminMapper = SpringContextConfig.getBean(AdminMapper.class);

    @Override
    public Integer deleteUsersByUsername(List<String> usernames) {
        Integer row;
        try {
            row = adminMapper.deleteUsers(usernames);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
        return row;
    }

    @Override
    public Integer updateUsersLogState(List<String> usernames, Boolean logState) {

        Integer row;
        try {
            row = adminMapper.updateUserLogState(usernames, logState);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
        return row;
    }

    @Override
    public Integer updateUsersType(List<String> usernames, Integer type) {
        Integer row;
        try {
            row = adminMapper.updateUserType(usernames, type);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
        return row;
    }

    @Override
    public List<User> getUsers() {
        List<User> users;
        try {
            users = adminMapper.selectUsers();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
        return users;
    }

    @Override
    public List<User> getAllUsers() {
        List<User> users;
        try {
            users = adminMapper.selectAllUsers();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
        return users;
    }
}

package com.example.server.service.impl;

import com.example.server.config.SpringContextConfig;
import com.example.server.dto.NewUserMessage;
import com.example.server.entity.User;
import com.example.server.mapper.SuperAdminMapper;
import com.example.server.service.SupperAdminService;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author 全鸿润
 */
@Service("SupperAdminImpl")
public class SupperAdminImpl extends AdminServiceImpl implements SupperAdminService {

    private final SuperAdminMapper superAdminMapper = SpringContextConfig.getBean(SuperAdminMapper.class);

    @Override
    public Integer auth(List<String> usernames, Boolean authType) {
        Integer row;
        try {
            row = superAdminMapper.updateUserAuthorization(usernames, authType);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
        return row;
    }

    @Override
    public Integer createUser(NewUserMessage message) {

        User user = new User()
                .username(message.getUsername())
                .password(message.getPassword())
                .accountType(message.getAccountType());
        Integer row;
        try {
            row = superAdminMapper.insertNewUser(user);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
        return row;
    }
}

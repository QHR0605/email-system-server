package com.example.server.mapper;

import com.example.server.entity.User;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author 全鸿润
 */
@Repository
public interface UserMapper {

    /**
     * 查询已发送过邮件的用户列表
     * @param username 用户名
     * @return 返回该用户之前发送过邮件的用户列表
     */
    List<User> selectContact(String username) throws Exception;
}

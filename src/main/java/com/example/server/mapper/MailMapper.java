package com.example.server.mapper;

import com.example.server.entity.Email;
import org.springframework.stereotype.Repository;

/**
 * @author 全鸿润
 */
@Repository
public interface MailMapper {

    /**
     * 添加邮件
     * @param email 发送的邮件
     * @return 影响的行数
     * @throws Exception 数据库操作异常
     */
    Integer addMail(Email email) throws Exception;
}

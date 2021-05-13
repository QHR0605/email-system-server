package com.example.server.mapper;

import com.example.server.entity.Email;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author 全鸿润
 */
@Repository
public interface MailMapper {

    /**
     * 添加邮件
     *
     * @param email 发送的邮件
     * @return 影响的行数
     * @throws Exception 数据库操作异常
     */
    Integer addMail(Email email) throws Exception;

    /**
     * 以username为接收方查询其收到的邮件列表
     *
     * @param username
     * @return 返回该用户收到邮件列表
     */
    List<Email> findMailsByRcpt(String username);

    /**
     * 根据用户标记要删除的邮件，删除mid为mid的邮件
     *
     * @param mid
     * @return 返回-1表示操作失败，其他是sql语句影响的行数
     */
    int delectMailByMid(Integer mid);
}

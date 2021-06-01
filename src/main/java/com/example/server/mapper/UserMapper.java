package com.example.server.mapper;

import com.example.server.dto.LatestLoginMsg;
import com.example.server.dto.UserPhoneMsg;
import com.example.server.entity.Contact;
import com.example.server.entity.ContactMsg;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author 全鸿润
 */
@Repository
public interface UserMapper {

    /**
     * @param contact 联系人信息
     * @return 添加行数
     * @throws Exception 数据库操作异常
     */
    Integer addContact(Contact contact) throws Exception;

    /**
     * @param contact 删除的联系人
     * @return 删除行数
     * @throws Exception 数据库操作异常
     */
    Integer deleteContact(Contact contact) throws Exception;

    /**
     * @param username 用户名
     * @return 该用户的联系人信息列表
     * @throws Exception 数据库操作异常
     */
    List<ContactMsg> getContact(String username) throws Exception;

    /**
     * 更新登录信息
     *
     * @param msg 最近登录信息
     * @return 修改的行数
     * @throws Exception 数据库操作异常
     */
    Integer updateUserLatestLoginMsg(LatestLoginMsg msg) throws Exception;

    /**
     * 修改用户手机
     *
     * @param msg 手机信息
     * @return 修改的行数
     * @throws Exception 数据库操作异常
     */
    Integer updateUserPhone(UserPhoneMsg msg) throws Exception;
}

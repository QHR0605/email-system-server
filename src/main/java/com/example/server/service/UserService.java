package com.example.server.service;

import com.example.server.entity.Contact;
import com.example.server.entity.ContactMsg;
import com.example.server.entity.User;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author 全鸿润
 */
@Service
public interface UserService {

    Integer addContact(Contact contact);
    Integer deleteContact(Contact contact);
    List<ContactMsg> getContactList(String username);
}

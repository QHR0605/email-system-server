package com.example.server.service;

import com.example.server.entity.User;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author 全鸿润
 */
@Service
public interface UserService {

    List<User> getContactList(String username);
}

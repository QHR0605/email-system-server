package com.example.server.dto;

import java.util.List;

/**
 * @author 全鸿润
 */
public class MailBoxSize {
    private List<String> username;
    private Integer size;

    public List<String> getUsername() {
        return username;
    }

    public void setUsername(List<String> username) {
        this.username = username;
    }

    public Integer getSize() {
        return size;
    }

    public void setSize(Integer size) {
        this.size = size;
    }
}

package com.example.server.dto;

import com.example.server.entity.Email;

import java.util.ArrayList;
import java.util.List;

public class Pop3Session {
    private boolean isUserSent = false;
    private boolean isAuth = false;
    private List<Email> emails = new ArrayList<>(); // 收到的邮件列表
    private String username = null;
    private Integer count = 0; // 总邮件数
    private Integer totalSize = 0; // 总字节数

    public Integer getTotalSize() {
        return totalSize;
    }

    public void setTotalSize(Integer totalSize) {
        this.totalSize = totalSize;
    }

    public Integer getCount() {
        return count;
    }

    public void setCount(Integer count) {
        this.count = count;
    }

    public boolean isUserSent() {
        return isUserSent;
    }

    public void setUserSent(boolean userSent) {
        isUserSent = userSent;
    }

    public boolean isAuth() {
        return isAuth;
    }

    public void setAuth(boolean auth) {
        isAuth = auth;
    }

    public List<Email> getEmails() {
        return emails;
    }

    public void setEmails(List<Email> emails) {
        this.emails = emails;
        setCount(emails.size());
        int total = 0;
        for(Email email : emails) {
            total += email.getSize();
        }
        setTotalSize(total);
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}

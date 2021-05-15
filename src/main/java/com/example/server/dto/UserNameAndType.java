package com.example.server.dto;

/**
 * @author 全鸿润
 */
public class UserNameAndType {

    private String username;
    private Boolean accountType;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public Boolean getAccountType() {
        return accountType;
    }

    public void setAccountType(Boolean accountType) {
        this.accountType = accountType;
    }
}

package com.example.server.entity;

import java.util.Date;

/**
 * @author 全鸿润
 */
public class Contact {

    private String contactName;
    private String username;
    private Date addTime;

    public String getContactName() {
        return contactName;
    }

    public void setContactName(String contactName) {
        this.contactName = contactName;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public Date getAddTime() {
        return addTime;
    }

    public void setAddTime(Date addTime) {
        this.addTime = addTime;
    }
}

package com.example.server.dto;

import java.sql.Timestamp;

/**
 * @author 全鸿润
 */
public class LatestLoginMsg {

    private String username;
    private Timestamp latestLoginTime;
    private String latestLoginIp;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public Timestamp getLatestLoginTime() {
        return latestLoginTime;
    }

    public void setLatestLoginTime(Timestamp latestLoginTime) {
        this.latestLoginTime = latestLoginTime;
    }

    public String getLatestLoginIp() {
        return latestLoginIp;
    }

    public void setLatestLoginIp(String latestLoginIp) {
        this.latestLoginIp = latestLoginIp;
    }
}

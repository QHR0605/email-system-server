package com.example.server.dto;

import java.util.List;

/**
 * @author 全鸿润
 */
public class ForbiddenUser {

    private List<String> username;
    private Boolean forbidden;

    public List<String> getUsername() {
        return username;
    }

    public void setUsername(List<String> username) {
        this.username = username;
    }

    public Boolean getForbidden() {
        return forbidden;
    }

    public void setForbidden(Boolean forbidden) {
        this.forbidden = forbidden;
    }
}

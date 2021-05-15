package com.example.server.dto;

/**
 * @author 全鸿润
 */
public class ServerState {

    private String serverName;
    private Boolean state;

    public String getServerName() {
        return serverName;
    }

    public void setServerName(String serverName) {
        this.serverName = serverName;
    }

    public Boolean getState() {
        return state;
    }

    public void setState(Boolean state) {
        this.state = state;
    }
}

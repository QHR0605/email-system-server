package com.example.server.dto;

/**
 * @author 全鸿润
 */
public class ServerStateMsg {

    private Integer sid;
    private Integer serverType;
    private Boolean serverState;

    public Integer getSid() {
        return sid;
    }

    public void setSid(Integer sid) {
        this.sid = sid;
    }

    public Integer getServerType() {
        return serverType;
    }

    public void setServerType(Integer serverType) {
        this.serverType = serverType;
    }

    public Boolean getServerState() {
        return serverState;
    }

    public void setServerState(Boolean serverState) {
        this.serverState = serverState;
    }

    @Override
    public String toString() {
        return "ServerStateMsg{" +
                "sid=" + sid +
                ", serverType=" + serverType +
                ", serverState=" + serverState +
                '}';
    }
}

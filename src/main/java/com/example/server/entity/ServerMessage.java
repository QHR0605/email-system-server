package com.example.server.entity;

/**
 * @author 全鸿润
 */
public class ServerMessage {

    private Integer sid;
    private String serverName;
    private Boolean smtpState;
    private Integer smtpPort;
    private Boolean pop3State;
    private Integer pop3Port;

    public ServerMessage() {
    }

    public ServerMessage serverName(String serverName){
        this.setServerName(serverName);
        return this;
    }

    public ServerMessage smtpState(Boolean smtpState){
        this.smtpState = smtpState;
        return this;
    }
    public ServerMessage smtpPort(Integer smtpPort){
        this.setSmtpPort(smtpPort);
        return this;
    }
    public ServerMessage pop3State(Boolean pop3State){
        this.setPop3State(pop3State);
        return this;
    }
    public ServerMessage pop3Port(Integer pop3Port){
        this.setPop3Port(pop3Port);
        return this;
    }

    public Integer getSid() {
        return sid;
    }

    public void setSid(Integer sid) {
        this.sid = sid;
    }
    public String getServerName() {
        return serverName;
    }

    public void setServerName(String serverName) {
        this.serverName = serverName;
    }

    public Boolean getSmtpState() {
        return smtpState;
    }

    public void setSmtpState(Boolean smtpState) {
        this.smtpState = smtpState;
    }

    public Integer getSmtpPort() {
        return smtpPort;
    }

    public void setSmtpPort(Integer smtpPort) {
        this.smtpPort = smtpPort;
    }

    public Boolean getPop3State() {
        return pop3State;
    }

    public void setPop3State(Boolean pop3State) {
        this.pop3State = pop3State;
    }

    public Integer getPop3Port() {
        return pop3Port;
    }

    public void setPop3Port(Integer pop3Port) {
        this.pop3Port = pop3Port;
    }
}

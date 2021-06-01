package com.example.server.entity;

import java.sql.Timestamp;

/**
 * @author 全鸿润
 */
public class Log {

    private Integer logId;
    private String username;
    private Timestamp time;
    private String operation;
    private Boolean state;
    private String reason;

    public Log() {

    }

    public Log logId(Integer logId) {
        this.setLogId(logId);
        return this;
    }

    public Log username(String username) {
        this.setUsername(username);
        return this;
    }

    public Log time(Timestamp time) {
        this.setTime(time);
        return this;
    }

    public Log operation(String operation) {
        this.setOperation(operation);
        return this;
    }

    public Log state(Boolean state) {
        this.setState(state);
        return this;
    }

    public Log reason(String reason) {
        this.setReason(reason);
        return this;
    }

    public Log build() {
        return this;
    }

    public Integer getLogId() {
        return logId;
    }

    public void setLogId(Integer logId) {
        this.logId = logId;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public Timestamp getTime() {
        return time;
    }

    public void setTime(Timestamp time) {
        this.time = time;
    }

    public String getOperation() {
        return operation;
    }

    public void setOperation(String operation) {
        this.operation = operation;
    }

    public Boolean getState() {
        return state;
    }

    public void setState(Boolean state) {
        this.state = state;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }
}

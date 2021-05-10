package com.example.server.entity;

/**
 * @author 全鸿润
 */
public class SmtpResult {

    private Integer state;
    private String message;

    public SmtpResult(Integer state, String message) {
        this.state = state;
        this.message = message;
    }

    public Integer getState() {
        return state;
    }

    public void setState(Integer state) {
        this.state = state;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}

package com.example.server.dto;

/**
 * @author 全鸿润
 */
public class SmtpSession {

    private boolean isHelloSent = false;
    private boolean isAuthSent = false;
    private boolean isMailSent = false;
    private boolean isRcptSent = false;

    public boolean isHelloSent() {
        return isHelloSent;
    }

    public void setHelloSent(boolean helloSent) {
        isHelloSent = helloSent;
    }

    public boolean isAuthSent() {
        return isAuthSent;
    }

    public void setAuthSent(boolean authSent) {
        isAuthSent = authSent;
    }

    public boolean isMailSent() {
        return isMailSent;
    }

    public void setMailSent(boolean mailSent) {
        isMailSent = mailSent;
    }

    public boolean isRcptSent() {
        return isRcptSent;
    }

    public void setRcptSent(boolean rcptSent) {
        isRcptSent = rcptSent;
    }
}

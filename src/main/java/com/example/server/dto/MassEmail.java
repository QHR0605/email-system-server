package com.example.server.dto;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.sql.Date;
import java.util.List;

/**
 * @author 全鸿润
 */
public class MassEmail {

    private String senderEmail;
    private List<String> receiverEmails;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date sendTime;
    private String subject;
    private String body;
    private String avatarUrl;

    public String getSenderEmail() {
        return senderEmail;
    }

    public void setSenderEmail(String senderEmail) {
        this.senderEmail = senderEmail;
    }

    public List<String> getReceiverEmails() {
        return receiverEmails;
    }

    public void setReceiverEmails(List<String> receiverEmails) {
        this.receiverEmails = receiverEmails;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public String getAvatarUrl() {
        return avatarUrl;
    }

    public void setAvatarUrl(String avatarUrl) {
        this.avatarUrl = avatarUrl;
    }

    public Date getSendTime() {
        return sendTime;
    }

    public void setSendTime(java.sql.Date sendTime) {
        this.sendTime = sendTime;
    }
}

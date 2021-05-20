package com.example.server.entity;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.sql.Date;

/**
 * @author 全鸿润
 */
public class Email {

    private Integer mid;
    private String senderEmail;
    private String receiverEmail;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    private Date sendTime;
    private String subject;
    private String body;
    private Boolean read;
    private Boolean deleted;
    private Boolean tag;
    private Boolean send;
    private String annexUrl;
    private String summary;
    private Integer size;

    public Email() {
    }

    public Email(Integer mid, String senderEmail, String receiverEmail, Date sendTime, String subject, String body, Boolean read, Boolean deleted, Boolean tag, Boolean send, String annexUrl, String summary, Integer size) {
        this.mid = mid;
        this.senderEmail = senderEmail;
        this.receiverEmail = receiverEmail;
        this.sendTime = sendTime;
        this.subject = subject;
        this.body = body;
        this.read = read;
        this.deleted = deleted;
        this.tag = tag;
        this.send = send;
        this.annexUrl = annexUrl;
        this.summary = summary;
        this.size = size;
    }

    public Boolean getDeleted() {
        return deleted;
    }

    public void setDeleted(Boolean deleted) {
        this.deleted = deleted;
    }

    public Boolean getTag() {
        return tag;
    }

    public void setTag(Boolean tag) {
        this.tag = tag;
    }

    public Boolean getSend() {
        return send;
    }

    public void setSend(Boolean send) {
        this.send = send;
    }

    public Integer getMid() {
        return mid;
    }

    public void setMid(Integer mid) {
        this.mid = mid;
    }

    public String getSenderEmail() {
        return senderEmail;
    }

    public void setSenderEmail(String senderEmail) {
        this.senderEmail = senderEmail;
    }

    public String getReceiverEmail() {
        return receiverEmail;
    }

    public void setReceiverEmail(String receiverEmail) {
        this.receiverEmail = receiverEmail;
    }

    public Date getSendTime() {
        return sendTime;
    }

    public void setSendTime(Date sendTime) {
        this.sendTime = sendTime;
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

    public Boolean getRead() {
        return read;
    }

    public void setRead(Boolean read) {
        this.read = read;
    }

    public String getAnnexUrl() {
        return annexUrl;
    }

    public void setAnnexUrl(String annexUrl) {
        this.annexUrl = annexUrl;
    }

    public String getSummary() {
        return summary;
    }

    public void setSummary(String summary) {
        this.summary = summary;
    }

    public Integer getSize() {
        return size;
    }

    public void setSize(Integer size) {
        this.size = size;
    }

    @Override
    public String toString() {
        return "Email{" +
                "mid=" + mid +
                ", senderEmail='" + senderEmail + '\'' +
                ", receiverEmail='" + receiverEmail + '\'' +
                ", sendTime=" + sendTime +
                ", subject='" + subject + '\'' +
                ", body='" + body + '\'' +
                ", read=" + read +
                ", deleted=" + deleted +
                ", tag=" + tag +
                ", send=" + send +
                ", annexUrl='" + annexUrl + '\'' +
                ", summary='" + summary + '\'' +
                ", size=" + size +
                '}';
    }
}

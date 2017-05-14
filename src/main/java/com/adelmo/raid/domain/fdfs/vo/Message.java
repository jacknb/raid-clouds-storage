package com.adelmo.raid.domain.fdfs.vo;

public class Message {
    private String statusCode;
    private String message;
    private String navTabId;
    private String rel;
    private String callbackType;
    private String forwardUrl;
    private String ips;

    public Message() {
    }

    public Message(String ips) {
        this.ips = ips;
    }

    public Message(String statusCode, String message, String navTabId, String rel, String callbackType, String forwardUrl) {
        this.statusCode = statusCode;
        this.message = message;
        this.navTabId = navTabId;
        this.rel = rel;
        this.callbackType = callbackType;
        this.forwardUrl = forwardUrl;
    }

    public String getStatusCode() {
        return this.statusCode;
    }

    public void setStatusCode(String statusCode) {
        this.statusCode = statusCode;
    }

    public String getMessage() {
        return this.message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getNavTabId() {
        return this.navTabId;
    }

    public void setNavTabId(String navTabId) {
        this.navTabId = navTabId;
    }

    public String getRel() {
        return this.rel;
    }

    public void setRel(String rel) {
        this.rel = rel;
    }

    public String getCallbackType() {
        return this.callbackType;
    }

    public void setCallbackType(String callbackType) {
        this.callbackType = callbackType;
    }

    public String getForwardUrl() {
        return this.forwardUrl;
    }

    public void setForwardUrl(String forwardUrl) {
        this.forwardUrl = forwardUrl;
    }

    public String getIps() {
        return this.ips;
    }

    public void setIps(String ips) {
        this.ips = ips;
    }
}

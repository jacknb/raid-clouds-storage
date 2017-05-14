package com.adelmo.raid.domain.fdfs.vo;

public class Machine {
    private String ip;
    private int port;
    private String username;
    private String password;
    private String logpath;
    private String ssh;
    private boolean configType;

    public Machine() {
    }

    public String getIp() {
        return this.ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public String getUsername() {
        return this.username;
    }

    public String getSsh() {
        return this.ssh;
    }

    public void setSsh(String ssh) {
        this.ssh = ssh;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return this.password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getLogpath() {
        return this.logpath;
    }

    public boolean isConfigType() {
        return this.configType;
    }

    public void setConfigType(boolean configType) {
        this.configType = configType;
    }

    public void setLogpath(String logpath) {
        this.logpath = logpath;
    }

    public int getPort() {
        return this.port;
    }

    public void setPort(int port) {
        this.port = port;
    }
}

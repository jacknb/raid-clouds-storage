package com.adelmo.raid.domain.fdfs.util;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class CommandMessage implements Serializable {
    private static final long serialVersionUID = 1L;
    private String cmdString;
    private Map<String, String> parameters;
    private MessageType type;
    private String message;
    private long executeTime;
    private long executeDate;
    private String requestIP;
    private List<String> executeLines = new ArrayList();

    public CommandMessage() {
    }

    public String getCmdString() {
        return this.cmdString;
    }

    public void setCmdString(String cmdString) {
        this.cmdString = cmdString;
    }

    public MessageType getType() {
        return this.type;
    }

    public void setType(MessageType type) {
        this.type = type;
    }

    public String getMessage() {
        return this.message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public long getExecuteTime() {
        return this.executeTime;
    }

    public Map<String, String> getParameters() {
        return this.parameters;
    }

    public void setParameters(Map<String, String> parameters) {
        this.parameters = parameters;
    }

    public void setExecuteTime(long executeTime) {
        this.executeTime = executeTime;
    }

    public List<String> getExecuteLines() {
        return this.executeLines;
    }

    public void setExecuteLines(List<String> executeLines) {
        this.executeLines = executeLines;
    }

    public long getExecuteDate() {
        return this.executeDate;
    }

    public void setExecuteDate(long executeDate) {
        this.executeDate = executeDate;
    }

    public String getRequestIP() {
        return this.requestIP;
    }

    public void setRequestIP(String requestIP) {
        this.requestIP = requestIP;
    }

    public static enum MessageType {
        OK,
        ERROR,
        CLIENT_ERROR;

        private MessageType() {
        }
    }
}

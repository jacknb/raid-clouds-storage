package com.adelmo.raid.domain.fdfs.vo;

import java.util.Date;
import javax.persistence.Entity;

@Entity
public class Forecast {
    private String ipAddr;
    private long warning;
    private Date timeForForecast;
    private long average;
    private Date now;
    private long useHour;
    private long freeMB;

    public Forecast() {
    }

    public String getIpAddr() {
        return this.ipAddr;
    }

    public void setIpAddr(String ipAddr) {
        this.ipAddr = ipAddr;
    }

    public long getWarning() {
        return this.warning;
    }

    public void setWarning(long warning) {
        this.warning = warning;
    }

    public Date getTimeForForecast() {
        return this.timeForForecast;
    }

    public void setTimeForForecast(Date timeForForecast) {
        this.timeForForecast = timeForForecast;
    }

    public long getAverage() {
        return this.average;
    }

    public void setAverage(long average) {
        this.average = average;
    }

    public Date getNow() {
        return this.now;
    }

    public void setNow(Date now) {
        this.now = now;
    }

    public long getUseHour() {
        return this.useHour;
    }

    public void setUseHour(long useHour) {
        this.useHour = useHour;
    }

    public long getFreeMB() {
        return this.freeMB;
    }

    public void setFreeMB(long freeMB) {
        this.freeMB = freeMB;
    }
}

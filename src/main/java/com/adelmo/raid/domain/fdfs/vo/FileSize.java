package com.adelmo.raid.domain.fdfs.vo;

import javax.persistence.Entity;

@Entity
public class FileSize {
    private String groupName;
    private int miniSmall;
    private int small;
    private int middle;
    private int large;

    public FileSize() {
    }

    public String getGroupName() {
        return this.groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

    public int getMiniSmall() {
        return this.miniSmall;
    }

    public void setMiniSmall(int miniSmall) {
        this.miniSmall = miniSmall;
    }

    public int getSmall() {
        return this.small;
    }

    public void setSmall(int small) {
        this.small = small;
    }

    public int getMiddle() {
        return this.middle;
    }

    public void setMiddle(int middle) {
        this.middle = middle;
    }

    public int getLarge() {
        return this.large;
    }

    public void setLarge(int large) {
        this.large = large;
    }
}

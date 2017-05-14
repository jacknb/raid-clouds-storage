package com.adelmo.raid.domain.fdfs.vo;

import java.util.ArrayList;
import java.util.List;

public class Line {
    private String name;
    private List<Object[]> data = new ArrayList();

    public Line(String name) {
        this.name = name;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Object[]> getData() {
        return this.data;
    }

    public void setData(List<Object[]> data) {
        this.data = data;
    }
}

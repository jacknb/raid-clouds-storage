package com.adelmo.raid.domain.fdfs.vo;

public class PageInfo {
    private int numPerPage;
    private String orderField;
    private String orderDirection;
    private int totalCount;
    private int pageNumShown;
    private int pageNum;

    public PageInfo() {
    }

    public PageInfo(int numPerPage, String orderField, String orderDirection, int totalCount, int pageNumShown, int pageNum) {
        this.numPerPage = numPerPage;
        this.orderField = orderField;
        this.orderDirection = orderDirection;
        this.totalCount = totalCount;
        this.pageNumShown = pageNumShown;
        this.pageNum = pageNum;
    }

    public int getNumPerPage() {
        if(this.numPerPage == 0) {
            this.numPerPage = 20;
        }

        return this.numPerPage;
    }

    public void setNumPerPage(int numPerPage) {
        this.numPerPage = numPerPage;
    }

    public String getOrderField() {
        return this.orderField;
    }

    public void setOrderField(String orderField) {
        this.orderField = orderField;
    }

    public String getOrderDirection() {
        return this.orderDirection;
    }

    public void setOrderDirection(String orderDirection) {
        this.orderDirection = orderDirection;
    }

    public int getTotalCount() {
        return this.totalCount;
    }

    public void setTotalCount(int totalCount) {
        this.totalCount = totalCount;
    }

    public int getPageNumShown() {
        return this.pageNumShown;
    }

    public void setPageNumShown(int pageNumShown) {
        this.pageNumShown = pageNumShown;
    }

    public int getPageNum() {
        if(this.pageNum == 0) {
            this.pageNum = 1;
        }

        return this.pageNum;
    }

    public void setpageNum(int pageNum) {
        this.pageNum = pageNum;
    }
}

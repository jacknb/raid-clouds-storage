package com.adelmo.raid.domain.fdfs.vo;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import org.hibernate.annotations.GenericGenerator;

@Entity
@Table(
    name = "tbdownloadfilerecord"
)
public class DownloadFileRecord {
    private String id;
    private String fileId;
    private long accessCount;
    private String src_ip;

    public DownloadFileRecord() {
    }

    @Id
    @GeneratedValue(
        generator = "system_uuid"
    )
    @GenericGenerator(
        name = "system_uuid",
        strategy = "uuid"
    )
    public String getId() {
        return this.id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getFileId() {
        return this.fileId;
    }

    public void setFileId(String fileId) {
        this.fileId = fileId;
    }

    public long getAccessCount() {
        return this.accessCount;
    }

    public void setAccessCount(long accessCount) {
        this.accessCount = accessCount;
    }

    public String getSrc_ip() {
        return this.src_ip;
    }

    public void setSrc_ip(String src_ip) {
        this.src_ip = src_ip;
    }
}

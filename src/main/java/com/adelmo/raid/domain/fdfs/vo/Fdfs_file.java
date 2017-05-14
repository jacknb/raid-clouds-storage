//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package com.adelmo.raid.domain.fdfs.vo;

import java.util.Date;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import org.hibernate.annotations.GenericGenerator;

@Entity
@Table(
    name = "tbfile"
)
public class Fdfs_file {
    private String id;
    private String file_id;
    private String file_name;
    private String MD5;
    private String type;
    private Date created;
    private String article_id;
    private long fileSize;

    public Fdfs_file() {
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

    public String getFile_id() {
        return this.file_id;
    }

    public void setFile_id(String file_id) {
        this.file_id = file_id;
    }

    public String getMD5() {
        return this.MD5;
    }

    public void setMD5(String mD5) {
        this.MD5 = mD5;
    }

    public String getType() {
        return this.type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Date getCreated() {
        return this.created;
    }

    public void setCreated(Date created) {
        this.created = created;
    }

    public String getFile_name() {
        return this.file_name;
    }

    public void setFile_name(String file_name) {
        this.file_name = file_name;
    }

    public String getArticle_id() {
        return this.article_id;
    }

    public void setArticle_id(String article_id) {
        this.article_id = article_id;
    }

    public long getFileSize() {
        return this.fileSize;
    }

    public void setFileSize(long fileSize) {
        this.fileSize = fileSize;
    }
}

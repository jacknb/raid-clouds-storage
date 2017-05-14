package com.adelmo.raid.domain.fdfs.vo;

import java.io.Serializable;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import org.hibernate.annotations.GenericGenerator;

@Entity
@Table(
    name = "tbuser"
)
public class User implements Serializable {
    private String id;
    private String name;
    private String psword;
    private String power;

    public User() {
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

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPsword() {
        return this.psword;
    }

    public void setPsword(String psword) {
        this.psword = psword;
    }

    public String getPower() {
        return this.power;
    }

    public void setPower(String power) {
        this.power = power;
    }
}

package com.timebusker.phoenix.entity;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * @Description:UserEntity
 * @Author:Administrator
 * @Date2019/11/13 22:35
 **/
@Entity
@Table(name = "tb_phoenix_user")
public class PhoenixUserEntity {

    @Id
    private String idx;

    private String name;

    private String sex;

    private String age;

    private String address;

    public String getIdx() {
        return idx;
    }

    public void setIdx(String idx) {
        this.idx = idx;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public String getAge() {
        return age;
    }

    public void setAge(String age) {
        this.age = age;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }
}

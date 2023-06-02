package com.ashok.lostfound.model;

import java.io.Serializable;

public class LostFound implements Serializable {
    String postType;
    String name;
    String phone;
    String desc;
    String date;
    String location;

    public LostFound(String postType, String name, String phone, String desc, String date, String location) {
        this.postType = postType;
        this.name = name;
        this.phone = phone;
        this.desc = desc;
        this.date = date;
        this.location = location;
    }

    public String getPostType() {
        return postType;
    }

    public void setPostType(String postType) {
        this.postType = postType;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }
}

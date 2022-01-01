package com.journey.entity;

import java.util.Date;

public class User {

    private String username;

    private String password;

    private Date createDate;

    public User(String username, String password, Date createDate) {
        this.username = username;
        this.password = password;
        this.createDate = createDate;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }
}

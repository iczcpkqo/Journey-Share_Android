package com.journey.entity;

import java.io.Serializable;
import java.util.Date;

/**
 * @author: Xiang Mao
 * @date: 2022-03-26-04:00
 * @tag: Dialogue, Chat
 */
public class ChatDeliver implements Serializable {

    private String username;
    private String gender;
    private String phone;
    private String email;
    // TODO: 增加聊天需要的东西

    public void setUsername(String username) {
        this.username = username;
    }

    public String getUsername() {
        return username;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getGender() {
        return gender;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getPhone() {
        return phone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}

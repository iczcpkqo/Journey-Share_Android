package com.journey.entity;

import java.util.Date;

public class User {

    private String username;

    private String password;

    private Date createDate;

    private String birthDate;

    private String gender;

    private String phone;

    private String email;

    private Double mark;

    public User(String username, String password, Date createDate, String birthDate, String gender, String phone, String email, Double mark) {
        this.username = username;
        this.password = password;
        this.createDate = createDate;
        this.birthDate = birthDate;
        this.gender = gender;
        this.phone = phone;
        this.email = email;
        this.mark = mark;
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

    public String getBirthDate() { return birthDate; }

    public void setBirthDate(String birthDate) { this.birthDate = birthDate; }

    public String getGender() { return gender; }

    public void setGender(String gender) { this.gender = gender; }

    public String getPhone() { return phone; }

    public void setPhone(String phone) { this.phone = phone; }

    public String getEmail() { return email; }

    public void setEmail(String email) { this.email = email; }

    public Double getMark() {  return mark; }

    public void setMark(Double mark) { this.mark = mark; }

}

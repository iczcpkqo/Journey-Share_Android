package com.journey.entity;

import android.util.Log;

import com.google.firebase.firestore.FieldValue;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class User implements Comparable<User>{

    private String username;

    private String password;

    private Date createDate;

    private String birthDate;

    private String gender;

    private String phone;

    private String email;

    private Double mark;

    private Integer order;

    private String uuid;

    private String age;

    public User(String username, String password, Date createDate, String birthDate, String gender, String phone, String email, Double mark, Integer order) {
        this.username = username;
        this.password = password;
        this.createDate = createDate;
        this.birthDate = birthDate;
        this.gender = gender;
        this.phone = phone;
        this.email = email;
        this.mark = mark;
        this.order = order;
    }

    public User(String email, String username, String gender){
        this.email = email;
        this.username = username;
        this.gender = gender;
    }
    public User(){}

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

    public void setBirthDate(String birthDate) throws ParseException {
        this.birthDate = birthDate;
        SimpleDateFormat BirthFormat = new SimpleDateFormat("yyyy-MM-dd");
        String str1 = this.birthDate;
        String str2 = BirthFormat.format(System.currentTimeMillis());
        Calendar bef = Calendar.getInstance();
        Calendar aft = Calendar.getInstance();

        bef.setTime(BirthFormat.parse(str1));
        aft.setTime(BirthFormat.parse(str2));
        int surplus = aft.get(Calendar.DATE) - bef.get(Calendar.DATE);
        int result = aft.get(Calendar.MONTH) - bef.get(Calendar.MONTH);
        int year = aft.get(Calendar.YEAR) - bef.get(Calendar.YEAR);
        this.age = Integer.toString(year);
    }

    public void setAge(String age) { this.age = age; }

    public String getAge() { return age; }

    public String getGender() { return gender; }

    public void setGender(String gender) { this.gender = gender; }

    public String getPhone() { return phone; }

    public void setPhone(String phone) { this.phone = phone; }

    public String getEmail() { return email; }

    public void setEmail(String email) { this.email = email; }

    public Double getMark() {  return mark; }

    public void setMark(Double mark) { this.mark = mark; }

    public Integer getOrder() { return order; }

    public void setOrder(Integer order) { this.order = order; }

    public void setUuid(String uuid) { this.uuid = uuid; }

    public String getUuid() { return uuid; }

    @Override
    public int compareTo(User user) { return this.email.compareTo(user.getEmail()); }
}

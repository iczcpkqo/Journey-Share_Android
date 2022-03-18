package com.journey.model;

import java.io.Serializable;

/**
 * @Description:
 * @author: Congqin Yan
 * @Email: yancongqin@gmail.com
 * @date: 2022-03-01-20:00
 * @Modify date and time:
 * @Modified by:
 * @Modified remark:
 */
public class Peer implements Serializable {
    String email;

    String gender;

    Integer age;

    Double score;

    Double longitude;

    Double latitude;

    Double dLongtitude;

    Double dLatitude;

    Long startTime;

    Long endTime;

    Integer order;

    Integer limit;

    Boolean isLeader;

    public Peer(String email, String gender, Integer age, Double score, Double longitude, Double latitude, Double dLongtitude, Double dLatitude, Long startTime, Long endTime, Integer order, Integer limit, Boolean isLeader) {
        this.email = email;
        this.gender = gender;
        this.age = age;
        this.score = score;
        this.longitude = longitude;
        this.latitude = latitude;
        this.dLongtitude = dLongtitude;
        this.dLatitude = dLatitude;
        this.startTime = startTime;
        this.endTime = endTime;
        this.order = order;
        this.limit = limit;
        this.isLeader = isLeader;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
    }

    public Double getScore() {
        return score;
    }

    public void setScore(Double score) {
        this.score = score;
    }

    public Double getLongitude() {
        return longitude;
    }

    public void setLongitude(Double longitude) {
        this.longitude = longitude;
    }

    public Double getLatitude() {
        return latitude;
    }

    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }

    public Double getdLongtitude() {
        return dLongtitude;
    }

    public void setdLongtitude(Double dLongtitude) {
        this.dLongtitude = dLongtitude;
    }

    public Double getdLatitude() {
        return dLatitude;
    }

    public void setdLatitude(Double dLatitude) {
        this.dLatitude = dLatitude;
    }

    public Long getStartTime() {
        return startTime;
    }

    public void setStartTime(Long startTime) {
        this.startTime = startTime;
    }

    public Long getEndTime() {
        return endTime;
    }

    public void setEndTime(Long endTime) {
        this.endTime = endTime;
    }

    public Integer getOrder() {
        return order;
    }

    public void setOrder(Integer order) {
        this.order = order;
    }

    public Integer getLimit() {
        return limit;
    }

    public void setLimit(Integer limit) {
        this.limit = limit;
    }

    public Boolean getLeader() {
        return isLeader;
    }

    public void setLeader(Boolean leader) {
        isLeader = leader;
    }
    //user info
//    String username;
//    String birthday;
//    String gender;
//    String phone;
//    String email;
//    Double mark;
//    Integer age;
//    Integer order;


    //condition info
//    String dateTime;
//    String originAddress;
//    String endAddress;
//    String preferGender;
//    Integer minAge;
//    Integer maxAge;
//    Double score;
//    Double originLon;
//    Double originLat;
//    Double endLon;
//    Double endLat;
//    Long startTime;
//    Long endTime;
//    Integer limit;
//    Boolean isLeader;

}

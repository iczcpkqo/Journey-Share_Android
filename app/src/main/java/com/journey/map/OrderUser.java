package com.journey.map;

import java.io.Serializable;

public class OrderUser implements Serializable {

    private   String emailTag;
    private   String genderTag;
    private   int ageTag;
    private   double scoreTag;
    private   double longitudeTag;
    private   double latitudeTag;
    private   double dLongitudeTag;
    private   double dLatitudeTag;
    private   long  startTimeTag;
    private   long  endTimeTag;
    private   int  limitTag;
    private   boolean  isLeaderTag;

    public OrderUser(String emailTag, String genderTag, int ageTag, double scoreTag, double longitudeTag, double latitudeTag, double dLongitudeTag, double dLatitudeTag, long startTimeTag, long endTimeTag, int limitTag, boolean isLeaderTag) {
        this.emailTag = emailTag;
        this.genderTag = genderTag;
        this.ageTag = ageTag;
        this.scoreTag = scoreTag;
        this.longitudeTag = longitudeTag;
        this.latitudeTag = latitudeTag;
        this.dLongitudeTag = dLongitudeTag;
        this.dLatitudeTag = dLatitudeTag;
        this.startTimeTag = startTimeTag;
        this.endTimeTag = endTimeTag;
        this.limitTag = limitTag;
        this.isLeaderTag = isLeaderTag;
    }

    public String getEmailTag() {
        return emailTag;
    }

    public void setEmailTag(String emailTag) {
        this.emailTag = emailTag;
    }

    public String getGenderTag() {
        return genderTag;
    }

    public void setGenderTag(String genderTag) {
        this.genderTag = genderTag;
    }

    public int getAgeTag() {
        return ageTag;
    }

    public void setAgeTag(int ageTag) {
        this.ageTag = ageTag;
    }

    public double getScoreTag() {
        return scoreTag;
    }

    public void setScoreTag(double scoreTag) {
        this.scoreTag = scoreTag;
    }

    public double getLongitudeTag() {
        return longitudeTag;
    }

    public void setLongitudeTag(double longitudeTag) {
        this.longitudeTag = longitudeTag;
    }

    public double getLatitudeTag() {
        return latitudeTag;
    }

    public void setLatitudeTag(double latitudeTag) {
        this.latitudeTag = latitudeTag;
    }

    public double getdLongitudeTag() {
        return dLongitudeTag;
    }

    public void setdLongitudeTag(double dLongitudeTag) {
        this.dLongitudeTag = dLongitudeTag;
    }

    public double getdLatitudeTag() {
        return dLatitudeTag;
    }

    public void setdLatitudeTag(double dLatitudeTag) {
        this.dLatitudeTag = dLatitudeTag;
    }

    public long getStartTimeTag() {
        return startTimeTag;
    }

    public void setStartTimeTag(long startTimeTag) {
        this.startTimeTag = startTimeTag;
    }

    public long getEndTimeTag() {
        return endTimeTag;
    }

    public void setEndTimeTag(long endTimeTag) {
        this.endTimeTag = endTimeTag;
    }

    public int getLimitTag() {
        return limitTag;
    }

    public void setLimitTag(int limitTag) {
        this.limitTag = limitTag;
    }

    public boolean isLeaderTag() {
        return isLeaderTag;
    }

    public void setLeaderTag(boolean leaderTag) {
        isLeaderTag = leaderTag;
    }
}

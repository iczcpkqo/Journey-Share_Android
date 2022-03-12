package com.journey.map;

public class OrderUser {

    private   String emailTag;
    private   int genderTag;
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

    public OrderUser(String emailTag, int genderTag, int ageTag, double scoreTag, double longitudeTag, double latitudeTag, double dLongitudeTag, double dLatitudeTag, long startTimeTag, long endTimeTag, int limitTag, boolean isLeaderTag) {
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

    public int getGenderTag() {
        return genderTag;
    }

    public int getAgeTag() {
        return ageTag;
    }

    public double getScoreTag() {
        return scoreTag;
    }

    public double getLongitudeTag() {
        return longitudeTag;
    }

    public double getLatitudeTag() {
        return latitudeTag;
    }

    public double getdLongitudeTag() {
        return dLongitudeTag;
    }

    public double getdLatitudeTag() {
        return dLatitudeTag;
    }

    public long getStartTimeTag() {
        return startTimeTag;
    }

    public long getEndTimeTag() {
        return endTimeTag;
    }

    public int getLimitTag() {
        return limitTag;
    }

    public boolean isLeaderTag() {
        return isLeaderTag;
    }

}

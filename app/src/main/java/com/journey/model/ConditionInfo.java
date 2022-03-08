package com.journey.model;

public class ConditionInfo {
    String originAddress;
    String endAddress;
    String preferGender;
    int minAge;
    int maxAge;
    double score;
    double originLat;
    double originLon;
    double endLat;
    double endLon;

    public ConditionInfo(String originAddress, String endAddress, String preferGender, int minAge, int maxAge, double score, double originLat, double originLon, double endLat, double endLon) {
        this.originAddress = originAddress;
        this.endAddress = endAddress;
        this.preferGender = preferGender;
        this.minAge = minAge;
        this.maxAge = maxAge;
        this.score = score;
        this.originLat = originLat;
        this.originLon = originLon;
        this.endLat = endLat;
        this.endLon = endLon;
    }

    public String getOriginAddress() {
        return originAddress;
    }

    public void setOriginAddress(String originAddress) {
        this.originAddress = originAddress;
    }

    public String getEndAddress() {
        return endAddress;
    }

    public void setEndAddress(String endAddress) {
        this.endAddress = endAddress;
    }

    public String getPreferGender() {
        return preferGender;
    }

    public void setPreferGender(String preferGender) {
        this.preferGender = preferGender;
    }

    public int getMinAge() {
        return minAge;
    }

    public void setMinAge(int minAge) {
        this.minAge = minAge;
    }

    public int getMaxAge() {
        return maxAge;
    }

    public void setMaxAge(int maxAge) {
        this.maxAge = maxAge;
    }

    public double getScore() {
        return score;
    }

    public void setScore(double score) {
        this.score = score;
    }

    public double getOriginLat() {
        return originLat;
    }

    public void setOriginLat(double originLat) {
        this.originLat = originLat;
    }

    public double getOriginLon() {
        return originLon;
    }

    public void setOriginLon(double originLon) {
        this.originLon = originLon;
    }

    public double getEndLat() {
        return endLat;
    }

    public void setEndLat(double endLat) {
        this.endLat = endLat;
    }

    public double getEndLon() {
        return endLon;
    }

    public void setEndLon(double endLon) {
        this.endLon = endLon;
    }
}

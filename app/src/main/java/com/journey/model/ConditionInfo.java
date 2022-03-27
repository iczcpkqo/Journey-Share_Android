package com.journey.model;

import java.io.Serializable;

public class ConditionInfo implements Serializable {
    private String dateTime;
    private String originAddress;
    private String endAddress;
    private String preferGender;
    private String minAge;
    private String maxAge;
    private String minScore;
    private String origin_lon;
    private String origin_lat;
    private String end_lon;
    private String end_lat;
    private String startAddress;
    private String destination;

    public ConditionInfo(String dateTime, String originAddress, String endAddress, String preferGender, String minAge, String maxAge, String minScore, String origin_lon, String origin_lat, String end_lon, String end_lat, String startAddress, String destination) {
        this.dateTime = dateTime;
        this.originAddress = originAddress;
        this.endAddress = endAddress;
        this.preferGender = preferGender;
        this.minAge = minAge;
        this.maxAge = maxAge;
        this.minScore = minScore;
        this.origin_lon = origin_lon;
        this.origin_lat = origin_lat;
        this.end_lon = end_lon;
        this.end_lat = end_lat;
        this.startAddress = startAddress;
        this.destination = destination;
    }

    public String getDateTime() {
        return dateTime;
    }

    public void setDateTime(String dateTime) {
        this.dateTime = dateTime;
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

    public String getMinAge() {
        return minAge;
    }

    public void setMinAge(String minAge) {
        this.minAge = minAge;
    }

    public String getMaxAge() {
        return maxAge;
    }

    public void setMaxAge(String maxAge) {
        this.maxAge = maxAge;
    }

    public String getMinScore() {
        return minScore;
    }

    public void setMinScore(String minScore) {
        this.minScore = minScore;
    }

    public String getOrigin_lon() {
        return origin_lon;
    }

    public void setOrigin_lon(String origin_lon) {
        this.origin_lon = origin_lon;
    }

    public String getOrigin_lat() {
        return origin_lat;
    }

    public void setOrigin_lat(String origin_lat) {
        this.origin_lat = origin_lat;
    }

    public String getEnd_lon() {
        return end_lon;
    }

    public void setEnd_lon(String end_lon) {
        this.end_lon = end_lon;
    }

    public String getEnd_lat() {
        return end_lat;
    }

    public void setEnd_lat(String end_lat) {
        this.end_lat = end_lat;
    }

    public String getStartAddress() {
        return startAddress;
    }

    public void setStartAddress(String startAddress) {
        this.startAddress = startAddress;
    }

    public String getDestination() {
        return destination;
    }

    public void setDestination(String destination) {
        this.destination = destination;
    }
}

package com.journey.model;

public class DailyInfo {
    private String email;
    private String dateTime;
    private String originAddress;
    private String endAddress;
    private String origin_lon;
    private String origin_lat;
    private String end_lon;
    private String end_lat;
    private String maxAge;
    private String minAge;
    private String minScore;
    private String preferGender;
    private String journeyMode;
    private String route;

    public DailyInfo(String email, String dateTime, String originAddress, String endAddress, String origin_lon, String origin_lat, String end_lon, String end_lat, String maxAge, String minAge, String minScore, String preferGender, String journeyMode, String route) {
        this.email = email;
        this.dateTime = dateTime;
        this.originAddress = originAddress;
        this.endAddress = endAddress;
        this.origin_lon = origin_lon;
        this.origin_lat = origin_lat;
        this.end_lon = end_lon;
        this.end_lat = end_lat;
        this.maxAge = maxAge;
        this.minAge = minAge;
        this.minScore = minScore;
        this.preferGender = preferGender;
        this.journeyMode = journeyMode;
        this.route = route;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
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

    public String getMaxAge() {
        return maxAge;
    }

    public void setMaxAge(String maxAge) {
        this.maxAge = maxAge;
    }

    public String getMinAge() {
        return minAge;
    }

    public void setMinAge(String minAge) {
        this.minAge = minAge;
    }

    public String getMinScore() {
        return minScore;
    }

    public void setMinScore(String minScore) {
        this.minScore = minScore;
    }

    public String getPreferGender() {
        return preferGender;
    }

    public void setPreferGender(String preferGender) {
        this.preferGender = preferGender;
    }

    public String getJourneyMode() {
        return journeyMode;
    }

    public void setJourneyMode(String journeyMode) {
        this.journeyMode = journeyMode;
    }

    public String getRoute() {
        return route;
    }

    public void setRoute(String route) {
        this.route = route;
    }
}

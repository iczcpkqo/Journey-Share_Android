package com.journey.entity;

import java.util.HashMap;
import java.util.Map;

public class Rating {

    public Rating(String record, String from, String to, double rating, boolean rated) {
        this.record = record;
        this.from = from;
        this.to = to;
        this.rating = rating;
        this.rated = rated;
    }

    public String getRecord() {
        return record;
    }

    public void setRecord(String record) {
        this.record = record;
    }

    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    public String getTo() {
        return to;
    }

    public void setTo(String to) {
        this.to = to;
    }

    public double getRating() {
        return rating;
    }

    public void setRating(Double rating) {
        this.rating = rating;
    }

    public boolean isRated() {
        return rated;
    }

    public void setRated(boolean rated) {
        this.rated = rated;
    }

    private String record;
    private String from;
    private String to;
    private Double rating;
    private boolean rated;

    public Map<String,Object> toHashmap(){
        Map<String,Object> map = new HashMap<>();
        map.put("record",getRecord());
        map.put("from",getFrom());
        map.put("to",getTo());
        map.put("rating",getRating());
        return map;
    }

    @Override
    public String toString() {
        return "Rating{" +
                "record='" + record + '\'' +
                ", from='" + from + '\'' +
                ", to='" + to + '\'' +
                ", rating=" + rating +
                ", rated=" + rated +
                '}';
    }
}

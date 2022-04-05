package com.journey.entity;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Record implements Serializable {

    private String departure;
    private String documentId;
    private String arrival;
    private Date createDate;
    private Date ArrvialDate;
    private String companion;

    public String getCost() {
        return cost;
    }

    public void setCost(String cost) {
        this.cost = cost;
    }

    public String getDistance() {
        return distance;
    }

    public void setDistance(String distance) {
        this.distance = distance;
    }

    private String cost;
    private String distance;

    public Record(String documentId, String departure, String arrival, Date createDate, Date ArrvialDate, String companion,String mcost,String mdistance) {
        this.documentId = documentId;
        this.departure = departure;
        this.arrival = arrival;
        this.createDate = createDate;
        this.ArrvialDate = ArrvialDate;
        this.companion = companion;
        this.cost = mcost;
        this.distance = mdistance;
    }

    public String getDeparture() {
        return departure;
    }

    public String getDocId() {
        return this.documentId;
    }

    public String getArrival() {
        return arrival;
    }

    public Date getCreateDate() {
        return this.createDate;
    }

    public Date getArrvialDate() {
        return this.ArrvialDate;
    }

    public String getcompanion() {
        return this.companion;
    }

    public String getCreateDateString() {

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return sdf.format(createDate);
    }

    public String getArrivalDateString() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return sdf.format(ArrvialDate);
    }

    public void setDeparture(String departure) {
        this.departure = departure;
    }

    public String getDocumentId() {
        return documentId;
    }

    public void setDocumentId(String documentId) {
        this.documentId = documentId;
    }

    public void setArrival(String arrival) {
        this.arrival = arrival;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    public void setArrvialDate(Date ArrvialDate) {
        this.ArrvialDate = ArrvialDate;
    }

    public void setcompanion(String companion) {
        this.companion = companion;
    }

    @Override
    public String toString() {
        return "Record{" +
                "departure='" + departure + '\'' +
                ", documentId='" + documentId + '\'' +
                ", arrival='" + arrival + '\'' +
                ", createDate=" + createDate + '\'' +
                ", arrivalDate=" + ArrvialDate + '\'' +
                ", companion=" + companion + '\'' +
                '}';
    }
}

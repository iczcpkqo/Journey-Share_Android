package com.journey.entity;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Record implements Serializable {

    private String departure;
    private String documentId;
    private String arrival;
    private Date createDate;

    public Record(String documentId, String departure, String arrival, Date createDate) {
        this.documentId = documentId;
        this.departure = departure;
        this.arrival = arrival;
        this.createDate = createDate;
    }
    public String getDeparture() {
        return departure;
    }
    public String getDocId(){
        return this.documentId;
    }
    public String getArrival() {return arrival;}

    public Date getCreateDate(){
        return this.createDate;
    }

    public String getCreateDateString() {

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return sdf.format(createDate);
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

    @Override
    public String toString() {
        return "Record{" +
                "departure='" + departure + '\'' +
                ", documentId='" + documentId + '\'' +
                ", arrival='" + arrival + '\'' +
                ", createDate=" + createDate +
                '}';
    }
}
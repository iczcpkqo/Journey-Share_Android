package com.journey.entity;

public class Notification {
    private String noti_id;
    private String noti_content;

    public Notification(String noti_id, String noti_content) {
        this.noti_id = noti_id;
        this.noti_content = noti_content;
    }

    public String getNoti_id() {
        return noti_id;
    }

    public void setNoti_id(String noti_id) {
        this.noti_id = noti_id;
    }

    public String getNoti_content() {
        return noti_content;
    }

    public void setNoti_content(String noti_content) {
        this.noti_content = noti_content;
    }


}

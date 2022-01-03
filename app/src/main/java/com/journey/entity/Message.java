package com.journey.entity;

import com.google.type.DateTime;

public class Message {
    private String message_id;
    private User message_from;
    private User message_to;
    private DateTime message_time;

    public Message(String message_id, User message_from, User message_to, DateTime message_time) {
        this.message_id = message_id;
        this.message_from = message_from;
        this.message_to = message_to;
        this.message_time = message_time;
    }

    public String getMessage_id() {
        return message_id;
    }

    public void setMessage_id(String message_id) {
        this.message_id = message_id;
    }

    public User getMessage_from() {
        return message_from;
    }

    public void setMessage_from(User message_from) {
        this.message_from = message_from;
    }

    public User getMessage_to() {
        return message_to;
    }

    public void setMessage_to(User message_to) {
        this.message_to = message_to;
    }

    public DateTime getMessage_time() {
        return message_time;
    }

    public void setMessage_time(DateTime message_time) {
        this.message_time = message_time;
    }

}

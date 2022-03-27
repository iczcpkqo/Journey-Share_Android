package com.journey.entity;

/**
 * @author: Xiang Mao
 * @date: 2022-03-26-04:00
 * @tag: Dialogue, Chat
 */
public class Msg {
    private String sender;
    private String receiver;
    private String content;
    // TODO: 添加消息参数, 消息类型

    public Msg(String con){
        this.content = con;
    }

    public void setSender(String sender) {
        this.sender = sender;
    }

    public String getSender() {
        return sender;
    }

    public void setReceiver(String receiver) {
        this.receiver = receiver;
    }

    public String getReceiver() {
        return receiver;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getContent() {
        return content;
    }
}

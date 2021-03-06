package com.journey.entity;


import java.io.Serializable;

/**
 * @author: Xiang Mao
 * @date: 2022-03-26-04:00
 * @tag: Dialogue, Chat
 */
public class ChatDeliver implements Serializable {

    private String dialogueTitle;
    private String dialogueId;
    private String type;

    public void setType(String type) {
        this.type = type;
    }

    public String getType() {
        return type;
    }

    public void setDialogueId(String dialogueId) {
        this.dialogueId = dialogueId;
    }

    public String getDialogueId() {
        return dialogueId;
    }

    public void setDialogueTitle(String dialogueTitle) {
        this.dialogueTitle = dialogueTitle;
    }

    public String getDialogueTitle() {
        return dialogueTitle;
    }
}

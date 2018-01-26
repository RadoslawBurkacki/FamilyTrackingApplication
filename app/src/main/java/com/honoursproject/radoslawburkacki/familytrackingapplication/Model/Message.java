package com.honoursproject.radoslawburkacki.familytrackingapplication.Model;

import java.util.Date;

public class Message {

    long messageId;
    long fromId;
    long toId;
    String message;
    String date;


    public Message() {

    }

    public Message(long messageId, long fromId, long toId, String message, String date) {
        this.messageId = messageId;
        this.fromId = fromId;
        this.toId = toId;
        this.message = message;
        this.date = date;
    }

    public long getMessageId() {
        return messageId;
    }

    public void setMessageId(long messageId) {
        this.messageId = messageId;
    }

    public long getFromId() {
        return fromId;
    }

    public void setFromId(long fromId) {
        this.fromId = fromId;
    }

    public long getToId() {
        return toId;
    }

    public void setToId(long toId) {
        this.toId = toId;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    @Override
    public String toString() {
        return "Message{" +
                "messageId=" + messageId +
                ", fromId=" + fromId +
                ", toId=" + toId +
                ", message='" + message + '\'' +
                ", date='" + date + '\'' +
                '}';
    }
}




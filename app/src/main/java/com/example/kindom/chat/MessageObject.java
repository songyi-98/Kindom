package com.example.kindom.chat;

import java.util.ArrayList;

public class MessageObject {

    private String messageId, senderId, message, timestamp;

    ArrayList<String> mediaUrlList;

    public MessageObject(String messageId, String senderId, String message, String timestamp, ArrayList<String> mediaUrlList) {
        this.messageId = messageId;
        this.senderId = senderId;
        this.message = message;
        this.timestamp= timestamp;
        this.mediaUrlList = mediaUrlList;
    }

    public String getMessageId() {
        return messageId;
    }

    public String getSenderId() {
        return senderId;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public String getMessage() {
        return message;
    }

    public ArrayList<String> getMediaUrlList() {
        return mediaUrlList;
    }
}

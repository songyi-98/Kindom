package com.example.kindom.chat;

public class MessageObject {

    private String messageId, senderId, message, timestamp;

    public MessageObject(String messageId, String senderId, String message, String timestamp) {
        this.messageId = messageId;
        this.senderId = senderId;
        this.message = message;
        this.timestamp= timestamp;
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
}

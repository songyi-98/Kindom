package com.example.kindom.chat;

public class ChatObject {

    private String chatId;
    private String title;
    private String chatUserId;

    public ChatObject() {
    }

    public ChatObject(String chatId, String title, String chatUserId) {
        this.chatId = chatId;
        this.title = title;
        this.chatUserId = chatUserId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getChatId() {
        return chatId;
    }

    public void setChatId(String chatId) {
        this.chatId = chatId;
    }

    public String getChatUserId() {
        return chatUserId;
    }

    public void setChatUserId(String chatUserId) {
        this.chatUserId = chatUserId;
    }
}
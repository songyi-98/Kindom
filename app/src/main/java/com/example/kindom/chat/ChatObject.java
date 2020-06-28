package com.example.kindom.chat;

public class ChatObject {

    private String title;
    private String chatId;

    public ChatObject() {}

    public ChatObject(String title, String chatId) {
        this.title = title;
        this.chatId = chatId;
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
}
package com.example.kindom;

public class ChatObject {

    private String title;
    private String chatId;

    public ChatObject() {}

    public ChatObject(String title, String chatId) {
        this.title = title;
        this.chatId = chatId;
    }

    public void setTitle(String title) {
        this.title = title;
    }
    public void setChatId(String chatId) {
        this.chatId = chatId;
    }

    public String getTitle() {
        return this.title;
    }
    public String getChatId() {
        return this.chatId;
    }
}

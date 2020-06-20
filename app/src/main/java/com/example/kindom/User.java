package com.example.kindom;

import java.util.ArrayList;

public class User {

    public static final String USER_GROUP_ADMIN = "admin";
    public static final String USER_GROUP_USER = "user";

    private String name;
    private String userGroup;
    private int postalCode;
    private String email;
    private String chatListKey;

    public User() {
        // Default constructor required for calls to DataSnapshot.getValue(User.class)
    }

    public User(String name, String userGroup, int postalCode, String email, String chatListKey) {
        this.name = name;
        this.userGroup = userGroup;
        this.postalCode = postalCode;
        this.email = email;
        this.chatListKey = chatListKey;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUserGroup() {
        return userGroup;
    }

    public void setUserGroup(String userGroup) {
        if (userGroup == User.USER_GROUP_ADMIN || userGroup == User.USER_GROUP_USER) {
            this.userGroup = userGroup;
        }
    }

    public void setChatListKey(String chatListKey) {
        this.chatListKey = chatListKey;
    }

    public String getChatListKey() {
        return chatListKey;
    }

    public int getPostalCode() {
        return postalCode;
    }

    public void setPostalCode(int postalCode) {
        this.postalCode = postalCode;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}

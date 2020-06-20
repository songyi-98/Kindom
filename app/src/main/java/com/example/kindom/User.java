package com.example.kindom;

/**
 * Represent a user of the app
 */
import java.util.ArrayList;

public class User {

    public static final String USER_GROUP_ADMIN = "Admin";
    public static final String USER_GROUP_USER = "User";

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
        this.userGroup = userGroup;
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

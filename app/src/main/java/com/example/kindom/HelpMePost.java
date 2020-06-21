package com.example.kindom;

import java.io.Serializable;
import java.util.Date;

public class HelpMePost implements Serializable {

    private long timeCreated;
    private String category;
    private String title;
    private String user;
    private String location;
    private String date;
    private String time;
    private String description;

    public HelpMePost() {
        // Default constructor required for calls to DataSnapshot.getValue(User.class)
    }

    public HelpMePost(String category, String title, String user, String location, String date, String time, String description) {
        this.timeCreated = new Date().getTime();
        this.category = category;
        this.title = title;
        this.user = user;
        this.location = location;
        this.date = date;
        this.time = time;
        this.description = description;
    }

    public long getTimeCreated() {
        return timeCreated;
    }

    public void setTimeCreated(long timeCreated) {
        this.timeCreated = timeCreated;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
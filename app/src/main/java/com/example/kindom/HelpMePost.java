package com.example.kindom;

import java.io.Serializable;

public class HelpMePost implements Serializable {

    private String category;
    private String title;
    private String user;
    private String location;
    private String date;
    private String time;
    private String description;

    public HelpMePost(String category, String title, String user, String location, String date, String time, String description) {
        this.category = category;
        this.title = title;
        this.user = user;
        this.location = location;
        this.date = date;
        this.time = time;
        this.description = description;
    }

    public String getCategory() {
        return category;
    }

    public String getTitle() {
        return title;
    }

    public String getUser() {
        return user;
    }

    public String getLocation() {
        return location;
    }

    public String getDate() {
        return date;
    }

    public String getTime() {
        return time;
    }

    public String getDescription() {
        return description;
    }
}

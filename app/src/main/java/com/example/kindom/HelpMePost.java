package com.example.kindom;

import android.net.Uri;

import java.io.Serializable;

public class HelpMePost implements Serializable {

    private Uri image;
    private String category;
    private String title;
    private String location;
    private String date;
    private String time;
    private String user;
    private String description;

    public HelpMePost(Uri image, String category, String title, String location, String date, String time, String user, String description) {
        this.image = image;
        this.category = category;
        this.title = title;
        this.location = location;
        this.date = date;
        this.time = time;
        this.user = user;
        this.description = description;
    }

    public Uri getImage() {
        return image;
    }

    public String getCategory() {
        return category;
    }

    public String getTitle() {
        return title;
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

    public String getUser() {
        return user;
    }

    public String getDescription() {
        return description;
    }
}

package com.example.kindom;

import android.net.Uri;

public class HelpMePost {

    private Uri image;
    private String category;
    private String title;
    private String location;
    private String time;
    private String user;

    public HelpMePost(Uri image, String category, String title, String location, String time, String user) {
        this.image = image;
        this.category = category;
        this.title = title;
        this.location = location;
        this.time = time;
        this.user = user;
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

    public String getTime() {
        return time;
    }

    public String getUser() {
        return user;
    }
}

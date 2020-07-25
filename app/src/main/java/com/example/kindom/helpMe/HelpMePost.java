package com.example.kindom.helpMe;

import com.example.kindom.utils.FirebaseHandler;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Represent a Help Me post
 */
public class HelpMePost implements Serializable {

    private long timeCreated;
    private String userUid;
    private String user;
    private String category;
    private String title;
    private String rc;
    private String blkNo;
    private String date;
    private String time;
    private String description;
    private boolean isReported;
    private ArrayList<String> usersOfferingHelp;

    public HelpMePost() {
        // Default constructor required for calls to DataSnapshot.getValue(HelpMePost.class)
    }

    public HelpMePost(long timeCreated, String category, String title, String rc, String blkNo, String date, String time, String description) {
        this.timeCreated = timeCreated;
        this.userUid = FirebaseHandler.getCurrentUserUid();
        this.user = FirebaseHandler.getCurrentUser().getDisplayName();
        this.category = category;
        this.title = title;
        this.rc = rc;
        this.blkNo = blkNo;
        this.date = date;
        this.time = time;
        this.description = description;
        this.isReported = false;
        this.usersOfferingHelp = new ArrayList<>();
        this.usersOfferingHelp.add("list");
    }

    public long getTimeCreated() {
        return timeCreated;
    }

    public void setTimeCreated(long timeCreated) {
        this.timeCreated = timeCreated;
    }

    public String getUserUid() {
        return userUid;
    }

    public void setUserUid(String userUid) {
        this.userUid = userUid;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
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

    public String getRc() {
        return rc;
    }

    public void setRc(String rc) {
        this.rc = rc;
    }

    public String getBlkNo() {
        return blkNo;
    }

    public void setBlkNo(String blkNo) {
        this.blkNo = blkNo;
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

    public boolean isReported() {
        return isReported;
    }

    public void setReported(boolean reported) {
        isReported = reported;
    }

    public ArrayList<String> getUsersOfferingHelp() {
        return usersOfferingHelp;
    }

    public void setUsersOfferingHelp(ArrayList<String> usersOfferingHelp) {
        this.usersOfferingHelp = usersOfferingHelp;
    }
}
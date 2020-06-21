package com.example.kindom;

/**
 * Represent a user of the app
 */
public class User {

    public static final String USER_GROUP_ADMIN = "Admin";
    public static final String USER_GROUP_USER = "User";

    private String name;
    private String userGroup;
    private int postalCode;
    private int blkNo;
    private String email;

    public User() {
        // Default constructor required for calls to DataSnapshot.getValue(User.class)
    }

    public User(String name, String userGroup, int postalCode, int blkNo, String email) {
        this.name = name;
        this.userGroup = userGroup;
        this.postalCode = postalCode;
        this.blkNo = blkNo;
        this.email = email;
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

    public int getPostalCode() {
        return postalCode;
    }

    public void setPostalCode(int postalCode) {
        this.postalCode = postalCode;
    }

    public int getBlkNo() {
        return blkNo;
    }

    public void setBlkNo(int blkNo) {
        this.blkNo = blkNo;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
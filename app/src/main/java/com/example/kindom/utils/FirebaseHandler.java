package com.example.kindom.utils;

import android.app.Application;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class FirebaseHandler extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
    }

    /**
     * Get current user from Firebase
     *
     * @return current user
     */
    public static FirebaseUser getCurrentUser() {
        return FirebaseAuth.getInstance().getCurrentUser();
    }

    /**
     * Get current user UID from Firebase
     *
     * @return UID of current user
     */
    public static String getCurrentUserUid() {
        return getCurrentUser().getUid();
    }
}
package com.example.kindom;

import android.app.Application;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.FirebaseDatabase;

public class FirebaseHandler extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
    }

    /**
     * Get user from Firebase
     *
     * @return current user
     */
    public static FirebaseUser getUser() {
        return FirebaseAuth.getInstance().getCurrentUser();
    }

    /**
     * Get user UID from Firebase
     *
     * @return UID of current user
     */
    public static String getUserUid() {
        return getUser().getUid();
    }
}
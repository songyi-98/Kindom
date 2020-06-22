package com.example.kindom.utils;

import android.app.Application;

import androidx.annotation.NonNull;

import com.example.kindom.User;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

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

    /**
     * Get current User Java object from Firebase
     *
     * @return User java object representing the current user
     */
    public static User getCurrentUserObj() {
        final User[] currUser = new User[1];
        DatabaseReference userDatabase = FirebaseDatabase.getInstance().getReference("users");
        userDatabase.child(FirebaseHandler.getCurrentUserUid()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                currUser[0] = dataSnapshot.getValue(User.class);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Do nothing
            }
        });
        return currUser[0];
    }
}
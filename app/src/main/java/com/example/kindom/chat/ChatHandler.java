package com.example.kindom.chat;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.NonNull;

import com.example.kindom.utils.FirebasePushIdGenerator;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 * Handle the checking of whether a current chat exists between two users
 */
public class ChatHandler {

    private String currUserUid;
    private String otherUserUid;
    private String otherUserName;

    public ChatHandler(String currUserUid, String otherUserUid, String otherUserName) {
        this.currUserUid = currUserUid;
        this.otherUserUid = otherUserUid;
        this.otherUserName = otherUserName;
    }

    /**
     * Create a chat between two users
     * 
     * @param view the view
     */
    public void createChat(final View view) {
        final Intent intent = new Intent(view.getContext(), ChatActivity.class);

        final ArrayList<String> possibleKey = new ArrayList<>();
        DatabaseReference usersRef = FirebaseDatabase.getInstance().getReference("users");
        final DatabaseReference currUserRef = usersRef.child(currUserUid).child("chatListKeys");
        final DatabaseReference otherUserRef = usersRef.child(otherUserUid).child("chatListKeys");

        // Check if a current chat already exists between the two users
        currUserRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    Map<String, Object> newMap = (Map<String, Object>) dataSnapshot.getValue();
                    assert newMap != null;
                    if (newMap.containsValue(otherUserUid)) {
                        String correctKey = null;
                        for (String key : newMap.keySet()) {
                            if (newMap.get(key).equals(otherUserUid)) {
                                correctKey = key;
                            }
                        }
                        if (correctKey != null) {
                            possibleKey.add(correctKey);
                        }
                    }
                }

                Map<String, Object> currUserNewMap = new HashMap<>();
                Map<String, Object> otherUserNewMap = new HashMap<>();
                String chatId = "";
                if (possibleKey.isEmpty()) {
                    // No chat exists yet between the two, create a new chat
                    chatId = FirebasePushIdGenerator.generatePushId();
                    currUserNewMap.put(chatId, currUserUid);
                    otherUserNewMap.put(chatId, otherUserUid);
                    currUserRef.updateChildren(currUserNewMap);
                    otherUserRef.updateChildren(otherUserNewMap);
                } else {
                    for (int k = 0; k < possibleKey.size(); k++) {
                        if (Objects.requireNonNull(dataSnapshot.child(possibleKey.get(k)).getValue()).toString().equals(otherUserUid)) {
                            chatId = possibleKey.get(k);
                            break;
                        }
                    }
                }

                // Start chat activity for user
                Bundle bundle = new Bundle();
                bundle.putString("CHAT_ID", chatId);
                bundle.putString("CHAT_USER_UID", otherUserUid);
                bundle.putString("CHAT_USER", otherUserName);
                intent.putExtras(bundle);
                view.getContext().startActivity(intent);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Do nothing
            }
        });
    }
}
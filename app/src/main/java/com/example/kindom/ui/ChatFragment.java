package com.example.kindom.ui;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.kindom.R;
import com.example.kindom.chat.ChatListAdapter;
import com.example.kindom.chat.ChatObject;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class ChatFragment extends Fragment {

    private RecyclerView mChatList;
    private RecyclerView.Adapter mChatListAdapter;
    private RecyclerView.LayoutManager mChatListLayoutManager;

    ArrayList<ChatObject> chatList;

    public ChatFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        chatList = new ArrayList<>();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_chat, container, false);
        mChatList = rootView.findViewById(R.id.chatList);
        initializeRecyclerView();
        getUserChatList();

        return rootView;
    }

    //retrieves the users' chatList from the database
    private void getUserChatList() {
        final DatabaseReference mUserChatDB = FirebaseDatabase.getInstance().getReference().child("users").child(FirebaseAuth.getInstance().getUid()).child("chatListKeys");

        mUserChatDB.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    for (DataSnapshot childSnapshot : dataSnapshot.getChildren()) {
                        String chatKey = childSnapshot.getKey();
                        ChatObject mChat = new ChatObject(childSnapshot.getValue().toString(), childSnapshot.getKey());
                        chatList.add(mChat);
                        mChatListAdapter.notifyDataSetChanged();
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    //Initializes Chat list with recycler view of Chat Objects
    private void initializeRecyclerView() {
        mChatList.setNestedScrollingEnabled(false);
        mChatList.setHasFixedSize(false);
        mChatListLayoutManager = new LinearLayoutManager(getContext(), RecyclerView.VERTICAL, false);
        mChatList.setLayoutManager(mChatListLayoutManager);
        mChatListAdapter = new ChatListAdapter(chatList);
        mChatList.setAdapter(mChatListAdapter);
    }
}
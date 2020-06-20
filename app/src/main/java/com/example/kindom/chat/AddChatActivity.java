package com.example.kindom.chat;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.kindom.R;

import java.util.ArrayList;

public class AddChatActivity extends AppCompatActivity {
    private RecyclerView mChatAdder;
    private RecyclerView.Adapter mChatAdderAdapter;
    private RecyclerView.LayoutManager mChatAdderLayoutManager;

    ArrayList<ChatObject> addChatList;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        initializeChatAdder();
    }

    private void initializeChatAdder() {
        addChatList = new ArrayList<>();
        mChatAdder = findViewById(R.id.chatAdderList);
        mChatAdder.setNestedScrollingEnabled(false);
        mChatAdder.setHasFixedSize(false);
        //this line below could cause problems
        mChatAdderLayoutManager = new LinearLayoutManager(getApplicationContext(), RecyclerView.VERTICAL, false);
        mChatAdder.setLayoutManager(mChatAdderLayoutManager);
        mChatAdderAdapter = new ChatAdderAdapter(addChatList);
        mChatAdder.setAdapter(mChatAdderAdapter);
    }
}
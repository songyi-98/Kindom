package com.example.kindom.chat;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.kindom.R;

import java.util.ArrayList;

public class ChatAdderAdapter extends RecyclerView.Adapter<ChatAdderAdapter.ChatAdderViewHolder>{

    ArrayList<ChatObject> chatObjectlist;

    public ChatAdderAdapter(ArrayList<ChatObject> chatObjectlist) {
        this.chatObjectlist = chatObjectlist;
    }
    @NonNull
    @Override
    public ChatAdderAdapter.ChatAdderViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_add_chat, parent, false);
        return new ChatAdderViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ChatAdderAdapter.ChatAdderViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return chatObjectlist.size();
    }

    public class ChatAdderViewHolder extends RecyclerView.ViewHolder {

        public ChatAdderViewHolder(@NonNull View itemView) {
            super(itemView);
        }
    }
}

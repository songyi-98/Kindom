package com.example.kindom.chat;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.kindom.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class ChatListAdapter extends RecyclerView.Adapter<ChatListAdapter.ChatListViewHolder> {

    ArrayList<ChatObject> chatList;
    Context context;

    public ChatListAdapter(Context context, ArrayList<ChatObject> chatList) {
        this.context = context;
        this.chatList = chatList;
    }

    @NonNull
    @Override
    public ChatListViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View layoutView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_chat, null, false);
        RecyclerView.LayoutParams lp = new RecyclerView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        layoutView.setLayoutParams(lp);
        return new ChatListViewHolder(layoutView);
    }

    @Override
    public void onBindViewHolder(@NonNull final ChatListViewHolder holder, final int position) {
        holder.mTitle.setText(chatList.get(position).getTitle());

        StorageReference storageRef = FirebaseStorage.getInstance().getReference("profileImages");
        storageRef.child(chatList.get(position).getChatUserId()).getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                Glide.with(context).load(uri).into(holder.mChatPicture);
            }
        });

        holder.mLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            //not sure if this part would be correct,
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), ChatActivity.class);
                Bundle bundle = new Bundle();
                bundle.putString("CHAT_ID", chatList.get(holder.getAdapterPosition()).getChatId());
                bundle.putString("CHAT_USER", chatList.get(holder.getAdapterPosition()).getTitle());
                // TODO: Include the user UID
                bundle.putString("CHAT_USER_UID", chatList.get(holder.getAdapterPosition()).getChatUserId());
                intent.putExtras(bundle);
                v.getContext().startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return chatList.size();
    }

    public class ChatListViewHolder extends RecyclerView.ViewHolder {
        public TextView mTitle;
        public LinearLayout mLayout;
        public CircleImageView mChatPicture;
        public ChatListViewHolder(View view) {
            super(view);
            mTitle = view.findViewById(R.id.chatTitle);
            mLayout = view.findViewById(R.id.chatLayout);
            mChatPicture = view.findViewById(R.id.chatPicture);
        }
    }
}
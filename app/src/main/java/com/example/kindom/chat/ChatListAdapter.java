package com.example.kindom.chat;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.kindom.R;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class ChatListAdapter extends RecyclerView.Adapter<ChatListAdapter.ChatListViewHolder> {

    private Context mContext;
    private ArrayList<ChatObject> mChatList;
    private LayoutInflater mInflater;

    public ChatListAdapter(Context context, ArrayList<ChatObject> chatList) {
        this.mContext = context;
        mInflater = LayoutInflater.from(context);
        this.mChatList = chatList;
    }

    static class ChatListViewHolder extends RecyclerView.ViewHolder {

        public TextView mTitle;
        public ConstraintLayout mLayout;
        public CircleImageView mChatPicture;

        public ChatListViewHolder(View view) {
            super(view);
            mTitle = view.findViewById(R.id.chat_user_name);
            mLayout = view.findViewById(R.id.chat_layout);
            mChatPicture = view.findViewById(R.id.chat_picture);
        }
    }

    @NonNull
    @Override
    public ChatListViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = mInflater.inflate(R.layout.item_chat, parent, false);
        return new ChatListViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull final ChatListViewHolder holder, final int position) {
        holder.mTitle.setText(mChatList.get(position).getTitle());

        StorageReference storageRef = FirebaseStorage.getInstance().getReference("profileImages");
        storageRef.child(mChatList.get(position).getChatUserId()).getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                Glide.with(mContext).load(uri).into(holder.mChatPicture);
            }
        });

        holder.mLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            //not sure if this part would be correct,
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), ChatActivity.class);
                Bundle bundle = new Bundle();
                bundle.putString("CHAT_ID", mChatList.get(holder.getAdapterPosition()).getChatId());
                bundle.putString("CHAT_USER", mChatList.get(holder.getAdapterPosition()).getTitle());
                bundle.putString("CHAT_USER_UID", mChatList.get(holder.getAdapterPosition()).getChatUserId());
                intent.putExtras(bundle);
                v.getContext().startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return mChatList.size();
    }
}
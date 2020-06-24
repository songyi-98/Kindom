package com.example.kindom.chat;

import android.graphics.Typeface;
import android.media.Image;
import android.net.Uri;
import android.text.SpannableStringBuilder;
import android.text.style.ImageSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.kindom.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;
import com.stfalcon.frescoimageviewer.ImageViewer;

import org.w3c.dom.Text;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class MessageAdapter extends RecyclerView.Adapter {

    private static final int VIEW_TYPE_MESSAGE_SENT = 1;
    private static final int VIEW_TYPE_MESSAGE_RECEIVED = 2;

    ArrayList<MessageObject> messageList;

    public MessageAdapter(ArrayList<MessageObject> Message) {
        this.messageList = Message;
    }

    @Override
    public int getItemViewType(int position) {
        MessageObject message = (MessageObject) messageList.get(position);

        if (message.getSenderId().equals(FirebaseAuth.getInstance().getUid())) {
            // If the current user is the sender of the message
            return VIEW_TYPE_MESSAGE_SENT;
        } else {
            // If some other user sent the message
            return VIEW_TYPE_MESSAGE_RECEIVED;
        }
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType == VIEW_TYPE_MESSAGE_SENT) {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.item_message_sent, parent, false);
            return new SentMessageViewHolder(view);
        } else if (viewType == VIEW_TYPE_MESSAGE_RECEIVED) {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.item_message_received, parent, false);
            return new ReceivedMessageViewHolder(view);
        }
        return null;
    }

    @Override
    public void onBindViewHolder(@NonNull final RecyclerView.ViewHolder holder, final int position) {
        final MessageObject message = (MessageObject) messageList.get(position);
        switch (holder.getItemViewType()) {
            case VIEW_TYPE_MESSAGE_SENT:
                ((SentMessageViewHolder) holder).bind(message);
                break;
            case VIEW_TYPE_MESSAGE_RECEIVED:
                ((ReceivedMessageViewHolder) holder).bind(message);
        }
    }

    @Override
    public int getItemCount() {
        return messageList.size();
    }

    public class SentMessageViewHolder extends RecyclerView.ViewHolder {
        private TextView mMessage, mTime;
        public ConstraintLayout mLayout;
        private ImageView mImageBody;
        public SentMessageViewHolder(View view) {
            super(view);
            mMessage = view.findViewById(R.id.text_message_body);
            mTime = view.findViewById(R.id.text_message_time);
            mLayout = view.findViewById(R.id.message_received_layout);
            mImageBody = view.findViewById(R.id.image_body);
        }

        void bind(final MessageObject message) {
            mMessage.setText(message.getMessage());
            SimpleDateFormat dateFormat = new SimpleDateFormat("hh.mm aa");
            String formattedTimeStamp = dateFormat.format(Long.parseLong(message.getTimestamp()));
            mTime.setText(formattedTimeStamp);
            if (!message.getMediaUrlList().isEmpty()) {
                Glide.with(itemView.getContext())
                        .load(Uri.parse(message.getMediaUrlList().get(0)))
                        .override(400,400)
                        .into(mImageBody);
                mImageBody.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        new ImageViewer.Builder(itemView.getContext(), message.getMediaUrlList())
                                .setStartPosition(0)
                                .show();
                    }
                });
                mMessage.setVisibility(View.GONE);
            } else {
                mImageBody.setVisibility(View.GONE);
            }
/*
            // Insert the profile image from the URL into the ImageView.
            Utils.displayRoundImageFromUrl(mContext, message.getSender().getProfileUrl(), profileImage);*/
        }
    }

    public class ReceivedMessageViewHolder extends RecyclerView.ViewHolder {
        private TextView mMessage, mTime, mSender;
        private ImageView mImageBody;
        public ConstraintLayout mLayout;
        public ReceivedMessageViewHolder(View view) {
            super(view);
            mMessage = view.findViewById(R.id.text_message_body);
            mTime = view.findViewById(R.id.text_message_time);
            mSender = view.findViewById(R.id.text_message_name);
            mLayout = view.findViewById(R.id.message_received_layout);
            mImageBody = view.findViewById(R.id.image_body);

        }

        void bind(final MessageObject message) {
            mMessage.setText(message.getMessage());
            mSender.setText(message.getSenderId());
            SimpleDateFormat dateFormat = new SimpleDateFormat("hh.mm aa");
            String formattedTimeStamp = dateFormat.format(message.getTimestamp());
            mTime.setText(formattedTimeStamp);
            if (!message.getMediaUrlList().isEmpty()) {
                Glide.with(itemView.getContext())
                        .load(Uri.parse(message.getMediaUrlList().get(0)))
                        .override(400,400)
                        .into(mImageBody);
                mImageBody.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        new ImageViewer.Builder(itemView.getContext(), message.getMediaUrlList())
                                .setStartPosition(0)
                                .show();
                    }
                });
                mMessage.setVisibility(View.GONE);
            } else {
                mImageBody.setVisibility(View.GONE);
            }
/*
            // Insert the profile image from the URL into the ImageView.
            Utils.displayRoundImageFromUrl(mContext, message.getSender().getProfileUrl(), profileImage);*/
        }
    }
}

package com.example.kindom.chat;

import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.example.kindom.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;
import com.stfalcon.frescoimageviewer.ImageViewer;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.Calendar;

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
        MessageObject message = (MessageObject) messageList.get(position);
        switch (holder.getItemViewType()) {
            case VIEW_TYPE_MESSAGE_SENT:
                ((SentMessageViewHolder) holder).bind(message);
                if (messageList.get(holder.getAdapterPosition()).getMediaUrlList().isEmpty()) {
                    ((SentMessageViewHolder) holder).mViewMedia.setVisibility(View.GONE);
                }
                ((SentMessageViewHolder) holder).mViewMedia.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        new ImageViewer.Builder(v.getContext(), messageList.get(holder.getAdapterPosition()).getMediaUrlList())
                                .setStartPosition(0)
                                .show();
                    }
                });
                break;
            case VIEW_TYPE_MESSAGE_RECEIVED:
                ((ReceivedMessageViewHolder) holder).bind(message);
                if (messageList.get(holder.getAdapterPosition()).getMediaUrlList().isEmpty()) {
                    ((ReceivedMessageViewHolder) holder).mViewMedia.setVisibility(View.GONE);
                }
                ((ReceivedMessageViewHolder) holder).mViewMedia.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    new ImageViewer.Builder(v.getContext(), messageList.get(holder.getAdapterPosition()).getMediaUrlList())
                            .setStartPosition(0)
                            .show();
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return messageList.size();
    }

    public class SentMessageViewHolder extends RecyclerView.ViewHolder {
        private TextView mMessage, mTime;
        Button mViewMedia;
        public ConstraintLayout mLayout;
        public SentMessageViewHolder(View view) {
            super(view);
            mMessage = view.findViewById(R.id.text_message_body);
            mTime = view.findViewById(R.id.text_message_time);
            mLayout = view.findViewById(R.id.message_received_layout);
            mViewMedia = view.findViewById(R.id.viewMedia);
        }

        void bind(MessageObject message) {
            mMessage.setText(message.getMessage());

            // Format the stored timestamp into a readable String using method.
            mTime.setText(message.getTimestamp());

/*
            // Insert the profile image from the URL into the ImageView.
            Utils.displayRoundImageFromUrl(mContext, message.getSender().getProfileUrl(), profileImage);*/
        }
    }

    public class ReceivedMessageViewHolder extends RecyclerView.ViewHolder {
        private TextView mMessage, mTime, mSender;
        Button mViewMedia;
        public ConstraintLayout mLayout;
        public ReceivedMessageViewHolder(View view) {
            super(view);
            mMessage = view.findViewById(R.id.text_message_body);
            mTime = view.findViewById(R.id.text_message_time);
            mSender = view.findViewById(R.id.text_message_name);
            mLayout = view.findViewById(R.id.message_received_layout);
            mViewMedia = view.findViewById(R.id.viewMedia);

        }

        void bind(MessageObject message) {
            mMessage.setText(message.getMessage());

            // Format the stored timestamp into a readable String using method.
            mTime.setText(message.getTimestamp());
            mSender.setText(message.getSenderId());
/*
            // Insert the profile image from the URL into the ImageView.
            Utils.displayRoundImageFromUrl(mContext, message.getSender().getProfileUrl(), profileImage);*/
        }
    }
}

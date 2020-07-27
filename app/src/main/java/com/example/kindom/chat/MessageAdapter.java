package com.example.kindom.chat;

import android.annotation.SuppressLint;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.kindom.R;
import com.google.firebase.auth.FirebaseAuth;
import com.stfalcon.frescoimageviewer.ImageViewer;

import java.text.SimpleDateFormat;
import java.util.ArrayList;

public class MessageAdapter extends RecyclerView.Adapter {

    private static final int VIEW_TYPE_MESSAGE_SENT = 1;
    private static final int VIEW_TYPE_MESSAGE_RECEIVED = 2;
    private static final int VIEW_TYPE_MESSAGE_TIME = 3;

    final ArrayList<MessageObject> messageList;

    public MessageAdapter(ArrayList<MessageObject> Message) {
        this.messageList = Message;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getItemViewType(int position) {
        MessageObject message = (MessageObject) messageList.get(position);

        if (message.getSenderId().equals(FirebaseAuth.getInstance().getUid())) {
            // If the current user is the sender of the message
            return VIEW_TYPE_MESSAGE_SENT;
        } else if (message.getSenderId().equals("Time")) {
            // The message is a time divider
            return VIEW_TYPE_MESSAGE_TIME;
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
        } else if (viewType == VIEW_TYPE_MESSAGE_TIME) {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.item_timestamp, parent, false);
            return new TimeMessageViewHolder(view);
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
                break;
            case VIEW_TYPE_MESSAGE_TIME:
                ((TimeMessageViewHolder) holder).bind(message);
        }
        holder.setIsRecyclable(false);

    }

    @Override
    public int getItemCount() {
        return messageList.size();
    }

    @Override
    public void onViewAttachedToWindow(@NonNull RecyclerView.ViewHolder holder) {
        super.onViewAttachedToWindow(holder);
        holder.itemView.setVisibility(View.VISIBLE);
    }

    public static class SentMessageViewHolder extends RecyclerView.ViewHolder {
        private final TextView mMessage;
        private final TextView mTime;
        public final ConstraintLayout mLayout;
        private final ImageView mImageBody;

        public SentMessageViewHolder(View view) {
            super(view);
            mMessage = view.findViewById(R.id.chat_text_body);
            mTime = view.findViewById(R.id.chat_text_time);
            mLayout = view.findViewById(R.id.message_sent_layout);
            mImageBody = view.findViewById(R.id.chat_image_sent_body);
        }

        void bind(final MessageObject message) {
            mMessage.setText(message.getMessage());
            SimpleDateFormat dateFormat = new SimpleDateFormat("hh.mm aa");
            String formattedTimeStamp = dateFormat.format(Long.parseLong(message.getTimestamp()));
            mTime.setText(formattedTimeStamp);
            if (message.getMessage().isEmpty()) {
                if (!message.getMediaUrlList().isEmpty()) {
                    itemView.setVisibility(View.VISIBLE);
                    Glide.with(itemView.getContext())
                            .load(Uri.parse(message.getMediaUrlList().get(0)))
                            .override(400, 400)
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
                    ConstraintLayout.LayoutParams newParams = new ConstraintLayout.LayoutParams(
                            ConstraintLayout.LayoutParams.WRAP_CONTENT,
                            ConstraintLayout.LayoutParams.WRAP_CONTENT);
                    newParams.rightToLeft = R.id.chat_image_sent_body;
                    newParams.bottomToBottom = R.id.chat_image_sent_body;
                    mTime.setLayoutParams(newParams);
                } else {
                    mImageBody.setVisibility(View.GONE);
                }
            } else {
                if (!message.getMediaUrlList().isEmpty()) {
                    itemView.setVisibility(View.VISIBLE);
                    mImageBody.setBackgroundResource(R.drawable.message_sent_image_bubble_upper);
                    Glide.with(itemView.getContext())
                            .load(Uri.parse(message.getMediaUrlList().get(0)))
                            .override(400, 400)
                            .into(mImageBody);
                    mImageBody.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            new ImageViewer.Builder(itemView.getContext(), message.getMediaUrlList())
                                    .setStartPosition(0)
                                    .show();
                        }
                    });
                    ConstraintLayout.LayoutParams newParams = new ConstraintLayout.LayoutParams(
                            ConstraintLayout.LayoutParams.MATCH_CONSTRAINT,
                            ConstraintLayout.LayoutParams.WRAP_CONTENT);
                    newParams.topToBottom = R.id.chat_image_sent_body;
                    newParams.rightToRight = R.id.chat_image_sent_body;
                    newParams.leftToLeft = R.id.chat_image_sent_body;
                    mMessage.setLayoutParams(newParams);
                    mMessage.setBackgroundResource(R.drawable.message_sent_image_bubble_lower);
                } else {
                    mImageBody.setVisibility(View.GONE);
                }
            }
        }
    }

    public static class ReceivedMessageViewHolder extends RecyclerView.ViewHolder {
        private final TextView mMessage;
        private final TextView mTime;
        private final ImageView mImageBody;
        public final ConstraintLayout mLayout;

        public ReceivedMessageViewHolder(View view) {
            super(view);
            mMessage = view.findViewById(R.id.chat_text_body);
            mTime = view.findViewById(R.id.chat_text_time);
            mLayout = view.findViewById(R.id.message_received_layout);
            mImageBody = view.findViewById(R.id.chat_image_received_body);
        }

        void bind(final MessageObject message) {
            mMessage.setText(message.getMessage());
            SimpleDateFormat dateFormat = new SimpleDateFormat("hh.mm aa");
            String formattedTimeStamp = dateFormat.format(Long.parseLong(message.getTimestamp()));
            mTime.setText(formattedTimeStamp);
            if (message.getMessage().isEmpty()) {
                if (!message.getMediaUrlList().isEmpty()) {
                    itemView.setVisibility(View.VISIBLE);
                    Glide.with(itemView.getContext())
                            .load(Uri.parse(message.getMediaUrlList().get(0)))
                            .override(400, 400)
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
                    ConstraintLayout.LayoutParams newParams = new ConstraintLayout.LayoutParams(
                            ConstraintLayout.LayoutParams.WRAP_CONTENT,
                            ConstraintLayout.LayoutParams.WRAP_CONTENT);
                    newParams.leftToRight = R.id.chat_image_received_body;
                    newParams.bottomToBottom = R.id.chat_image_received_body;
                    mTime.setLayoutParams(newParams);
                } else {
                    mImageBody.setVisibility(View.GONE);
                }
            } else {
                if (!message.getMediaUrlList().isEmpty()) {
                    itemView.setVisibility(View.VISIBLE);
                    mImageBody.setBackgroundResource(R.drawable.message_received_image_bubble_upper);
                    Glide.with(itemView.getContext())
                            .load(Uri.parse(message.getMediaUrlList().get(0)))
                            .override(600, 600)
                            .into(mImageBody);
                    mImageBody.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            new ImageViewer.Builder(itemView.getContext(), message.getMediaUrlList())
                                    .setStartPosition(0)
                                    .show();
                        }
                    });
                    ConstraintLayout.LayoutParams newParams = new ConstraintLayout.LayoutParams(
                            ConstraintLayout.LayoutParams.MATCH_CONSTRAINT,
                            ConstraintLayout.LayoutParams.WRAP_CONTENT);
                    newParams.topToBottom = R.id.chat_image_received_body;
                    newParams.rightToRight = R.id.chat_image_received_body;
                    newParams.leftToLeft = R.id.chat_image_received_body;
                    mMessage.setBackgroundResource(R.drawable.message_received_image_bubble_lower);
                    mMessage.setLayoutParams(newParams);
                } else {
                    mImageBody.setVisibility(View.GONE);
                }
            }
        }
    }

    public static class TimeMessageViewHolder extends RecyclerView.ViewHolder {
        private final TextView mTimestamp;

        public TimeMessageViewHolder(View view) {
            super(view);
            mTimestamp = view.findViewById(R.id.chat_time_body);
            ConstraintLayout mLayout = view.findViewById(R.id.item_timestamp_layout);
        }

        @SuppressLint("SetTextI18n")
        void bind(final MessageObject message) {
            Long timeFromMidnight = (System.currentTimeMillis()) % (24 * 60 * 60 * 1000);
            Long closestMidnightTime = (System.currentTimeMillis()) - timeFromMidnight;
            mTimestamp.setText("Today");
            if (Long.parseLong(message.getTimestamp()) < closestMidnightTime) {
                mTimestamp.setText("Yesterday");
            }
            if (Long.parseLong(message.getTimestamp()) < closestMidnightTime - 24 * 60 * 60 * 1000) {
                SimpleDateFormat oldDateFormat = new SimpleDateFormat("MMM dd");
                String formattedTimeStamp = oldDateFormat.format(Long.parseLong(message.getTimestamp()));
                mTimestamp.setText(formattedTimeStamp);
            }
            if (Long.parseLong(message.getTimestamp()) < closestMidnightTime - 365 * 24 * 60 * 60 *1000) {
                SimpleDateFormat olderDateFormat = new SimpleDateFormat("MMM dd YYYY");
                String formattedTimeStamp = olderDateFormat.format(Long.parseLong(message.getTimestamp()));
                mTimestamp.setText(formattedTimeStamp);
            }
        }
    }
}
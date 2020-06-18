package com.example.kindom;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.chip.Chip;

import java.util.ArrayList;

public class HelpMeUserListingAdapter extends RecyclerView.Adapter<HelpMeUserListingAdapter.HelpMePostViewHolder> {

    private Context mContext;
    private final ArrayList<HelpMePost> mPostList;
    private LayoutInflater mInflater;

    class HelpMePostViewHolder extends RecyclerView.ViewHolder {

        public Chip categoryChip;
        public TextView titleTextView;
        public TextView locationTextView;
        public TextView dateTextView;
        public TextView timeTextView;
        public MaterialButton editButton;
        public MaterialButton deleteButton;

        public HelpMePostViewHolder(@NonNull View itemView) {
            super(itemView);
            categoryChip = itemView.findViewById(R.id.list_item_help_me_user_listing_category);
            titleTextView = itemView.findViewById(R.id.list_item_help_me_user_listing_title);
            locationTextView = itemView.findViewById(R.id.list_item_help_me_user_listing_location);
            dateTextView = itemView.findViewById(R.id.list_item_help_me_user_listing_date);
            timeTextView = itemView.findViewById(R.id.list_item_help_me_user_listing_time);
            editButton = itemView.findViewById(R.id.list_item_help_me_user_listing_edit);
            deleteButton = itemView.findViewById(R.id.list_item_help_me_user_listing_delete);
        }
    }

    public HelpMeUserListingAdapter(Context context, ArrayList<HelpMePost> postList) {
        mContext = context;
        mInflater =LayoutInflater.from(context);
        mPostList = postList;
    }

    @NonNull
    @Override
    public HelpMeUserListingAdapter.HelpMePostViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = mInflater.inflate(R.layout.list_item_help_me_user_listing, parent, false);
        return new HelpMeUserListingAdapter.HelpMePostViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull HelpMeUserListingAdapter.HelpMePostViewHolder holder, int position) {
        final HelpMePost currPost = mPostList.get(position);
        holder.categoryChip.setText(currPost.getCategory());
        holder.titleTextView.setText(currPost.getTitle());
        holder.locationTextView.setText(currPost.getLocation());
        holder.dateTextView.setText(currPost.getDate());
        holder.timeTextView.setText(currPost.getTime());
        holder.editButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, HelpMePostEditActivity.class);
                intent.putExtra("Post", currPost);
                mContext.startActivity(intent);
            }
        });
        holder.deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO: Delete post
            }
        });
    }

    @Override
    public int getItemCount() {
        return mPostList.size();
    }
}

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

public class HelpMeAllListingAdapter extends RecyclerView.Adapter<HelpMeAllListingAdapter.HelpMePostViewHolder> {

    private Context mContext;
    private final ArrayList<HelpMePost> mPostList;
    private LayoutInflater mInflater;

    static class HelpMePostViewHolder extends RecyclerView.ViewHolder {

        public Chip categoryChip;
        public TextView titleTextView;
        public TextView locationTextView;
        public TextView dateTextView;
        public TextView timeTextView;
        public Chip userChip;
        public MaterialButton detailsButton;

        public HelpMePostViewHolder(@NonNull View itemView) {
            super(itemView);
            categoryChip = itemView.findViewById(R.id.list_item_help_me_all_listing_category);
            titleTextView = itemView.findViewById(R.id.list_item_help_me_all_listing_title);
            locationTextView = itemView.findViewById(R.id.list_item_help_me_all_listing_location);
            dateTextView = itemView.findViewById(R.id.list_item_help_me_all_listing_date);
            timeTextView = itemView.findViewById(R.id.list_item_help_me_all_listing_time);
            userChip = itemView.findViewById(R.id.list_item_help_me_all_listing_user);
            detailsButton = itemView.findViewById(R.id.list_item_help_me_all_listing_details);
        }
    }

    public HelpMeAllListingAdapter(Context context, ArrayList<HelpMePost> postList) {
        mContext = context;
        mInflater =LayoutInflater.from(context);
        mPostList = postList;
    }

    @NonNull
    @Override
    public HelpMePostViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = mInflater.inflate(R.layout.list_item_help_me_all_listing, parent, false);
        return new HelpMePostViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull HelpMePostViewHolder holder, int position) {
        final HelpMePost currPost = mPostList.get(position);
        holder.categoryChip.setText(currPost.getCategory());
        holder.titleTextView.setText(currPost.getTitle());
        holder.locationTextView.setText(currPost.getLocation());
        holder.dateTextView.setText(currPost.getDate());
        holder.timeTextView.setText(currPost.getTime());
        holder.userChip.setText(currPost.getUser());
        holder.detailsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, HelpMePostDetailsActivity.class);
                intent.putExtra("Post", currPost);
                mContext.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return mPostList.size();
    }
}

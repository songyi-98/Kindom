package com.example.kindom;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.chip.Chip;

import java.util.ArrayList;

public class HelpMePostAdapter extends RecyclerView.Adapter<HelpMePostAdapter.HelpMePostViewHolder> {

    private final ArrayList<HelpMePost> mPostList;
    private LayoutInflater mInflater;

    class HelpMePostViewHolder extends RecyclerView.ViewHolder {

        public ImageView imageView;
        public Chip categoryChip;
        public TextView titleTextView;
        public TextView locationTextView;
        public TextView timeTextView;
        public TextView userTextView;

        public HelpMePostViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.help_me_list_item_image);
            categoryChip = itemView.findViewById(R.id.help_me_list_item_category);
            titleTextView = itemView.findViewById(R.id.help_me_list_item_title);
            locationTextView = itemView.findViewById(R.id.help_me_list_item_location);
            timeTextView = itemView.findViewById(R.id.help_me_list_item_time);
            userTextView = itemView.findViewById(R.id.help_me_list_item_user);
        }
    }

    public HelpMePostAdapter(Context context, ArrayList<HelpMePost> postList) {
        mInflater =LayoutInflater.from(context);
        mPostList = postList;
    }

    @NonNull
    @Override
    public HelpMePostViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView =mInflater.inflate(R.layout.list_item_help_me, parent, false);
        return new HelpMePostViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull HelpMePostViewHolder holder, int position) {
        HelpMePost currPost = mPostList.get(position);
        //holder.imageView.setImageURI(currPost.getImage());
        holder.categoryChip.setText(currPost.getCategory());
        holder.titleTextView.setText(currPost.getTitle());
        holder.locationTextView.setText(currPost.getLocation());
        holder.timeTextView.setText(currPost.getTime());
        holder.userTextView.setText(currPost.getUser());
    }

    @Override
    public int getItemCount() {
        return mPostList.size();
    }
}

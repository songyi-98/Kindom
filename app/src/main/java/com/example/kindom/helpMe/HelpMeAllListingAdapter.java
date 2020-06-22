package com.example.kindom.helpMe;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.kindom.R;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.chip.Chip;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class HelpMeAllListingAdapter extends RecyclerView.Adapter<HelpMeAllListingAdapter.HelpMePostViewHolder> {

    private Context mContext;
    private final ArrayList<HelpMePost> mPostList;
    private LayoutInflater mInflater;

    static class HelpMePostViewHolder extends RecyclerView.ViewHolder {

        public Chip categoryChip;
        public TextView titleTextView;
        public TextView blkNoTextView;
        public TextView dateTextView;
        public TextView timeTextView;
        public Chip userChip;
        public MaterialButton detailsButton;

        public HelpMePostViewHolder(@NonNull View itemView) {
            super(itemView);
            categoryChip = itemView.findViewById(R.id.list_item_help_me_all_listing_category);
            titleTextView = itemView.findViewById(R.id.list_item_help_me_all_listing_title);
            blkNoTextView = itemView.findViewById(R.id.list_item_help_me_all_listing_blk_no);
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
    @SuppressLint("SimpleDateFormat")
    public void onBindViewHolder(@NonNull HelpMePostViewHolder holder, int position) {
        final HelpMePost currPost = mPostList.get(position);
        String date = "";
        try {
            Date dateObj = new SimpleDateFormat("dd/MM/yyyy").parse(currPost.getDate());
            DateFormat dateFormat = new SimpleDateFormat("d MMM");
            assert dateObj != null;
            date = dateFormat.format(dateObj);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        holder.categoryChip.setText(currPost.getCategory());
        holder.titleTextView.setText(currPost.getTitle());
        holder.blkNoTextView.setText(currPost.getBlkNo());
        holder.dateTextView.setText(date);
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
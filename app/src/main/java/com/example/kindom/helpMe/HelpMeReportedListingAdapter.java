package com.example.kindom.helpMe;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import com.example.kindom.R;
import com.example.kindom.ui.helpMe.HelpMeReportedListingFragment;
import com.example.kindom.utils.FirebaseHandler;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.chip.Chip;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class HelpMeReportedListingAdapter extends RecyclerView.Adapter<HelpMeReportedListingAdapter.HelpMePostViewHolder> {

    private final DatabaseReference mUserPostsRef = FirebaseDatabase.getInstance().getReference("helpMe");
    private final Context mContext;
    private final HelpMeReportedListingFragment mFragment;
    private final ArrayList<HelpMePost> mPostList;
    private final LayoutInflater mInflater;

    static class HelpMePostViewHolder extends RecyclerView.ViewHolder {

        public final Chip categoryChip;
        public final TextView titleTextView;
        public final TextView blkNoTextView;
        public final TextView dateTextView;
        public final TextView timeTextView;
        public final Chip userChip;
        public final MaterialButton detailsButton;
        public final MaterialButton ignoreButton;
        public final MaterialButton deleteButton;

        public HelpMePostViewHolder(@NonNull View itemView) {
            super(itemView);
            categoryChip = itemView.findViewById(R.id.list_item_help_me_reported_listing_category);
            titleTextView = itemView.findViewById(R.id.list_item_help_me_reported_listing_title);
            blkNoTextView = itemView.findViewById(R.id.list_item_help_me_reported_listing_blk_no);
            dateTextView = itemView.findViewById(R.id.list_item_help_me_reported_listing_date);
            timeTextView = itemView.findViewById(R.id.list_item_help_me_reported_listing_time);
            userChip = itemView.findViewById(R.id.list_item_help_me_reported_listing_user);
            detailsButton = itemView.findViewById(R.id.list_item_help_me_reported_listing_details);
            ignoreButton = itemView.findViewById(R.id.list_item_help_me_reported_listing_ignore);
            deleteButton = itemView.findViewById(R.id.list_item_help_me_reported_listing_delete);
        }
    }

    public HelpMeReportedListingAdapter(Context context, HelpMeReportedListingFragment fragment, ArrayList<HelpMePost> postList) {
        mContext = context;
        mFragment = fragment;
        mInflater = LayoutInflater.from(context);
        mPostList = postList;
    }

    @NonNull
    @Override
    public HelpMePostViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = mInflater.inflate(R.layout.list_item_help_me_reported_listing, parent, false);
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
        holder.ignoreButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new AlertDialog.Builder(mContext)
                        .setMessage(R.string.help_me_post_ignore)
                        .setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                currPost.setReported(false);
                                DatabaseReference uploadRef = FirebaseDatabase
                                        .getInstance()
                                        .getReference("helpMe")
                                        .child(currPost.getRc())
                                        .child(currPost.getUserUid())
                                        .child(String.valueOf(currPost.getTimeCreated()));
                                uploadRef.child("reported").setValue(false);
                                mFragment.refresh();
                            }
                        })
                        .setNegativeButton(R.string.no, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                // Do nothing
                            }
                        })
                .show();
            }
        });
        holder.deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new AlertDialog.Builder(mContext)
                        .setMessage(R.string.help_me_post_delete)
                        .setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                mUserPostsRef.child(currPost.getRc()).child(FirebaseHandler.getCurrentUserUid()).child(String.valueOf(currPost.getTimeCreated())).removeValue();
                                mFragment.refresh();
                            }
                        })
                        .setNegativeButton(R.string.no, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                // Do nothing
                            }
                        })
                        .show();

            }
        });
    }

    @Override
    public int getItemCount() {
        return mPostList.size();
    }
}
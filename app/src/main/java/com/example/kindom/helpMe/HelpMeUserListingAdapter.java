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

import com.example.kindom.utils.CalendarHandler;
import com.example.kindom.utils.FirebaseHandler;
import com.example.kindom.ui.helpMe.HelpMeUserListingFragment;
import com.example.kindom.R;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.chip.Chip;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class HelpMeUserListingAdapter extends RecyclerView.Adapter<HelpMeUserListingAdapter.HelpMePostViewHolder> {

    private final DatabaseReference mUserPostsRef = FirebaseDatabase.getInstance().getReference("helpMe");
    private final Context mContext;
    private final HelpMeUserListingFragment mFragment;
    private final ArrayList<HelpMePost> mPostList;
    private final LayoutInflater mInflater;

    static class HelpMePostViewHolder extends RecyclerView.ViewHolder {

        public final Chip categoryChip;
        public final TextView titleTextView;
        public final TextView dateTextView;
        public final TextView timeTextView;
        public final MaterialButton editButton;
        public final MaterialButton deleteButton;

        public HelpMePostViewHolder(@NonNull View itemView) {
            super(itemView);
            categoryChip = itemView.findViewById(R.id.list_item_help_me_user_listing_category);
            titleTextView = itemView.findViewById(R.id.list_item_help_me_user_listing_title);
            dateTextView = itemView.findViewById(R.id.list_item_help_me_user_listing_date);
            timeTextView = itemView.findViewById(R.id.list_item_help_me_user_listing_time);
            editButton = itemView.findViewById(R.id.list_item_help_me_user_listing_edit);
            deleteButton = itemView.findViewById(R.id.list_item_help_me_user_listing_delete);
        }
    }

    public HelpMeUserListingAdapter(Context context, HelpMeUserListingFragment fragment, ArrayList<HelpMePost> postList) {
        mContext = context;
        mFragment = fragment;
        mInflater = LayoutInflater.from(context);
        mPostList = postList;
    }

    @NonNull
    @Override
    public HelpMeUserListingAdapter.HelpMePostViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = mInflater.inflate(R.layout.list_item_help_me_user_listing, parent, false);
        return new HelpMePostViewHolder(itemView);
    }

    @Override
    @SuppressLint("SimpleDateFormat")
    public void onBindViewHolder(@NonNull HelpMeUserListingAdapter.HelpMePostViewHolder holder, int position) {
        final HelpMePost currPost = mPostList.get(position);

        // TODO: Mark expired posts and allow auto-renew

        holder.categoryChip.setText(currPost.getCategory());
        holder.titleTextView.setText(currPost.getTitle());
        holder.dateTextView.setText(CalendarHandler.getSimplifiedDateString(currPost.getDate()));
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
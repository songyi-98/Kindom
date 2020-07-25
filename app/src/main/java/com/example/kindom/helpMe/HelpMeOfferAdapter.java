package com.example.kindom.helpMe;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.kindom.HomeActivity;
import com.example.kindom.R;
import com.example.kindom.User;
import com.example.kindom.chat.ChatHandler;
import com.example.kindom.utils.FirebaseHandler;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.button.MaterialButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class HelpMeOfferAdapter extends RecyclerView.Adapter<HelpMeOfferAdapter.HelpMeOfferViewHolder> {

    private final Context mContext;
    private final Activity mActivity;
    private final HelpMePost mPost;
    private final List<String> mUsersOfferingHelp;
    private final LayoutInflater mInflater;

    static class HelpMeOfferViewHolder extends RecyclerView.ViewHolder {

        public final CircleImageView userPicture;
        public final TextView userName;
        public final MaterialButton userChat;
        public final MaterialButton userDecline;
        public final MaterialButton userAccept;

        public HelpMeOfferViewHolder(@NonNull View itemView) {
            super(itemView);
            userPicture = itemView.findViewById(R.id.list_item_help_me_offer_user_picture);
            userName = itemView.findViewById(R.id.list_item_help_me_offer_user_name);
            userChat = itemView.findViewById(R.id.list_item_help_me_offer_chat);
            userDecline = itemView.findViewById(R.id.list_item_help_me_offer_decline);
            userAccept = itemView.findViewById(R.id.list_item_help_me_offer_accept);
        }
    }

    public HelpMeOfferAdapter(Context context, Activity activity, HelpMePost post, List<String> usersOfferingHelp) {
        mContext = context;
        mActivity = activity;
        mInflater = LayoutInflater.from(context);
        mPost = post;
        mUsersOfferingHelp = usersOfferingHelp;
    }

    @NonNull
    @Override
    public HelpMeOfferAdapter.HelpMeOfferViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = mInflater.inflate(R.layout.list_item_help_me_offer, parent, false);
        return new HelpMeOfferViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull final HelpMeOfferAdapter.HelpMeOfferViewHolder holder, final int position) {
        final String userUid = mUsersOfferingHelp.get(position);

        FirebaseStorage.getInstance().getReference("profileImages").child(userUid).getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                Glide.with(mActivity)
                        .load(uri)
                        .into(holder.userPicture);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                // Load placeholder profile image
                Glide.with(mActivity)
                        .load(mActivity.getDrawable(R.drawable.blank_profile_picture))
                        .into(holder.userPicture);
            }
        });

        FirebaseDatabase.getInstance().getReference("users").child(userUid).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                final User user = snapshot.getValue(User.class);
                assert user != null;
                final String otherUserName = user.getName();
                holder.userName.setText(otherUserName);
                holder.userChat.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        new ChatHandler(FirebaseHandler.getCurrentUserUid(), userUid, otherUserName)
                                .createChat(v);
                    }
                });
                holder.userDecline.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        new AlertDialog.Builder(mContext)
                                .setMessage(R.string.help_me_offer_decline)
                                .setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        mUsersOfferingHelp.remove(userUid);
                                        List<String> newUsersOfferingHelp = new ArrayList<>();
                                        newUsersOfferingHelp.add("list");
                                        newUsersOfferingHelp.addAll(mUsersOfferingHelp);
                                        DatabaseReference uploadRef = FirebaseDatabase.getInstance()
                                                .getReference("helpMe")
                                                .child(user.getRc())
                                                .child(mPost.getUserUid())
                                                .child(String.valueOf(mPost.getTimeCreated()));
                                        uploadRef.child("usersOfferingHelp").setValue(newUsersOfferingHelp);
                                        refresh();
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
                holder.userAccept.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        new AlertDialog.Builder(mContext)
                                .setMessage(R.string.help_me_offer_accept)
                                .setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        FirebaseDatabase.getInstance()
                                                .getReference("helpMe")
                                                .child(user.getRc())
                                                .child(mPost.getUserUid())
                                                .child(String.valueOf(mPost.getTimeCreated()))
                                                .removeValue();
                                        refresh();
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
            public void onCancelled(@NonNull DatabaseError error) {
                // Do nothing
            }
        });
    }

    @Override
    public int getItemCount() {
        return mUsersOfferingHelp.size();
    }

    private void refresh() {
        mActivity.finish();
        Intent intent = new Intent(mContext, HomeActivity.class);
        mActivity.startActivity(intent);
    }
}
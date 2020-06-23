package com.example.kindom.ui.helpMe;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.kindom.R;
import com.example.kindom.helpMe.HelpMeAllListingAdapter;
import com.example.kindom.helpMe.HelpMePost;
import com.example.kindom.utils.FirebaseHandler;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Objects;

public class HelpMeAllListingFragment extends Fragment {

    // TODO: Allow refresh of posts

    private ArrayList<HelpMePost> mHelpMePosts = new ArrayList<>();
    private DatabaseReference mAllPostsRef;

    public HelpMeAllListingFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Initialize Firebase Database
        mAllPostsRef = FirebaseDatabase.getInstance().getReference().child("helpMe");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.activity_help_me_all_listing_fragment, container, false);
    }

    @Override
    public void onStart() {
        super.onStart();

        // Add all users' (except current user) posts to list
        mHelpMePosts.clear();

        // TODO: Filter only nearby posts

        mAllPostsRef.orderByChild("timeCreated").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot userSnapshot: dataSnapshot.getChildren()) {
                    if (!Objects.equals(userSnapshot.getKey(), FirebaseHandler.getCurrentUserUid())) {
                        for (DataSnapshot postSnapshot: userSnapshot.getChildren()) {
                            HelpMePost post = postSnapshot.getValue(HelpMePost.class);
                            mHelpMePosts.add(post);
                        }
                    }
                }
                Collections.reverse(mHelpMePosts);
                show();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Do nothing
            }
        });
    }

    /**
     * Show the list of all users' (except current user) posts
     */
    private void show() {
        // Get a handle to the RecyclerView
        RecyclerView mRecyclerView = Objects.requireNonNull(getActivity()).findViewById(R.id.help_me_all_listing_recycler_view);

        // Create an adapter and supply the data to be displayed
        HelpMeAllListingAdapter mAdapter = new HelpMeAllListingAdapter(getContext(), mHelpMePosts);

        // Connect the adapter with the RecyclerView
        mRecyclerView.setAdapter(mAdapter);

        // Give the RecyclerView a default layout manager
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
    }
}
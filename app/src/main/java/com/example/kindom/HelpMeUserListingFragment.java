package com.example.kindom;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Objects;

public class HelpMeUserListingFragment extends Fragment {

    private ArrayList<HelpMePost> mHelpMePosts = new ArrayList<>();
    private DatabaseReference userPostsRef;

    public HelpMeUserListingFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Initialize Firebase Database
        userPostsRef = FirebaseDatabase.getInstance().getReference().child("helpMe").child(FirebaseHandler.getUserUid());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.activity_help_me_user_listing_fragment, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Set click listener for FAB
        FloatingActionButton fab = Objects.requireNonNull(getActivity()).findViewById(R.id.help_me_add_fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), HelpMePostAddActivity.class);
                startActivity(intent);
            }
        });
    }

    @Override
    public void onStart() {
        super.onStart();

        // Add all user's posts to list
        mHelpMePosts.clear();
        userPostsRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                    HelpMePost post = postSnapshot.getValue(HelpMePost.class);
                    mHelpMePosts.add(post);
                }
                show();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Do nothing
            }
        });
    }

    /**
     * Show the list of user's posts
     */
    private void show() {
        // Get a handle to the RecyclerView.
        RecyclerView mRecyclerView = Objects.requireNonNull(getActivity()).findViewById(R.id.help_me_user_listing_recycler_view);

        // Create an adapter and supply the data to be displayed.
        HelpMeUserListingAdapter mAdapter = new HelpMeUserListingAdapter(getContext(), mHelpMePosts);

        // Connect the adapter with the RecyclerView.
        mRecyclerView.setAdapter(mAdapter);

        // Give the RecyclerView a default layout manager.
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
    }
}
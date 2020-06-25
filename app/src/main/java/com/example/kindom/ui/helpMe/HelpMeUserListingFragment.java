package com.example.kindom.ui.helpMe;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.kindom.R;
import com.example.kindom.helpMe.HelpMePost;
import com.example.kindom.helpMe.HelpMePostAddActivity;
import com.example.kindom.helpMe.HelpMeUserListingAdapter;
import com.example.kindom.utils.FirebaseHandler;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class HelpMeUserListingFragment extends Fragment {

    private View mView;
    private ArrayList<HelpMePost> mHelpMePosts = new ArrayList<>();
    private DatabaseReference mUserPostsRef;

    public HelpMeUserListingFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Initialize Firebase Database
        mUserPostsRef = FirebaseDatabase.getInstance().getReference().child("helpMe").child(FirebaseHandler.getCurrentUserUid());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        mView = inflater.inflate(R.layout.activity_help_me_user_listing_fragment, container, false);
        return mView;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mHelpMePosts.clear();

        // Set click listener for FAB
        FloatingActionButton fab = view.findViewById(R.id.help_me_add_fab);
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

        // Add user's posts to list
        mHelpMePosts.clear();
        mUserPostsRef.orderByChild("timeCreated").addListenerForSingleValueEvent(new ValueEventListener() {
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
     * Refresh fragment
     */
    public void refresh() {
        assert getFragmentManager() != null;
        FragmentTransaction ft = getFragmentManager().beginTransaction();
        ft.detach(this).attach(this).commit();
    }

    /**
     * Show the list of user's posts
     */
    private void show() {
        // Get a handle to the RecyclerView
        RecyclerView mRecyclerView = mView.findViewById(R.id.help_me_user_listing_recycler_view);

        // Create an adapter and supply the data to be displayed
        HelpMeUserListingAdapter mAdapter = new HelpMeUserListingAdapter(getContext(), this, mHelpMePosts);

        // Connect the adapter with the RecyclerView
        mRecyclerView.setAdapter(mAdapter);

        // Give the RecyclerView a default layout manager
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
    }
}
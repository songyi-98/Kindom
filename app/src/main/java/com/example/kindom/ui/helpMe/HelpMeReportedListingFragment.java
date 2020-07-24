package com.example.kindom.ui.helpMe;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.example.kindom.R;
import com.example.kindom.User;
import com.example.kindom.helpMe.HelpMeAllListingAdapter;
import com.example.kindom.helpMe.HelpMePost;
import com.example.kindom.helpMe.HelpMeReportedListingAdapter;
import com.example.kindom.utils.FirebaseHandler;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Objects;

public class HelpMeReportedListingFragment extends Fragment {

    private View mView;
    private SwipeRefreshLayout mSwipeRefreshLayout;
    private final ArrayList<HelpMePost> mHelpMePosts = new ArrayList<>();
    private DatabaseReference mUserRef;
    private DatabaseReference mReportedPostsRef;

    public HelpMeReportedListingFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Initialize Firebase Database
        mUserRef = FirebaseDatabase.getInstance().getReference("users").child(FirebaseHandler.getCurrentUserUid());
        mReportedPostsRef = FirebaseDatabase.getInstance().getReference("helpMe");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        mView = inflater.inflate(R.layout.activity_help_me_reported_listing_fragment, container, false);

        // Initialize SwipeRefreshLayout
        mSwipeRefreshLayout = mView.findViewById(R.id.help_me_reported_listing_swipe_container);
        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                mSwipeRefreshLayout.setRefreshing(true);
                getPosts();
            }
        });

        return mView;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mHelpMePosts.clear();
    }

    @Override
    public void onStart() {
        super.onStart();

        getPosts();
    }

    /**
     * Get HelpMePosts from Firebase Database
     */
    private void getPosts() {
        mHelpMePosts.clear();

        // Add nearby users' (except current user) posts to list
        mUserRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                User user = dataSnapshot.getValue(User.class);
                assert user != null;
                String rc = user.getRc();

                // Retrieve only posts from the same RC as the querying user
                mReportedPostsRef.child(rc).orderByChild("timeCreated").addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        for (DataSnapshot userSnapshot : dataSnapshot.getChildren()) {
                            if (!Objects.equals(userSnapshot.getKey(), FirebaseHandler.getCurrentUserUid())) {
                                for (DataSnapshot postSnapshot : userSnapshot.getChildren()) {
                                    HelpMePost post = postSnapshot.getValue(HelpMePost.class);
                                    assert post != null;
                                    if (post.isReported()) {
                                        mHelpMePosts.add(post);
                                    }
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
     * Show the list of all users' (except current user) posts
     */
    private void show() {
        // Get a handle to the RecyclerView
        RecyclerView mRecyclerView = mView.findViewById(R.id.help_me_reported_listing_recycler_view);

        if (getContext() != null) {
            // Hide progress indicator
            mView.findViewById(R.id.help_me_reported_listing_progress_circular).setVisibility(View.GONE);

            // Create an adapter and supply the data to be displayed
            HelpMeReportedListingAdapter mAdapter = new HelpMeReportedListingAdapter(getContext(), this, mHelpMePosts);

            // Connect the adapter with the RecyclerView
            mRecyclerView.setAdapter(mAdapter);

            // Give the RecyclerView a default layout manager
            mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

            mSwipeRefreshLayout.setRefreshing(false);

            if (mAdapter.getItemCount() == 0) {
                mView.findViewById(R.id.help_me_reported_listing_empty).setVisibility(View.VISIBLE);
            }

            Toast.makeText(getContext(), R.string.help_me_all_listing_refresh_toast, Toast.LENGTH_SHORT).show();
        }
    }
}
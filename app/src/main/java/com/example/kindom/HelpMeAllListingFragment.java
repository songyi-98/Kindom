package com.example.kindom;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.Objects;

public class HelpMeAllListingFragment extends Fragment {

    private ArrayList<HelpMePost> mHelpMePosts = new ArrayList<>();

    public HelpMeAllListingFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.activity_help_me_all_listing_fragment, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        for (int i = 0; i < 5; i++) {
            mHelpMePosts.add(new HelpMePost("Food", "Buy desserts", "Aaron", "Blk 123", "Today", "10:00 AM", "Test"));
            mHelpMePosts.add(new HelpMePost("Care", "Pick up child from school", "Patricia", "Blk 999", "Tomorrow", "12:00 PM", "Test"));
        }

        // Get a handle to the RecyclerView.
        RecyclerView mRecyclerView = Objects.requireNonNull(getActivity()).findViewById(R.id.help_me_all_listing_recycler_view);

        // Create an adapter and supply the data to be displayed.
        HelpMeAllListingAdapter mAdapter = new HelpMeAllListingAdapter(getContext(), mHelpMePosts);

        // Connect the adapter with the RecyclerView.
        mRecyclerView.setAdapter(mAdapter);

        // Give the RecyclerView a default layout manager.
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
    }
}
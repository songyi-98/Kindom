package com.example.kindom;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;

public class HelpMeFragment extends Fragment {

    private ArrayList<HelpMePost> mHelpMePosts = new ArrayList<>();
    private RecyclerView mRecyclerView;
    private HelpMePostAdapter mAdapter;

    public HelpMeFragment() {
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
        return inflater.inflate(R.layout.fragment_help_me, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        for (int i = 0; i < 5; i++) {
            mHelpMePosts.add(new HelpMePost("Food", "Buy desserts", "Aaron", "Blk 123", "Today" , "10:00 AM", "Test"));
            mHelpMePosts.add(new HelpMePost("Care", "Pick up child from school", "Patricia","Blk 999", "Tomorrow", "12:00 PM", "Test"));
        }

        // Get a handle to the RecyclerView.
        mRecyclerView = getActivity().findViewById(R.id.help_me_recycler_view);

        // Create an adapter and supply the data to be displayed.
        mAdapter = new HelpMePostAdapter(getContext(), mHelpMePosts);

        // Connect the adapter with the RecyclerView.
        mRecyclerView.setAdapter(mAdapter);

        // Give the RecyclerView a default layout manager.
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        // Set click listener for FAB
        FloatingActionButton fab = getActivity().findViewById(R.id.help_me_fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), HelpMePostAddActivity.class);
                startActivity(intent);
            }
        });
    }
}
package com.example.kindom;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

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
            mHelpMePosts.add(new HelpMePost(null, "Food", "Buy desserts", "Blk 123", "10 am", "Aaron"));
            mHelpMePosts.add(new HelpMePost(null, "Care", "Pick up child from school", "Blk 999", "12 pm", "Patricia"));
        }

        // Get a handle to the RecyclerView.
        mRecyclerView = getActivity().findViewById(R.id.help_me_recycler_view);

        // Create an adapter and supply the data to be displayed.
        mAdapter = new HelpMePostAdapter(getContext(), mHelpMePosts);

        // Connect the adapter with the RecyclerView.
        mRecyclerView.setAdapter(mAdapter);

        // Give the RecyclerView a default layout manager.
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
    }
}
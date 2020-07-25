package com.example.kindom.helpMe;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

import com.example.kindom.R;

import java.util.List;

public class HelpMeOfferActivity extends AppCompatActivity {

    private HelpMePost mPost;
    private List<String> mUsersOfferingHelp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_help_me_offer);

        // Set toolbar
        Toolbar toolbar = findViewById(R.id.toolbar_main);
        setSupportActionBar(toolbar);

        // Get a support ActionBar corresponding to this toolbar
        ActionBar ab = getSupportActionBar();

        // Enable the Up button
        assert ab != null;
        ab.setDisplayHomeAsUpEnabled(true);

        // Receive intent
        if (getIntent().getExtras() != null) {
            mPost = (HelpMePost) getIntent().getSerializableExtra("Post");
            assert mPost != null;
            mUsersOfferingHelp = mPost.getUsersOfferingHelp().subList(1, mPost.getUsersOfferingHelp().size());
        }

        initializeRecyclerView();
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    private void initializeRecyclerView() {
        // Get a handle to the RecyclerView
        RecyclerView recyclerView = findViewById(R.id.help_me_offer_recycler_view);

        // Create an adapter and supply the data to be displayed
        HelpMeOfferAdapter adapter = new HelpMeOfferAdapter(this, this, mPost, mUsersOfferingHelp);

        // Connect the adapter with the RecyclerView
        recyclerView.setAdapter(adapter);

        // Give the RecyclerView a default layout manager
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
    }
}
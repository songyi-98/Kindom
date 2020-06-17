package com.example.kindom;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.material.chip.Chip;

public class HelpMePostDetailsActivity extends AppCompatActivity {

    HelpMePost mPost;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_help_me_post_details);

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
        }

        // Set the texts for the post
        setText();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater =getMenuInflater();
        inflater.inflate(R.menu.help_me_post_details_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_report_listing:
                // TODO: Implement report listing feature
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    private void setText() {
        ImageView imageView = findViewById(R.id.help_me_post_image);
        Chip categoryChip = findViewById(R.id.help_me_post_category);
        TextView titleTextView = findViewById(R.id.help_me_post_title);
        TextView locationTextView = findViewById(R.id.help_me_post_location);
        TextView dateTextView = findViewById(R.id.help_me_post_date);
        TextView timeTextView = findViewById(R.id.help_me_post_time);
        TextView userTextView = findViewById(R.id.help_me_post_user);
        TextView descriptionTextView = findViewById(R.id.help_me_post_description);

        //imageView.setImageURI(mPost.getImage());
        titleTextView.setText(mPost.getTitle());
        categoryChip.setText(mPost.getCategory());
        locationTextView.setText(mPost.getLocation());
        dateTextView.setText(mPost.getDate());
        timeTextView.setText(mPost.getTime());
        userTextView.setText(mPost.getUser());
        descriptionTextView.setText(mPost.getDescription());
    }
}
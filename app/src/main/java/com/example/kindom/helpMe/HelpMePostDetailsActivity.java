package com.example.kindom.helpMe;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.example.kindom.R;
import com.example.kindom.User;
import com.example.kindom.chat.ChatHandler;
import com.example.kindom.utils.FirebaseHandler;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.chip.Chip;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class HelpMePostDetailsActivity extends AppCompatActivity {

    private HelpMePost mPost;
    private DatabaseReference mUserRef;

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

        // Initialize Firebase Database
        mUserRef = FirebaseDatabase.getInstance().getReference("users").child(FirebaseHandler.getCurrentUserUid());

        // Set the texts for the post
        setText();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.help_me_post_details_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.menu_report_listing) {
            mPost.setReported(true);
            DatabaseReference uploadRef = FirebaseDatabase
                    .getInstance()
                    .getReference("helpMe")
                    .child(mPost.getRc())
                    .child(mPost.getUserUid())
                    .child(String.valueOf(mPost.getTimeCreated()));
            uploadRef.child("reported").setValue(true);
            Toast.makeText(this, R.string.help_me_post_reported, Toast.LENGTH_SHORT).show();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    private void setText() {
        Chip categoryChip = findViewById(R.id.help_me_post_category);
        TextView titleTextView = findViewById(R.id.help_me_post_title);
        TextView blkNoTextView = findViewById(R.id.help_me_post_blk_no);
        TextView dateTextView = findViewById(R.id.help_me_post_date);
        TextView timeTextView = findViewById(R.id.help_me_post_time);
        TextView userTextView = findViewById(R.id.help_me_post_user);
        TextView descriptionTextView = findViewById(R.id.help_me_post_description);
        MaterialButton offerHelpButton = findViewById(R.id.help_me_post_offer_help_button);
        MaterialButton chatButton = findViewById(R.id.help_me_post_chat_button);

        categoryChip.setText(mPost.getCategory());
        titleTextView.setText(mPost.getTitle());
        blkNoTextView.setText(mPost.getBlkNo());
        dateTextView.setText(mPost.getDate());
        timeTextView.setText(mPost.getTime());
        userTextView.setText(mPost.getUser());
        descriptionTextView.setText(mPost.getDescription());
        offerHelpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mUserRef.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        User user = snapshot.getValue(User.class);
                        assert user != null;
                        String rc = user.getRc();
                        ArrayList<String> usersOfferingHelp = mPost.getUsersOfferingHelp();
                        if (!usersOfferingHelp.contains(user.getUid())) {
                            usersOfferingHelp.add(user.getUid());
                            DatabaseReference uploadRef = FirebaseDatabase.getInstance()
                                    .getReference("helpMe")
                                    .child(rc)
                                    .child(mPost.getUserUid())
                                    .child(String.valueOf(mPost.getTimeCreated()));
                            uploadRef.child("usersOfferingHelp").setValue(usersOfferingHelp);
                            Toast.makeText(HelpMePostDetailsActivity.this, R.string.help_me_post_offer_received, Toast.LENGTH_SHORT).show();

                        } else {
                            Toast.makeText(HelpMePostDetailsActivity.this, R.string.help_me_post_offer_received_already, Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        // Do nothing
                    }
                });
            }
        });
        chatButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                new ChatHandler(FirebaseHandler.getCurrentUserUid(), mPost.getUserUid(), mPost.getUser())
                        .createChat(v);
            }
        });
    }
}
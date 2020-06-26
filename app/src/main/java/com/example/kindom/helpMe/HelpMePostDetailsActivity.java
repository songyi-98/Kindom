package com.example.kindom.helpMe;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.renderscript.Sampler;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.kindom.R;
import com.example.kindom.chat.ChatActivity;
import com.example.kindom.chat.ChatFragment;
import com.example.kindom.utils.FirebasePushIdGenerator;
import com.google.android.material.chip.Chip;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

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
        MenuInflater inflater = getMenuInflater();
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
        Chip categoryChip = findViewById(R.id.help_me_post_category);
        TextView titleTextView = findViewById(R.id.help_me_post_title);
        TextView blkNoTextView = findViewById(R.id.help_me_post_blk_no);
        TextView dateTextView = findViewById(R.id.help_me_post_date);
        TextView timeTextView = findViewById(R.id.help_me_post_time);
        TextView userTextView = findViewById(R.id.help_me_post_user);
        TextView descriptionTextView = findViewById(R.id.help_me_post_description);
        Button chatButton = findViewById(R.id.help_me_post_chat_button);

        categoryChip.setText(mPost.getCategory());
        titleTextView.setText(mPost.getTitle());
        blkNoTextView.setText(mPost.getBlkNo());
        dateTextView.setText(mPost.getDate());
        timeTextView.setText(mPost.getTime());
        userTextView.setText(mPost.getUser());
        descriptionTextView.setText(mPost.getDescription());
        chatButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                final Intent intent = new Intent(v.getContext(), ChatActivity.class);

                final ArrayList<String> possibleKey = new ArrayList<>();

                final DatabaseReference postUserDb = FirebaseDatabase.getInstance().getReference().child("users").child(mPost.getUserUid()).child("chatListKeys");
                final DatabaseReference currentUserDb = FirebaseDatabase.getInstance().getReference().child("users").child(FirebaseAuth.getInstance().getUid()).child("chatListKeys");
                //checks if there is a current chat that already exists between the two users
                currentUserDb.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        if (dataSnapshot.exists()) {
                            Map<String, Object> newMap = (Map<String, Object>) dataSnapshot.getValue();
                            if (newMap.containsValue(mPost.getUser())) {
                                String correctKey = null;
                                for (String key : newMap.keySet()) {
                                    if (newMap.get(key).equals(mPost.getUser())) {
                                        correctKey = key;
                                    }
                                }
                                if (correctKey != null) {
                                    possibleKey.add(correctKey);
                                }
                            }
                        }

                        Map<String, Object> newMapCurrentUser = new HashMap<>();
                        Map<String, Object> newMapPostUser = new HashMap<>();
                        String chatId = "";
                        if (possibleKey.isEmpty()) {
                            //no chat exists yet between the two, create a new chat
                            chatId = FirebasePushIdGenerator.generatePushId();
                            newMapCurrentUser.put(chatId, mPost.getUser());
                            newMapPostUser.put(chatId, FirebaseAuth.getInstance().getCurrentUser().getDisplayName());
                            currentUserDb.updateChildren(newMapCurrentUser);
                            postUserDb.updateChildren(newMapPostUser);
                        } else {
                            for (int k = 0; k < possibleKey.size(); k++) {
                                if (dataSnapshot.child(possibleKey.get(k)).getValue().toString().equals(mPost.getUser())) {
                                    chatId = possibleKey.get(k);
                                    break;
                                }
                            }
                        }

                        //starts chat activity for user

                        Bundle bundle = new Bundle();
                        bundle.putString("ChatId", chatId);
                        bundle.putString("ChatUser", mPost.getUser());
                        intent.putExtras(bundle);
                        v.getContext().startActivity(intent);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
            }
        });
    }
}
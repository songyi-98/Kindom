package com.example.kindom.chat;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.kindom.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import de.hdodenhof.circleimageview.CircleImageView;

public class ChatActivity extends AppCompatActivity {

    private RecyclerView.Adapter mChatAdapter, mMediaAdapter;
    private RecyclerView.LayoutManager mChatLayoutManager;

    ArrayList<MessageObject> messageList;

    private String mChatID;
    private String mChatUserUid;
    private String mChatUser;

    DatabaseReference mChatDb;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        // Receive intent from chat fragment or from help me post listing
        Intent intent = getIntent();
        if (intent != null) {
            mChatID = Objects.requireNonNull(intent.getExtras()).getString("CHAT_ID");
            mChatUserUid = intent.getExtras().getString("CHAT_USER_UID");
            mChatUser = intent.getExtras().getString("CHAT_USER");
            mChatDb = FirebaseDatabase.getInstance().getReference("chat").child(mChatID);
        }

        // Set the layout for chat activity
        TextView mChatTitle = findViewById(R.id.chatTitle);
        mChatTitle.setText(mChatUser);
        Button mSend = findViewById(R.id.sendBtn);
        mSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendMessage();
            }
        });
        Button mAddMedia = findViewById(R.id.addMediaBtn);
        mAddMedia.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openGallery();
            }
        });

        getProfilePicture();
        getChatMessages();
        initializeMessages();
        initializeMedia();
    }

    /**
     * Retrieve user's profile picture from Firebase Storage
     */
    private void getProfilePicture() {
        final CircleImageView profilePicture = findViewById(R.id.image_chat_profile);
        StorageReference storageRef = FirebaseStorage.getInstance().getReference("profileImages");
        storageRef.child(mChatUserUid).getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                Glide.with(ChatActivity.this)
                        .load(uri)
                        .into(profilePicture);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                // Load placeholder profile image
                Glide.with(ChatActivity.this)
                        .load(getDrawable(R.drawable.blank_profile_picture))
                        .into(profilePicture);

                // Create toast to inform user
                Toast toast = Toast.makeText(ChatActivity.this, R.string.error_profile_image_load, Toast.LENGTH_LONG);
                toast.show();
            }
        });
    }

    /**
     * Retrieve messages from database
     */
    private void getChatMessages() {
        mChatDb.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                if (dataSnapshot.exists()) {
                    // messages have either media or text
                    String text = "", sender = "";
                    String timestamp = "";
                    ArrayList<String> mediaUrlList = new ArrayList<>();
                    if (dataSnapshot.child("text").getValue() != null) {
                        text = Objects.requireNonNull(dataSnapshot.child("text").getValue()).toString();
                    }
                    if (dataSnapshot.child("sender").getValue() != null) {
                        sender = Objects.requireNonNull(dataSnapshot.child("sender").getValue()).toString();
                    }
                    if (dataSnapshot.child("media").getValue() != null) {
                        for (DataSnapshot mediaSnapshot : dataSnapshot.child("media").getChildren()) {
                            mediaUrlList.add(Objects.requireNonNull(mediaSnapshot.getValue()).toString());
                        }
                    }
                    if (dataSnapshot.child("timestamp").getValue() != null) {
                        timestamp = Objects.requireNonNull(dataSnapshot.child("timestamp").getValue()).toString();
                    }
                    MessageObject mMessage = new MessageObject(dataSnapshot.getKey(), sender, text, timestamp, mediaUrlList);
                    messageList.add(mMessage);
                    mChatLayoutManager.scrollToPosition(messageList.size() - 1);
                    mChatAdapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                // Do nothing
            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {
                // Do nothing
            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                // Do nothing
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Do nothing
            }
        });
    }

    // Send Message, can also send media
    // Variables only used in sendMessage
    ArrayList<String> mediaIdList = new ArrayList<>();
    int totalMediaUploaded = 0;
    EditText mMessage;

    private void sendMessage() {
        mMessage = findViewById(R.id.message);
        String messageId = mChatDb.push().getKey();
        assert messageId != null;
        final DatabaseReference newMessageDb = mChatDb.child(messageId);

        final Map newMessageMap = new HashMap<>();

        if (!mMessage.getText().toString().isEmpty()) {
            while (mMessage.getText().toString().endsWith("\n")) {
                mMessage.getText().delete(mMessage.getText().length() - 1, mMessage.getText().length());
            }
            newMessageMap.put("text", mMessage.getText().toString());
        }
        newMessageMap.put("sender", FirebaseAuth.getInstance().getUid());
        newMessageMap.put("timestamp", Long.toString(System.currentTimeMillis()));

        // Sending media in this
        if (!mediaUriList.isEmpty()) {
            for (String mediaUri : mediaUriList) {
                String mediaId = newMessageDb.child("media").push().getKey();
                mediaIdList.add(mediaId);
                final StorageReference filePath = FirebaseStorage.getInstance().getReference().child("chat").child(mChatID).child(messageId).child("mediaId");
                UploadTask uploadTask = filePath.putFile(Uri.parse(mediaUri));
                uploadTask.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        filePath.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                            @Override
                            public void onSuccess(Uri uri) {
                                newMessageMap.put("/media/" + mediaIdList.get(totalMediaUploaded) + "/", uri.toString());

                                totalMediaUploaded++;
                                if (totalMediaUploaded == mediaUriList.size()) {
                                    updateDatabaseWithNewMessage(newMessageDb, newMessageMap);

                                }

                            }
                        });
                    }
                });
            }
        } else {
            if (!mMessage.getText().toString().isEmpty()) {
                updateDatabaseWithNewMessage(newMessageDb, newMessageMap);
            }
        }
    }

    /**
     * Complete send message function and resets after updating database
     *
     * @param newMessageDb  message database
     * @param newMessageMap message map object
     */
    private void updateDatabaseWithNewMessage(DatabaseReference newMessageDb, Map newMessageMap) {
        newMessageDb.updateChildren(newMessageMap);
        mMessage.setText(null);
        mediaUriList.clear();
        mediaIdList.clear();
        mMediaAdapter.notifyDataSetChanged();
        mChatAdapter.notifyDataSetChanged();
    }

    /**
     * Initialize messages objects with recycler view
     */
    private void initializeMessages() {
        messageList = new ArrayList<>();
        RecyclerView mChat = findViewById(R.id.messageList);
        mChat.setNestedScrollingEnabled(false);
        mChat.setHasFixedSize(false);
        //this line below could cause problems
        mChatLayoutManager = new LinearLayoutManager(getApplicationContext(), RecyclerView.VERTICAL, false);
        mChat.setLayoutManager(mChatLayoutManager);
        mChatAdapter = new MessageAdapter(messageList);
        mChat.setAdapter(mChatAdapter);
    }

    int PICK_IMAGE_INTENT = 1;
    ArrayList<String> mediaUriList = new ArrayList<>();

    /**
     * Initialize media recycler view when selecting multiple media
     */
    private void initializeMedia() {
        mediaUriList = new ArrayList<>();
        RecyclerView mMedia = findViewById(R.id.mediaList);
        mMedia.setNestedScrollingEnabled(false);
        mMedia.setHasFixedSize(false);
        //this line below could cause problems
        RecyclerView.LayoutManager mMediaLayoutManager = new LinearLayoutManager(getApplicationContext(), RecyclerView.HORIZONTAL, false);
        mMedia.setLayoutManager(mMediaLayoutManager);
        mMediaAdapter = new MediaAdapter(getApplicationContext(), mediaUriList);
        mMedia.setAdapter(mMediaAdapter);
    }

    /**
     * Access devices' gallery to retrieve images
     */
    private void openGallery() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Picture(s)"), PICK_IMAGE_INTENT);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == PICK_IMAGE_INTENT) {
                assert data != null;
                if (data.getClipData() == null) {
                    mediaUriList.add(Objects.requireNonNull(data.getData()).toString());
                } else {
                    for (int i = 0; i < data.getClipData().getItemCount(); i++) {
                        mediaUriList.add(data.getClipData().getItemAt(i).getUri().toString());
                    }
                }
                mMediaAdapter.notifyDataSetChanged();
            }
        }
    }
}
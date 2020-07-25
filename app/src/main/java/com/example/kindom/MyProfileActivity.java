package com.example.kindom;

import android.net.Uri;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.bumptech.glide.Glide;
import com.example.kindom.utils.FirebaseHandler;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.chip.Chip;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import de.hdodenhof.circleimageview.CircleImageView;

public class MyProfileActivity extends AppCompatActivity {

    private StorageReference mStorageRef;
    private DatabaseReference mUserDatabase;
    private CircleImageView mProfileImage;
    private TextView mNameField;
    private Chip mUserGroupChip;
    private TextView mEmailField;
    private TextView mPostalCodeField;
    private TextView mRcField;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_profile);

        // Set toolbar
        Toolbar toolbar = findViewById(R.id.toolbar_main);
        setSupportActionBar(toolbar);

        // Get a support ActionBar corresponding to this toolbar
        ActionBar ab = getSupportActionBar();

        // Enable the Up button
        assert ab != null;
        ab.setDisplayHomeAsUpEnabled(true);

        // Initialize Firebase components
        mStorageRef = FirebaseStorage.getInstance().getReference("profileImages");
        mUserDatabase = FirebaseDatabase.getInstance().getReference("users");
        mUserDatabase.keepSynced(true);

        // Initialize layout variables
        mProfileImage = findViewById(R.id.my_profile_image);
        mNameField = findViewById(R.id.my_profile_name);
        mUserGroupChip = findViewById(R.id.my_profile_user_group);
        mEmailField = findViewById(R.id.my_profile_email);
        mPostalCodeField = findViewById(R.id.my_profile_postal_code);
        mRcField = findViewById(R.id.my_profile_rc);

        populateFields();
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    /**
     * Populate the fields with the user's information
     */
    private void populateFields() {
        String uid = FirebaseHandler.getCurrentUserUid();

        // Retrieve user's profile image from Firebase Storage
        mStorageRef.child(uid).getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                Glide.with(MyProfileActivity.this)
                        .load(uri)
                        .into(mProfileImage);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                // Load placeholder profile image
                Glide.with(MyProfileActivity.this)
                        .load(getDrawable(R.drawable.blank_profile_picture))
                        .into(mProfileImage);

                // Create toast to inform user
                Toast toast = Toast.makeText(MyProfileActivity.this, R.string.error_profile_image_load, Toast.LENGTH_LONG);
                toast.show();
            }
        });

        // Retrieve user's data from Firebase Database
        mUserDatabase.child(FirebaseHandler.getCurrentUserUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                User user = dataSnapshot.getValue(User.class);
                assert user != null;
                mNameField.setText(user.getName());
                mUserGroupChip.setText(user.getUserGroup());
                mEmailField.setText(user.getEmail());
                mPostalCodeField.setText(String.valueOf(user.getPostalCode()));
                mRcField.setText(user.getRc());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Do nothing
            }
        });
    }
}
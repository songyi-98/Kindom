package com.example.kindom;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.net.Uri;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.chip.Chip;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.Objects;

import de.hdodenhof.circleimageview.CircleImageView;

public class MyProfileActivity extends AppCompatActivity {

    private FirebaseUser mUser;
    private StorageReference mStorageRef;
    private DatabaseReference mUserDatabase;
    private CircleImageView mProfileImage;
    private TextView mNameField;
    private Chip mUserGroupChip;
    private TextView mEmailField;
    private TextView mPostalCodeField;

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
        mUser = FirebaseAuth.getInstance().getCurrentUser();
        mStorageRef = FirebaseStorage.getInstance().getReference("profileImages");
        mUserDatabase = FirebaseDatabase.getInstance().getReference("users");

        // Initialize layout variables
        mProfileImage = findViewById(R.id.my_profile_image);
        mNameField = findViewById(R.id.my_profile_name);
        mUserGroupChip = findViewById(R.id.my_profile_user_group);
        mEmailField = findViewById(R.id.my_profile_email);
        mPostalCodeField = findViewById(R.id.my_profile_postal_code);

        // TODO: Allow user to edit profile and password

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
        String uid = mUser.getUid();

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
        mUserDatabase.orderByChild("uid").equalTo(uid).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot user : dataSnapshot.getChildren()) {
                    String name = Objects.requireNonNull(user.child("name").getValue()).toString();
                    String userGroup = Objects.requireNonNull(user.child("userGroup").getValue()).toString();
                    String email = Objects.requireNonNull(user.child("email").getValue()).toString();
                    String postalCode = Objects.requireNonNull(user.child("postalCode").getValue()).toString();
                    mNameField.setText(name);
                    mUserGroupChip.setText(userGroup);
                    mEmailField.setText(email);
                    mPostalCodeField.setText(postalCode);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Do nothing
            }
        });
    }
}
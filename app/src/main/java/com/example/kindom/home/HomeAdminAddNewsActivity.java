package com.example.kindom.home;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.bumptech.glide.Glide;
import com.example.kindom.HomeActivity;
import com.example.kindom.R;
import com.example.kindom.User;
import com.example.kindom.utils.Alert;
import com.example.kindom.utils.FirebaseHandler;
import com.example.kindom.utils.FirebasePushIdGenerator;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.button.MaterialButtonToggleGroup;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

public class HomeAdminAddNewsActivity extends AppCompatActivity {

    private DatabaseReference mUserDatabase;
    private DatabaseReference mNewsDatabase;
    private StorageReference mStorageRef;
    private ImageView mNewsImage;
    private Uri mNewsImageUri;
    private MaterialButtonToggleGroup mNewsType;
    private MaterialButton mAddNewsButton;
    private boolean isValidImage = false;
    private boolean isValidType = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_admin_add_news);

        // Set toolbar
        Toolbar toolbar = findViewById(R.id.toolbar_main);
        setSupportActionBar(toolbar);

        // Get a support ActionBar corresponding to this toolbar
        ActionBar ab = getSupportActionBar();

        // Enable the Up button
        assert ab != null;
        ab.setDisplayHomeAsUpEnabled(true);

        // Initialize layout variables
        mNewsImage = findViewById(R.id.home_admin_add_news_image);
        mNewsType = findViewById(R.id.home_admin_news_type_toggle);
        mAddNewsButton = findViewById(R.id.home_admin_add_news_button);

        // Initialize Firebase components
        mUserDatabase = FirebaseDatabase.getInstance().getReference("users");
        mNewsDatabase = FirebaseDatabase.getInstance().getReference("news");
        mStorageRef = FirebaseStorage.getInstance().getReference("news");

        setImageClickListener();
        validateInput();
        setAddClickListener();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();

        // Bring user to home page
        Intent intent = new Intent(this, HomeActivity.class);
        finish();
        startActivity(intent);
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    /**
     * Set click listener for adding an image
     */
    private void setImageClickListener() {
        mNewsImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CropImage.activity()
                        .setAspectRatio(16, 9)
                        .setGuidelines(CropImageView.Guidelines.ON)
                        .start(HomeAdminAddNewsActivity.this);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if (resultCode == RESULT_OK) {
                assert result != null;
                mNewsImageUri = result.getUri();
                isValidImage = true;
                findViewById(R.id.home_admin_add_news_image_icon).setVisibility(View.GONE);
                Glide.with(this)
                        .load(mNewsImageUri)
                        .into(mNewsImage);
            }
        }
    }

    /**
     * Validate the selection of the types of news
     */
    private void validateInput() {
        mNewsType.addOnButtonCheckedListener(new MaterialButtonToggleGroup.OnButtonCheckedListener() {
            @Override
            public void onButtonChecked(MaterialButtonToggleGroup group, int checkedId, boolean isChecked) {
                isValidType = isChecked;
            }
        });
    }

    /**
     * Set click listener for add news button
     */
    private void setAddClickListener() {
        mAddNewsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!isValidImage) {
                    Alert.showAlertDialog(HomeAdminAddNewsActivity.this, getString(R.string.error_news_image_empty));
                } else if (!isValidType) {
                    Alert.showAlertDialog(HomeAdminAddNewsActivity.this, getString(R.string.error_news_type));
                } else {
                    int checkedId = mNewsType.getCheckedButtonId();
                    if (checkedId == R.id.home_admin_news_type_neighbourhood) {
                        // Retrieve admin's RC
                        mUserDatabase.child(FirebaseHandler.getCurrentUserUid()).addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                User user = snapshot.getValue(User.class);
                                assert user != null;
                                final String rc = user.getRc();
                                final String id = FirebasePushIdGenerator.generatePushId();

                                // Upload image on Firebase Storage
                                UploadTask uploadTask = mStorageRef.child("neighbourhoodNews").child(rc).child(id).putFile(mNewsImageUri);
                                uploadTask.addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        Alert.showAlertDialog(HomeAdminAddNewsActivity.this, getString(R.string.error_news_image_upload));
                                    }
                                }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                                    @Override
                                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                        // Update id on Firebase Database
                                        mNewsDatabase.child("neighbourhoodNews").child(rc).child(id).setValue("");
                                    }
                                });
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {
                                // Do nothing
                            }
                        });
                    } else {
                        final String id = FirebasePushIdGenerator.generatePushId();

                        // Upload image on Firebase Storage
                        UploadTask uploadTask = mStorageRef.child("singaporeNews").child(id).putFile(mNewsImageUri);
                        uploadTask.addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Alert.showAlertDialog(HomeAdminAddNewsActivity.this, getString(R.string.error_news_image_upload));
                            }
                        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                            @Override
                            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                // Update id on Firebase Database
                                mNewsDatabase.child("singaporeNews").child(id).setValue("");
                            }
                        });
                    }

                    // Bring user to home page
                    Intent intent = new Intent(HomeAdminAddNewsActivity.this, HomeActivity.class);
                    finish();
                    startActivity(intent);
                    Toast.makeText(HomeAdminAddNewsActivity.this, "News added successfully", Toast.LENGTH_LONG).show();
                }
            }
        });
    }
}
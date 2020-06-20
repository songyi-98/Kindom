package com.example.kindom;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.Objects;

public class RegisterAccountActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private DatabaseReference mUserDatabase;
    private StorageReference mStorageRef;
    private String mProfileImage;
    private String mName;
    private int mPostalCode;
    private String mUserGroup;
    private TextInputLayout mEmailField;
    private TextInputLayout mPasswordField;
    private TextInputLayout mConfirmPasswordField;
    private MaterialButton mSignUpButton;
    private boolean isValidEmail = false;
    private boolean isValidPassword = false;
    private boolean isValidConfirmPassword = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_account);

        // Retrieve data from intent
        Intent intent = getIntent();
        if (intent != null) {
            mProfileImage = intent.getStringExtra(RegisterProfileActivity.USER_PROFILE_IMAGE_TAG);
            mName = intent.getStringExtra(RegisterProfileActivity.USER_NAME_TAG);
            mPostalCode = intent.getIntExtra(RegisterProfileActivity.USER_POSTAL_CODE_TAG, 0);
            mUserGroup = intent.getStringExtra(RegisterProfileActivity.USER_GROUP_TAG);
        }

        // Initialize Firebase components
        mAuth = FirebaseAuth.getInstance();
        mUserDatabase = FirebaseDatabase.getInstance().getReference().child("users");
        mStorageRef = FirebaseStorage.getInstance().getReference().child("profileImages");

        // Initialize layout variables
        mEmailField = findViewById(R.id.edit_email_register);
        mPasswordField = findViewById(R.id.edit_password_register);
        mConfirmPasswordField = findViewById(R.id.edit_confirm_password);
        mSignUpButton = findViewById(R.id.sign_up_button);

        validateInputs();
        setSignUpListener();
    }

    @Override
    protected void onStop() {
        super.onStop();
        finishAffinity();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(RegisterAccountActivity.this, RegisterProfileActivity.class);
        startActivity(intent);
    }

    /**
     * Check email and password validity
     */
    private void validateInputs() {
        Objects.requireNonNull(mEmailField.getEditText()).addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                // Do nothing
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // Do nothing
            }

            @Override
            public void afterTextChanged(Editable s) {
                if (!Validation.isValidEmail(s)) {
                    isValidEmail = false;
                    mEmailField.setError(getString(R.string.error_email));
                } else {
                    // TODO: Check if email has been registered before
                    isValidEmail = true;
                    mEmailField.setError(null);
                }
            }
        });

        Objects.requireNonNull(mPasswordField.getEditText()).addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                // Do nothing
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // Do nothing
            }

            @Override
            public void afterTextChanged(Editable s) {
                if (!Validation.isValidPassword(s)) {
                    isValidPassword = false;
                    mPasswordField.setError(getString(R.string.error_password_requirements));
                } else {
                    isValidPassword = true;
                    mPasswordField.setError(null);
                }
            }
        });

        Objects.requireNonNull(mConfirmPasswordField.getEditText()).addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                // Do nothing
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // Do nothing
            }

            @Override
            public void afterTextChanged(Editable s) {
                String password = mPasswordField.getEditText().getText().toString();
                if (!Validation.isNonEmpty(password)) {
                    isValidConfirmPassword = false;
                    mConfirmPasswordField.setError(getString(R.string.error_password_empty));
                } else if (!password.contentEquals(s)) {
                    isValidConfirmPassword = false;
                    mConfirmPasswordField.setError(getString(R.string.error_password_match));
                } else {
                    isValidConfirmPassword = true;
                    mConfirmPasswordField.setError(null);
                }
            }
        });
    }

    /**
     * Set click listener for sign up button
     */
    private void setSignUpListener() {
        mSignUpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!isValidEmail || !isValidPassword) {
                    Alert.showAlertDialog(RegisterAccountActivity.this, getString(R.string.error_sign_in));
                } else if (!isValidConfirmPassword) {
                    Alert.showAlertDialog(RegisterAccountActivity.this, getString(R.string.error_confirm_password));
                } else {
                    String email = Objects.requireNonNull(mEmailField.getEditText()).getText().toString();
                    String password = Objects.requireNonNull(mPasswordField.getEditText()).getText().toString();
                    createAccount(email, password);
                }
            }
        });
    }

    /**
     * Create an account with Firebase Authentication
     *
     * @param email    email of the user
     * @param password password of the user
     */
    private void createAccount(final String email, String password) {
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign up success
                            FirebaseUser user = FirebaseHandler.getUser();
                            String uid = FirebaseHandler.getUserUid();
                            UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                                    .setDisplayName(mName)
                                    .setPhotoUri(Uri.parse(mProfileImage))
                                    .build();
                            user.updateProfile(profileUpdates);

                            // Add profile image to storage
                            StorageReference uploadRef = mStorageRef.child(uid);
                            UploadTask uploadTask = uploadRef.putFile(Uri.parse(mProfileImage));
                            uploadTask.addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Alert.showAlertDialog(RegisterAccountActivity.this, getString(R.string.error_profile_image_upload));
                                }
                            }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                                @Override
                                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                    // Do nothing
                                }
                            });

                            // Add user to database
                            User addUser = new User(mName, mUserGroup, mPostalCode, email);
                            mUserDatabase.child(uid).setValue(addUser);

                            // Bring user to Home page
                            Intent intent = new Intent(RegisterAccountActivity.this, HomeActivity.class);
                            startActivity(intent);
                        } else {
                            // Sign up fail
                            Alert.showAlertDialog(RegisterAccountActivity.this, getString(R.string.error_sign_in));
                        }
                    }
                });
    }
}
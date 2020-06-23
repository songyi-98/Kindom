package com.example.kindom;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.loader.app.LoaderManager;
import androidx.loader.content.Loader;

import com.bumptech.glide.Glide;
import com.example.kindom.utils.AddressLoader;
import com.example.kindom.utils.Alert;
import com.example.kindom.utils.Validation;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.button.MaterialButtonToggleGroup;
import com.google.android.material.textfield.TextInputLayout;

import java.util.Objects;

import de.hdodenhof.circleimageview.CircleImageView;

public class RegisterProfileActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Integer> {

    public static final int PICK_IMAGE = 1;
    public static final String USER_PROFILE_IMAGE_TAG = "USER_PROFILE_IMAGE";
    public static final String USER_NAME_TAG = "USER_NAME";
    public static final String USER_POSTAL_CODE_TAG = "USER_POSTAL_CODE";
    public static final String USER_BLK_NO_TAG = "USER_BLK_NO";
    public static final String USER_GROUP_TAG = "USER_GROUP";

    private CircleImageView mProfileImage;
    private Uri mProfileImageUri;
    private TextInputLayout mNameField;
    private TextInputLayout mPostalCodeField;
    private Integer mBlkNo = -1;
    private MaterialButtonToggleGroup mUserGroup;
    private MaterialButton mNextButton;
    private boolean isValidProfileImage = false;
    private boolean isValidName = false;
    private boolean isValidPostalCode = false;
    private boolean isCheckedUserGroup = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_profile);

        // Initialize layout variables
        mProfileImage = findViewById(R.id.user_image);
        mNameField = findViewById(R.id.edit_name);
        mPostalCodeField = findViewById(R.id.edit_postal_code);
        mUserGroup = findViewById(R.id.user_group_toggle);
        mNextButton = findViewById(R.id.next_button);

        // Initialize empty profile image
        Glide.with(this)
                .load(getDrawable(R.drawable.blank_profile_picture))
                .into(mProfileImage);

        setProfileImageClickListener();
        validateInputs();
        setNextClickListener();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(RegisterProfileActivity.this, MainActivity.class);
        startActivity(intent);
    }

    /**
     * Set click listener for profile image
     */
    private void setProfileImageClickListener() {
        mProfileImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                if (intent.resolveActivity(getPackageManager()) != null) {
                    startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE);
                }
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE && resultCode == RESULT_OK) {
            assert data != null;
            mProfileImageUri = data.getData();
            isValidProfileImage = true;
            Glide.with(this)
                    .load(mProfileImageUri)
                    .into(mProfileImage);
        }
    }

    @NonNull
    @Override
    public Loader<Integer> onCreateLoader(int id, @Nullable Bundle args) {
        return new AddressLoader(this, Integer.parseInt(Objects.requireNonNull(mPostalCodeField.getEditText()).getText().toString()));
    }

    @Override
    public void onLoadFinished(@NonNull Loader<Integer> loader, Integer data) {
        mBlkNo = data;
    }

    @Override
    public void onLoaderReset(@NonNull Loader<Integer> loader) {
        mBlkNo = -1;
        isValidPostalCode = false;
    }

    /**
     * Validate he inputs of name, postal code and user group
     */
    private void validateInputs() {
        Objects.requireNonNull(mNameField.getEditText()).addTextChangedListener(new TextWatcher() {
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
                if (!Validation.isNonEmpty(s)) {
                    isValidName = false;
                    mNameField.setError(getString(R.string.error_name));
                } else {
                    isValidName = true;
                    mNameField.setError(null);
                }
            }
        });

        Objects.requireNonNull(mPostalCodeField.getEditText()).addTextChangedListener(new TextWatcher() {
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
                int len = s.toString().length();
                if (!Validation.isNonEmpty(s)) {
                    isValidPostalCode = false;
                    mPostalCodeField.setError(getString(R.string.error_postal_code_empty));
                } else if (len != 6) {
                    isValidPostalCode = false;
                    mPostalCodeField.setError(getString(R.string.error_postal_code_length));
                } else {
                    // Start loader to query postal code received from the intent
                    getSupportLoaderManager().initLoader(0, null, RegisterProfileActivity.this);

                    mPostalCodeField.setError(null);
                }
            }
        });

        mUserGroup.addOnButtonCheckedListener(new MaterialButtonToggleGroup.OnButtonCheckedListener() {
            @Override
            public void onButtonChecked(MaterialButtonToggleGroup group, int checkedId, boolean isChecked) {
                isCheckedUserGroup = isChecked;
            }
        });
    }

    /**
     * Set click listener for next button
     */
    private void setNextClickListener() {
        mNextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Check postal code validity
                if (mBlkNo == -1) {
                    Alert.showAlertDialog(RegisterProfileActivity.this, getString(R.string.error_postal_code_processing));
                }
                if (mBlkNo != null) {
                    isValidPostalCode = true;
                }

                if (!isValidProfileImage) {
                    Alert.showAlertDialog(RegisterProfileActivity.this, getString(R.string.error_profile_image));
                } else if (!isValidName) {
                    Alert.showAlertDialog(RegisterProfileActivity.this, getString(R.string.error_name));
                } else if (!isValidPostalCode) {
                    Alert.showAlertDialog(RegisterProfileActivity.this, getString(R.string.error_postal_code));
                } else if (!isCheckedUserGroup) {
                    Alert.showAlertDialog(RegisterProfileActivity.this, getString(R.string.error_user_group));
                } else {
                    Intent intent = new Intent(RegisterProfileActivity.this, RegisterAccountActivity.class);
                    intent.putExtra(USER_PROFILE_IMAGE_TAG, mProfileImageUri.toString());
                    intent.putExtra(USER_NAME_TAG, Objects.requireNonNull(mNameField.getEditText()).getText().toString());
                    intent.putExtra(USER_POSTAL_CODE_TAG, Integer.parseInt(Objects.requireNonNull(mPostalCodeField.getEditText()).getText().toString()));
                    intent.putExtra(USER_BLK_NO_TAG, mBlkNo);
                    int checkedId = mUserGroup.getCheckedButtonId();
                    if (checkedId == R.id.user_group_admin) {
                        intent.putExtra(USER_GROUP_TAG, User.USER_GROUP_ADMIN);
                    } else {
                        intent.putExtra(USER_GROUP_TAG, User.USER_GROUP_USER);
                    }
                    startActivity(intent);
                }
            }
        });
    }
}
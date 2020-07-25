package com.example.kindom;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;

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
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Objects;

import de.hdodenhof.circleimageview.CircleImageView;

public class RegisterProfileActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<String> {
    
    public static final String USER_PROFILE_IMAGE_TAG = "USER_PROFILE_IMAGE";
    public static final String USER_NAME_TAG = "USER_NAME";
    public static final String USER_POSTAL_CODE_TAG = "USER_POSTAL_CODE";
    public static final String USER_RC_TAG = "USER_RC";
    public static final String USER_BLK_NO_TAG = "USER_BLK_NO";
    public static final String USER_GROUP_TAG = "USER_GROUP";
    private static final ArrayList<String> RC = new ArrayList<>();

    private CircleImageView mProfileImage;
    private Uri mProfileImageUri;
    private TextInputLayout mNameField;
    private TextInputLayout mPostalCodeField;
    private TextInputLayout mRcField;
    private String mBlkNo = "";
    private MaterialButtonToggleGroup mUserGroup;
    private MaterialButton mNextButton;
    private boolean isValidProfileImage = false;
    private boolean isValidName = false;
    private boolean isValidPostalCode = false;
    private boolean isValidRc = false;
    private boolean isCheckedUserGroup = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_profile);

        // Initialize layout variables
        mProfileImage = findViewById(R.id.user_image);
        mNameField = findViewById(R.id.edit_name);
        mPostalCodeField = findViewById(R.id.edit_postal_code);
        mRcField = findViewById(R.id.edit_rc);
        mUserGroup = findViewById(R.id.user_group_toggle);
        mNextButton = findViewById(R.id.next_button);

        // Initialize empty profile image
        Glide.with(this)
                .load(getDrawable(R.drawable.blank_profile_picture))
                .into(mProfileImage);

        setProfileImageClickListener();
        validateInputs();
        setNextClickListener();

        // Initialize list of RCs
        InputStream file;
        try {
            file = getAssets().open("list_rc.xls");
            HSSFWorkbook workbook = new HSSFWorkbook(file);
            HSSFSheet sheet = workbook.getSheetAt(0);
            for (Row row : sheet) {
                Cell cell = row.getCell(0, Row.MissingCellPolicy.RETURN_BLANK_AS_NULL);
                if (cell == null) {
                    break;
                } else {
                    RC.add(cell.getStringCellValue());
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        // Initialize dropdown menu for RC
        ArrayAdapter<String> categoryAdapter = new ArrayAdapter<>(this, R.layout.list_item_dropdown_menu, RC);
        AutoCompleteTextView categoryTextView = findViewById(R.id.edit_rc_dropdown_menu);
        categoryTextView.setAdapter(categoryAdapter);
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
                CropImage.activity()
                        .setCropShape(CropImageView.CropShape.OVAL)
                        .setFixAspectRatio(true)
                        .setGuidelines(CropImageView.Guidelines.ON)
                        .start(RegisterProfileActivity.this);
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
                mProfileImageUri = result.getUri();
                isValidProfileImage = true;
                Glide.with(this)
                        .load(mProfileImageUri)
                        .into(mProfileImage);
            }
        }
    }

    @NonNull
    @Override
    public Loader<String> onCreateLoader(int id, @Nullable Bundle args) {
        return new AddressLoader(this, Integer.parseInt(Objects.requireNonNull(mPostalCodeField.getEditText()).getText().toString()));
    }

    @Override
    public void onLoadFinished(@NonNull Loader<String> loader, String data) {
        mBlkNo = data;
    }

    @Override
    public void onLoaderReset(@NonNull Loader<String> loader) {
        mBlkNo = "";
        isValidPostalCode = false;
    }

    /**
     * Validate the inputs of name, postal code, RC and user group
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

        Objects.requireNonNull(mRcField.getEditText()).addTextChangedListener(new TextWatcher() {
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
                if (!RC.contains(s.toString())) {
                    isValidRc = false;
                    mRcField.setError(getString(R.string.error_rc));
                } else {
                    isValidRc = true;
                    mRcField.setError(null);
                }
            }
        });

        mUserGroup.addOnButtonCheckedListener(new MaterialButtonToggleGroup.OnButtonCheckedListener() {
            @Override
            public void onButtonChecked(MaterialButtonToggleGroup group, int checkedId, boolean isChecked) {
                isCheckedUserGroup = mUserGroup.getCheckedButtonId() == R.id.user_group_admin ||
                        mUserGroup.getCheckedButtonId() == R.id.user_group_user;
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
                if (mBlkNo.equals("") || mBlkNo.equals("invalid")) {
                    Alert.showAlertDialog(RegisterProfileActivity.this, getString(R.string.error_postal_code));
                } else {
                    isValidPostalCode = true;
                }

                if (!isValidProfileImage) {
                    Alert.showAlertDialog(RegisterProfileActivity.this, getString(R.string.error_profile_image));
                } else if (!isValidName) {
                    Alert.showAlertDialog(RegisterProfileActivity.this, getString(R.string.error_name));
                } else if (!isValidPostalCode) {
                    Alert.showAlertDialog(RegisterProfileActivity.this, getString(R.string.error_postal_code));
                } else if (!isValidRc) {
                    Alert.showAlertDialog(RegisterProfileActivity.this, getString(R.string.error_rc));
                } else if (!isCheckedUserGroup) {
                    Alert.showAlertDialog(RegisterProfileActivity.this, getString(R.string.error_user_group));
                } else {
                    Intent intent = new Intent(RegisterProfileActivity.this, RegisterAccountActivity.class);
                    intent.putExtra(USER_PROFILE_IMAGE_TAG, mProfileImageUri.toString());
                    intent.putExtra(USER_NAME_TAG, Objects.requireNonNull(mNameField.getEditText()).getText().toString());
                    intent.putExtra(USER_POSTAL_CODE_TAG, Integer.parseInt(Objects.requireNonNull(mPostalCodeField.getEditText()).getText().toString()));
                    intent.putExtra(USER_RC_TAG, Objects.requireNonNull(mRcField.getEditText()).getText().toString());
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
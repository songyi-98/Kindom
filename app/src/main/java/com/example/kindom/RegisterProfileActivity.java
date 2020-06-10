package com.example.kindom;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.button.MaterialButtonToggleGroup;
import com.google.android.material.textfield.TextInputLayout;

public class RegisterProfileActivity extends AppCompatActivity {

    private TextInputLayout mNameField;
    private TextInputLayout mPostalCodeField;
    private MaterialButtonToggleGroup mUserGroup;
    private boolean isValidName = false;
    private boolean isValidPostalCode = false;
    private boolean isCheckedUserGroup = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_profile);

        mNameField = findViewById(R.id.edit_name);
        mPostalCodeField = findViewById(R.id.edit_postal_code);
        mUserGroup = findViewById(R.id.user_group_toggle);

        // Check name and postal code validity
        mNameField.getEditText().addTextChangedListener(new TextWatcher() {
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
        mPostalCodeField.getEditText().addTextChangedListener(new TextWatcher() {
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
                // TODO: Check postal code validity
                int len = s.toString().length();
                if (!Validation.isNonEmpty(s)) {
                    isValidPostalCode = false;
                    mPostalCodeField.setError(getString(R.string.error_postal_code_empty));
                } else if (len != 6) {
                    isValidPostalCode = false;
                    mPostalCodeField.setError(getString(R.string.error_postal_code_length));
                } else {
                    isValidPostalCode = true;
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

        Button nextButton = findViewById(R.id.next_button);
        nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            if (!isCheckedUserGroup) {
                showAlertDialog(getString(R.string.error_user_group));
            } else if (isValidName && isValidPostalCode) {
                    Intent intent = new Intent(RegisterProfileActivity.this, RegisterAccountActivity.class);
                    intent.putExtra(
                            "USER_NAME",
                            mNameField.getEditText().getText().toString());
                    intent.putExtra(
                            "USER_POSTAL_CODE",
                            Integer.parseInt(mPostalCodeField.getEditText().getText().toString()));
                    int checkedId = mUserGroup.getCheckedButtonId();
                    if (checkedId == R.id.user_group_admin) {
                        intent.putExtra("USER_GROUP", User.USER_GROUP_ADMIN);
                    } else {
                        intent.putExtra("USER_GROUP", User.USER_GROUP_USER);
                    }
                    startActivity(intent);
                } else {
                    showAlertDialog(getString(R.string.error_profile));
                }
            }
        });
    }

    /**
     * Show alert dialog
     * @param message the message to be displayed in the dialog
     */
    private void showAlertDialog(String message) {
        new AlertDialog.Builder(this)
                .setMessage(message)
                .setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // User clicked OK button
                    }
                })
                .show();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(RegisterProfileActivity.this, MainActivity.class);
        startActivity(intent);
    }
}
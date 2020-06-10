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

import com.google.android.material.textfield.TextInputLayout;

public class RegisterProfileActivity extends AppCompatActivity {

    private TextInputLayout mNameField;
    private TextInputLayout mPostalCodeField;
    private boolean isValidName;
    private boolean isValidPostalCode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_profile);

        mNameField = findViewById(R.id.edit_name);
        mPostalCodeField = findViewById(R.id.edit_postal_code);

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
                if (!Validation.isNonEmpty(s)) {
                    isValidPostalCode = false;
                    mPostalCodeField.setError(getString(R.string.error_postal_code));
                } else {
                    isValidPostalCode = true;
                    mPostalCodeField.setError(null);
                }
            }
        });

        Button nextButton = findViewById(R.id.next_button);
        nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isValidName && isValidPostalCode) {
                    Intent intent = new Intent(RegisterProfileActivity.this, RegisterAccountActivity.class);
                    startActivity(intent);
                } else {
                    showAlertDialog();
                }
            }
        });
    }

    /**
     * Shows alert dialog
     */
    private void showAlertDialog() {
        new AlertDialog.Builder(this)
                .setMessage(R.string.error_profile)
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
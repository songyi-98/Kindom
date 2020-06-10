package com.example.kindom;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class RegisterAccountActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private TextInputLayout mEmailField;
    private TextInputLayout mPasswordField;
    private TextInputLayout mConfirmPasswordField;
    private boolean isValidEmail = false;
    private boolean isValidPassword = false;
    private boolean isValidConfirmPassword = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_account);

        mAuth = FirebaseAuth.getInstance();

        mEmailField = findViewById(R.id.edit_email_register);
        mPasswordField = findViewById(R.id.edit_password_register);
        mConfirmPasswordField = findViewById(R.id.edit_confirm_password);

        // Check email and password validity
        mEmailField.getEditText().addTextChangedListener(new TextWatcher() {
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
                if (Validation.isValidEmail(s)) {
                    isValidEmail = true;
                    mEmailField.setError(null);
                } else {
                    isValidEmail = false;
                    mEmailField.setError(getString(R.string.error_invalid_email));
                }
            }
        });
        mPasswordField.getEditText().addTextChangedListener(new TextWatcher() {
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
                if (Validation.isValidPassword(s)) {
                    isValidPassword = true;
                    mPasswordField.setError(null);
                } else {
                    isValidPassword = false;
                    mPasswordField.setError(getString(R.string.error_password_requirements));
                }
            }
        });
        mConfirmPasswordField.getEditText().addTextChangedListener(new TextWatcher() {
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
                    mConfirmPasswordField.setError("Please enter a password in the above box first");
                } else if (!password.contentEquals(s)) {
                    isValidConfirmPassword = false;
                    mConfirmPasswordField.setError("Password does not match above");
                } else {
                    isValidConfirmPassword = true;
                    mConfirmPasswordField.setError(null);
                }
            }
        });

        // Set click listener for sign up button
        Button signUpButton = findViewById(R.id.sign_up_button);
        signUpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isValidEmail && isValidPassword && isValidConfirmPassword) {
                    String email = mEmailField.getEditText().getText().toString();
                    String password = mPasswordField.getEditText().getText().toString();
                    createAccount(email, password);
                } else {
                    showAlertDialog();
                }
            }
        });
    }

    /**
     * Creates an account with Firebase Authentication
     * @param email email of the user
     * @param password password of the user
     */
    private void createAccount(String email, String password) {
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign up success. Bring user to Home page.
                            Intent intent = new Intent(RegisterAccountActivity.this, HomeActivity.class);
                            startActivity(intent);
                        } else {
                            // Sign up fail
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
                .setMessage(R.string.error_sign_in)
                .setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // User clicked OK button
                    }
                })
                .show();
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
}
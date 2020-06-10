package com.example.kindom;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MainActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private TextInputLayout mEmailField;
    private TextInputLayout mPasswordField;
    private boolean isValidEmail = false;
    private boolean isValidPassword = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mAuth = FirebaseAuth.getInstance();
    }

    @Override
    protected void onStart() {
        super.onStart();
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser == null) {
            // User is not signed in yet. Hide progress bar and show sign in layout.
            findViewById(R.id.progress_circular).setVisibility(View.GONE);
            findViewById(R.id.sign_in).setVisibility(View.VISIBLE);

            // Assign text fields and text view
            mEmailField = findViewById(R.id.edit_email);
            mEmailField.setErrorEnabled(true);
            mPasswordField = findViewById(R.id.edit_password);
            mPasswordField.setErrorEnabled(true);
            TextView mRegisterText = findViewById(R.id.register_prompt);

            // Set autofill hints
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                mEmailField.setAutofillHints(View.AUTOFILL_HINT_EMAIL_ADDRESS);
                mPasswordField.setAutofillHints(View.AUTOFILL_HINT_PASSWORD);
            }

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
                    if (Validation.isNonEmpty(s)) {
                        isValidPassword = true;
                        mPasswordField.setError(null);
                    } else {
                        isValidPassword = false;
                        mPasswordField.setError(getString(R.string.error_empty_password));
                    }
                }
            });

            // Set click listener for sign in button
            Button signInButton = findViewById(R.id.sign_in_button);
            signInButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (isValidEmail && isValidPassword) {
                        String email = mEmailField.getEditText().getText().toString();
                        String password = mPasswordField.getEditText().getText().toString();
                        signIn(email, password);
                    } else {
                        showAlertDialog();
                    }
                }
            });

            // Set click listener for register text
            mRegisterText.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // Bring user to Register page
                    Intent intent = new Intent(MainActivity.this, RegisterProfileActivity.class);
                    startActivity(intent);
                }
            });
        } else {
            // User is signed in
            launchHomePage();
        }
    }

    /**
     * Sign in the user with Firebase Authentication
     * @param email email of the user
     * @param password password of the user
     */
    private void signIn(String email, String password) {
        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success
                            launchHomePage();
                        } else {
                            // Sign in fail
                            showAlertDialog();
                        }
                    }
                });
    }

    /**
     * Launch the home page of the app
     */
    private void launchHomePage() {
        Intent intent = new Intent(this, HomeActivity.class);
        startActivity(intent);
    }

    /**
     * Show alert dialog
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
}
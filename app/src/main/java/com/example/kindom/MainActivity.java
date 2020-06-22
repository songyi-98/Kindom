package com.example.kindom;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.kindom.utils.Alert;
import com.example.kindom.utils.FirebaseHandler;
import com.example.kindom.utils.Validation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.Objects;

public class MainActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private TextInputLayout mEmailField;
    private TextInputLayout mPasswordField;
    private MaterialButton mSignInButton;
    private TextView mRegisterText;
    private boolean isConn = false;
    private boolean isValidEmail = false;
    private boolean isValidPassword = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Initialize Firebase Authentication
        mAuth = FirebaseAuth.getInstance();
    }

    @Override
    protected void onStart() {
        super.onStart();

        checkInternetConnection();
        if (isConn) {
            checkSignIn();
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        finishAffinity();
    }

    /**
     * Check if an internet connection is available
     */
    private void checkInternetConnection() {
        ConnectivityManager connMgr = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
        if (networkInfo == null || !networkInfo.isConnected()) {
            Alert.showAlertDialogAndFinish(this, this, getString(R.string.error_internet_connection));
        } else {
            isConn = true;
        }
    }

    /**
     * Check if the user is already signed in
     */
    private void checkSignIn() {
        FirebaseUser currentUser = FirebaseHandler.getCurrentUser();
        if (currentUser == null) {
            // User is not signed in yet. Hide progress bar and show sign in layout.
            findViewById(R.id.progress_circular).setVisibility(View.GONE);
            findViewById(R.id.sign_in).setVisibility(View.VISIBLE);

            // Initialize layout variables
            mEmailField = findViewById(R.id.edit_email);
            mPasswordField = findViewById(R.id.edit_password);
            mSignInButton = findViewById(R.id.sign_in_button);
            mRegisterText = findViewById(R.id.register_prompt);

            // Set autofill hints
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                mEmailField.setAutofillHints(View.AUTOFILL_HINT_EMAIL_ADDRESS);
                mPasswordField.setAutofillHints(View.AUTOFILL_HINT_PASSWORD);
            }

            validateInputs();
            setClickListeners();
        } else {
            // User is signed in
            launchHomePage();
        }
    }

    /**
     * Validate email and password inputs
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
                if (!Validation.isNonEmpty(s)) {
                    isValidPassword = false;
                    mPasswordField.setError(getString(R.string.error_empty_password));
                } else {
                    isValidPassword = true;
                    mPasswordField.setError(null);
                }
            }
        });
    }

    /**
     * Set click listeners for sign in and register
     */
    private void setClickListeners() {
        mSignInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isValidEmail && isValidPassword) {
                    String email = Objects.requireNonNull(mEmailField.getEditText()).getText().toString();
                    String password = Objects.requireNonNull(mPasswordField.getEditText()).getText().toString();
                    signIn(email, password);
                } else {
                    Alert.showAlertDialog(MainActivity.this, getString(R.string.error_sign_in));
                }
            }
        });

        mRegisterText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Bring user to Register page
                Intent intent = new Intent(MainActivity.this, RegisterProfileActivity.class);
                startActivity(intent);
            }
        });
    }

    /**
     * Sign in user with Firebase Authentication
     *
     * @param email    email of the user
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
                            Alert.showAlertDialog(MainActivity.this, getString(R.string.error_sign_in));
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
}
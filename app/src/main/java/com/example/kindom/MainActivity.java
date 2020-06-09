package com.example.kindom;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
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
            mPasswordField = findViewById(R.id.edit_password);
            TextView mRegisterText = findViewById(R.id.register_prompt);

            // Set autofill hints
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                mEmailField.setAutofillHints(View.AUTOFILL_HINT_EMAIL_ADDRESS);
                mPasswordField.setAutofillHints(View.AUTOFILL_HINT_PASSWORD);
            }

            // TODO: Check email validity

            // Set click listener for sign in button
            Button signInButton = findViewById(R.id.sign_in_button);
            signInButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String email = mEmailField.getEditText().getText().toString();
                    String password = mPasswordField.getEditText().getText().toString();
                    signIn(email, password);
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
                            // TODO: Implement if sign in fails
                        }
                    }
                });
    }

    private void launchHomePage() {
        // Bring user to Home page
        Intent intent = new Intent(this, HomeActivity.class);
        startActivity(intent);
    }

    @Override
    protected void onStop() {
        super.onStop();
        finishAffinity();
    }
}
package com.example.kindom;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_account);

        mAuth = FirebaseAuth.getInstance();

        mEmailField = findViewById(R.id.edit_email_register);
        mPasswordField = findViewById(R.id.edit_password_register);

        // TODO: Check email and password validity

        Button signUpButton = findViewById(R.id.sign_up_button);
        signUpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = mEmailField.getEditText().getText().toString();
                String password = mPasswordField.getEditText().getText().toString();
                createAccount(email, password);
            }
        });
    }

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
                            // TODO: Implement if sign up fails
                        }
                    }
                });
    }

    @Override
    protected void onStop() {
        super.onStop();
        finishAffinity();
    }
}
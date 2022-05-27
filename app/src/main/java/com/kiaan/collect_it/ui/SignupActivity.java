package com.kiaan.collect_it.ui;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.kiaan.collect_it.R;

import Model.CURRENT_USER;
import Model.dbHandler;
import Model.User;

public class SignupActivity extends AppCompatActivity {

    private static final String TAG = "EmailPassword";
    // declare java variables
    private EditText etFirstname, etLastname, etEmail, etPassword;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        // initialise mAuth
        mAuth = FirebaseAuth.getInstance();

        // initialise java variables
        etFirstname = findViewById(R.id.editTextFirstname);
        etLastname = findViewById(R.id.editTextLastname);
        etEmail = findViewById(R.id.editTextSignupEmail);
        etPassword = findViewById(R.id.editTextSignupPassword);
        Button btnSignup = findViewById(R.id.buttonSignup);
        TextView tvLogin = findViewById(R.id.textViewLogin);

        // textview click to switch to new view
        tvLogin.setOnClickListener(view -> {

            // instantiate new intent object
            Intent intent = new Intent(SignupActivity.this, LoginActivity.class);
            startActivity(intent);
        });

        // login button click action
        btnSignup.setOnClickListener(view -> {

            // declare variables
            String firstname, lastname, email, password;

            // initialise variables
            firstname = etFirstname.getText().toString();
            lastname = etLastname.getText().toString();
            email = etEmail.getText().toString();
            password = etPassword.getText().toString();


            // method call to create a account
            createAccount(email, password);

            // instantiate a user object
            User u = new User(firstname, lastname, email);

            // initialise the current user
            getUsername();

            // instantiate DbHandler object
            dbHandler db = new dbHandler();

            Toast.makeText(this, CURRENT_USER.displayName, Toast.LENGTH_SHORT).show();

            db.writeToFirebase("User", CURRENT_USER.displayName, u);

            // instantiate new intent object
            Intent intent = new Intent(SignupActivity.this, NavigationActivity.class);
            startActivity(intent);
        });
    }

    // method to create an account for the user to use for the app
    private void createAccount(String email, String password) {
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, task -> {
                    if (task.isSuccessful()) {

                        // Sign in success, update UI with the signed-in user's information
                        Log.d(TAG, "createUserWithEmail:success");
                        getUsername();

                    } else {

                        // if sign in fails, display a message to the user.
                        Log.w(TAG, "createUserWithEmail:failure", task.getException());
                        Toast.makeText(SignupActivity.this, "Authentication failed.",
                                Toast.LENGTH_SHORT).show();
                        reload();
                    }
                });
    }

    // reload the activity
    private void reload() {
        finish();
        startActivity(getIntent());
    }

    private void getUsername()
    {
        // get substring of @ and use as username for user

        String s = etEmail.getText().toString();
        String[] split = s.split("@");
        CURRENT_USER.displayName = split[0].toLowerCase();
        CURRENT_USER.email = etEmail.getText().toString().toLowerCase();
    }
}

/*
Code Attribution
Author : Firebase - Google Developers
Subject: Authenticate with Firebase using Password-Based Accounts on Android
Code available : https://firebase.google.com/docs/auth/android/password-auth?hl=en&authuser=1
Date accessed : [22/05/2022]"
 */
package com.kiaan.collect_it.ui;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.kiaan.collect_it.R;

import Model.CURRENT_USER;

public class LoginActivity extends AppCompatActivity {


    private static final String TAG = "EmailPassword";
    // declare java variables
    private EditText etEmail, etPassword;
    private Button btnLogin;
    private TextView tvSignup;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // initialise mAuth
        mAuth = FirebaseAuth.getInstance();

        // initialise java variables
        etEmail = findViewById(R.id.editTextEmail);
        etPassword = findViewById(R.id.editTextPassword);
        btnLogin = findViewById(R.id.buttonLogin);
        tvSignup = findViewById(R.id.textViewSignUp);

        // textview click to switch to new view
        tvSignup.setOnClickListener(view -> {

            // instantiate new intent object
            Intent intent = new Intent(LoginActivity.this, SignupActivity.class);
            startActivity(intent);
        });

        // login button click action
        btnLogin.setOnClickListener(view -> {

            // declare variables
            String email, password;

            // initialise variables
            email = etEmail.getText().toString().trim();
            password = etPassword.getText().toString().trim();

            // method call to sign into an existing  account
            signIn(email, password);

        });
    }

    // method to sign the user into the app
    private void signIn(String email, String password) {
        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "signInWithEmail:success");
                            FirebaseUser user = mAuth.getCurrentUser();

                            // instantiate new intent to navigate to new view
                            Intent intent = new Intent(LoginActivity.this, NavigationActivity.class);
                            startActivity(intent);

                            // check if the user object is populated
                            if (user != null) {

                                // initialise current user properties
                                CURRENT_USER.email = user.getEmail();
                                CURRENT_USER.displayName = user.getDisplayName();
                            }

                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "signInWithEmail:failure", task.getException());
                            Toast.makeText(LoginActivity.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                            reload();
                        }
                    }
                });
    }

    // reload the activity
    private void reload() {
        finish();
        startActivity(getIntent());
    }
}



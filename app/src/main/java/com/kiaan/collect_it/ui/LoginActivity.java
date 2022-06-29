package com.kiaan.collect_it.ui;

import static Model.Global.userID;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.kiaan.collect_it.R;
import com.kiaan.collect_it.ResetPasswordActivity;

import java.util.Locale;

import Model.CURRENT_USER;
import Model.User;

public class LoginActivity extends AppCompatActivity {

    private static final String TAG = "EmailPassword";

    // declare java variables
    private EditText etEmail, etPassword;
    private Button btnLogin;
    private TextView tvSignup, tvForgottenPassword;
    private FirebaseAuth mAuth;
    private GoogleSignInClient mGoogleSignInClient;
    private final static int RC_SIGN_IN = 123;
    private TextInputLayout inputLayoutemail, inputLayoutpassword;
    private DatabaseReference mDatabase;

    //Google Sign-In
    @Override
    protected void onStart() {
        super.onStart();
        FirebaseUser user = mAuth.getCurrentUser();
        if(user!=null){
            Intent intent = new Intent(getApplicationContext(),NavigationActivity.class);
            startActivity(intent);
            getUsername();
        }

    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        //Force dark mode off
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);

        // initialise mAuth
        mAuth = FirebaseAuth.getInstance();

        //Google Sign-In
        createRequest();

        findViewById(R.id.google_signIn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                signIn();
            }
        });

        // initialise java variables
        etEmail = findViewById(R.id.editTextEmail);
        etPassword = findViewById(R.id.editTextPassword);
        btnLogin = findViewById(R.id.buttonLogin);
        tvSignup = findViewById(R.id.textViewSignUp);
        tvForgottenPassword = findViewById(R.id.textViewForgottenPassword);
        inputLayoutemail = findViewById(R.id.textInputLayout);
        inputLayoutpassword = findViewById(R.id.textInputLayout2);

        // textview click to switch to new view
        tvSignup.setOnClickListener(view -> {

            // instantiate new intent object
            Intent intent = new Intent(LoginActivity.this, SignupActivity.class);
            startActivity(intent);
        });

        tvForgottenPassword.setOnClickListener(view -> {

            // instantiate new intent object
            Intent intent = new Intent(LoginActivity.this, ResetPasswordActivity.class);
            startActivity(intent);
        });

        // login button click action
        btnLogin.setOnClickListener(view -> {

            if(!validateEmail() | !validatePassword())
            {
                return;
            }

            // declare variables
            String email, password;

            // initialise variables
            email = etEmail.getText().toString().trim();
            password = etPassword.getText().toString().trim();

            // method call to sign into an existing  account
            signIn(email, password);

            //ActivityCompat.requestPermissions(this,new String [] {Manifest.permission.CAMERA, Manifest.permission.READ_EXTERNAL_STORAGE},101);
            //askCamPermission();

        });
    }

    private void askCamPermission() {
        if(ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA)!= PackageManager.PERMISSION_GRANTED){


            ActivityCompat.requestPermissions(this,new String [] {Manifest.permission.CAMERA, Manifest.permission.READ_EXTERNAL_STORAGE},101);
        }else{
            CURRENT_USER.permissions = true;
        }
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
                                getUsername();
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

    //Google sign-in

    private void createRequest() {

        // Configure Google Sign In
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();

        // Build a GoogleSignInClient with the options specified by gso.
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);

    }


    private void signIn() {
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);


        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                // Google Sign In was successful, authenticate with Firebase
                GoogleSignInAccount account = task.getResult(ApiException.class);
                firebaseAuthWithGoogle(account);
            } catch (ApiException e) {
                // Google Sign In failed, update UI appropriately
                // ...
                Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        }
    }


    private void firebaseAuthWithGoogle(GoogleSignInAccount acct) {
        Log.d(TAG, "firebaseAuthWithGoogle:" + acct.getId());
        AuthCredential credential = GoogleAuthProvider.getCredential(acct.getIdToken(), null);
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                            if (user != null) {
                                //Email address
                                String email = user.getEmail();
                                getGoogleUsername(email);
                                Toast.makeText(LoginActivity.this, CURRENT_USER.displayName , Toast.LENGTH_SHORT).show();
                            }

                            Log.d("User Id", user.getUid());

                            //CURRENT_USER.email=mAuth.getCurrentUser().getEmail();
                            Intent intent = new Intent(getApplicationContext(),NavigationActivity.class);
                            startActivity(intent);


                        } else {
                            Toast.makeText(LoginActivity.this, "Authentication Failed", Toast.LENGTH_SHORT).show();


                        }


                        // ...
                    }
                });
    }

    // reload the activity
    private void reload() {
        finish();
        startActivity(getIntent());
    }

    private void getUsername() {
        // get substring of @ and use as username for user

        String s = etEmail.getText().toString();
        String[] split = s.replace(".","").split("@");
        CURRENT_USER.displayName = split[0].toLowerCase();
        CURRENT_USER.email = etEmail.getText().toString().toLowerCase();
    }

    private void getGoogleUsername(String email) {
        // get substring of @ and use as username for user

        String s = email;
        String[] split = s.replace(".","").split("@");
        CURRENT_USER.displayName = split[0].toLowerCase();
        CURRENT_USER.email = email.toLowerCase();
    }

    private boolean validateEmail() {

        String emailInput = inputLayoutemail.getEditText().getText().toString().trim();

        if (emailInput.isEmpty()) {
            inputLayoutemail.setError("Email Address is  required*");
            return false;
        } else {
            inputLayoutemail.setError(null);
            return true;
        }
    }

    private boolean validatePassword() {

        String passwordInput = inputLayoutpassword.getEditText().getText().toString().trim();

        if (passwordInput.isEmpty()) {
            inputLayoutpassword.setError("Password is required*");
            return false;
        } else {
            inputLayoutpassword.setError(null);
            return true;
        }
    }

}

/*
Code Attribution
Author : Firebase - Google Developers
Subject: Authenticate with Firebase using Password-Based Accounts on Android
Code available : https://firebase.google.com/docs/auth/android/password-auth?hl=en&authuser=1
Date accessed : [22/05/2022]"

Author : Google Identity - Google developers
Title: Integrating Google Sign-In into Your Android App
URL : https://developers.google.com/identity/sign-in/android/sign-in
Accessed: 23/06/2022
 */

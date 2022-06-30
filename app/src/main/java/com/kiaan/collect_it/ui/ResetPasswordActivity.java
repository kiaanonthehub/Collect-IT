package com.kiaan.collect_it.ui;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.kiaan.collect_it.R;

public class ResetPasswordActivity extends AppCompatActivity {
    private EditText inputEmail;

    private FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reset_password);

        inputEmail = findViewById(R.id.email);

        Button btnReset = findViewById(R.id.btn_reset_password);

        Button btnBack = findViewById(R.id.btn_back);

        auth = FirebaseAuth.getInstance();

        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        btnReset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String email = inputEmail.getText().toString().trim();

                if (TextUtils.isEmpty(email)) {
                    Toast.makeText(getApplication(), "Enter your registered email", Toast.LENGTH_SHORT).show();
                    return;
                }

                FirebaseAuth.getInstance().sendPasswordResetEmail(email)

                        .addOnCompleteListener(task -> {
                            if (task.isSuccessful()) {
                                Toast.makeText(ResetPasswordActivity.this, "We have sent you instructions to reset your password. " +
                                                "Please check your spam folder for the email", Toast.LENGTH_LONG).show();
                            } else {
                                Toast.makeText(ResetPasswordActivity.this, "Failed to send reset email", Toast.LENGTH_LONG).show();
                            }

                        });
            }
        });
    }

}


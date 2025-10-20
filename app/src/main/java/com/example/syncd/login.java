package com.example.syncd;

import android.app.ProgressDialog; // Import this
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
// Import this, com.google.firebase.Firebase is not correct
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class login extends AppCompatActivity {
    FirebaseAuth auth;
    EditText editText_email, editText_password;
    Button button_login;
    ProgressDialog progressDialog; // Added for better UX

    String emailPattern = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_login);

        // --- FIX 1: Initialize FirebaseAuth ---
        auth = FirebaseAuth.getInstance();

        // --- FIX 2: Initialize your UI elements ---
        editText_email = findViewById(R.id.editText_email);
        editText_password = findViewById(R.id.editText_password);
        button_login = findViewById(R.id.button_login); // <<< CRITICAL FIX

        // Initialize ProgressDialog
        progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("Logging In");
        progressDialog.setMessage("Please wait...");
        progressDialog.setCancelable(false);

        button_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String email = editText_email.getText().toString();
                String pass = editText_password.getText().toString();

                // --- FIX 3: Corrected Validation Logic ---
                if (TextUtils.isEmpty(email)) {
                    editText_email.setError("Email cannot be empty");
                    Toast.makeText(login.this, "Email cannot be empty", Toast.LENGTH_SHORT).show();
                } else if (TextUtils.isEmpty(pass)) {
                    editText_password.setError("Password cannot be empty");
                    Toast.makeText(login.this, "Password cannot be empty", Toast.LENGTH_SHORT).show();
                } else if (!email.matches(emailPattern)) {
                    editText_email.setError("Enter a valid email");
                }
                // --- FIX 4: Removed incorrect password length check ---
                else {
                    // All checks passed, show progress and try to log in
                    progressDialog.show();

                    auth.signInWithEmailAndPassword(email, pass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            progressDialog.dismiss(); // Hide progress dialog

                            if (task.isSuccessful()) {
                                // Login success
                                Intent intent = new Intent(login.this, MainActivity.class);
                                startActivity(intent);
                                finish();
                            } else {
                                // Login failed
                                Toast.makeText(login.this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }
            }
        });

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }
}

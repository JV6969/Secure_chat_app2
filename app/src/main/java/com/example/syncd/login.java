package com.example.syncd;

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
import com.google.firebase.Firebase;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class login extends AppCompatActivity {
    FirebaseAuth auth;
    EditText editText_email,editText_password;
    Button button_login;

    String emailPattern = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$";



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_login);

       editText_email =findViewById(R.id.editText_email);
       editText_password = findViewById(R.id.editText_password);

        button_login.setOnClickListener(new View.OnClickListener() {
                                            @Override
                                            public void onClick(View view) {
                                                String email = editText_email.getText().toString();
                                                String pass = editText_password.getText().toString();

                                                if((TextUtils.isEmpty(email))){
                                                    Toast.makeText(login.this, "Email cannot be empty", Toast.LENGTH_SHORT).show();
                                                }
                                                if((TextUtils.isEmpty(pass))){
                                                    Toast.makeText(login.this, "Password cannot be empty", Toast.LENGTH_SHORT).show();
                                                } else if (!email.matches(emailPattern)) {
                                                    editText_email.setError("Enter a valid email");
                                                } else if (pass.length()<8) {
                                                    editText_password.setError("Password should be atleast of 8 characters");
                                                }
                                                else{
                                                    auth.signInWithEmailAndPassword(email, pass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                                        @Override
                                                        public void onComplete(@NonNull Task<AuthResult> task) {
                                                            if (task.isSuccessful()) {
                                                                try {
                                                                    Intent intent = new Intent(login.this, MainActivity.class);
                                                                    startActivity(intent);
                                                                    finish();
                                                                } catch (Exception e) {
                                                                    Toast.makeText(login.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                                                                }
                                                            } else {
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
                    return insets;});
    }
}




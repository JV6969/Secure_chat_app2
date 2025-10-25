package com.example.syncd;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

// This activity will be your launcher. It shows your animation and checks login state.
public class splash extends AppCompatActivity {

    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Make sure this layout has your animation
        setContentView(R.layout.activity_splash);

        mAuth = FirebaseAuth.getInstance();

        // A delay to show your app animation
        // Adjust the time (in milliseconds) as needed
        new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
            @Override
            public void run() {
                // Check if user is signed in
                FirebaseUser currentUser = mAuth.getCurrentUser();

                if (currentUser == null) {
                    // User is NOT signed in, go to LoginActivity
                    Intent intent = new Intent(splash.this, login.class);
                    startActivity(intent);
                } else {
                    // User IS signed in, go to your MainActivity (with the chat list)
                    Intent intent = new Intent(splash.this, MainActivity.class);
                    startActivity(intent);
                }

                // Close this activity so the user can't press "back" to it
                finish();
            }
        }, 2000); // 2-second delay for your animation.
    }
}

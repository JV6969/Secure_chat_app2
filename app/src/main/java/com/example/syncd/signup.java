package com.example.syncd;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import de.hdodenhof.circleimageview.CircleImageView;

public class signup extends AppCompatActivity {
    TextView textView_login;
    CircleImageView profilerg;
    EditText editText_fullname, editText_email_signup, editText_password_signup, editText_confirm_password;
    Button button_signup;
    Uri imageURI;
    String imageuri;
    String emailPattern = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$";
    FirebaseAuth auth;
    FirebaseDatabase database;
    FirebaseStorage storage;
    ProgressDialog progressDialog;
    String defaultImageURL = "https://firebasestorage.googleapis.com/v0/b/syncd6969.firebasestorage.app/o/22-223968_default-profile-picture-circle-hd-png-download.png?alt=media&token=0eafe101-49cc-43af-a86b-4e338fcb86fc";


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Check if this is the correct request AND the result was successful
        if (requestCode == 10 && resultCode == RESULT_OK) {
            // Check if data was returned
            if (data != null && data.getData() != null) {
                imageURI = data.getData(); // Get the local URI
                profilerg.setImageURI(imageURI); // Set the preview
            }
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_signup);

        auth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();
        storage = FirebaseStorage.getInstance();

        // Initialize ProgressDialog
        progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("Creating Account");
        progressDialog.setMessage("Please wait...");
        progressDialog.setCancelable(false);

        profilerg = findViewById(R.id.profilerg);
        editText_fullname = findViewById(R.id.editText_fullname);
        editText_email_signup = findViewById(R.id.editText_email_signup);
        editText_password_signup = findViewById(R.id.editText_password_signup);
        editText_confirm_password = findViewById(R.id.editText_confirm_password);
        button_signup = findViewById(R.id.button_signup);
        textView_login = findViewById(R.id.textView_login);

        textView_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent innn = new Intent(signup.this, login.class);
                startActivity(innn);
                finish();
            }
        });

        profilerg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent, "select image"), 10);
            }
        });

        button_signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String name2 = editText_fullname.getText().toString();
                String emaill2 = editText_email_signup.getText().toString();
                String pass2 = editText_password_signup.getText().toString();
                String cpass2 = editText_confirm_password.getText().toString();
                String status2 = "Hey!! i am using sync'd ";

                if (TextUtils.isEmpty(name2) || (TextUtils.isEmpty(emaill2)) || TextUtils.isEmpty(pass2) ||
                        TextUtils.isEmpty(cpass2)) {
                    Toast.makeText(signup.this, "Fields cannot be empty", Toast.LENGTH_SHORT).show();
                    return; // Stop
                }
                if (!emaill2.matches(emailPattern)) {
                    editText_email_signup.setError("Enter a valid email");
                    return; // Stop
                }
                if (pass2.length() < 8) {
                    editText_password_signup.setError("Password should be atleast of 8 characters");
                    return; // Stop
                }
                if (!pass2.equals(cpass2)) {
                    editText_confirm_password.setError("Password and Confirm password should be same");
                    return; // Stop
                }

                // Show progress dialog
                progressDialog.show();

                auth.createUserWithEmailAndPassword(emaill2, pass2).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            String id = task.getResult().getUser().getUid();
                            DatabaseReference reference = database.getReference().child("user").child(id);
                            StorageReference storageReference = storage.getReference().child("user").child(id);

                            // --- CASE 1: User selected an image ---
                            if (imageURI != null) {
                                storageReference.putFile(imageURI).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                                    @Override
                                    public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                                        if (task.isSuccessful()) {
                                            storageReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                                @Override
                                                public void onSuccess(Uri uri) {
                                                    imageuri = uri.toString();
                                                    // Create user with uploaded image
                                                    // DANGER: Update Users class to NOT store passwords
                                                    Users users = new Users(id, name2, emaill2, pass2,imageuri, status2);
                                                    saveUserToDatabase(reference, users);
                                                }
                                            });
                                        } else {
                                            // Image upload failed, use default
                                            Toast.makeText(signup.this, "Image upload failed, using default.", Toast.LENGTH_SHORT).show();
                                            imageuri = defaultImageURL;
                                            Users users = new Users(id, name2, emaill2,pass2, imageuri, status2);
                                            saveUserToDatabase(reference, users);
                                        }
                                    }
                                });
                            }
                            // --- CASE 2: User did NOT select an image ---
                            else {
                                imageuri = defaultImageURL;
                                Users users = new Users(id, name2, emaill2,pass2, imageuri, status2);
                                saveUserToDatabase(reference, users);
                            }

                        } else {
                            // Auth creation failed
                            progressDialog.dismiss();
                            Toast.makeText(signup.this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        });


        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });


    }

    // Helper method to save user and finish activity
    private void saveUserToDatabase(DatabaseReference reference, Users users) {
        reference.setValue(users).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                progressDialog.dismiss(); // Dismiss dialog here
                if (task.isSuccessful()) {
                    // Navigate to MainActivity
                    Intent intent = new Intent(signup.this, MainActivity.class);
                    startActivity(intent);
                    finish();
                } else {
                    Toast.makeText(signup.this, "Error in creating the user in database", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}
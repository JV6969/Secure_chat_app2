package com.example.syncd;

import android.annotation.SuppressLint;
import android.content.Intent; // <-- ADDED THIS IMPORT
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

// import androidx.activity.EdgeToEdge; // <-- CHANGED: Removed to fix keyboard
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
// import androidx.core.graphics.Insets; // <-- CHANGED: Removed to fix keyboard
// import androidx.core.view.ViewCompat; // <-- CHANGED: Removed to fix keyboard
// import androidx.core.view.WindowInsetsCompat; // <-- CHANGED: Removed to fix keyboard
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser; // <-- ADDED THIS IMPORT
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Date;

import de.hdodenhof.circleimageview.CircleImageView;

public class chatWin extends AppCompatActivity {
    String reciverimg, reciverUid, reciverName, SenderUID;
    CircleImageView profile;
    TextView reciverNName;
    CardView sendbtn;
    EditText textmsg;
    public static String senderImg;
    public static String reciverIImg;
    FirebaseAuth firebaseAuth;
    FirebaseDatabase database;
    String senderRoom, reciverRoom;
    RecyclerView messageAdpter;
    ArrayList<msgModelclass> messagesArrayList;
    messagesAdpter mmessagesAdpter; // <-- This is your adapter variable


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_chat_win);


        database = FirebaseDatabase.getInstance();
        firebaseAuth = FirebaseAuth.getInstance();

        // --- FIX 1: CHECK IF USER IS LOGGED IN ---
        FirebaseUser currentUser = firebaseAuth.getCurrentUser();
        if (currentUser == null) {
            Toast.makeText(this, "User not authenticated.", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(chatWin.this, login.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
            finish();
            return;
        }
        SenderUID = currentUser.getUid();
        // --- END FIX 1 ---


        // --- FIX 2: CHECK INTENT DATA ---
        reciverName = getIntent().getStringExtra("nameeee");
        reciverimg = getIntent().getStringExtra("reciverImg");
        reciverUid = getIntent().getStringExtra("uid");

        if (reciverName == null || reciverimg == null || reciverUid == null) {
            Toast.makeText(this, "Error: Could not load chat data.", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }
        // --- END FIX 2 ---


        messagesArrayList = new ArrayList<>();

        sendbtn = findViewById(R.id.sendbtnn);
        textmsg = findViewById(R.id.textmsg);
        reciverNName = findViewById(R.id.recivername);
        profile = findViewById(R.id.profileimgg);
        messageAdpter = findViewById(R.id.msgadpter); // <-- This is your RecyclerView

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setStackFromEnd(true);
        messageAdpter.setLayoutManager(linearLayoutManager);
        mmessagesAdpter = new messagesAdpter(chatWin.this, messagesArrayList);
        messageAdpter.setAdapter(mmessagesAdpter);

        Picasso.get().load(reciverimg).into(profile);
        reciverNName.setText("" + reciverName);

        senderRoom = SenderUID + reciverUid;
        reciverRoom = reciverUid + SenderUID;

        DatabaseReference reference = database.getReference().child("user").child(SenderUID);
        DatabaseReference chatreference = database.getReference().child("chats").child(senderRoom).child("messages");

        chatreference.addValueEventListener(new ValueEventListener() {
            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                messagesArrayList.clear();
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    msgModelclass messages = dataSnapshot.getValue(msgModelclass.class);

                    // --- FIX 3: CHECK FOR BAD MESSAGE DATA ---
                    // If a message is null, adding it will crash the adapter.
                    if (messages != null) {
                        messagesArrayList.add(messages);
                    }
                    // --- END FIX 3 ---
                }
                mmessagesAdpter.notifyDataSetChanged();
            }


            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                // --- FIX 4: CHECK FOR MISSING "profilepic" ---
                // This line will crash if "profilepic" doesn't exist.
                // senderImg = snapshot.child("profilepic").getValue().toString();

                // This is the safe way to get the value:
                Object profilePicValue = snapshot.child("profilepic").getValue();
                if (profilePicValue != null) {
                    senderImg = profilePicValue.toString();
                } else {
                    senderImg = ""; // Use an empty string or a default placeholder URL
                }
                // --- END FIX 4 ---

                reciverIImg = reciverimg;
                if (mmessagesAdpter != null) {
                    mmessagesAdpter.notifyDataSetChanged();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


        sendbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String message = textmsg.getText().toString();
                if (message.isEmpty()) {
                    Toast.makeText(chatWin.this, "Cannot send an empty text", Toast.LENGTH_SHORT).show();
                    return;
                }
                textmsg.setText("");
                Date date = new Date();
                msgModelclass messagess = new msgModelclass(message, SenderUID, date.getTime());

                database = FirebaseDatabase.getInstance();
                database.getReference().child("chats")
                        .child(senderRoom)
                        .child("messages")
                        .push().setValue(messagess).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                database.getReference().child("chats")
                                        .child(reciverRoom)
                                        .child("messages")
                                        .push().setValue(messagess).addOnCompleteListener(new OnCompleteListener<Void>() {
                                            @Override
                                            public void onComplete(@NonNull Task<Void> task) {

                                            }
                                        });
                            }
                        });
            }
        });
    }
}


package com.example.syncd;

import android.os.Bundle;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;

public class chatWin extends AppCompatActivity {
    String reciverimg, reciverUid,reciverName,SenderUID;
    CircleImageView profile;
    TextView reciverNName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
      //  EdgeToEdge.enable(this);
        setContentView(R.layout.activity_chat_win);

       /* super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chatwindo);
        getSupportActionBar().hide();
        database = FirebaseDatabase.getInstance();
        firebaseAuth = FirebaseAuth.getInstance();*/

        reciverName = getIntent().getStringExtra("nameeee");
        reciverimg = getIntent().getStringExtra("reciverImg");
        reciverUid = getIntent().getStringExtra("uid");

        profile = findViewById(R.id.profileimgg);
        reciverNName = findViewById(R.id.recivername);

        Picasso.get().load(reciverimg).into(profile);
        reciverNName.setText(""+reciverName);




























        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }
}
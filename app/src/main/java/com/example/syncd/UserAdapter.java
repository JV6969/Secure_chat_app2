package com.example.syncd;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class UserAdapter extends RecyclerView.Adapter<UserAdapter.viewholder> {
    MainActivity mainActivity;
    ArrayList<Users> userArrayList;
    public UserAdapter(MainActivity mainActivity, ArrayList<Users> userArrayList) {
        this.mainActivity=mainActivity;
        this.userArrayList = userArrayList;
    }

    @NonNull
    @Override
    public UserAdapter.viewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mainActivity).inflate(R.layout.user_xml,parent,false);

        return new viewholder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull UserAdapter.viewholder holder, int position) {
        Users users = userArrayList.get(position);
        holder.usernamme.setText(users.name);
        holder.userstatus.setText(users.status);
        Picasso.get().load(users.imageuri).into(holder.userimg);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(mainActivity, chatWin.class);
                intent.putExtra("nameeee", users.getName());
                intent.putExtra("reciverImg", users.getImageuri());
                intent.putExtra("uid", users.getId());
                mainActivity.startActivity(intent);
            }
        });




    }

    @Override
    public int getItemCount() {
        return userArrayList.size();
    }

    public class viewholder extends RecyclerView.ViewHolder {
        CircleImageView userimg;
        TextView usernamme,userstatus;

        public viewholder(@NonNull View itemView) {
            super(itemView);
            userimg = itemView.findViewById(R.id.userimg);
            usernamme= itemView.findViewById(R.id.username);
            userstatus = itemView.findViewById(R.id.userstatus);
        }
    }
}

package com.example.syncd;

import static com.example.syncd.chatWin.reciverIImg;
import static com.example.syncd.chatWin.senderImg;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class messagesAdpter extends RecyclerView.Adapter {
    Context context;
    ArrayList<msgModelclass> messagesAdpterArrayList;
    int ITEM_SEND=1;
    int ITEM_RECIVE=2;

    public messagesAdpter(Context context, ArrayList<msgModelclass> messagesAdpterArrayList) {
        this.context = context;
        this.messagesAdpterArrayList = messagesAdpterArrayList;
    }



    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType == ITEM_SEND){
            View view = LayoutInflater.from(context).inflate(R.layout.sender_layout, parent, false);
            return new senderVierwHolder(view); // Corrected typo
        } else {
            View view = LayoutInflater.from(context).inflate(R.layout.reciver_layout, parent, false);
            return new reciverViewHolder(view);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        msgModelclass messages = messagesAdpterArrayList.get(position);

        // --- FIX: Add null check for messages ---
        if (messages == null) {
            return; // Don't try to bind a null message
        }
        // --- END FIX ---

        holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                new AlertDialog.Builder(context).setTitle("Delete")
                        .setMessage("Are you sure you want to delete this message?")
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                // TODO: Implement delete logic using message ID
                            }
                        }).setNegativeButton("No", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                dialogInterface.dismiss();
                            }
                        }).show();

                return true; // Return true to indicate the long click was handled
            }
        });

        if (holder.getClass()==senderVierwHolder.class){ // Corrected typo
            senderVierwHolder viewHolder = (senderVierwHolder) holder; // Corrected typo

            // --- FIX: Add null check for message text ---
            if (messages.getMessage() != null) {
                viewHolder.msgtxt.setText(messages.getMessage());
            } else {
                viewHolder.msgtxt.setText(""); // Set empty if null
            }
            // --- END FIX ---

            // --- FIX: PICASSO CRASH FIX for Sender ---
            // Check if senderImg is valid before loading
            if (!TextUtils.isEmpty(senderImg)) {
                Picasso.get().load(senderImg).into(viewHolder.circleImageView);
            } else {
                // Load placeholder if image isn't ready yet or invalid
                viewHolder.circleImageView.setImageResource(R.drawable.photocamera);
            }
            // --- END FIX ---

        } else {
            reciverViewHolder viewHolder = (reciverViewHolder) holder;

            // --- FIX: Add null check for message text ---
            if (messages.getMessage() != null) {
                viewHolder.msgtxt.setText(messages.getMessage());
            } else {
                viewHolder.msgtxt.setText(""); // Set empty if null
            }
            // --- END FIX ---

            // --- FIX: PICASSO CRASH FIX for Receiver ---
            // Check if reciverIImg is valid before loading (Corrected variable name)
            if (!TextUtils.isEmpty(reciverIImg)) {
                Picasso.get().load(reciverIImg).into(viewHolder.circleImageView);
            } else {
                // Load placeholder if image isn't ready yet or invalid
                viewHolder.circleImageView.setImageResource(R.drawable.photocamera);
            }
            // --- END FIX ---
        }
    }

    @Override
    public int getItemCount() {
        // --- FIX: Add null check for list ---
        return messagesAdpterArrayList != null ? messagesAdpterArrayList.size() : 0;
        // --- END FIX ---
    }

    @Override
    public int getItemViewType(int position) {
        msgModelclass messages = messagesAdpterArrayList.get(position);

        // --- FIX: Add null check before accessing user ---
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        if (currentUser != null && messages.getSenderid() != null && currentUser.getUid().equals(messages.getSenderid())) {
            return ITEM_SEND;
        } else {
            return ITEM_RECIVE;
        }
        // --- END FIX ---
    }

    class  senderVierwHolder extends RecyclerView.ViewHolder { // Corrected typo
        CircleImageView circleImageView;
        TextView msgtxt;
        public senderVierwHolder(@NonNull View itemView) { // Corrected typo
            super(itemView);
            circleImageView = itemView.findViewById(R.id.profilerggg);
            msgtxt = itemView.findViewById(R.id.msgsendertyp);
        }
    }

    class reciverViewHolder extends RecyclerView.ViewHolder {
        CircleImageView circleImageView;
        TextView msgtxt;
        public reciverViewHolder(@NonNull View itemView) {
            super(itemView);
            circleImageView = itemView.findViewById(R.id.pro);
            msgtxt = itemView.findViewById(R.id.recivertextset);
        }
    }
}

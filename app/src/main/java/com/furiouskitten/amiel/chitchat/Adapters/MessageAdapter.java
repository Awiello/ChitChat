package com.furiouskitten.amiel.chitchat.Adapters;

import android.content.Context;
import android.graphics.Color;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.furiouskitten.amiel.chitchat.Model.AllMethods;
import com.furiouskitten.amiel.chitchat.Model.Message;
import com.furiouskitten.amiel.chitchat.R;
import com.google.firebase.database.DatabaseReference;

import java.util.List;

public class MessageAdapter extends RecyclerView.Adapter<MessageAdapter.MessageAdapterViewHolder>{

    Context context;
    List<Message> messages;
    DatabaseReference messageDb;

    public MessageAdapter(Context context, List<Message> messages, DatabaseReference messageDb) {
        this.context = context;
        this.messages = messages;
        this.messageDb = messageDb;
    }

    @NonNull
    @Override
    public MessageAdapterViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_message, parent, false);
        return new MessageAdapterViewHolder(view);

    }

    @Override
    public void onBindViewHolder(@NonNull MessageAdapterViewHolder holder, int position) {
        Message message = messages.get(position);

        if(message.getName().equals(AllMethods.name)){

            holder.tvTitle.setText("You: " + message.getMessage());
            holder.tvTitle.setGravity(Gravity.START);
            holder.mLinearLayout.setBackgroundColor(Color.parseColor("#7F9E73"));

        } else{

            holder.tvTitle.setText(message.getName() + ":" +message.getMessage());
            holder.ibDelete.setVisibility(View.GONE);



        }

    }

    @Override
    public int getItemCount() {
        return messages.size();
    }

    public class MessageAdapterViewHolder extends RecyclerView.ViewHolder{

        //MessageAdapterViewHolder
        TextView tvTitle;
        ImageButton ibDelete;
        LinearLayout mLinearLayout;

        public MessageAdapterViewHolder(@NonNull View itemView) {
            super(itemView);

            tvTitle = itemView.findViewById(R.id.textViewTitle);
            ibDelete = itemView.findViewById(R.id.ibDelete);
            mLinearLayout = itemView.findViewById(R.id.mLinearLayoutMessage);


            ibDelete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    messageDb.child(messages.get(getAdapterPosition()).getKey()).removeValue();
                }
            });

        }
    }




}

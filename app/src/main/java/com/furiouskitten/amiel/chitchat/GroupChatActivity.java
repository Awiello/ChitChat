package com.furiouskitten.amiel.chitchat;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.furiouskitten.amiel.chitchat.Adapters.MessageAdapter;
import com.furiouskitten.amiel.chitchat.Model.AllMethods;
import com.furiouskitten.amiel.chitchat.Model.Message;
import com.furiouskitten.amiel.chitchat.Model.User;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class GroupChatActivity extends AppCompatActivity implements View.OnClickListener{


    FirebaseAuth mGroupChatActivityFirebaseAuth;
    FirebaseDatabase mGroupChatActivityFirebaseDatabase;
    DatabaseReference mGroupChatActivityDatabaseReference;
    MessageAdapter mGroupMessageAdapter;
    User mUser;
    List<Message> mMessage;


    RecyclerView mMessageRecyclerView;
    EditText mMessageEditText;
    Button mSendMessageButton;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group_chat);

        mGroupMessageInit();

    }

    private void mGroupMessageInit() {

    mGroupChatActivityFirebaseAuth = FirebaseAuth.getInstance();
    mGroupChatActivityFirebaseDatabase = FirebaseDatabase.getInstance();
    mUser = new User();

    mMessageRecyclerView = findViewById(R.id.mGroupChatRecyclerView);
    mMessageEditText = findViewById(R.id.mWriteMessageEditText);
    mSendMessageButton = findViewById(R.id.mWriteMessageSendButton);
    mSendMessageButton.setOnClickListener(this);
    mMessage = new ArrayList<>();

    }

    @Override
    public void onClick(View view) {

        if(!TextUtils.isEmpty(mMessageEditText.getText().toString())){
            Message message = new Message(mMessageEditText.getText().toString(), mUser.getName());

            mMessageEditText.setText("");
            mGroupChatActivityDatabaseReference.push().setValue(message);


        }else{

            Toast.makeText(getApplicationContext(),"Really? a blank message? GTFO here.",Toast.LENGTH_SHORT).show();

        }


    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        if(item.getItemId() == R.id.menuLogout){

            mGroupChatActivityFirebaseAuth.signOut();
            finish();
            startActivity(new Intent(GroupChatActivity.this, MainActivity.class));

        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onStart() {
        super.onStart();

        final FirebaseUser mGroupChatCurrentUser = mGroupChatActivityFirebaseAuth.getCurrentUser();

        mUser.setUid(mGroupChatCurrentUser.getUid());
        mUser.setEmail(mGroupChatCurrentUser.getEmail());

        mGroupChatActivityFirebaseDatabase.getReference("Users").child(mGroupChatCurrentUser.getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                mUser = dataSnapshot.getValue(User.class);
                mUser.setUid(mGroupChatCurrentUser.getUid());
                AllMethods.name = mUser.getName();

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


        mGroupChatActivityDatabaseReference = mGroupChatActivityFirebaseDatabase.getReference("messages");
        mGroupChatActivityDatabaseReference.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                Message message = dataSnapshot.getValue(Message.class);
                message.setKey(dataSnapshot.getKey());
                mMessage.add(message);
                displayMessages(mMessage);

            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

                Message message = dataSnapshot.getValue(Message.class);
                message.setKey(dataSnapshot.getKey());
                List<Message> newMessages = new ArrayList<Message>();

                for(Message m: mMessage){

                    if(m.getKey().equals(message.getKey())){

                        newMessages.add(message);

                    }else{

                        newMessages.add(m);

                    }

                }


                mMessage = newMessages;

                displayMessages(mMessage);

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {

                Message message = dataSnapshot.getValue(Message.class);
                message.setKey(dataSnapshot.getKey());

                List<Message> newMessages = new ArrayList<Message>();

                for(Message m: mMessage){

                    if(!m.getKey().equals(message.getKey())){

                        newMessages.add(m);


                    }
                }

                mMessage = newMessages;
                displayMessages(mMessage);

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {



            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

    @Override
    protected void onResume() {
        super.onResume();
        mMessage = new ArrayList<>();

    }

    //baguhin sa mMessages pag nagkaroon ng error. Try lang naman..
    private void displayMessages(List<Message> messages) {

        mMessageRecyclerView.setLayoutManager(new LinearLayoutManager(GroupChatActivity.this));
        mGroupMessageAdapter = new MessageAdapter(GroupChatActivity.this, messages, mGroupChatActivityDatabaseReference);
        mMessageRecyclerView.setAdapter(mGroupMessageAdapter);
        //mMessageRecyclerView.setHasFixedSize(true);

    }

}

package com.furiouskitten.amiel.chitchat;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.furiouskitten.amiel.chitchat.Model.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class RegisterActivity extends AppCompatActivity {


    EditText mRegisterPageEmailEditText;
    EditText mRegisterPagePasswordEditText;
    EditText mRegisterPageNameEditText;
    Button mRegisterPageCreateButton;
    TextView mRegisterPageLoginUserText;
    DatabaseReference mRegisterDatabaseReference;
    FirebaseAuth mRegisterFirebaseAuth;



    @SuppressLint("WrongViewCast")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);


        //object reference

        mRegisterPageLoginUserText = findViewById(R.id.register_activity_login_account_text);
        mRegisterPageEmailEditText = findViewById(R.id.register_activity_email_edit_text);
        mRegisterPagePasswordEditText = findViewById(R.id.register_activity_password_edit_text);
        mRegisterPageNameEditText = findViewById(R.id.register_activity_name_edit_text);
        mRegisterPageCreateButton = findViewById(R.id.register_activity_create_button);

        //Firebase object reference
        mRegisterFirebaseAuth = FirebaseAuth.getInstance();
        mRegisterDatabaseReference = FirebaseDatabase.getInstance().getReference().child("Users");

        //This part is for Register User function
        mRegisterPageCreateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mRegisterUser();
            }
        });



        //login page function
        mRegisterPageLoginUserText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent backToLogin = new Intent(getApplicationContext(),MainActivity.class);
                startActivity(backToLogin);
            }
        });



    }

    //Register User Function

    public void mRegisterUser(){

       final String mRegisterEmail = mRegisterPageEmailEditText.getText().toString();
       String mRegisterPassword = mRegisterPagePasswordEditText.getText().toString();
       final String mRegisterName = mRegisterPageNameEditText.getText().toString();

       if(!mRegisterEmail.equals("") && !mRegisterPassword.equals("") && mRegisterPassword.length() > 6){

           mRegisterFirebaseAuth.createUserWithEmailAndPassword(mRegisterEmail,mRegisterPassword).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
               @Override
               public void onComplete(@NonNull Task<AuthResult> task) {

                   if(task.isSuccessful()){

                       FirebaseUser mFirebaseUser = mRegisterFirebaseAuth.getCurrentUser();
                       User mAddUser = new User();
                       mAddUser.setEmail(mRegisterEmail);
                       mAddUser.setName(mRegisterName);

                       mRegisterDatabaseReference.child(mFirebaseUser.getUid()).setValue(mAddUser).addOnCompleteListener(new OnCompleteListener<Void>() {
                           @Override
                           public void onComplete(@NonNull Task<Void> task) {

                               if(task.isSuccessful()){

                                   Toast.makeText(getApplicationContext(),"User Added Successfully", Toast.LENGTH_SHORT).show();
                                   Intent successIntent = new Intent(RegisterActivity.this, GroupChatActivity.class);
                                   startActivity(successIntent);

                               }else{
                                   Toast.makeText(getApplicationContext(),"User could not be added.", Toast.LENGTH_SHORT).show();
                               }

                           }
                       });

                   }

               }
           });

       }

    }


}

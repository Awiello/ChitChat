package com.furiouskitten.amiel.chitchat;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class MainActivity extends AppCompatActivity {

    EditText mLoginPageEmailEditText;
    EditText mLoginPagePasswordEditText;
    Button mLoginPageLoginButton;
    TextView mLoginPageForgotPasswordText;
    TextView mLoginPageCreateUserText;
    Button mTestButton;


    //firebase
    FirebaseAuth mFirebaseAuth;
    //DatabaseReference mDatabaseReference;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //object reference


        mLoginPageLoginButton = findViewById(R.id.login_activity_login_button);
        mLoginPageForgotPasswordText = findViewById(R.id.login_activity_forgot_pw_text);
        mLoginPageCreateUserText = findViewById(R.id.login_activity_create_account_text);

        mFirebaseAuth = FirebaseAuth.getInstance();

        if(mFirebaseAuth.getCurrentUser() != null){

            Intent mAuthIntent = new Intent(getApplicationContext(), GroupChatActivity.class);
            startActivity(mAuthIntent);
        }

        else {

            mLoginPageEmailEditText = findViewById(R.id.login_activity_email_edit_text);
            mLoginPagePasswordEditText = findViewById(R.id.login_activity_password_edit_text);
            //mDatabaseReference = FirebaseDatabase.getInstance().getReference().child("Users");

        }


        //FORGOT PASSWORD
        //Forgot Password Function
        mLoginPageForgotPasswordText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                forgotPassword();
            }
        });




        //REGISTER
        //Create user function
        mLoginPageCreateUserText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent mRegisterIntent  = new Intent(getApplicationContext(),RegisterActivity.class);
                startActivity(mRegisterIntent);
            }
        });



        //LOGIN
        //Login User Functions
        mLoginPageLoginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                final String mLoginEmail = mLoginPageEmailEditText.getText().toString();
                String mLoginPassword = mLoginPagePasswordEditText.getText().toString();


                if(!mLoginEmail.equals("") && !mLoginPassword.equals("")){

                    mFirebaseAuth.signInWithEmailAndPassword(mLoginEmail, mLoginPassword).addOnCompleteListener(new OnCompleteListener<AuthResult>() {

                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if(task.isSuccessful()){

                                Toast.makeText(getApplicationContext(),"Welcome," + mLoginEmail, Toast.LENGTH_SHORT).show();
                                Intent mAuthIntent = new Intent(getApplicationContext(), GroupChatActivity.class);
                                startActivity(mAuthIntent);

                            }
                            else{

                                Toast.makeText(getApplicationContext(),"Wait, Something is wrong. Please try again.", Toast.LENGTH_SHORT).show();

                            }
                        }
                    });
                }

            }
        });





    }


    //FORGOT PASSWORD
    //Create Forgot Password Function
    public void forgotPassword(){


        //Create DialogBuilder
        AlertDialog.Builder mForgotPasswordAlertDialogBuilder = new AlertDialog.Builder(MainActivity.this);

        //Generate layout for Alert Dialog
        LinearLayout mForgotPasswordContainer = new LinearLayout(MainActivity.this);
        mForgotPasswordContainer.setOrientation(LinearLayout.VERTICAL);

        //Set Layout Parameters for generated Layout
        //comes with margin, but not really required tho.
        LinearLayout.LayoutParams mForgotPwLayoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,LinearLayout.LayoutParams.WRAP_CONTENT);
        mForgotPwLayoutParams.setMargins(50,0,0,100);


        //Generating EditText
        final EditText mForgotPwInputEditText = new EditText(MainActivity.this);
        mForgotPwInputEditText.setLayoutParams(mForgotPwLayoutParams);


        //set gravity position of mForgotPwInputEditText
        //also the InputTypes
        mForgotPwInputEditText.setGravity(Gravity.TOP|Gravity.START);
        mForgotPwInputEditText.setInputType(InputType.TYPE_TEXT_FLAG_CAP_SENTENCES);
        mForgotPwInputEditText.setLines(1);
        mForgotPwInputEditText.setMaxLines(1);


        //Introducing mForgotPwInputEditText and mForgotPwLayoutParams to our layout: mForgotPasswordContainer
        mForgotPasswordContainer.addView(mForgotPwInputEditText, mForgotPwLayoutParams);


        //mForgotPasswordAlertDialogBuilder elements show
        mForgotPasswordAlertDialogBuilder.setMessage("Please enter your e-mail address");
        mForgotPasswordAlertDialogBuilder.setTitle("Forgot Password");


        //show mForgotPasswordAlertDialogBuilder elements to mForgotPasswordContainer
        mForgotPasswordAlertDialogBuilder.setView(mForgotPasswordContainer);


        mForgotPasswordAlertDialogBuilder.setPositiveButton("Done",new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

             final String mForgotPwEnteredEmail = mForgotPwInputEditText.getText().toString();

             mFirebaseAuth.sendPasswordResetEmail(mForgotPwEnteredEmail).addOnCompleteListener(new OnCompleteListener<Void>() {
                 @Override
                 public void onComplete(@NonNull Task<Void> task) {

                     if(task.isSuccessful()){

                         Toast.makeText(getApplicationContext(),"Password sent to" + mForgotPwEnteredEmail, Toast.LENGTH_LONG).show();

                     }

                 }
             });


            }
        });



    }



}

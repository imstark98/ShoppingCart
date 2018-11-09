package com.example.hemantsingh.shoppingcart;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.hemantsingh.shoppingcart.Model.Users;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class LoginActivity extends AppCompatActivity {
    
    private EditText InputNumber, InputPassword;
    private Button LoginButton;
    private ProgressDialog loadingBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        LoginButton = (Button)findViewById(R.id.login_btn);
        InputNumber = (EditText)findViewById(R.id.login_phone_number_input);
        InputPassword = (EditText)findViewById(R.id.login_password_input);
        loadingBar = new ProgressDialog(this);
        
        LoginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                LoginUser();
            }
        });
        
    }

    private void LoginUser() {
        
        String phone = InputNumber.getText().toString();
        String password = InputPassword.getText().toString();
        
        if(TextUtils.isEmpty(phone))
        {
            Toast.makeText(LoginActivity.this, "Please enter your phone number", Toast.LENGTH_SHORT).show();
        }
        else if(TextUtils.isEmpty(password))
        {
            Toast.makeText(LoginActivity.this, "Please enter your password", Toast.LENGTH_SHORT).show();
        }
        else{
            loadingBar.setTitle("Login Account");
            loadingBar.setMessage("Please, wait while we are checking your credentials");
            loadingBar.setCanceledOnTouchOutside(false);
            loadingBar.show();
            
            AllowAccessToAccount(phone, password);
        }
        
    }

    private void AllowAccessToAccount(final String phone, final String password) {
        
        final DatabaseReference RootRef;
        RootRef = FirebaseDatabase.getInstance().getReference();
        
        RootRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.child("Users").child(phone).exists()){

                    Users userData = dataSnapshot.child("Users").child(phone).getValue(Users.class);

                    if(userData.getPhone().equals(phone))
                    {
                        if(userData.getPassword().equals(password))
                        {
                            Toast.makeText(LoginActivity.this, "Logged in successfully", Toast.LENGTH_SHORT).show();
                            loadingBar.dismiss();
                            startActivity(new Intent(LoginActivity.this, HomeActivity.class));
                        }
                        else
                        {
                            loadingBar.dismiss();
                            Toast.makeText(LoginActivity.this, "Wrong password", Toast.LENGTH_SHORT).show();
                        }
                    }
                }
                else{
                    Toast.makeText(LoginActivity.this, "Wrong phone number", Toast.LENGTH_SHORT).show();
                    loadingBar.dismiss();
                    Toast.makeText(LoginActivity.this, "Check your number and try again", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        
    }
}

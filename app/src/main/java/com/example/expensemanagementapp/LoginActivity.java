package com.example.expensemanagementapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class LoginActivity extends AppCompatActivity {

    TextInputLayout memail;
    TextInputLayout mpassword;
    Button loginButton;
    private FirebaseAuth mAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        mAuth = FirebaseAuth.getInstance();
        memail = (TextInputLayout) findViewById(R.id.email_login);
        mpassword = (TextInputLayout) findViewById(R.id.password_login);
        loginButton = (Button) findViewById(R.id.login_page_login_button);
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = memail.getEditText().getText().toString();
                String pass = mpassword.getEditText().getText().toString();
                if(!TextUtils.isEmpty(email)&&!TextUtils.isEmpty(pass))
                {
                    loginuser(email,pass);
                }
                else
                {
                    Toast.makeText(LoginActivity.this,"PLEASE FILL IN THE DETAILS",Toast.LENGTH_LONG).show();
                }
            }
        });
    }
    private void loginuser(String email,String pass)
    {
        mAuth.signInWithEmailAndPassword(email,pass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful())
                {
                    Intent mainIntent = new Intent(LoginActivity.this,MainActivity.class);
                    startActivity(mainIntent);
                    finish();
                }
                else
                {
                    Toast.makeText(LoginActivity.this,"CAN NOT SIGN IN",Toast.LENGTH_LONG).show();
                }
            }
        });
    }
}

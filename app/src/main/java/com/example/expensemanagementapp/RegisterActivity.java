package com.example.expensemanagementapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
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
import com.google.firebase.auth.FirebaseUser;

public class RegisterActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private TextInputLayout mDisplayName;
    private TextInputLayout mEmail;
    private TextInputLayout mPassword;
    private Button filldetails;
    String displayname;
    String email;
    String password;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        mAuth = FirebaseAuth.getInstance();
        mDisplayName = (TextInputLayout) findViewById(R.id.reg_diplay_name);
        mEmail = (TextInputLayout) findViewById(R.id.reg_email);
        mPassword = (TextInputLayout) findViewById(R.id.reg_password);
        filldetails = (Button) findViewById(R.id.fillingDetails);
        filldetails.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                displayname = mDisplayName.getEditText().getText().toString();
                email = mEmail.getEditText().getText().toString().trim();
                password = mPassword.getEditText().getText().toString().trim();
                if(!TextUtils.isEmpty(displayname)&&!TextUtils.isEmpty(email)&&!TextUtils.isEmpty(password))
                {
                    registerUser();
                }
                else
                {
                    Toast.makeText(RegisterActivity.this,"PLEASE FILL IN THE DETAILS",Toast.LENGTH_LONG).show();
                }
            }
        });
    }
    public void registerUser()
    {
            Log.i("Email",email);
            Log.i("Password",password);
            mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if (task.isSuccessful()) {
                        Intent mainIntent = new Intent(RegisterActivity.this, MainActivity.class);
                        startActivity(mainIntent);
                        finish();
                    } else {
                        Log.w("Error", task.getException());
                        Toast.makeText(RegisterActivity.this, "THERE WAS SOME ERROR", Toast.LENGTH_LONG).show();
                    }
                }
            });
    }
}

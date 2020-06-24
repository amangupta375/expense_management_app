package com.example.expensemanagementapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.List;

public class IncomingTransfer extends AppCompatActivity {

    private FirebaseAuth mAuth;
    FirebaseUser currentUser;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private DocumentReference amountRef;
    public String amountStoredInitially = "0";
    private EditText amount;
    private EditText details;
    List<String> description = new ArrayList<String>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_incoming_transfer);
        mAuth = FirebaseAuth.getInstance();
        amount = findViewById(R.id.incoming_amount);
        details = findViewById(R.id.incoming_details);
        Button button = findViewById(R.id.add_incoming_button);
        currentUser = mAuth.getCurrentUser();
        final String id = currentUser.getUid();
        amountRef =db.document("Users/"+id);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getamount();
            }
        });
    }
    void getamount()
    {
        amountRef.get()
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        if(documentSnapshot.exists())
                        {
                            AmountAndDescription amountAndDescription1 = documentSnapshot.toObject(AmountAndDescription.class);
                            amountStoredInitially = amountAndDescription1.getIncomingAmount();
                            description = amountAndDescription1.getDescription();
                            String amountadded = amount.getText().toString();
                            String str = "Incoming Transaction  : "+amountadded+"\nDetails : "+details.getText().toString();
                            description.add(str);
                            int totalAmount = Integer.parseInt(amountadded) + Integer.parseInt(amountStoredInitially);
                            amountadded = String.valueOf(totalAmount);
                            String outgoingamount = amountAndDescription1.getOutgoingAmount();
                            AmountAndDescription amountAndDescription2 = new AmountAndDescription(amountadded,outgoingamount,description);
                            amountRef.set(amountAndDescription2)
                                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void aVoid) {
                                            Intent mainIntent = new Intent(IncomingTransfer.this,MainActivity.class);
                                            startActivity(mainIntent);
                                            finish();
                                            Toast.makeText(IncomingTransfer.this,"Amount Added",Toast.LENGTH_LONG).show();
                                        }
                                    })
                                    .addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {
                                            Toast.makeText(IncomingTransfer.this,"Error Occured",Toast.LENGTH_LONG).show();
                                            Log.i("On Failure",e.toString());
                                        }
                                    });
                        }
                        else
                        {
                            amountStoredInitially = "0";
                            String amountadded = amount.getText().toString();
                            int totalAmount = Integer.parseInt(amountadded) + Integer.parseInt(amountStoredInitially);
                            amountadded = String.valueOf(totalAmount);
                            String str = "Incoming Transaction  : "+amountadded+"\nDetails : "+details.getText().toString();
                            description.add(str);
                            AmountAndDescription amountAndDescription2 = new AmountAndDescription(amountadded,"0",description);
                            amountRef.set(amountAndDescription2)
                                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void aVoid) {
                                            Intent mainIntent = new Intent(IncomingTransfer.this,MainActivity.class);
                                            startActivity(mainIntent);
                                            finish();
                                            Toast.makeText(IncomingTransfer.this,"Amount Added",Toast.LENGTH_LONG).show();
                                        }
                                    })
                                    .addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {
                                            Toast.makeText(IncomingTransfer.this,"Error Occured",Toast.LENGTH_LONG).show();
                                            Log.i("On Failure",e.toString());
                                        }
                                    });
                        }
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(IncomingTransfer.this,"Error",Toast.LENGTH_LONG).show();
                    }
                });
    };
}

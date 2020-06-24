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
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.List;

public class OutgoingTransfer extends AppCompatActivity {

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
        setContentView(R.layout.activity_outgoing_transfer);
        mAuth = FirebaseAuth.getInstance();
        amount = findViewById(R.id.outgoing_transaction_amount);
        details = findViewById(R.id.outgoing_tansaction_description);
        Button button = findViewById(R.id.add_outgoing_transaction_button);
        currentUser = mAuth.getCurrentUser();
        final String id = currentUser.getUid();
        amountRef = db.document("Users/"+id);
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
                            amountStoredInitially = amountAndDescription1.getOutgoingAmount();
                            String amountadded = amount.getText().toString();
                            description = amountAndDescription1.getDescription();
                            String str = "Outgoing Transaction  : "+amountadded+"\nDetails : "+details.getText().toString();
                            description.add(str);
                            int totalAmount = Integer.parseInt(amountadded) + Integer.parseInt(amountStoredInitially);
                            amountadded = String.valueOf(totalAmount);
                            String incomingAmount = amountAndDescription1.getIncomingAmount();
                            AmountAndDescription amountAndDescription2 = new AmountAndDescription(incomingAmount,amountadded,description);
                            amountRef.set(amountAndDescription2)
                                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void aVoid) {
                                            Intent mainIntent = new Intent(OutgoingTransfer.this,MainActivity.class);
                                            startActivity(mainIntent);
                                            finish();
                                            Toast.makeText(OutgoingTransfer.this,"Amount Added",Toast.LENGTH_LONG).show();
                                        }
                                    })
                                    .addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {
                                            Toast.makeText(OutgoingTransfer.this,"Error Occured",Toast.LENGTH_LONG).show();
                                            Log.i("On Failure",e.toString());
                                        }
                                    });
                        }
                        else
                        {
                            amountStoredInitially = "0";
                            String amountadded = amount.getText().toString();
                            String str = "Outgoing Transaction  : "+amountadded+"\nDetails : "+details.getText().toString();
                            description.add(str);
                            int totalAmount = Integer.parseInt(amountadded) + Integer.parseInt(amountStoredInitially);
                            amountadded = String.valueOf(totalAmount);
                            AmountAndDescription amountAndDescription2 = new AmountAndDescription("0",amountadded,description);
                            amountRef.set(amountAndDescription2)
                                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void aVoid) {
                                            Intent mainIntent = new Intent(OutgoingTransfer.this,MainActivity.class);
                                            startActivity(mainIntent);
                                            finish();
                                            Toast.makeText(OutgoingTransfer.this,"Amount Added",Toast.LENGTH_LONG).show();
                                        }
                                    })
                                    .addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {
                                            Toast.makeText(OutgoingTransfer.this,"Error Occured",Toast.LENGTH_LONG).show();
                                            Log.i("On Failure",e.toString());
                                        }
                                    });
                        }
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(OutgoingTransfer.this,"Error",Toast.LENGTH_LONG).show();
                    }
                });
    }
}

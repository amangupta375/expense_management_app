package com.example.expensemanagementapp;

import android.content.Intent;

import androidx.annotation.FractionRes;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private DocumentReference amountRef;
    private FirebaseAuth mAuth;
    private Toolbar mtoolbar;
    private TextView totalAmountReceived;
    private TextView totalAmountSent;
    FirebaseUser currentUser;
    List<String> myList = new ArrayList<>();
    ListView myListView;
    ArrayAdapter adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mAuth = FirebaseAuth.getInstance();
        mtoolbar = (Toolbar) findViewById(R.id.main_page_toolbar);
        totalAmountReceived = findViewById(R.id.total_amount_received);
        totalAmountSent = findViewById(R.id.total_amount_sent);
        currentUser = mAuth.getCurrentUser();
        myListView = (ListView) findViewById(R.id.list_view);
        String id;
        if(currentUser != null) {
            id = currentUser.getUid();
            amountRef = db.document("Users/" + id);
        }
        setSupportActionBar(mtoolbar);
        getSupportActionBar().setTitle("Expense Management");
    }
    @Override
    public void onStart() {
        super.onStart();
        currentUser = mAuth.getCurrentUser();
        if(currentUser == null)
        {
            sendtostart();
        }
        else
        {
            amountRef.get()
                    .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                        @Override
                        public void onSuccess(DocumentSnapshot documentSnapshot) {
                            if(documentSnapshot.exists()) {
                                AmountAndDescription amountAndDescription = documentSnapshot.toObject(AmountAndDescription.class);
                                myList = amountAndDescription.getDescription();
                                Collections.reverse(myList);
                                adapter = new ArrayAdapter(MainActivity.this,android.R.layout.simple_list_item_1,myList);
                                myListView.setAdapter(adapter);
                                totalAmountReceived.setText(amountAndDescription.getIncomingAmount());
                                totalAmountSent.setText((amountAndDescription.getOutgoingAmount()));
                            }
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(MainActivity.this,"Error Occured",Toast.LENGTH_LONG).show();
                            Log.i("On Failure",e.toString());
                        }
                    });
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        getMenuInflater().inflate(R.menu.main_menu,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        super.onOptionsItemSelected(item);
        if(item.getItemId()==R.id.log_out_button)
        {
            FirebaseAuth.getInstance().signOut();
            sendtostart();
        }
        else if(item.getItemId()==R.id.outgoing_transaction)
        {
            Intent outgoingTransactionIntent = new Intent(MainActivity.this,OutgoingTransfer.class);
            startActivity(outgoingTransactionIntent);
            finish();
        }
        else if(item.getItemId()==R.id.incoming_transaction)
        {
            Intent incomingTransactionIntent  = new Intent(MainActivity.this,IncomingTransfer.class);
            startActivity(incomingTransactionIntent);
            finish();
        }
        return true;
    }

    public void sendtostart()
    {
        Intent startIntent = new Intent(MainActivity.this,StartActivity.class);
        startActivity(startIntent);
        finish();
    }
}

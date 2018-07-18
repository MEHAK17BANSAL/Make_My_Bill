package com.mehak.make_my_bill.ui;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.mehak.make_my_bill.R;

public class MainActivity extends AppCompatActivity {
    FirebaseFirestore firestore;
    FirebaseAuth auth;
    String uid;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        android.support.v7.widget.Toolbar toolbar = (android.support.v7.widget.Toolbar)findViewById(R.id.app_bar);
        setSupportActionBar(toolbar);
        firestore = FirebaseFirestore.getInstance();
        auth = FirebaseAuth.getInstance();
        uid = auth.getCurrentUser().getUid();
    }

    public void product(View view) {
        Intent intent=new Intent(MainActivity.this,inventory.class);
        startActivity(intent);
        finish();
    }

    public void innvoice(View view) {
        Intent intent=new Intent(MainActivity.this,InvoiceGenerator.class);
        startActivity(intent);
        finish();
    }
}

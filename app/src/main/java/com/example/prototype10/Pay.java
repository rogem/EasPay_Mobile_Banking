package com.example.prototype10;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;

public class Pay extends AppCompatActivity {

    DatabaseReference reference;
    String userID, fname,key;
    Bundle bundle,userbundle;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pay);

        bundle = getIntent().getExtras();
        userID = bundle.getString("Email");
        fname = bundle.getString("FirstName");

        userbundle = new Bundle();
        userbundle.putString("FirstName", fname);
        userbundle.putString("Email", userID);
    }

    public void btnpyback(View view) {
        Toast.makeText(this, "Back Clicked", Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(getApplicationContext(),HomeScreen.class);
        intent.putExtras(userbundle);
        startActivity(intent);
    }

    public void btnupldqr(View view) {
        Toast.makeText(this, "Upload QR Clicked ", Toast.LENGTH_SHORT).show();
    }

    public void btnscnqr(View view) {
        Toast.makeText(this, "Scan QR Clicked ", Toast.LENGTH_SHORT).show();
    }

}
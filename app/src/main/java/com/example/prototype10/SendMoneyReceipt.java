package com.example.prototype10;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

public class SendMoneyReceipt extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_send_money_receipt);
    }
    public void btnbckhme(View view) {
        Toast.makeText(this, "× Clicked", Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(this, HomeScreen.class);
        startActivity(intent);
    }
}
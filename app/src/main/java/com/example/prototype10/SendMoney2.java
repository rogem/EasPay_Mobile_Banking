package com.example.prototype10;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

public class SendMoney2 extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_send_money2);
    }

    public void btnsndback2(View view) {
        Toast.makeText(this, "Back Clicked", Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(this, SendMoney.class);
        startActivity(intent);
    }

    public void btnprcd(View view) {
        Toast.makeText(this, "Proceed Clicked", Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(this, SendMoneyReceipt.class);
        startActivity(intent);
    }

    public void btnbckhme(View view) {
    }
}
package com.example.prototype10;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

public class Menu extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);
    }
    public void btmenubck(View view) {
        Toast.makeText(this, "Back Clicked", Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(this, HomeScreen.class);
        startActivity(intent);
    }

    public void btnmnedit(View view) {
        Toast.makeText(this, "Edit Information Clicked", Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(this, EditInfo.class);
        startActivity(intent);
    }

    public void btnsttng(View view) {
        Toast.makeText(this, "Edit Information Clicked", Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(this, Setting.class);
        startActivity(intent);
    }

    public void sgnout(View view) {
        Toast.makeText(this, "Sign Out Clicked", Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(this, LogIn.class);
        startActivity(intent);
    }
}
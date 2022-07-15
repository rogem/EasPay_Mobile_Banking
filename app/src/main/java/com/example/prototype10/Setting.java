package com.example.prototype10;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

public class Setting extends AppCompatActivity {

    String userID, fname,key;
    Bundle bundle,userbundle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);

        bundle = getIntent().getExtras();
        userID = bundle.getString("Email");
        fname = bundle.getString("FirstName");

        userbundle = new Bundle();
        userbundle.putString("FirstName", fname);
        userbundle.putString("Email", userID);
    }

    public void btnsttngbck(View view) {
        Toast.makeText(this, "Back Clicked", Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(getApplicationContext(),HomeScreen.class);
        intent.putExtras(userbundle);
        startActivity(intent);
    }

    public void chpwd(View view) {
        Toast.makeText(this, "Change Password Clicked", Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(getApplicationContext(),ChangePassword.class);
        intent.putExtras(userbundle);
        startActivity(intent);
    }
}
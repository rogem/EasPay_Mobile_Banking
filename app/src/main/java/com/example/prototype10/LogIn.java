package com.example.prototype10;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class LogIn extends AppCompatActivity {

    EditText Email,Password;
    FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_log_in);

        Email =(EditText) findViewById(R.id.EdtTxtEmailAddress);
        Password =(EditText) findViewById(R.id.EdtTxPassword);

        mAuth = FirebaseAuth.getInstance();

    }

    public void signups(View view) {
        Toast.makeText(this, "Signup Clicked", Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(this, SignUp.class);
        startActivity(intent);

    }

    public void login(View view) {
        UserLogin();


    }

    private void UserLogin() {
        String email = Email.getText().toString().trim();
        String password = Password.getText().toString().trim();

        Bundle bundle = new Bundle();
        bundle.putString("Email", email);

        if (email.isEmpty()){
            Email.setError("Email is required!");
            Email.requestFocus();
            return;
        }
        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
            Email.setError("PLease enter a valid Email!");
            Email.requestFocus();
            return;
        }
        if (password.isEmpty()){
            Password.setError("Password is required!");
            Password.requestFocus();
            return;
        }
        if (password.length() < 8){
            Password.setError("Min password length is 8 character!");
            Password.requestFocus();
            return;
        }

        mAuth.signInWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()){



                    Toast.makeText(LogIn.this,"User has been Login successfully",Toast.LENGTH_LONG).show();
                    Intent intent = new Intent(getApplicationContext(),HomeScreen.class);
                    intent.putExtras(bundle);
                    startActivity(intent);
//                    startActivity(new Intent(LogIn.this,HomeScreen.class));
                }else {
                    Toast.makeText(LogIn.this,"Failed to login! Please check your credentials",Toast.LENGTH_LONG).show();
                }
            }
        });

    }
}
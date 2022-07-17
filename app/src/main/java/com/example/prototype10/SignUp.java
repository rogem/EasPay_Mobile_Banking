
package com.example.prototype10;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

public class SignUp extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
    }

    public void btn1 (View view){
        Toast.makeText(this, "Student Clicked", Toast.LENGTH_SHORT).show();
        studentLogIn();
    }
    private void studentLogIn() {
        Intent intent = new Intent(this, StudentSignUp.class);
        startActivity(intent);
    }

    public void btn2(View view) {
        Toast.makeText(this, "Faculty Clicked", Toast.LENGTH_SHORT).show();
        FacultyLogIn();
    }
    private void FacultyLogIn() {
        Intent intent = new Intent(this, FacultySignUp.class);
        startActivity(intent);
    }
}
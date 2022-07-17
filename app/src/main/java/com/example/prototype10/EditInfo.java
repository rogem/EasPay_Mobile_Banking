package com.example.prototype10;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.MutableData;
import com.google.firebase.database.Query;
import com.google.firebase.database.Transaction;
import com.google.firebase.database.ValueEventListener;

public class EditInfo extends AppCompatActivity {

    EditText etfirstname,etlastname,etgender,etage,etemployeenumber,etcontactnumber,etemail,etpassword,etbalance,etuserstatus;
//     String userID;
//     Button btnsv;
//     FirebaseUser user;
//     DatabaseReference referenceStudent,referenceFaculty;

    String FirstName,LastName,Gender,Age,EmployeeNumber,ContactNumber,Email,Password,Balance,AUserStatus;

    FirebaseAuth authProfile;

    DatabaseReference reference;
    String userID, fname,key;
    Bundle bundle,userbundle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_info);

        etfirstname =(EditText) findViewById(R.id.ETFirstName);
        etlastname =(EditText) findViewById(R.id.ETLastName);
        etgender = (EditText) findViewById(R.id.ETGender);
        etage = (EditText) findViewById(R.id.ETAge);
        etemployeenumber=(EditText) findViewById(R.id.ETEmployeeNumber);
        etcontactnumber =(EditText) findViewById(R.id.ETContactNumber);
        etemail =(EditText) findViewById(R.id.ETEmail);
        etpassword =(EditText) findViewById(R.id.ETPassword);
        etbalance =(EditText) findViewById(R.id.ETBalance);
        etuserstatus=(EditText) findViewById(R.id.ETUserStatus);

        bundle = getIntent().getExtras();
        userID = bundle.getString("Email");
        fname = bundle.getString("FirstName");

        userbundle = new Bundle();
        userbundle.putString("FirstName", fname);
        userbundle.putString("Email", userID);

        authProfile = FirebaseAuth.getInstance();
        FirebaseUser firebaseUser = authProfile.getCurrentUser();

        showProfile(firebaseUser);

        Button buttonUpdateProfile = findViewById(R.id.btnsave);
        buttonUpdateProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateProfile(firebaseUser);
            }
        });

    }

    private void updateProfile(FirebaseUser firebaseUser) {
        if (TextUtils.isEmpty(FirstName)){
            etfirstname.setError("First Name is required");
            etfirstname.requestFocus();
        }else if (TextUtils.isEmpty(LastName)){
            etlastname.setError("First Name is required");
            etlastname.requestFocus();
        }else if(TextUtils.isEmpty(Gender)){
            etgender.setError("Gender is required");
            etgender.requestFocus();
        }else if(TextUtils.isEmpty(Age)){
            etage.setError("Age is required");
            etage.requestFocus();
        }else if(TextUtils.isEmpty(EmployeeNumber)){
            etemployeenumber.setError("Employee Number is required");
            etemployeenumber.requestFocus();
        }else if (!Patterns.EMAIL_ADDRESS.matcher(Email).matches()){
            etemail.setError("Please valid email");
            etemail.requestFocus();
        }else {
            FirstName = etfirstname.getText().toString();
            LastName = etlastname.getText().toString();
            Gender = etgender.getText().toString();
            Age = etage.getText().toString();
            EmployeeNumber = etemployeenumber.getText().toString();
            ContactNumber = etcontactnumber.getText().toString();
            Email = etemail.getText().toString();
            Password = etpassword.getText().toString();
            Balance = etbalance.getText().toString();
            AUserStatus = etuserstatus.getText().toString();

            reference = FirebaseDatabase.getInstance().getReference();
            Query query = reference.child("User").orderByChild("Email").equalTo(userID);
            query.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    for (DataSnapshot ds: snapshot.getChildren()){
                        key = ds.getKey();

                        User writeUserDetails = new User(FirstName,LastName,Gender,Age,EmployeeNumber,ContactNumber,Email,Password,Balance,AUserStatus);

                        DatabaseReference referenceProfile = FirebaseDatabase.getInstance().getReference("User");

                        String userID = key;

                        referenceProfile.child(userID).setValue(writeUserDetails).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isSuccessful()){

                                    UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder().
                                            setDisplayName(Email).build();
                                    firebaseUser.updateProfile(profileUpdates);

                                    Toast.makeText(EditInfo.this,"Edit Successful", Toast.LENGTH_LONG).show();

                                    Intent intent = new Intent(getApplicationContext(),HomeScreen.class);
                                    intent.putExtras(userbundle);
                                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                                    startActivity(intent);
                                    finish();
                                }else {
                                    try {
                                        throw task.getException();
                                    }catch (Exception e){
                                        Toast.makeText(EditInfo.this, e.getMessage(), Toast.LENGTH_LONG).show();
                                    }
                                }
                            }
                        });

                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });


        }
    }

    private void showProfile(@NonNull FirebaseUser firebaseUser) {

        reference = FirebaseDatabase.getInstance().getReference();
        Query query = reference.child("User").orderByChild("Email").equalTo(userID);
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot ds: snapshot.getChildren()){
                    key = ds.getKey();

                    String userIDofRegistered = key;

                    DatabaseReference referenceProfile = FirebaseDatabase.getInstance().getReference("User");

                    referenceProfile.child(userIDofRegistered).addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {


                            if (snapshot.exists()){

                                FirstName = snapshot.child("FirstName").getValue().toString();
                                LastName = snapshot.child("LastName").getValue().toString();
                                Gender = snapshot.child("Gender").getValue().toString();
                                Age = snapshot.child("Age").getValue().toString();
                                EmployeeNumber = snapshot.child("EmployeeNumber").getValue().toString();
                                ContactNumber = snapshot.child("ContactNumber").getValue().toString();
                                Email = snapshot.child("Email").getValue().toString();
                                Password = snapshot.child("Password").getValue().toString();
                                Balance = snapshot.child("Balance").getValue().toString();
                                AUserStatus = snapshot.child("AUserStatus").getValue().toString();


                                etfirstname.setText(FirstName);
                                etlastname.setText(LastName);
                                etgender.setText(Gender);
                                etage.setText(Age);
                                etemployeenumber.setText(EmployeeNumber);
                                etcontactnumber.setText(ContactNumber);
                                etemail.setText(Email);
                                etpassword.setText(Password);
                                etbalance.setText(Balance);
                                etuserstatus.setText(AUserStatus);

                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {
                            Toast.makeText(EditInfo.this,"Something Wrong Happen", Toast.LENGTH_LONG).show();
                        }
                    });

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

    public void btnedtbck(View view) {
        Toast.makeText(this, "Back Clicked", Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(getApplicationContext(),HomeScreen.class);
        intent.putExtras(userbundle);
        startActivity(intent);
    }
}

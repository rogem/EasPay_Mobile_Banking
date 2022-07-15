package com.example.prototype10;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.EmailAuthCredential;
import com.google.firebase.auth.EmailAuthProvider;
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

public class ChangePassword extends AppCompatActivity {
        private FirebaseAuth authProfile;
        private EditText editTextPwdCurr,editTextPwdNew,editTextPwdConfirmNew;
        private Button btnChangePass,btnReAuthenticate;
        private String userPwdCurr;

    DatabaseReference reference;
    String userID, fname,key;
    Bundle bundle,userbundle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_password);

        editTextPwdNew = findViewById(R.id.EditTextNewPassword);
        editTextPwdCurr = findViewById(R.id.EditTextOldPassword);
        editTextPwdConfirmNew = findViewById(R.id.EditTextRetypePassword);
        btnReAuthenticate = findViewById(R.id.ButtonAuthenticate);
        btnChangePass = findViewById(R.id.ButtonSave);

        bundle = getIntent().getExtras();
        userID = bundle.getString("Email");
        fname = bundle.getString("FirstName");

        userbundle = new Bundle();
        userbundle.putString("FirstName", fname);
        userbundle.putString("Email", userID);

        editTextPwdNew.setEnabled(false);
        editTextPwdConfirmNew.setEnabled(false);
        btnChangePass.setEnabled(false);

        authProfile = FirebaseAuth.getInstance();
        FirebaseUser firebaseUser = authProfile.getCurrentUser();

        if (firebaseUser.equals("")){
            Toast.makeText(ChangePassword.this,"Something went wrong! User's details not available", Toast.LENGTH_SHORT).show();

            Intent intent = new Intent(getApplicationContext(),HomeScreen.class);
            intent.putExtras(userbundle);
            startActivity(intent);
            finish();
        }else {
            reAuthenticateUser(firebaseUser);
        }
    }

    private void reAuthenticateUser(FirebaseUser firebaseUser) {
        btnReAuthenticate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                userPwdCurr = editTextPwdCurr.getText().toString();

                if (TextUtils.isEmpty(userPwdCurr)) {
                    Toast.makeText(ChangePassword.this,"Password is needed", Toast.LENGTH_SHORT).show();
                    editTextPwdCurr.setError("Please enter your current password to authenticate");
                    editTextPwdCurr.requestFocus();
                }else {
                    AuthCredential credential = EmailAuthProvider.getCredential(firebaseUser.getEmail(),userPwdCurr);

                    firebaseUser.reauthenticate(credential).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()){
                                editTextPwdCurr.setEnabled(false);
                                editTextPwdNew.setEnabled(true);
                                editTextPwdConfirmNew.setEnabled(true);

                                btnReAuthenticate.setEnabled(false);
                                btnChangePass.setEnabled(true);

                                Toast.makeText(ChangePassword.this,"Password has been verified." + "Change password now.", Toast.LENGTH_SHORT).show();

                                btnChangePass.setBackgroundTintList(ContextCompat.getColorStateList(ChangePassword.this, R.color.teal_200));
                                btnChangePass.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        changePwd(firebaseUser);
                                    }
                                });
                            }else {
                                try {
                                    throw task.getException();
                                }catch (Exception e){
                                    Toast.makeText(ChangePassword.this,e.getMessage(),Toast.LENGTH_SHORT).show();
                                }
                            }
                        }
                    });
                }
            }
        });
    }

    private void changePwd(FirebaseUser firebaseUser) {
        String userPwdNew = editTextPwdNew.getText().toString();
        String userPwdConfirmNew = editTextPwdConfirmNew.getText().toString();

        if (TextUtils.isEmpty(userPwdNew)){
            Toast.makeText(ChangePassword.this,"New Password is needed",Toast.LENGTH_SHORT).show();
            editTextPwdNew.setError("Please enter your new password");
            editTextPwdNew.requestFocus();
        }else if (TextUtils.isEmpty(userPwdConfirmNew)){
            Toast.makeText(ChangePassword.this,"Please Confirm your new password",Toast.LENGTH_SHORT).show();
            editTextPwdConfirmNew.setError("Please re-enter your new password");
            editTextPwdConfirmNew.requestFocus();
        }
        else if(!userPwdNew.matches(userPwdConfirmNew)){
            Toast.makeText(ChangePassword.this,"Password did not match",Toast.LENGTH_SHORT).show();
            editTextPwdConfirmNew.setError("Please re-enter your new password");
            editTextPwdConfirmNew.requestFocus();
        }else if (!userPwdConfirmNew.matches(userPwdNew)){
            Toast.makeText(ChangePassword.this," New Password cannot be same as old password",Toast.LENGTH_SHORT).show();
            editTextPwdNew.setError("New Password cannot be same as old password");
            editTextPwdNew.requestFocus();
        }
        else {
            firebaseUser.updatePassword(userPwdNew).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if (task.isSuccessful()){

                        reference = FirebaseDatabase.getInstance().getReference();
                        Query query = reference.child("User").orderByChild("Email").equalTo(userID);
                        query.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                for (DataSnapshot ds: snapshot.getChildren()){
                                    key = ds.getKey();

                                    DatabaseReference update = FirebaseDatabase.getInstance().getReference();
                                    Query userquery = update.child("User").orderByChild("Password").equalTo(userPwdCurr);
                                    userquery.addListenerForSingleValueEvent(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                                            for (DataSnapshot change : snapshot.getChildren()){
                                                change.getRef().child("Password").setValue(userPwdNew);
                                            }
                                        }

                                        @Override
                                        public void onCancelled(@NonNull DatabaseError error) {

                                        }
                                    });

                                }
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {

                            }
                        });



                        Toast.makeText(ChangePassword.this,"Password has been change",Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(ChangePassword.this,HomeScreen.class);
                        intent.putExtras(userbundle);
                        startActivity(intent);
                        finish();
                    }else {
                        try {
                            throw task.getException();
                        }catch (Exception e){
                            Toast.makeText(ChangePassword.this,e.getMessage(),Toast.LENGTH_SHORT).show();
                        }
                    }
                }
            });
        }
    }

    public void btnchbck(View view) {
        Toast.makeText(this, "Back Clicked", Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(getApplicationContext(),Setting.class);
        intent.putExtras(userbundle);
        startActivity(intent);
    }
}
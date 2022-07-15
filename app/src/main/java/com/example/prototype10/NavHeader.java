package com.example.prototype10;

import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class NavHeader extends AppCompatActivity {

    TextView userfirstname,userlastname,useremail,usernumber;
    String userID;

    FirebaseUser user;
    DatabaseReference referenceStudent,referenceFaculty;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.nav_header);

        userfirstname = (TextView) findViewById(R.id.nav_user_fname);
        userlastname = (TextView) findViewById(R.id.nav_user_lname);
        useremail = (TextView) findViewById(R.id.nav_user_email);


        user = FirebaseAuth.getInstance().getCurrentUser();
        referenceStudent = FirebaseDatabase.getInstance().getReference("User");
//        referenceFaculty = FirebaseDatabase.getInstance().getReference("FacultySignUpConnectFirebase");
        userID = user.getUid();

        referenceStudent.child(userID).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                User userProfile =snapshot.getValue(User.class);


                if (userProfile != null ){
                    String firstname = userProfile.FirstName;
                    String lastname = userProfile.LastName;
                    String email = userProfile.Email;


                    userfirstname.setText(firstname);
                    userlastname.setText(lastname);
                    useremail.setText(email);


                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(NavHeader.this,"Something Wrong Happen", Toast.LENGTH_LONG).show();
            }
        });

//        referenceFaculty.child(userID).addListenerForSingleValueEvent(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot snapshot) {
//                FacultySignUpConnectFirebase userProfile =snapshot.getValue(FacultySignUpConnectFirebase.class);
//
//                if (userProfile != null ){
//                    String firstname = userProfile.FirstName;
//                    String lastname = userProfile.LastName;
//                    String email = userProfile.Email;
//
//                    userfirstname.setText(firstname);
//                    userlastname.setText(lastname);
//                    useremail.setText(email);
//
//                }
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError error) {
//                Toast.makeText(NavHeader.this,"Something Wrong Happen", Toast.LENGTH_LONG).show();
//            }
//        });
    }

}
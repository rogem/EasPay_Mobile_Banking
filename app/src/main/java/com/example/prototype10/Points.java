package com.example.prototype10;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.MutableData;
import com.google.firebase.database.Query;
import com.google.firebase.database.Transaction;
import com.google.firebase.database.ValueEventListener;

public class Points extends AppCompatActivity {

    DatabaseReference reference;
    String userID, fname,key;
    Bundle bundle,userbundle;

    TextView displaypoints;
    EditText enterpoints;
    String pointbalance,getenterpoints,setbalance;

    Double totaladdbalance,getbalance,getpoints;

    String subpoints,pointsconvertion;
    String senderName,receiverNumber,balance,getMeassage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_points);

        bundle = getIntent().getExtras();
        userID = bundle.getString("Email");
        fname = bundle.getString("FirstName");

        userbundle = new Bundle();
        userbundle.putString("FirstName", fname);
        userbundle.putString("Email", userID);

        displaypoints = findViewById(R.id.txtPoints);
        enterpoints = findViewById(R.id.enterPoints);



        reference = FirebaseDatabase.getInstance().getReference();
        Query query = reference.child("User").orderByChild("Email").equalTo(userID);
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot ds: snapshot.getChildren()){
                    key = ds.getKey();

                    DatabaseReference referenceProfile = FirebaseDatabase.getInstance().getReference("Points");

                    referenceProfile.child(key).addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {


                            if (snapshot.exists()){

                                pointbalance = snapshot.child("userPoints").getValue().toString();

                                getbalance = Double.parseDouble(pointbalance);
                                setbalance = String.valueOf(getbalance);
                                displaypoints.setText(setbalance);

                            }
                            Button redeempoints = findViewById(R.id.btnRedeem);
                            redeempoints.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {

                                    getenterpoints = enterpoints.getText().toString();
                                    getpoints = Double.parseDouble(getenterpoints);
                                    totaladdbalance = 0.1 * getpoints;

                                    subpoints = "-" + getpoints;
                                    pointsconvertion = String.valueOf(totaladdbalance);

                                    senderName = "Points  " + subpoints;
                                    receiverNumber = "Amount ₱" + pointsconvertion;
                                    getMeassage ="Convert Points to Cash";

                                    if (getpoints < 10 ){
                                        enterpoints.setError("The Minimum Conversion of Points is 10");
                                        enterpoints.requestFocus();
                                        return;
                                    }
                                    else if (getenterpoints == ""){
                                        enterpoints.setError("The Minimum Conversion of Points is 10");
                                        enterpoints.requestFocus();
                                        return;
                                    }
                                    else {

                                        String points = enterpoints.getText().toString();
                                        Integer pointstoSub = Integer.parseInt(points);
                                        DatabaseReference ref = FirebaseDatabase.getInstance().getReference()
                                                .child("Points").child(key).child("userPoints");

                                        ref.runTransaction(new Transaction.Handler() {
                                            @NonNull
                                            @Override
                                            public Transaction.Result doTransaction(@NonNull MutableData currentData) {
                                                Object currentMoney = currentData.getValue();
                                                int totalMoney = 0;
                                                if (currentMoney == null){
                                                    totalMoney = pointstoSub;
                                                }else {
                                                    totalMoney = Integer.parseInt(String.valueOf(currentMoney)) - pointstoSub;

//                                                    Intent intent = new Intent(getApplicationContext(),HomeScreen.class);
//                                                        intent.putExtras(userbundle);
//                                                        startActivity(intent);
//                                                        finish();
                                                }
                                                currentData.setValue(totalMoney);
                                                return Transaction.success(currentData);
                                            }

                                            @Override
                                            public void onComplete(@Nullable DatabaseError error, boolean committed, @Nullable DataSnapshot currentData) {

                                            }
                                        });

                                        DatabaseReference refpoints = FirebaseDatabase.getInstance().getReference()
                                                .child("User").child(key).child("Balance");

                                        refpoints.runTransaction(new Transaction.Handler() {
                                            @NonNull
                                            @Override
                                            public Transaction.Result doTransaction(@NonNull MutableData currentData) {
                                                Object currentMoney = currentData.getValue();
                                                double totalMoney = 0;
                                                if (currentMoney == null){
                                                    totalMoney = totaladdbalance;
                                                }else {
                                                    totalMoney = Double.parseDouble(String.valueOf(currentMoney)) + totaladdbalance;

                                                    balance = String.valueOf(totalMoney);
                                                    DatabaseReference reference = FirebaseDatabase.getInstance().getReference().child("History").child(key);
                                                    HistoryModel historyModel =  new HistoryModel(senderName,receiverNumber,balance,getMeassage);
                                                    reference.push().setValue(historyModel);

                                                    Intent intent = new Intent(getApplicationContext(),HomeScreen.class);
                                                    intent.putExtras(userbundle);
                                                    startActivity(intent);
                                                    finish();
                                                }
                                                currentData.setValue(totalMoney);
                                                return Transaction.success(currentData);
                                            }

                                            @Override
                                            public void onComplete(@Nullable DatabaseError error, boolean committed, @Nullable DataSnapshot currentData) {

                                            }
                                        });

                                    }

                                }
                            });
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {
                            Toast.makeText(Points.this,"Something Wrong Happen", Toast.LENGTH_LONG).show();
                        }
                    });



                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

//    public void btnredeem(View view) {
//        Toast.makeText(this, "Points Converted", Toast.LENGTH_SHORT).show();
//    }
    public void btnpsback(View view) {
        Toast.makeText(this, "Back Clicked", Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(getApplicationContext(),HomeScreen.class);
        intent.putExtras(userbundle);
        startActivity(intent);
    }
}
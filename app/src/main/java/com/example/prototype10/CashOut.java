package com.example.prototype10;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.MutableData;
import com.google.firebase.database.Query;
import com.google.firebase.database.Transaction;
import com.google.firebase.database.ValueEventListener;

public class CashOut extends AppCompatActivity {

    EditText mobilenumber,amount,message;
    DatabaseReference reference;
    String userID, fname,key;
    Bundle bundle,userbundle;

    String senderName,receiverNumber,balance,getMeassage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cash_out);
        mobilenumber = findViewById(R.id.EditTextMobileNumber);
        amount = findViewById(R.id.EditAmount);
        message = findViewById(R.id.EditTextMessage);

        bundle = getIntent().getExtras();
        userID = bundle.getString("Email");
        fname = bundle.getString("FirstName");

        userbundle = new Bundle();
        userbundle.putString("FirstName", fname);
        userbundle.putString("Email", userID);

        reference = FirebaseDatabase.getInstance().getReference();
        Query query = reference.child("User").orderByChild("Email").equalTo(userID);
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot ds: snapshot.getChildren()){
                    key = ds.getKey();

                    getMeassage="CashOut";

                    Button buttonUpdateProfile = findViewById(R.id.btnSendMoney);
                    buttonUpdateProfile.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {


                            String money = amount.getText().toString();
                            Integer moneyToAdd = Integer.parseInt(money);
                            DatabaseReference ref = FirebaseDatabase.getInstance().getReference()
                                    .child("User").child(key).child("Balance");
                            receiverNumber = mobilenumber.getText().toString();
                            senderName = key;
                            balance = "-"+money;



                            ref.runTransaction(new Transaction.Handler() {
                                @NonNull
                                @Override
                                public Transaction.Result doTransaction(@NonNull MutableData currentData) {
                                    Object currentMoney = currentData.getValue();
                                    int totalMoney = 0;
                                    if (currentMoney == null){
                                        totalMoney = moneyToAdd;
                                    }else {
                                        totalMoney = Integer.parseInt(String.valueOf(currentMoney)) - moneyToAdd;

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
                    });

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


    }

    public void btnbchme(View view) {
        Toast.makeText(this, "Back Clicked", Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(getApplicationContext(),HomeScreen.class);
        intent.putExtras(userbundle);
        startActivity(intent);
    }

}
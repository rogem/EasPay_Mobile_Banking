package com.example.prototype10;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.MutableData;
import com.google.firebase.database.Query;
import com.google.firebase.database.Transaction;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class SendMoney extends AppCompatActivity {

    String senderUserId,receiverUserId,senderName,receiverNumber;
    long senderBalance,receiverBalance;
    DatabaseReference mReference = FirebaseDatabase.getInstance().getReference().child("User");
    DatabaseReference senderReference,receiverReference;

    Spinner userSpinner;
    ArrayAdapter<String> arrayAdapter;
    ArrayList<String> spinnerUserList;
    ArrayList<String> getSpinnerUserIdList;

    EditText enterBalance,enterMessage;
    Button sendBtn;
    String getMeassage;

    FirebaseUser firebaseUser;
    FirebaseAuth authProfile;

    DatabaseReference reference;
    String userID, fname,key;
    Bundle bundle,userbundle;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_send_money);

        authProfile = FirebaseAuth.getInstance();
        firebaseUser = authProfile.getCurrentUser();


        bundle = getIntent().getExtras();
        userID = bundle.getString("Email");
        fname = bundle.getString("FirstName");

        userbundle = new Bundle();
        userbundle.putString("FirstName", fname);
        userbundle.putString("Email", userID);


//       senderUserId = firebaseUser.getUid();
       senderReference =  FirebaseDatabase.getInstance().getReference("User");
       receiverReference = FirebaseDatabase.getInstance().getReference().child("User");
       enterBalance = findViewById(R.id.EditAmount);
       enterMessage = findViewById(R.id.EditTextMessage);
       sendBtn = findViewById(R.id.btnSendMoney);

       enterBalance.setText(fname);

       userSpinner = findViewById(R.id.userMobileNumber);
       spinnerUserList = new ArrayList<>();
       getSpinnerUserIdList = new ArrayList<>();

       retrieveData();
       arrayAdapter = new ArrayAdapter<String>(getApplicationContext(),
               R.layout.custom_spinner_me, spinnerUserList);
       arrayAdapter.setDropDownViewResource(R.layout.drop_down_spinner);
       userSpinner.setAdapter(arrayAdapter);


    }

    @Override
    protected void onStart(){
        super.onStart();

        reference = FirebaseDatabase.getInstance().getReference();
        Query query = reference.child("User").orderByChild("Email").equalTo(userID);
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot ds: snapshot.getChildren()){
                    key = ds.getKey();

                    senderReference.child(key).addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            if (snapshot.exists()){
                                senderBalance = Long.parseLong(snapshot.child("Balance").getValue().toString());
                                senderName = snapshot.child("FirstName").getValue().toString();
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


    }

    @Override
    protected void onResume(){
        super.onResume();
        userSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                receiverUserId = getSpinnerUserIdList.get(position);
                receiverNumber = parent.getSelectedItem().toString();
                receiverReference.child(receiverUserId).addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if (snapshot.exists()){
                            receiverBalance = Long.parseLong(snapshot.child("Balance").getValue().toString());
                        }

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        sendBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final  String balance = enterBalance.getText().toString().trim();
                getMeassage = enterMessage.getText().toString().trim();
                String submoney= "-"+balance;

                if (TextUtils.isEmpty(balance)|| Long.valueOf(balance) == 0){
                    enterBalance.setError("Minimun amount ₱1");
                    return;
                }
                else if (receiverNumber == senderName){
                    reference = FirebaseDatabase.getInstance().getReference();
                    Query query = reference.child("User").orderByChild("Email").equalTo(userID);
                    query.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            for (DataSnapshot ds: snapshot.getChildren()){
                                key = ds.getKey();

                                CustomDialog customDialog = new CustomDialog("Payment Successful");
                                customDialog.show(getSupportFragmentManager(),"example");

                                DatabaseReference reference = FirebaseDatabase.getInstance().getReference().child("History").child(key);
                                HistoryModel historyModel =  new HistoryModel(senderName,receiverNumber,submoney,getMeassage);
                                reference.push().setValue(historyModel);

                                String money = "1";
                                Integer moneyToAdd = Integer.parseInt(money);
                                DatabaseReference ref = FirebaseDatabase.getInstance().getReference()
                                        .child("Points").child(key).child("userPoints");

                                ref.runTransaction(new Transaction.Handler() {
                                    @NonNull
                                    @Override
                                    public Transaction.Result doTransaction(@NonNull MutableData currentData) {
                                        Object currentMoney = currentData.getValue();
                                        int totalMoney = 0;
                                        if (currentMoney == null){
                                            totalMoney = moneyToAdd;
                                        }else {
                                            totalMoney = Integer.parseInt(String.valueOf(currentMoney)) + moneyToAdd;

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

                                Intent intent = new Intent(getApplicationContext(),HomeScreen.class);
                                intent.putExtras(userbundle);
                                startActivity(intent);
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });

                }
                else if (Long.parseLong(balance)>senderBalance){
                    CustomDialog customDialog = new CustomDialog("Insufficient Balance");
                    customDialog.show(getSupportFragmentManager(),"example");
                }
                else if (senderName != receiverNumber){
                    reference = FirebaseDatabase.getInstance().getReference();
                    Query query = reference.child("User").orderByChild("Email").equalTo(userID);
                    query.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            for (DataSnapshot ds: snapshot.getChildren()){
                                key = ds.getKey();

                                senderReference.child(key).child("Balance").setValue(senderBalance-(Long.parseLong(balance)));
                                DatabaseReference localReceiverReference = receiverReference.child(receiverUserId);
                                localReceiverReference.child("Balance").setValue(receiverBalance+(Long.parseLong(balance)))
                                        .addOnCompleteListener(SendMoney.this, new OnCompleteListener<Void>() {
                                            @Override
                                            public void onComplete(@NonNull Task<Void> task) {
                                                enterBalance.getText().clear();
                                                InputMethodManager inputMethodManager = (InputMethodManager)getSystemService(SendMoney.INPUT_METHOD_SERVICE);
                                                inputMethodManager.hideSoftInputFromWindow(enterBalance.getWindowToken(),0);
                                                CustomDialog customDialog = new CustomDialog("Payment Successful");
                                                customDialog.show(getSupportFragmentManager(),"example");

                                                DatabaseReference reference = FirebaseDatabase.getInstance().getReference().child("History").child(key);
                                                HistoryModel historyModel =  new HistoryModel(senderName,receiverNumber,submoney,getMeassage);
                                                reference.push().setValue(historyModel);

                                                String money = "1";
                                                Integer moneyToAdd = Integer.parseInt(money);
                                                DatabaseReference ref = FirebaseDatabase.getInstance().getReference()
                                                        .child("Points").child(key).child("userPoints");

                                                ref.runTransaction(new Transaction.Handler() {
                                                    @NonNull
                                                    @Override
                                                    public Transaction.Result doTransaction(@NonNull MutableData currentData) {
                                                        Object currentMoney = currentData.getValue();
                                                        int totalMoney = 0;
                                                        if (currentMoney == null){
                                                            totalMoney = moneyToAdd;
                                                        }else {
                                                            totalMoney = Integer.parseInt(String.valueOf(currentMoney)) + moneyToAdd;

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

                                                Intent intent = new Intent(getApplicationContext(),HomeScreen.class);
                                                intent.putExtras(userbundle);
                                                startActivity(intent);
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
        });
    }

    private void retrieveData() {
        mReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot user : snapshot.getChildren()){
                    spinnerUserList.add(user.child("ContactNumber").getValue().toString());
                    getSpinnerUserIdList.add(user.child("FirstName").getValue().toString());
                }
                arrayAdapter.notifyDataSetChanged();
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
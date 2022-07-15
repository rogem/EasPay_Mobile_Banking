package com.example.prototype10;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class TransactionHistory extends AppCompatActivity {

    DatabaseReference reference;
    String userID, fname,key;
    Bundle bundle,userbundle;

    RecyclerView recyclerView;
    HistoryRecyclerAdapter historyRecyclerAdapter;
    DatabaseReference database;
    HistoryRecyclerAdapter myAdapter;
    ArrayList<HistoryModel> list;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_transaction_history);

        bundle = getIntent().getExtras();
        userID = bundle.getString("Email");
        fname = bundle.getString("FirstName");

        userbundle = new Bundle();
        userbundle.putString("FirstName", fname);
        userbundle.putString("Email", userID);

        recyclerView =findViewById(R.id.historyRecyclerView);

        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        list = new ArrayList<>();
        myAdapter = new HistoryRecyclerAdapter(this,list);
        recyclerView.setAdapter(myAdapter);


        reference = FirebaseDatabase.getInstance().getReference();
        Query query = reference.child("User").orderByChild("Email").equalTo(userID);
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot ds: snapshot.getChildren()){
                    key = ds.getKey();


                    database = FirebaseDatabase.getInstance().getReference("History").child(key);

                    database.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            for (DataSnapshot dataSnapshot: snapshot.getChildren()){
                                HistoryModel user = dataSnapshot.getValue(HistoryModel.class);
                                list.add(user);
                            }
                            myAdapter.notifyDataSetChanged();
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

    public void btntrnback(View view) {
        Toast.makeText(this, "Back Clicked", Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(getApplicationContext(),HomeScreen.class);
        intent.putExtras(userbundle);
        startActivity(intent);
    }

}